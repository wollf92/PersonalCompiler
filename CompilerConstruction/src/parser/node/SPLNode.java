package parser.node;

import Tokenizer.Token;

public class SPLNode extends Node{
	public MultiNode<VarDeclNode> varNode;
	public MultiNode<FunDeclNode> funNode;
	public SPLNode(MultiNode<VarDeclNode> varNode, MultiNode<FunDeclNode> funNode) {
		super(Token.SPL, varNode, funNode);
		this.varNode = varNode;
		this.funNode = funNode;
	}
}
