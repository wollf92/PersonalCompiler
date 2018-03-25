package Tokenizer;

import java.math.BigInteger;
import java.util.Optional;

public class Token{
	public static Token WHILE;
	public static Token FUNDECL = new Token(null, "FUNDECL");
	public static Token ID;
	public static Token ROOT;
	public static Token SPL;
	public static Token VARDECLS = new Token(null, "VARDECLS");
	public static Token FUNDECLS = new Token(null, "FUNDECLS");
	public static Token VARDECL;
	public static Token FARGS = new Token(null, "FARGS");
	public static Token LIST = new Token(null, "LIST");
	public static Token TUPLE;
	public static Token BODY = new Token(null, "BODY");
	public static Token STATEMENTS = new Token(null, "STATEMENTS");
	
	private TokenType tokentype;
	private String representation;
	public Token(TokenType t, String rep) {
		this.tokentype = t;
		this.representation = rep;
	}
	public String toString() {
		return representation;
	}
	public TokenType getTokenType() {
		return tokentype;
	}
	
	public Optional<BigInteger> getAsInt(){
		return tokentype == TokenType.INTVALUE ? Optional.of(((ValueToken<BigInteger>)this).getValue()) : Optional.empty();
	}
	
	public Optional<Boolean> getAsBool(){
		return tokentype == TokenType.BOOLVALUE ? Optional.of(((ValueToken<Boolean>)this).getValue()) : Optional.empty();
	}
}
