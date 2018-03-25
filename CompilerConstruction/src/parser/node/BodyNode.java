package parser.node;

import java.util.Optional;

import Tokenizer.Token;

public class BodyNode extends Node{
	private MultiNode<VarDeclNode> vardecls;
	private MultiNode<StmtNode> stmts;
	private Optional<ReturnStmtNode> returnStmt;
	public BodyNode(MultiNode<VarDeclNode> vardecls, MultiNode<StmtNode> stmts, Optional<ReturnStmtNode> returnStmt){
		super(Token.BODY, vardecls, stmts, returnStmt.orElse(null));
		this.vardecls = vardecls;
		this.stmts = stmts;
		this.returnStmt = returnStmt;
	}

}
