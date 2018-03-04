package parser.node;

import javax.swing.tree.DefaultMutableTreeNode;

import Tokenizer.Token;

public abstract class Node extends DefaultMutableTreeNode{
	public Node(Token token, Node...nodes){
		super(token);
		for(Node n : nodes)
			add(n);
	}
	public Token getToken() {
		return (Token)super.getUserObject();
	}
}
