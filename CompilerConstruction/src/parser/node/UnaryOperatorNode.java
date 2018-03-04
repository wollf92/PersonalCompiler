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
		switch(this.getToken().getTokenType()) {
			case HD: return typeOfChild instanceof ListTypeNode ? ((ListTypeNode)typeOfChild).getListType() : null;
			case TL: return typeOfChild instanceof ListTypeNode ? typeOfChild : null;
			case MINUS: return typeOfChild instanceof IntegerTypeNode ? typeOfChild : null;
			default:
				break;
		}
	}
	

}
