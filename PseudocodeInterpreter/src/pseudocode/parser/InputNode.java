/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
public class InputNode implements ASTNode {
    public final String varName;
    public final String prompt; // may be null

    public InputNode(String varName, String prompt) {
        this.varName = varName;
        this.prompt = prompt;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInput(this);
    }
}
