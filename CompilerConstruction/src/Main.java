import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import Tokenizer.GrammarToken;
import Tokenizer.Token;
import Tokenizer.Tokenizer;

public class Main{

	public static void main(String[] args) throws IOException{
		Tokenizer t = new Tokenizer(new FileReader("Test.spl"));
		List<GrammarToken> tokens = t.parse();
		tokens.forEach(to->{
			if(to instanceof GrammarToken) {
				System.out.println(((GrammarToken)to).getLineNo() + " " + to.getTokenType());
			}
		});
	}

}
