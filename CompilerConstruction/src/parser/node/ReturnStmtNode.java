package parser.node;
import java.util.Optional;

import Tokenizer.Token;

public class ReturnStmtNode extends Node{
	private Optional<TypeNode> expr;
	public ReturnStmtNode(Token rtrn, Optional<TypeNode> expr){
		super(rtrn, expr.orElse(null));
		this.expr = expr;
	}
}
