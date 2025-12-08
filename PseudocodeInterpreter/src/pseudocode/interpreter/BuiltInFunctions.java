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
import java.util.function.Function;

public class BuiltInFunctions {
    private static final Scanner SC = new Scanner(System.in);
    /**
     * Optional provider to collect user input when a console is not available
     * (e.g. when running inside the Swing GUI). If null, stdin is used.
     */
    private static Function<String, String> inputProvider = null;

    public static void setInputProvider(Function<String, String> provider) {
        inputProvider = provider;
    }

    public static void builtinPrint(Object o) {
        System.out.println(o == null ? "null" : o.toString());
    }

    public static Object builtinInput(String prompt) {
        String line;
        if (inputProvider != null) {
            String effectivePrompt = (prompt == null || prompt.isEmpty()) ? "Enter a value:" : prompt;
            line = inputProvider.apply(effectivePrompt);
            // If user cancels, treat as empty string to avoid blocking.
            if (line == null) {
                line = "";
            }
        } else {
            if (prompt != null && !prompt.isEmpty()) System.out.print(prompt);
            line = SC.nextLine();
        }
        // try parse double
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return line;
        }
    }
}
