package parser.node;

import Tokenizer.Token;

public class IDNode extends TypeNode{
	public IDNode(Token id) {
		super(id);
	}
	public TypeNode getType(){
		return null;
	}
}
