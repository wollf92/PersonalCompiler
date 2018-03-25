package Tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer extends StreamTokenizer{
	List<GrammarToken> tokens = new ArrayList<>();
	public Tokenizer(Reader r){
		super(r);
		this.resetSyntax();
		this.wordChars('a', 'z');
		this.wordChars('A', 'Z');
		this.whitespaceChars(0, 31);
		this.slashSlashComments(true);
		this.slashStarComments(true);
	}
	public List<GrammarToken> parse() throws IOException {
		while(nextToken() != TT_EOF) {
			String word = ttype == TT_WORD ? parseAsWord(sval) : parseAsChar((char)ttype);
			if(!word.trim().isEmpty()) {
				TokenType type = getTokenType(word);
					switch(type) {
						case ERROR:			break;
						case BOOLVALUE: tokens.add(new ValueToken<Boolean>(type, word, lineno(), word.equals("False") ? false : true)); break;
						case INTVALUE:	tokens.add(new ValueToken<BigInteger>(type, word, lineno(), new BigInteger(word))); break;
						default:				tokens.add(new GrammarToken(type, word, lineno())); break;
					}
			}
		}
		return tokens;
	}
	private String parseAsChar(char input) throws IOException{
		if(input == '-' && checkNext('>')) return "->";
		if(input == '[' && checkNext(']')) return "[]";
		if(input == ':' && checkNext(':')) return "::";
		if(input == '=' && checkNext('=')) return "==";
		if(input == '<' && checkNext('=')) return "<=";
		if(input == '!' && checkNext('=')) return "!=";
		if(input == '>' && checkNext('=')) return ">=";
		if(input == '|' && checkNext('|')) return "||";
		if(input == '&' && checkNext('&')) return "&&";
		if(Character.isDigit(input)) {
			StringBuilder sb = new StringBuilder().append(input);
			while(Character.isDigit(nextToken()))
				sb.append((char)ttype);
			pushBack();
			return sb.toString();
		}
		return input == ' ' ? ""+'\u0000' : Character.toString(input);
	}
	private String parseAsWord(String word) throws IOException{
		while(nextToken() == '_' || Character.isDigit((char)ttype) || ttype == TT_WORD)
			word += ttype == TT_WORD ? sval : (char)ttype;
		pushBack();
		return word;
	}
	
	private TokenType getTokenType(String word){
		switch(word){
			case "if":		return TokenType.IF;
			case "else":	return TokenType.ELSE;
			case "then": 	return TokenType.THEN;
			case "var":		return TokenType.VAR;
			case "Void":	return TokenType.VOID;
			case "Int":		return TokenType.INT;
			case "Bool":	return TokenType.BOOL;
			case "Char":	return TokenType.CHAR;
			case "while":	return TokenType.WHILE;
			case "return":return TokenType.RETURN;
			case "hd":		return TokenType.HD;
			case "tl":		return TokenType.TL;
			case "fst":		return TokenType.FST;
			case "snd":		return TokenType.SND;
			case "False": return TokenType.BOOLVALUE;
			case "True":	return TokenType.BOOLVALUE;
			//case "print":	return TokenType.PRINT;
			//case "isEmpty":return TokenType.ISEMPTY;
			//case "read":	return TokenType.READ;
			case "(":			return TokenType.ROUNDBRACKETOPEN;
			case ")":			return TokenType.ROUNDBRACKETCLOSE;
			case "{":			return TokenType.CURLYBRACKETOPEN;
			case "}":			return TokenType.CURLYBRACKETCLOSE;
			case "[":			return TokenType.SQUAREBRACKETOPEN;
			case "]":			return TokenType.SQUAREBRACKETCLOSE;
			case "+":			return TokenType.PLUS;
			case "-":			return TokenType.MINUS;
			case "*":			return TokenType.MULTIPLY;
			case "/":			return TokenType.DIVIDE;
			case ">":			return TokenType.BIGGER;
			case "<":			return TokenType.SMALLER;
			case "=":			return TokenType.ASSIGNMENT;
			case ";":			return TokenType.SEMICOLON;
			case ":":			return TokenType.COLON;
			case ",":			return TokenType.COMMA;
			case ".":			return TokenType.DOT;
			case "!":			return TokenType.EXCLAMATION;
			case "->":		return TokenType.ARROW;
			case "[]":		return TokenType.EMPTYLIST;
			case "::":		return TokenType.DOUBLECOLON;
			case "==":		return TokenType.EQUALS;
			case "<=":		return TokenType.SMALLEREQUALS;
			case "!=":		return TokenType.INEQUALS;
			case ">=":		return TokenType.BIGGEREQUALS;
			case "||":		return TokenType.OR;
			case "&&":		return TokenType.AND;
			default:			if(word.chars().allMatch(Character::isDigit)) {
				return TokenType.INTVALUE;
			} else if(word.length() == 1 && !Character.isLetter(word.charAt(0))) {
				System.err.printf("Error found on line %s after %s, invalid Token: %d\n", lineno(), tokens.get(tokens.size()-1), (int)word.charAt(0));
				return TokenType.ERROR;
			} else {
				return TokenType.ID;
			}
		}
	}
	private boolean checkNext(char t) throws IOException {
		if(nextToken() == t)
			return true;
		pushBack();
		return false;
	}

}
