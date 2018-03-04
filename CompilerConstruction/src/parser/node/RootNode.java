package parser.node;

import Tokenizer.Token;

public class RootNode extends Node{
	public SPLNode spl;
	public RootNode(SPLNode spl){
		super(Token.ROOT, spl);
		this.spl = spl;
	}

}
