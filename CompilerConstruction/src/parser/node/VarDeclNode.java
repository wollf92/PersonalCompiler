package parser.node;

import Tokenizer.Token;

public class VarDeclNode extends Node{
	
	public VarDeclNode(Token id, TypeNode vardecltype, TypeNode expr){
		super(id, vardecltype, expr);
	}

}
