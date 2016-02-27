package example;

import grammar.antlr.ANTLRv3Lexer;

import java.io.IOException;
import java.util.List;

import lang.csharp.CSharp4;
import lang.csharp.CSharp4PreProcessorImpl;

import org.antlr.grammar.v3.ANTLRv3Parser;
import org.antlr.runtime.ANTLRFileStream;
//import org.antlr.runtime.ANTLRInputStream;
//import org.antlr.grammar.v3.ANTLRv3Lexer;
//import org.antlr.grammar.v3.ANTLRv3Parser;
//import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import util.unicode.ANTLRFileStreamWithBOM;
import antlr.CommonToken;

public class Main {

    public static void main(String[] args) throws IOException,
    RecognitionException {
    	String filename = "resource/if_statement.cs";
	
        CharStream input = null;
    	
    	input = new ANTLRFileStream(filename);
    ANTLRv3Lexer lex = new ANTLRv3Lexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    ANTLRv3Parser g = new ANTLRv3Parser(tokens);
    ANTLRv3Parser.grammarDef_return r = g.grammarDef();
    CommonTree t = (CommonTree) r.getTree();
    System.out.println(t.toStringTree());
    
    }
    
	
    /**
     * @param args
     * @throws IOException
     * @throws RecognitionException
     */
    public static void main3(String[] args) throws IOException,
            RecognitionException {
        String filename = "resource/if_statement.cs";
        
        //CSharp4 parser = createParserFor(filename);
        
		ANTLRFileStreamWithBOM charStream = new ANTLRFileStreamWithBOM(filename);
        Lexer lex = new CSharp4PreProcessorImpl(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lex);
        CSharp4 parser = new CSharp4(tokenStream);
        
        //lex.nextToken()
        
//        ANTLRv3Parser.grammarDef_return r = parser.grammarDef();
//        CommonTree t = (CommonTree) r.getTree();
//        // System.out.println(t.toStringTree());
        
        parser.compilation_unit();
        
        System.out.println(((CommonTokenStream)parser.getTokenStream()).getTokens());
        
        CommonTokenStream cts =  (CommonTokenStream)parser.getTokenStream();
        
        List<CommonToken> l = cts.getTokens();
        
        for(Object ct  : l) {
        	System.out.println(ct.toString());
        	//System.out.println(ct.getType() + " " + ct.getText());
        }
        
       System.out.println("");
        
    }

	private static CSharp4 createParserFor(String filename) throws IOException {
		ANTLRFileStreamWithBOM charStream = new ANTLRFileStreamWithBOM(filename);
        Lexer lex = new CSharp4PreProcessorImpl(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lex);
        CSharp4 parser = new CSharp4(tokenStream);
        return parser;
	}

}
