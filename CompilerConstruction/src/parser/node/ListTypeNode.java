package parser.node;

import Tokenizer.Token;

public class ListTypeNode extends TypeNode{
	private TypeNode type;
	public ListTypeNode(TypeNode type) {
		super(Token.LIST, type);
		this.type = type;
	}
	public TypeNode getType() {
		return this;
	}
	public TypeNode getListType() {
		return type;
	}
}
