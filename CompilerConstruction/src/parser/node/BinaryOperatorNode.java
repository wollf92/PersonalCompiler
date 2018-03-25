package parser.node;

import java.util.Optional;

import Tokenizer.Token;

public class BinaryOperatorNode extends TypeNode{
	private TypeNode left, right;
	public BinaryOperatorNode(Token t, TypeNode left, TypeNode right){
		super(t, left, right);
		this.left = left;
		this.right = right;
	}
	
	@Override 
	public TypeNode getType() {
		TypeNode leftType = left.getType();
		TypeNode rightType = right.getType();
		switch(getToken().getTokenType()) {
			case COLON: 
				if(rightType instanceof ValueNode ) {
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
		}
		return null;
	}

}
