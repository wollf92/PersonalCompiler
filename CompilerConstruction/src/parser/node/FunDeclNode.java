package parser.node;

import Tokenizer.Token;

public class FunDeclNode extends Node{
	private MultiNode<IDNode> fargs;
	private MultiNode<TypeNode> input;
	private TypeNode ret;
	private BodyNode body;
	public FunDeclNode(Token id, MultiNode<IDNode> fargs, MultiNode<TypeNode> input, TypeNode retType, BodyNode body){
		super(id, fargs, input, retType, body);
		this.fargs = fargs;
		this.input = input;
		this.ret = retType;
		this.body = body;
	}
	public TypeNode getReturnType() {
		return ret;
	}
	public BodyNode getBody() {
		return body;
	}
//	public boolean analyze(Scope s) {
//		
//	}
}

