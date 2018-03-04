package parser.node;

import Tokenizer.Token;

public class IDNode extends TypeNode{
	public IDNode(Token id) {
		super(id);
	}
	public TypedNode getType(){
		return null;
	}
}
