import java.util.LinkedList;
import java.util.regex.*;

public class Tokeniser {
	private class TokenInfo {
		public final Pattern regex;
		public final int token;

		public TokenInfo(Pattern regex, int token) {
			super();
			this.regex = regex;
			this.token = token;
		}
	}

	public class Token {
		
		  public static final int EPSILON = 0;
		  public static final int ASSIGNER = 1;
		  public static final int PLUSMINUS = 4;
		  public static final int MULTDIV = 5;
		  public static final int RAISED = 6;
		  public static final int FUNCTION = 11;
		  public static final int OPEN_BRACKET = 2;
		  public static final int CLOSE_BRACKET = 3;
		  public static final int NUMBER = 7;
		  public static final int VARIABLE = 8;
		  public static final int EQUALS = 9;
		  public static final int END = 10;
		  public static final int OPEN_CURL = 12;
		  public static final int CLOSE_CURL = 13;


		public final int token;
		public final String seq;

		public Token(int token, String sequence) {
			super();
			this.token = token;
			seq = sequence;
		}
	}


	private LinkedList<TokenInfo> infos;
	private LinkedList<Token> tokens;

	public Tokeniser() {
		infos  = new LinkedList<TokenInfo>();
		tokens = new LinkedList<Token>();
	}

	public void addInfo(String regex, int tok) {
		infos.add(new TokenInfo(Pattern.compile("^("+regex+")"), tok));
	}

	public void tokenize (String str) {
		 String s = str.trim();
		    tokens.clear();
		    while (!s.equals(""))
		    {
		      boolean match = false;
		      for (TokenInfo info : infos)
		      {
		        Matcher m = info.regex.matcher(s);
		        if (m.find())
		        {
		          match = true;
		          String tok = m.group().trim();
		          s = m.replaceFirst("").trim();
		          tokens.add(new Token(info.token, tok));
		          break;
		        }
		      }
		      if (!match) throw new ParserException("Unexpected character in input: "+s);
		    }
	}
	public LinkedList<Token> getTokens() {
		return tokens;
	}

	public static void main(String[] args) {
		Tokeniser tokenizer = new Tokeniser();
		tokenizer.addInfo("let", 1); // assigner
		tokenizer.addInfo("\\(", 2); // open bracket
		tokenizer.addInfo("\\)", 3); // close bracket
		tokenizer.addInfo("[+-]", 4); // plus or minus
		tokenizer.addInfo("[*/]", 5); // mult or divide
		tokenizer.addInfo("\\^", 6); // raised
		tokenizer.addInfo("[0-9]+",7); // integer number
		tokenizer.addInfo("[a-zA-Z][a-zA-Z0-9_]*", 8); // variable
		tokenizer.addInfo("\\=", 9);
		tokenizer.addInfo("\\;", 10);
		tokenizer.addInfo("func", 11);
		tokenizer.addInfo("\\{", 12); 
		tokenizer.addInfo("\\}", 13);

		try {
			tokenizer.tokenize("let a = 5;");

			for (Tokeniser.Token tok : tokenizer.getTokens()) {
				System.out.println("" + tok.token + " " + tok.seq);
			}
		}
		catch (ParserException e) {
			System.out.println(e.getMessage());
		}

	}

}
