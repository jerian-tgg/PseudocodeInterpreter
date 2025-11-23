/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
public interface ASTNode {
    <T> T accept(ASTVisitor<T> visitor);
}
