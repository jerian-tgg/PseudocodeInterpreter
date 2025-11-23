/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser.expressions;

import pseudocode.parser.ASTNode;
import pseudocode.parser.ASTVisitor;
import java.util.List;

public class CallNode implements ASTNode {
    public final String name;
    public final List<ASTNode> arguments;

    public CallNode(String name, List<ASTNode> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCall(this);
    }
}

