/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
import pseudocode.parser.expressions.BinaryOpNode;
import pseudocode.parser.expressions.CallNode;
import pseudocode.parser.expressions.LiteralNode;
import pseudocode.parser.expressions.UnaryOpNode;
import pseudocode.parser.expressions.VariableNode;

// visitor interface used by interpreter
public interface ASTVisitor<T> {
    T visitProgram(ProgramNode program);

    // statements
    T visitAssignment(AssignmentNode node);
    T visitPrint(PrintNode node);
    T visitInput(InputNode node);
    T visitIf(IfNode node);
    T visitWhile(WhileNode node);
    T visitFor(ForNode node);
    T visitBlock(BlockNode node);
    T visitFunction(FunctionNode node);
    T visitReturn(ReturnNode node);

    // expressions
    T visitBinary(BinaryOpNode node);
    T visitUnary(UnaryOpNode node);
    T visitLiteral(LiteralNode node);
    T visitVariable(VariableNode node);
    T visitCall(CallNode node);
}
