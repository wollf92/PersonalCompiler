package parser.node;

import Tokenizer.Token;
import Tokenizer.TokenType;

public abstract class TypeNode extends Node{
	public static TypeNode ANY;
	public TypeNode(Token t, Node...types){
		super(t, types);
		// TODO Auto-generated constructor stub
	}
	public TypeNode getType(){
		return this;
	}

}
