package parser.node;

import java.util.Optional;

import Tokenizer.Token;

public class IfStmtNode extends StmtNode{
	private TypeNode expr;
	private BodyNode body;
	private Optional<BodyNode> body2;
	
	public IfStmtNode(Token ifToken, TypeNode expr, BodyNode body, Optional<BodyNode> body2){
		super(ifToken, expr, body, body2.orElse(null));
		this.expr = expr;
		this.body = body;
		this.body2 = body2;
	}

}
