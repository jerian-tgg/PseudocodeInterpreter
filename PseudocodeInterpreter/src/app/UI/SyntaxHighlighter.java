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
    
    // Color scheme
private static final Color KEYWORD_COLOR     = new Color(198, 120, 221);  // Purple
private static final Color STRING_COLOR      = new Color(152, 195, 121);  // Green
private static final Color NUMBER_COLOR      = new Color(224, 108, 117);  // Red/Pink
private static final Color OPERATOR_COLOR    = new Color( 97, 175, 239);  // Blue
private static final Color IDENTIFIER_COLOR  = new Color(171, 178, 191);  // Soft Gray
private static final Color COMMENT_COLOR     = new Color( 92,  99, 112);  // Dim Gray
private static final Color DEFAULT_COLOR     = new Color(197, 200, 198);  // Light Gray

    
    // Styles
    private Style keywordStyle;
    private Style stringStyle;
    private Style numberStyle;
    private Style operatorStyle;
    private Style identifierStyle;
    private Style commentStyle;
    private Style defaultStyle;
    
    public SyntaxHighlighter(StyledDocument doc) {
        this.document = doc;
        initializeStyles();
    }
    
    private void initializeStyles() {
        // Keyword style
        keywordStyle = document.addStyle("keyword", null);
        StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
        StyleConstants.setBold(keywordStyle, true);
        
        // String style
        stringStyle = document.addStyle("string", null);
        StyleConstants.setForeground(stringStyle, STRING_COLOR);
        
        // Number style
        numberStyle = document.addStyle("number", null);
        StyleConstants.setForeground(numberStyle, NUMBER_COLOR);
        
        // Operator style
        operatorStyle = document.addStyle("operator", null);
        StyleConstants.setForeground(operatorStyle, OPERATOR_COLOR);
        
        // Identifier style
        identifierStyle = document.addStyle("identifier", null);
        StyleConstants.setForeground(identifierStyle, IDENTIFIER_COLOR);
        
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
                    
                    // Check if it's a keyword
                    boolean isKeyword = isKeyword(lexeme);
                    Style style = isKeyword ? keywordStyle : identifierStyle;
                    
                    int actualLength = Math.min(idLength, docLength - idStart);
                    document.setCharacterAttributes(idStart, actualLength, style, true);
                }
                continue;
            }
            
            // Handle operators
            int opStart = pos;
            
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                case '(':
                case ')':
                case ',':
                    pos++;
                    break;
                case '=':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // ==
                    }
                    break;
                case '!':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // !=
                    }
                    break;
                case '>':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // >=
                    }
                    break;
                case '<':
                    pos++;
                    if (pos < length && text.charAt(pos) == '=') {
                        pos++; // <=
                    }
                    break;
                default:
                    pos++; // skip unknown character (keep default style)
                    break;
            }
            
            int opLength = pos - opStart;
            if (opStart < docLength && opLength > 0) {
                int actualLength = Math.min(opLength, docLength - opStart);
                document.setCharacterAttributes(opStart, actualLength, operatorStyle, true);
            }
        }
    }
    
    /**
     * Checks if a lexeme is a keyword
     */
    private boolean isKeyword(String lexeme) {
        switch (lexeme) {
            case "PRINT":
            case "INPUT":
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
            case "FUNCTION":
            case "RETURN":
            case "AND":
            case "OR":
            case "NOT":
                return true;
            default:
                return false;
        }
    }
    
}

