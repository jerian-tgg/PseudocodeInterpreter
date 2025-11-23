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

public class LiteralNode implements ASTNode {
    public final Object value; // Double or String or Boolean

    public LiteralNode(Object value) {
        this.value = value;
    }

    @Override
    public <T> T accept(pseudocode.parser.ASTVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
