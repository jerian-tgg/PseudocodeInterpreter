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

public class BinaryOpNode implements ASTNode {
    public final ASTNode left;
    public final String op;
    public final ASTNode right;

    public BinaryOpNode(ASTNode left, String op, ASTNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public <T> T accept(pseudocode.parser.ASTVisitor<T> visitor) {
        return visitor.visitBinary(this);
    }
}
