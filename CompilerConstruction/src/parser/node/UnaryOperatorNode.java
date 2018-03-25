package parser.node;

import Tokenizer.Token;

public class UnaryOperatorNode extends TypeNode{
	private TypeNode typeNode;
	public UnaryOperatorNode(Token t, TypeNode typeNode){
		super(t, typeNode);
		this.typeNode = typeNode;
	}
	
	public TypeNode getType() {
		TypeNode typeOfChild = typeNode.getType();
		return this;
	}
	

}
