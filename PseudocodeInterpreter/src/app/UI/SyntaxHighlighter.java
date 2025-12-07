/*
 * Syntax Highlighter for Pseudocode Interpreter
 * Uses the existing Lexer to tokenize and apply syntax highlighting
 */
package app.UI;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Syntax highlighter for pseudocode editor
 * @author Josh
 */
public class SyntaxHighlighter {
    private final StyledDocument document;
    
    // SUBDUED COLOR PALETTE with VIBRANT OPERATORS
    private static final Color CONTROL_KEYWORD_COLOR = new Color(220, 140, 140);     // Muted Red - IF, WHILE, FOR
    private static final Color DECLARATION_KEYWORD_COLOR = new Color(220, 180, 100); // Muted Orange - FUNCTION, RETURN
    private static final Color IO_KEYWORD_COLOR = new Color(120, 180, 220);          // Muted Cyan - PRINT, INPUT
    private static final Color BOOLEAN_KEYWORD_COLOR = new Color(180, 140, 220);     // Muted Purple - AND, OR, NOT
    private static final Color STRING_COLOR = new Color(140, 200, 140);              // Muted Green - "text"
    private static final Color NUMBER_COLOR = new Color(220, 180, 100);              // Muted Gold - 123, 3.14
    private static final Color OPERATOR_COLOR = new Color(255, 100, 100);            // VIBRANT RED - + - * / =
    private static final Color VARIABLE_COLOR = new Color(200, 200, 200);            // Light Gray - x, y, count
    private static final Color BOOLEAN_VALUE_COLOR = new Color(220, 160, 200);       // Muted Pink - TRUE, FALSE
    private static final Color PARENTHESIS_COLOR = new Color(180, 180, 220);         // Muted Blue - ( )
    private static final Color COMMA_COLOR = new Color(180, 180, 180);               // Medium Gray - ,
    private static final Color COMMENT_COLOR = new Color(120, 160, 120);             // Muted Green - # comment
    private static final Color DEFAULT_COLOR = new Color(200, 200, 200);             // Light Gray - other text
    
    // Alternative: Bright blue operators
    /*
    private static final Color OPERATOR_COLOR = new Color(100, 180, 255);            // VIBRANT BLUE - + - * / =
    */
    
    // Alternative: Bright cyan operators  
    /*
    private static final Color OPERATOR_COLOR = new Color(0, 220, 220);              // VIBRANT CYAN - + - * / =
    */
    
    // Alternative: Bright yellow operators
    /*
    private static final Color OPERATOR_COLOR = new Color(255, 220, 0);              // VIBRANT YELLOW - + - * / =
    */
    
    // Styles
    private Style controlKeywordStyle;
    private Style declarationKeywordStyle;
    private Style ioKeywordStyle;
    private Style booleanKeywordStyle;
    private Style stringStyle;
    private Style numberStyle;
    private Style operatorStyle;
    private Style variableStyle;
    private Style booleanValueStyle;
    private Style parenthesisStyle;
    private Style commaStyle;
    private Style commentStyle;
    private Style defaultStyle;
    
    public SyntaxHighlighter(StyledDocument doc) {
        this.document = doc;
        initializeStyles();
    }
    
