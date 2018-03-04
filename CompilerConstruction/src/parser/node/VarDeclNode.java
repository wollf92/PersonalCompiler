package parser.node;

import Tokenizer.Token;

public class VarDeclNode extends Node{
	
	public VarDeclNode(TypeNode vardecltype, IDNode id, ExprNode expr){
		super(Token.VARDECL, vardecltype, id, expr);
		// TODO Auto-generated constructor stub
	}

}
