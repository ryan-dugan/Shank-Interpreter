package shank_interpreter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * The Shank class is the main class for a lexer program.
 * It tokenizes the lines in a file using the lexer class and prints the list of tokens.
 * 
 * @author Ryan Dugan
 */
public class Shank {

	/**
	 * The main method of the Shank class tokenizes the lines in the file using the lexer class,
	 * and prints the list of tokens. If the number of command line arguments less/greater than one,
	 * it displays an error message and exits.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * Reads in the lines from the input file into a list of strings.
		 */
		Path file = Paths.get("input.shank");
		List <String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
		
		Lexer lexer = new Lexer();
		
		/**
		 * Loop through each line in the file and tokenize the line using lex method.
		 */
		for (String line : lines) {
			lexer.lex(line);
		}
		
		/**
		 * Generate list of tokens created by the lexer.
		 */
		List<Token> tokens = lexer.getTokens();
		
		System.out.println("/********** Lexer Output **********/\n");
		
		for(Token token : tokens) {
			System.out.print(token.toString());
		}
		
		System.out.println("\n/********** End Lexer Output **********/\n\n");
		
		/**
		 * Create new Parser object.
		 */
		Parser parser = new Parser(tokens);
		ProgramNode program = parser.parse();
		
		/**
		 * Print each token.
		 */
		System.out.println("\n/********** Parser Output **********/\n");
		System.out.print(program);
		System.out.println("\n\n/********** End Parser Output **********/\n\n");
		
		SemanticAnalysis semanticAnalysis = new SemanticAnalysis(program);
		
		System.out.println("\n/********** Interpreter Output **********/\n");
		
		Interpreter interpreter = new Interpreter(program);
		
		System.out.println("\n/********** End Interpreter Output **********/\n");
				
	}

}
