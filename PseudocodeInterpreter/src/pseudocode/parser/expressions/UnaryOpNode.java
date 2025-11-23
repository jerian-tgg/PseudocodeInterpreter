/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser.expressions;

/**
 *
 * @author Josh
 */
import pseudocode.parser.ASTNode;

public class UnaryOpNode implements ASTNode {
    public final String op;
    public final ASTNode expr;

    public UnaryOpNode(String op, ASTNode expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public <T> T accept(pseudocode.parser.ASTVisitor<T> visitor) {
        return visitor.visitUnary(this);
    }
}
