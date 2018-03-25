package parser.node;
import Tokenizer.Token;

public class AssignStmtNode extends StmtNode{
	TypeNode access, expr;
	public AssignStmtNode(Token assign, TypeNode access, TypeNode expr){
		super(assign, access, expr);
		this.access = access;
		this.expr = expr;
	}
	

}
