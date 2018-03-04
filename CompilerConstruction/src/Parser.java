import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import Tokenizer.GrammarToken;
import Tokenizer.Token;
import Tokenizer.TokenType;
import parser.node.BodyNode;
import parser.node.ExprNode;
import parser.node.FArgsNode;
import parser.node.FunDeclNode;
import parser.node.IDNode;
import parser.node.IfStmtNode;
import parser.node.ListTypeNode;
import parser.node.MultiNode;
import parser.node.Node;
import parser.node.ReturnStmtNode;
import parser.node.RootNode;
import parser.node.SPLNode;
import parser.node.SingleTypeNode;
import parser.node.StmtNode;
import parser.node.TupleTypeNode;
import parser.node.TypeNode;
import parser.node.VarDeclNode;
import parser.node.WhileStmtNode;
public class Parser{
	private int pointer;
	private List<GrammarToken> tokens;
	
	public Parser(List<GrammarToken> tokens) {
		pointer = 0;
		this.tokens = tokens;
	}
	
	public Optional<RootNode> parseTree(){
		int temp = pointer;
		try {
			SPLNode spl = parseSpl().get();
			return of(new RootNode(spl));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	public Optional<SPLNode> parseSpl(){
		int temp = pointer;
		try {
			MultiNode<VarDeclNode> vardecls = parseVarDecls();
			MultiNode<FunDeclNode> fundecls = parseFunDecls();
			return of(new SPLNode(vardecls, fundecls));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	//TODO: temp = pointer werkt niet voor (a)*
	private MultiNode<VarDeclNode> parseVarDecls(){
		int temp = pointer;
		MultiNode<VarDeclNode> varDeclsNode = new MultiNode<VarDeclNode>(Token.VARDECLS);
		try {
			for(Optional<VarDeclNode> var = parseVarDecl(); var.isPresent(); var = parseVarDecl()) 
				varDeclsNode.add(var.get());
		} catch (Exception e) {pointer = temp;}
		return varDeclsNode;
	}
	
	private MultiNode<FunDeclNode> parseFunDecls(){
		int temp = pointer;
		MultiNode<FunDeclNode> funDeclsNode = new MultiNode<FunDeclNode>(Token.FUNDECLS);
		try {
			for(Optional<FunDeclNode> var = parseFunDecl(); var.isPresent(); var = parseFunDecl()) 
				funDeclsNode.add(var.get());
		} catch (Exception e) {pointer = temp;}
		return funDeclsNode;
	}

	private Optional<VarDeclNode> parseVarDecl(){
		int temp = pointer;
		try {
			TypeNode varDeclType = parseVarDeclType().get();
			Token idToken =	eat(TokenType.ID).get();
			Token assign = eat(TokenType.ASSIGNMENT).get();
			ExprNode expr = parseExprNode().get();
			return of(new VarDeclNode(varDeclType, new IDNode(idToken), expr));
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
			Token idToken 				= eat(TokenType.ID).get();
			Token roundO 					= eat(TokenType.ROUNDBRACKETOPEN).orElseThrow(()->parserException("("));
			MultiNode<IDNode> fargsM = oneOrMoreParse("FARGS", ()->new IDNode(eat(TokenType.ID).get()));
			Token roundC 					= eat(TokenType.ROUNDBRACKETCLOSE).get();
			Token doubleC 				= eat(TokenType.DOUBLECOLON).get();
			MultiNode<TypeNode> input = oneOrMoreParse("INPUT", ()->parseType().get());	//SHOULD BE MORE SPECIFIC
			Token arrow 					= eat(TokenType.ARROW).get();
			TypeNode retType 			= parseRetType().get();
			BodyNode body 				= parseBody().get();
			return of(new FunDeclNode(new IDNode(idToken), fargsM, input, retType, body));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}


	private Optional<FArgsNode> parseFArgs(){
		int temp = pointer;
		FArgsNode fargs = new FArgsNode();
		try {
			fargs.add(new IDNode(eat(TokenType.ID).get()));
		} catch (Exception e) { pointer = temp;}
		return of(fargs);
	}
	
	private Optional<InputTypesNode> parseInputTypes(){
		InputTypesNode itn = new InputTypesNode();
		//TODO logic here
		return of(itn);
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
			MultiNode<VarDeclNode> vardecls = parseVarDecls();
			MultiNode<StmtNode> stmts = parseStmts();
			Optional<ReturnStmtNode> returnStmt = parseReturnStmt();
			Token curlyC = eat(TokenType.CURLYBRACKETCLOSE).get();
			return returnStmt.isPresent() ? of(new BodyNode(vardecls, stmts, returnStmt.get())) : of(new BodyNode(vardecls, stmts));
		} catch(Exception e) {pointer = temp;}
		return empty();
	}

	private MultiNode<StmtNode> parseStmts(){
		MultiNode<StmtNode> stmts = new MultiNode<>(Token.STATEMENTS);
		for(Optional<StmtNode> o = parseStmt(); o.isPresent(); o = parseStmt())
			stmts.add(o.get());
		return stmts;
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
		return empty();
	}

	private Optional<IfStmtNode> parseIfStmt(){
		int temp = pointer;
		try {
			Token ifToken = eat(TokenType.IF).get();
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			ExprNode expr = parseExpr().get();
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			BodyNode body = parseBody().get();
			Optional<BodyNode> body2 = eat(TokenType.ELSE).isPresent() ? of(parseBody().get()) : empty();
			return body2.isPresent() ? of(new IfStmtNode(ifToken, expr, body, body2.get())) : of(new IfStmtNode(ifToken, expr, body));
		} catch(Exception e) { pointer = temp;}
		return empty();
	}


	private Optional<WhileStmtNode> parseWhileStmt(){
		int temp = pointer;
		try {
			Token whileToken = eat(TokenType.WHILE).get();
			Token roundO = eat(TokenType.ROUNDBRACKETOPEN).get();
			ExprNode expr = parseExpr().get();
			Token roundC = eat(TokenType.ROUNDBRACKETCLOSE).get();
			BodyNode body = parseBody().get();
			return of(new WhileStmtNode(whileToken, expr, body));
		} catch (Exception e) {pointer = temp;}
		return empty();
	}
	
	private Optional<AssignStmtNode> parseAssignStmt(){
		int temp = pointer;
		try {
			TypedNode access = parseAccess().get();
			
		}
	}
	
	
	private Optional<ExprNode> parseExpr(){
		// TODO Auto-generated method stub
		return null;
	}

	private Optional<ReturnStmtNode> parseReturnStmt(){
		// TODO Auto-generated method stub
		return null;
	}



	private Optional<ExprNode> parseExprNode(){
		// TODO Auto-generated method stub
		return null;
	}
	
	private <T extends Node> MultiNode<T> oneOrMoreParse(String string, Supplier<T> sups){
		MultiNode<T> toReturn = new MultiNode<T>(new Token(TokenType.MULTI, string));
		try {
			T sup = sups.get();
			toReturn.add(sup);
		} catch (Exception e) {}
		return toReturn;
	}
	
	private Optional<GrammarToken> eat(TokenType... type){
		Optional<TokenType> tt = Arrays.stream(type).filter(t->tokens.get(pointer).getTokenType() == t).findFirst();
		if(tt.isPresent()) {
			return Optional.of(tokens.get(pointer++));
		} else return Optional.empty();
	}
	
	private Exception parserException(String s){
		GrammarToken got = tokens.get(pointer);
		System.err.printf("Error on line %d: Expected '%s' but instead got '%s'", got.getLineNo(), s, got.getTokenType().toString());
		return new Exception();
	}
	

}
