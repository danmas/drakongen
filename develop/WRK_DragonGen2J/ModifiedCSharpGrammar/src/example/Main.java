package example;

import java.io.IOException;

import lang.csharp.CSharp4;
import lang.csharp.CSharp4PreProcessorImpl;
import lang.csharp.csParser;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import util.unicode.ANTLRFileStreamWithBOM;

public class Main {

    /**
     * @param args
     * @throws IOException
     * @throws RecognitionException
     */
    public static void main(String[] args) throws IOException,
            RecognitionException {
        String filename = "resource/if_statement.cs";
        
        CSharp4 parser = createParserFor(filename);
        parser.compilation_unit();
        
        System.out.println(((CommonTokenStream)parser.getTokenStream()).getTokens());
        csParser csp = new csParser(parser.getTokenStream());
        
         //csp.compilation_unit();
        
         csParser.compilation_unit_return cu = csp.compilation_unit();
         CommonTree t = (CommonTree) cu.getTree();
         if(t == null) {
        	 System.err.println(" t IS NULL!");
        	 return;
         }
         
         System.out.println(((CommonTokenStream)parser.getTokenStream()).getTokens());
         System.out.println(t.toString());
    }

	private static CSharp4 createParserFor(String filename) throws IOException {
		ANTLRFileStreamWithBOM charStream = new ANTLRFileStreamWithBOM(filename);
        Lexer lex = new CSharp4PreProcessorImpl(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lex);
        CSharp4 parser = new CSharp4(tokenStream);
        return parser;
	}

}
