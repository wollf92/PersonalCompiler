package Tokenizer;

public class ValueToken<T> extends GrammarToken{
	private T value;
	public ValueToken(TokenType tokenType, String rep, int lineno, T value) {
		super(tokenType, rep, lineno);
		this.value = value;
	}
	public T getValue() {
		return value;
	}
}
