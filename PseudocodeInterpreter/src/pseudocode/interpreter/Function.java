/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.interpreter;

import pseudocode.parser.BlockNode;
import java.util.List;

public class Function {
    public final String name;
    public final List<String> parameters;
    public final BlockNode body;

    public Function(String name, List<String> parameters, BlockNode body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
}

