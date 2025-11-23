/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
import java.util.List;

public class FunctionNode implements ASTNode {
    public final String name;
    public final List<String> parameters;
    public final BlockNode body;

    public FunctionNode(String name, List<String> parameters, BlockNode body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFunction(this);
    }
}

