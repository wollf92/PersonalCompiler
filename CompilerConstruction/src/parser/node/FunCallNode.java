package parser.node;

import Tokenizer.Token;

public class FunCallNode extends TypeNode{

	public FunCallNode(Token idToken, MultiNode<TypeNode> actargs){
		super(idToken, actargs);
	}
	
}
