package parser.node;

import Tokenizer.Token;

public class StmtNode extends ReturnNode{

	public StmtNode(Token token, Node... nodes){
		super(token, nodes);
	}

}
