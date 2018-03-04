package parser.node;
import Tokenizer.Token;

public class WhileStmtNode extends StmtNode{
	private ExprNode expr;
	private BodyNode body;
	public WhileStmtNode(Token token, ExprNode expr, BodyNode body){
		super(token, expr, body);
		this.expr = expr;
		this.body = body;
	}

}