    private void initializeStyles() {
        // Control flow keyword style (IF, WHILE, FOR, etc.)
        controlKeywordStyle = document.addStyle("control_keyword", null);
        StyleConstants.setForeground(controlKeywordStyle, CONTROL_KEYWORD_COLOR);
        StyleConstants.setBold(controlKeywordStyle, true);
        
        // Declaration keyword style (FUNCTION, RETURN)
        declarationKeywordStyle = document.addStyle("declaration_keyword", null);
        StyleConstants.setForeground(declarationKeywordStyle, DECLARATION_KEYWORD_COLOR);
        StyleConstants.setBold(declarationKeywordStyle, true);
        
        // I/O keyword style (PRINT, INPUT)
        ioKeywordStyle = document.addStyle("io_keyword", null);
        StyleConstants.setForeground(ioKeywordStyle, IO_KEYWORD_COLOR);
        StyleConstants.setBold(ioKeywordStyle, true);
        
        // Boolean keyword style (AND, OR, NOT)
        booleanKeywordStyle = document.addStyle("boolean_keyword", null);
        StyleConstants.setForeground(booleanKeywordStyle, BOOLEAN_KEYWORD_COLOR);
        StyleConstants.setBold(booleanKeywordStyle, true);
        
        // String style
        stringStyle = document.addStyle("string", null);
        StyleConstants.setForeground(stringStyle, STRING_COLOR);
        StyleConstants.setBold(stringStyle, false);
        
        // Number style
        numberStyle = document.addStyle("number", null);
        StyleConstants.setForeground(numberStyle, NUMBER_COLOR);
        StyleConstants.setBold(numberStyle, false);
        
        // Operator style - VIBRANT and BOLD
        operatorStyle = document.addStyle("operator", null);
        StyleConstants.setForeground(operatorStyle, OPERATOR_COLOR);
        StyleConstants.setBold(operatorStyle, true); // Make operators bold and vibrant
        
        // Variable style (identifiers) - LIGHT GRAY
        variableStyle = document.addStyle("variable", null);
        StyleConstants.setForeground(variableStyle, VARIABLE_COLOR);
        StyleConstants.setBold(variableStyle, false);
        
        // Boolean value style (TRUE, FALSE)
        booleanValueStyle = document.addStyle("boolean_value", null);
        StyleConstants.setForeground(booleanValueStyle, BOOLEAN_VALUE_COLOR);
        StyleConstants.setBold(booleanValueStyle, false);
        
        // Parenthesis style
        parenthesisStyle = document.addStyle("parenthesis", null);
        StyleConstants.setForeground(parenthesisStyle, PARENTHESIS_COLOR);
        StyleConstants.setBold(parenthesisStyle, false);
        
        // Comma style
        commaStyle = document.addStyle("comma", null);
        StyleConstants.setForeground(commaStyle, COMMA_COLOR);
        StyleConstants.setBold(commaStyle, false);
        
        // Comment style
        commentStyle = document.addStyle("comment", null);
        StyleConstants.setForeground(commentStyle, COMMENT_COLOR);
        StyleConstants.setItalic(commentStyle, true);
        
        // Default style
        defaultStyle = document.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, DEFAULT_COLOR);
    }
    
    /**
     * Highlights the entire document
     */
    public void highlightDocument() {
        try {
            String text = document.getText(0, document.getLength());
            highlightText(text, 0);
        } catch (BadLocationException e) {
            // Ignore - document might be empty or invalid
        }
    }
    
    /**
     * Highlights text starting from a given offset
     * @param text The text to highlight (can be null, will read from document)
     * @param startOffset The starting offset in the document
     */
    public void highlightText(String text, int startOffset) {
        try {
            int docLength = document.getLength();
            if (docLength == 0) {
                return;
            }
            
            // Always read text directly from document to ensure synchronization
            String docText = document.getText(0, docLength);
            if (docText == null || docText.isEmpty()) {
                return;
            }
            
            // Create a completely plain attribute set to clear everything
            MutableAttributeSet plainAttributes = new SimpleAttributeSet();
            StyleConstants.setForeground(plainAttributes, DEFAULT_COLOR);
            StyleConstants.setBold(plainAttributes, false);
            StyleConstants.setItalic(plainAttributes, false);
            
            // Clear ALL existing styles first - this is critical
            document.setCharacterAttributes(0, docLength, plainAttributes, true);
            
            // Use a custom tokenizer that tracks positions
            // Use the document text, not the parameter (which might be stale)
            highlightWithPositionTracking(docText);
            
        } catch (BadLocationException e) {
            // Ignore - position might be invalid
        }
    }
    
    /**
     * Highlights text by tracking positions as we tokenize
     * This method processes the text sequentially, ensuring no overlapping styles
     */
    private void highlightWithPositionTracking(String text) throws BadLocationException {
        int pos = 0;
        int length = text.length();
        int docLength = document.getLength();
        
        // Ensure we don't go beyond document length
        if (length > docLength) {
            length = docLength;
        }
        
        while (pos < length) {
            char c = text.charAt(pos);
            
            // Skip whitespace (these keep default style)
            if (c == ' ' || c == '\t' || c == '\r') {
                pos++;
                continue;
            }
            
            // Handle newlines (keep default style)
            if (c == '\n') {
                pos++;
                continue;
            }
            
            // Handle comments FIRST (before other processing)
            if (c == '#') {
                int commentStart = pos;
                while (pos < length && text.charAt(pos) != '\n') {
                    pos++;
                }
                // Apply comment style to the entire comment including #
                if (commentStart < docLength) {
                    int commentLength = Math.min(pos - commentStart, docLength - commentStart);
                    if (commentLength > 0) {
                        document.setCharacterAttributes(commentStart, commentLength, commentStyle, true);
                    }
                }
                continue;
            }
            
            // Handle strings (must check before identifiers to catch string literals)
            if (c == '"') {
                int strStart = pos;
                pos++; // skip opening quote
                // Find closing quote, handling escape sequences
                while (pos < length && text.charAt(pos) != '"') {
                    if (text.charAt(pos) == '\\' && pos + 1 < length) {
                        pos += 2; // skip escape sequence
                    } else {
                        pos++;
                    }
                }
                // Include closing quote if found
                if (pos < length && text.charAt(pos) == '"') {
                    pos++; // include closing quote
                }
                int strLength = pos - strStart;
                if (strStart < docLength && strLength > 0) {
                    int actualLength = Math.min(strLength, docLength - strStart);
                    document.setCharacterAttributes(strStart, actualLength, stringStyle, true);
                }
                continue;
            }
            
            // Handle numbers
            if (Character.isDigit(c)) {
                int numStart = pos;
                while (pos < length && Character.isDigit(text.charAt(pos))) {
                    pos++;
                }
                // Handle decimal point
                if (pos < length && text.charAt(pos) == '.') {
                    pos++;
                    while (pos < length && Character.isDigit(text.charAt(pos))) {
                        pos++;
                    }
                }
                int numLength = pos - numStart;
                if (numStart < docLength && numLength > 0) {
                    int actualLength = Math.min(numLength, docLength - numStart);
                    document.setCharacterAttributes(numStart, actualLength, numberStyle, true);
                }
                continue;
            }
            
            // Handle identifiers and keywords (check after strings to avoid matching inside strings)
            if (Character.isLetter(c) || c == '_') {
                int idStart = pos;
                // Collect the entire identifier
                while (pos < length && (Character.isLetterOrDigit(text.charAt(pos)) || text.charAt(pos) == '_')) {
                    pos++;
                }
                int idLength = pos - idStart;
                
                if (idLength > 0 && idStart < docLength) {
                    // Get the actual lexeme from the document to ensure accuracy
                    String lexeme = text.substring(idStart, Math.min(idStart + idLength, length)).toUpperCase();
                    
                    // Get the appropriate style based on keyword category
                    Style style = getKeywordStyle(lexeme);
                    
                    int actualLength = Math.min(idLength, docLength - idStart);
                    document.setCharacterAttributes(idStart, actualLength, style, true);
                }
                continue;
            }
            
            // Handle special values like TRUE, FALSE
            if (c == 'T' || c == 'F' || c == 't' || c == 'f') {
                int valueStart = pos;
                // Check if this might be TRUE or FALSE (case-insensitive)
                String upperText = text.substring(pos, Math.min(pos + 5, length)).toUpperCase();
                if (upperText.startsWith("TRUE")) {
                    pos += 4;
                    if (valueStart < docLength) {
                        int valueLength = pos - valueStart;
                        int actualLength = Math.min(valueLength, docLength - valueStart);
                        document.setCharacterAttributes(valueStart, actualLength, booleanValueStyle, true);
                    }
                    continue;
                } else if (upperText.startsWith("FALSE")) {
                    pos += 5;
                    if (valueStart < docLength) {
                        int valueLength = pos - valueStart;
                        int actualLength = Math.min(valueLength, docLength - valueStart);
                        document.setCharacterAttributes(valueStart, actualLength, booleanValueStyle, true);
                    }
                    continue;
                }
            }
            
            // Handle operators and punctuation - OPERATORS ARE VIBRANT!
            int opStart = pos;
            
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                    pos++;
                    if (opStart < docLength) {
                        document.setCharacterAttributes(opStart, 1, operatorStyle, true);
                    }
                    break;
                case '(':
                case ')':
                    pos++;
                    if (opStart < docLength) {
                        document.setCharacterAttributes(opStart, 1, parenthesisStyle, true);
                    }
                    break;
                case ',':
                    pos++;
                    if (opStart < docLength) {
                        document.setCharacterAttributes(opStart, 1, commaStyle, true);
                    }
                    break;
                case '=':
                    pos++;
                    // Check if it's comparison operator (==)
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // ==
                    }
                    if (opStart < docLength) {
                        int opLength = pos - opStart;
                        int actualLength = Math.min(opLength, docLength - opStart);
                        document.setCharacterAttributes(opStart, actualLength, operatorStyle, true);
                    }
                    break;
                case '!':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // !=
                    }
                    if (opStart < docLength) {
                        int opLength = pos - opStart;
                        int actualLength = Math.min(opLength, docLength - opStart);
                        document.setCharacterAttributes(opStart, actualLength, operatorStyle, true);
                    }
                    break;
                case '>':
                case '<':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // >= or <=
                    }
                    if (opStart < docLength) {
                        int opLength = pos - opStart;
                        int actualLength = Math.min(opLength, docLength - opStart);
                        document.setCharacterAttributes(opStart, actualLength, operatorStyle, true);
                    }
                    break;
                default:
                    pos++; // skip unknown character (keep default style)
                    break;
            }
        }
    }
    
    /**
     * Returns the appropriate style for a keyword based on its category
     */
    private Style getKeywordStyle(String lexeme) {
        // Control flow keywords
        if (isControlFlowKeyword(lexeme)) {
            return controlKeywordStyle;
        }
        
        // Declaration keywords
        if (isDeclarationKeyword(lexeme)) {
            return declarationKeywordStyle;
        }
        
        // I/O keywords
        if (isIOKeyword(lexeme)) {
            return ioKeywordStyle;
        }
        
        // Boolean keywords
        if (isBooleanKeyword(lexeme)) {
            return booleanKeywordStyle;
        }
        
        // Boolean values
        if (lexeme.equals("TRUE") || lexeme.equals("FALSE")) {
            return booleanValueStyle;
        }
        
        // Default to variable style for non-keywords (identifiers)
        return variableStyle;
    }
    
    /**
     * Checks if a lexeme is a control flow keyword
     */
    private boolean isControlFlowKeyword(String lexeme) {
        switch (lexeme) {
            case "IF":
            case "THEN":
            case "ELSE":
            case "ENDIF":
            case "END_IF":
            case "END":
            case "WHILE":
            case "DO":
            case "ENDWHILE":
            case "END_WHILE":
            case "FOR":
            case "FROM":
            case "TO":
            case "ENDFOR":
            case "END_FOR":
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Checks if a lexeme is a declaration keyword
     */
    private boolean isDeclarationKeyword(String lexeme) {
        switch (lexeme) {
            case "FUNCTION":
            case "RETURN":
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Checks if a lexeme is an I/O keyword
     */
    private boolean isIOKeyword(String lexeme) {
        switch (lexeme) {
            case "PRINT":
            case "INPUT":
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Checks if a lexeme is a boolean keyword
     */
    private boolean isBooleanKeyword(String lexeme) {
        switch (lexeme) {
            case "AND":
            case "OR":
            case "NOT":
                return true;
            default:
                return false;
        }
    }
}