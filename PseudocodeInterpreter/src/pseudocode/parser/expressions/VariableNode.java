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

public class VariableNode implements ASTNode {
    public final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(pseudocode.parser.ASTVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }
}