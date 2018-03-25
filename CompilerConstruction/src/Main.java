import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import Tokenizer.GrammarToken;
import Tokenizer.Token;
import Tokenizer.Tokenizer;
import parser.node.Node;
import parser.node.SPLNode;

public class Main{

	public static void main(String[] args) throws IOException{
		Tokenizer t = new Tokenizer(new FileReader("Test.spl"));
		List<GrammarToken> tokens = t.parse();
		for(Token tok : tokens) {
			System.out.println(tok.getTokenType());
		}
		Parser p = new Parser(tokens);
		Optional<SPLNode> o = p.parseSpl();
		showOnGUI(o.get());
	}
	
	public static void showOnGUI(Node node){
		JFrame frame = new JFrame();
		JTree tree = new JTree(node);
		JScrollPane scroller = new JScrollPane(tree);
		for(int i = 0; i < tree.getRowCount(); i++)
			tree.expandRow(i);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(scroller);
		frame.pack();
		frame.setVisible(true);
	}

}
