import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import Tokenizer.GrammarToken;
import Tokenizer.Token;
import Tokenizer.TokenType;
import parser.node.AssignStmtNode;
import parser.node.BinaryOperatorNode;
import parser.node.BodyNode;
import parser.node.FunCallNode;
import parser.node.FunCallStmtNode;
import parser.node.FunDeclNode;
import parser.node.IDNode;
import parser.node.IfStmtNode;
import parser.node.ListTypeNode;
import parser.node.MultiNode;
import parser.node.Node;
import parser.node.ReturnStmtNode;
import parser.node.SPLNode;
import parser.node.SingleTypeNode;
import parser.node.StmtNode;
import parser.node.TupleTypeNode;
import parser.node.TypeNode;
import parser.node.UnaryOperatorNode;
import parser.node.ValueNode;
import parser.node.VarDeclNode;
import parser.node.WhileStmtNode;
public class Parser{
	private int pointer;
	private List<GrammarToken> tokens;
	
	public Parser(List<GrammarToken> tokens) {
		pointer = 0;
		this.tokens = tokens;
	}
	
	public Optional<SPLNode> parseSpl(){
		int temp = pointer;
		try {
			MultiNode<VarDeclNode> vardecls = zeroOrMoreParse("VARDECLS", ()->parseVarDecl().get());
			MultiNode<FunDeclNode> fundecls = zeroOrMoreParse("FUNDECLS", ()->parseFunDecl().get());
			return of(new SPLNode(vardecls, fundecls));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<VarDeclNode> parseVarDecl(){
		int temp = pointer;
		try {
			TypeNode varDeclType 	= parseVarDeclType().get();
			Token idToken 				=	eat(TokenType.ID).get();
			Token assign 					= eat(TokenType.ASSIGNMENT).orElseThrow(()->parserException("="));
			TypeNode expr 				= parseExpr().get();
			Token semiColon				= eat(TokenType.SEMICOLON).orElseThrow(()->parserException(";"));
			return of(new VarDeclNode(idToken, varDeclType, expr));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<TypeNode> parseVarDeclType(){
		int temp = pointer;
		try {
			Token varToken = eat(TokenType.VAR).get();
			return of(new SingleTypeNode(varToken));
		} catch (Exception e) {pointer = temp;}
		try {
			TypeNode type = parseType().get();
			return of(type);
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<FunDeclNode> parseFunDecl(){
		int temp = pointer;
		try {
			Token idToken 						= eat(TokenType.ID).get();
			Token roundO 							= eat(TokenType.ROUNDBRACKETOPEN)		.orElseThrow(()->parserException("("));
			MultiNode<IDNode> fargsM 	= zeroOrMoreParse("FARGS" , ()->new IDNode(eat(TokenType.ID).get()), ()->eat(TokenType.COMMA));
			Token roundC 							= eat(TokenType.ROUNDBRACKETCLOSE)	.orElseThrow(()->parserException(")"));
			Token doubleC 						= eat(TokenType.DOUBLECOLON)				.orElseThrow(()->parserException(":"));
			MultiNode<TypeNode> input = zeroOrMoreParse("INPUT", ()->parseType().get());
			Token arrow 							= eat(TokenType.ARROW)							.orElseThrow(()->parserException("->"));
			TypeNode retType 					= parseRetType()										.orElseThrow(()->parserException("return type"));
			BodyNode body 						= parseBody()												.orElseThrow(()->parserException("body"));
			return of(new FunDeclNode(idToken, fargsM, input, retType, body));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<TypeNode> parseRetType(){
		int temp = pointer;
		try {
			Token varToken = eat(TokenType.VOID).get();
			return of(new SingleTypeNode(varToken));
		} catch (Exception e) {pointer = temp;}
		try {
			TypeNode type = parseType().get();
			return of(type);
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<TypeNode> parseType(){
		int temp = pointer;
		try {
			return of(new SingleTypeNode(eat(TokenType.INT, TokenType.BOOL, TokenType.CHAR).get()));
		} catch (Exception e) { pointer = temp;}
		try {
			return of(new IDNode(eat(TokenType.ID).get()));
		} catch (Exception e) { pointer = temp;}
		try {
			return of(parseListType().get());
		} catch (Exception e) { pointer = temp;}
		try {
			return of(parseTupleType().get());
		} catch (Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<ListTypeNode> parseListType(){
		int temp = pointer;
		try {
			Token squareO = eat(TokenType.SQUAREBRACKETOPEN).get();
			TypeNode type = parseType().get();
			Token squareC = eat(TokenType.SQUAREBRACKETCLOSE).get();
			return of(new ListTypeNode(type));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<TupleTypeNode> parseTupleType(){
		int temp = pointer;
		try {
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			TypeNode type1 = parseType().get();
			Token comma = eat(TokenType.COMMA).get();
			TypeNode type2 = parseType().get();
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			return of(new TupleTypeNode(type1, type2));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<BodyNode> parseBody(){
		int temp = pointer;
		try {
			Token curlyO = eat(TokenType.CURLYBRACKETOPEN).get();
			MultiNode<VarDeclNode> vardecls = zeroOrMoreParse("VARDECLS", ()->parseVarDecl().get());
			MultiNode<StmtNode> stmts = zeroOrMoreParse("STATEMENTS", ()->parseStmt().get());
			Optional<ReturnStmtNode> returnStmt = parseReturnStmt();
			Token curlyC = eat(TokenType.CURLYBRACKETCLOSE).get();
			return of(new BodyNode(vardecls, stmts, returnStmt));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<StmtNode> parseStmt(){
		int temp = pointer;
		try {
			return of(parseIfStmt().get());
		} catch (Exception e) {pointer = temp;}
		try {
			return of(parseWhileStmt().get());
		} catch (Exception e) {pointer = temp;}
		try {
			return of(parseAssignStmt().get());
		} catch (Exception e) {pointer = temp;}
		try {
			return of(parseFunCallStmt().get());
		} catch (Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<IfStmtNode> parseIfStmt(){
		int temp = pointer;
		try {
			Token ifToken = eat(TokenType.IF).get();
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			TypeNode expr = parseExpr().get();
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			BodyNode body = parseBody().get();
			Optional<BodyNode> body2 = eat(TokenType.ELSE).isPresent() ? of(parseBody().get()) : empty();
			return of(new IfStmtNode(ifToken, expr, body, body2));
		} catch(Exception e) { pointer = temp;}
		return empty();
	}


	private Optional<WhileStmtNode> parseWhileStmt(){
		int temp = pointer;
		try {
			Token whileToken = eat(TokenType.WHILE).get();
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			TypeNode expr = parseExpr().get();
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			BodyNode body = parseBody().get();
			return of(new WhileStmtNode(whileToken, expr, body));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<AssignStmtNode> parseAssignStmt(){
		int temp = pointer;
		try {
			Token id = eat(TokenType.ID).get();
			Token assign = eat(TokenType.ASSIGNMENT).get();
			TypeNode expr = parseExpr().get();
			Token semi = eat(TokenType.SEMICOLON).get();
			return of(new AssignStmtNode(assign, new IDNode(id), expr));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<FunCallStmtNode> parseFunCallStmt(){
		int temp = pointer;
		try {
			FunCallNode funcall = parseFunCall().get();
			Token semi = eat(TokenType.SEMICOLON).get();
			return of(new FunCallStmtNode(semi, funcall));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<ReturnStmtNode> parseReturnStmt(){
		int temp = pointer;
		try {
			Token rtrn = eat(TokenType.RETURN).get();
			Optional<TypeNode> oExpr = parseExpr();
			Token semi = eat(TokenType.SEMICOLON).get();
			return of(new ReturnStmtNode(rtrn, oExpr));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<FunCallNode> parseFunCall(){
		int temp = pointer;
		try {
			Token id = eat(TokenType.ID).get();
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			MultiNode<TypeNode> actargs = zeroOrMoreParse("ACTARGS", ()->parseExpr().get(), ()->eat(TokenType.COMMA));
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			return of(new FunCallNode(id, actargs));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}

	private Optional<TypeNode> parseExpr(){
		int temp = pointer;
		try {
			TypeNode expr1 = parseExpr1().get();
			Token colon = eat(TokenType.COLON).get();
			TypeNode expr2 = parseExpr().get();
			return of(new BinaryOperatorNode(colon, expr1, expr2));
		}	catch(Exception e) {pointer = temp;}
		return parseExpr1();
	}
	/**
	 * Expr1 = Expr2 ('||' Expr2)*
	 * @return
	 */
	private Optional<TypeNode> parseExpr1(){
		return leftAssociative(()->parseExpr2(), ()->eat(TokenType.OR));
	}
	
	private Optional<TypeNode> parseExpr2(){
		return leftAssociative(()->parseExpr3(), ()->eat(TokenType.AND));
	}
	
	private Optional<TypeNode> parseExpr3(){
		return leftAssociative(()->parseExpr4(), ()->eat(TokenType.INEQUALS, TokenType.EQUALS));
	}
	
	private Optional<TypeNode> parseExpr4(){
		return leftAssociative(()->parseExpr5(), ()->eat(TokenType.SMALLEREQUALS, TokenType.SMALLER, TokenType.BIGGER, TokenType.BIGGEREQUALS));
	}
	
	private Optional<TypeNode> parseExpr5(){
		return leftAssociative(()->parseExpr6(), ()->eat(TokenType.PLUS, TokenType.MINUS));
	}
	
	private Optional<TypeNode> parseExpr6(){
		return leftAssociative(()->parseExpr7(), ()->eat(TokenType.MULTIPLY, TokenType.MODULO, TokenType.DIVIDE));
	}
	
	private Optional<TypeNode> parseExpr7(){
		int temp = pointer;
		try {
			Token negate = eat(TokenType.MINUS, TokenType.EXCLAMATION).get();
			TypeNode expr7 = parseExpr7().get();
			return of(new UnaryOperatorNode(negate, expr7));
		} catch(Exception e) {pointer = temp;}
		return parseExpr8();
	}
	
	private Optional<TypeNode> parseExpr8(){
		int temp = pointer;
		try {
			TypeNode base = parseBaseExpr().get();
			for(Optional<GrammarToken> dot = eat(TokenType.DOT); dot.isPresent(); dot = eat(TokenType.DOT)) {
				Token func = eat(TokenType.TL, TokenType.HD, TokenType.SND, TokenType.FST).get();
				UnaryOperatorNode uon = new UnaryOperatorNode(func, base);
				base = uon;
			}
			return of(base);
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<TypeNode> parseBaseExpr(){
		int temp = pointer;
		try {
			ValueNode vn = parseValueExpr().get();
			return of(vn);
		} catch(Exception e) {pointer = temp;}
		try {
			Token rbo = eat(TokenType.ROUNDBRACKETOPEN).get();
			TypeNode tn = parseExpr().get();
			Token rbc = eat(TokenType.ROUNDBRACKETCLOSE).get();
			return of(tn);
		} catch(Exception e) {pointer = temp;}
		try {
			Token rbo = eat(TokenType.ROUNDBRACKETOPEN).get();
			TypeNode tn1 = parseExpr().get();
			Token c = eat(TokenType.COMMA).get();
			TypeNode tn2 = parseExpr().get();
			Token rbc = eat(TokenType.ROUNDBRACKETCLOSE).get();
			return of(new TupleTypeNode(tn1, tn2));
		} catch(Exception e) {pointer = temp;}
		try {
			FunCallNode fcn = parseFunCall().get();
			return of(fcn);
		} catch(Exception e) {pointer = temp;}
		try {
			Token id = eat(TokenType.ID).get();
			return of(new IDNode(id));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<ValueNode> parseValueExpr(){
		int temp = pointer;
		try {
			Token value = eat(TokenType.INTVALUE, TokenType.BOOLVALUE, TokenType.EMPTYLIST).get();
			return of(new ValueNode(value));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<TypeNode> leftAssociative(Supplier<Optional<TypeNode>> child, Supplier<Optional<GrammarToken>> operator) {
		int temp = pointer;
		try {
			TypeNode left = child.get().get();
			for(Optional<GrammarToken> o = operator.get(); o.isPresent(); o = operator.get()) {
				TypeNode right = child.get().get();
				BinaryOperatorNode bon = new BinaryOperatorNode(o.get(), left, right);
				left = bon;
			}
			return of(left);
		} catch(Exception e) { pointer = temp;}
		return empty();
	}

	private <T extends Node> MultiNode<T> zeroOrMoreParse(String name, Supplier<T> sups, Supplier<Optional>... delimiters){
		MultiNode<T> toReturn = new MultiNode<T>(new Token(TokenType.MULTI, name));
		int temp = pointer;
		try {
			T first = sups.get();
			toReturn.add(first);
			while(delimiters.length == 0 || delimiters[0].get().isPresent()) {
				temp = pointer;
				for(int i = 1; i < delimiters.length; i++)
					delimiters[i].get().get();
				T sequential = sups.get();
				toReturn.add(sequential);
			}
		} catch(Exception e) {pointer = temp;}
		return toReturn;
	}
	
	
	private Optional<GrammarToken> eat(TokenType... type){
		Optional<TokenType> tt = Arrays.stream(type).filter(t->tokens.get(pointer).getTokenType() == t).findFirst();
		if(tt.isPresent()) {
			return of(tokens.get(pointer++));
		} else return empty();
	}
	
	private Exception parserException(String s){
		GrammarToken got = tokens.get(pointer);
		System.err.printf("Error on line %d: Expected '%s' but instead got '%s'", got.getLineNo(), s, got.getTokenType().toString());
		return new Exception();
	}
	

}
