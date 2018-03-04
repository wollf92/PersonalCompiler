package parser.node;

import Tokenizer.Token;

public class FunDeclNode extends Node{
	private IDNode id;
	private MultiNode<IDNode> fargs;
	private MultiNode<TypeNode> input;
	private TypeNode ret;
	private BodyNode body;
	public FunDeclNode(IDNode id, MultiNode<IDNode> fargs, MultiNode<TypeNode> input, TypeNode retType, BodyNode body){
		super(Token.FUNDECL, id, fargs, input, retType, body);
		this.id = id;
		this.fargs = fargs;
		this.input = input;
		this.ret = retType;
		this.body = body;
	}
	public IDNode getId() {
		return id;
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

