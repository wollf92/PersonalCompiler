package parser.node;
import Tokenizer.Token;

public class TupleTypeNode extends TypeNode{
	private TypeNode t1, t2;
	public TupleTypeNode(TypeNode t1, TypeNode t2) {
		super(Token.TUPLE, t1, t2);
		this.t1 = t1;
		this.t2 = t2;
	}
}
