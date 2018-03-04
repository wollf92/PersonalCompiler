package parser.node;

import Tokenizer.Token;

public class BinaryOperator extends TypeNode{
	private TypeNode left, right;
	public BinaryOperator(Token t, TypeNode left, TypeNode right){
		super(t, left, right);
		this.left = left;
		this.right = right;
	}
	
	public TypeNode getType() {
		TypeNode leftType = left.getType();
		TypeNode rightType = right.getType();
		switch(getToken().getTokenType()) {
			case COLON: 
				if(rightType instanceof EmptyListValueNode) {
					return new ListTypeNode(leftType);
				} else if(rightType instanceof ListTypeNode) {
					if(leftType.equals(((ListTypeNode)rightType).getListType())) {
						return rightType;
					} else {
						return new ListTypeNode(TypeNode.ANY);
					}
				} else {
					return null;
				}
			case MINUS:
				if(leftType instanceof IntegerTypeNode && rightType instanceof IntegerTypeNode) {
					return leftType;
				} else {
					return null;
				}
			case EQUALS:
				if(leftType.equals(rightType)) {
					return new BooleanType();
				} else {
					return null;
				}
		}
	}

}
