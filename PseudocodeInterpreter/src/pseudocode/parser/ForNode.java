/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
public class ForNode implements ASTNode {
    public final String iterator;
    public final ASTNode startExpr;
    public final ASTNode endExpr;
    public final BlockNode body;

    public ForNode(String iterator, ASTNode startExpr, ASTNode endExpr, BlockNode body) {
        this.iterator = iterator;
        this.startExpr = startExpr;
        this.endExpr = endExpr;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFor(this);
    }
}
