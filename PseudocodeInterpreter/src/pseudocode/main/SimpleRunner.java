/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.main;

/**
 *
 * @author Josh
 */
import pseudocode.lexer.Lexer;
import pseudocode.parser.Parser;
import pseudocode.interpreter.Interpreter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import pseudocode.lexer.Token;
import pseudocode.parser.ProgramNode;

public class SimpleRunner {
    public static void main(String[] args) throws Exception {
        String program;
        if (args.length > 0) {
            program = new String(Files.readAllBytes(Paths.get(args[0])));
        } else {
            program = """
                # sample program
                x = 1
                FOR i FROM 1 TO 5 DO
                    x = x + i
                END FOR
                PRINT "Sum is: " + x
                IF x > 10 THEN
                    PRINT "Large"
                END IF
                """;
        }

        Lexer lexer = new Lexer(program);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ProgramNode p = parser.parseProgram();

        Interpreter interpreter = new Interpreter();
        interpreter.runProgram(p);
    }
}
