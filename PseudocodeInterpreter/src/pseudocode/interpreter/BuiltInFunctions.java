/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.interpreter;

/**
 *
 * @author Josh
 */
import java.util.Scanner;

public class BuiltInFunctions {
    private static final Scanner SC = new Scanner(System.in);

    public static void builtinPrint(Object o) {
        System.out.println(o == null ? "null" : o.toString());
    }

    public static Object builtinInput(String prompt) {
        if (prompt != null && !prompt.isEmpty()) System.out.print(prompt);
        String line = SC.nextLine();
        // try parse double
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return line;
        }
    }
}
