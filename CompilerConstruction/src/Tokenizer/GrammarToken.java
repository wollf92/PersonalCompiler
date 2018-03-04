package Tokenizer;

public class GrammarToken extends Token{
	private int lineno;
	public GrammarToken(TokenType t, String rep, int lineno) {
		super(t, rep);
		this.lineno = lineno;
	}
	public String getAsError(String message) {
		return String.format("Problem on line %d\n\t%s : %s\n", lineno, toString(), message);
	}
	public int getLineNo() {
		return lineno;
	}
}
