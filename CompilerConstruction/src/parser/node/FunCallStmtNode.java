package parser.node;

import Tokenizer.Token;

public class FunCallStmtNode extends StmtNode{
	FunCallNode funcall;
	public FunCallStmtNode(Token semi, FunCallNode funcall){
		super(semi, funcall);
		this.funcall = funcall;
	}

}
