package parser.node;

import java.util.Optional;

import Tokenizer.Token;

public class IfStmtNode extends StmtNode{
	private ExprNode expr;
	private BodyNode body;
	private Optional<BodyNode> body2;
	
	public IfStmtNode(Token ifToken, ExprNode expr, BodyNode body){
		super(ifToken, expr, body);
		this.expr = expr;
		this.body = body;
		this.body2 = Optional.empty();
	}
	
	public IfStmtNode(Token ifToken, ExprNode expr, BodyNode body, BodyNode body2){
		super(ifToken, expr, body);
		this.expr = expr;
		this.body = body;
		this.body2 = Optional.of(body2);
	}

}
