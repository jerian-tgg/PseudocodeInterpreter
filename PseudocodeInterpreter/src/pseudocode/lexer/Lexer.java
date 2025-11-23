/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.lexer;

/**
 *
 * @author Josh
 */
import pseudocode.errors.LexerException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String src;
    private final int length;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    public Lexer(String src) {
        this.src = src.replace("\r\n", "\n");
        this.length = this.src.length();
    }

    private char peek() {
        return pos < length ? src.charAt(pos) : '\0';
    }

    private char next() {
        char c = peek();
        pos++;
        if (c == '\n') { line++; col = 1; }
        else col++;
        return c;
    }

    private boolean match(char expected) {
        if (peek() == expected) { next(); return true; }
        return false;
    }

    public List<Token> tokenize() {
        List<Token> out = new ArrayList<>();
        while (pos < length) {
            char c = peek();

            if (c == ' ' || c == '\t' || c == '\r') {
                next(); continue;
            }

            if (c == '\n') {
                next();
                out.add(new Token(TokenType.NEWLINE, "\\n", line-1, col));
                continue;
            }

            // Handle comments (lines starting with #)
            if (c == '#') {
                // Skip until end of line
                while (peek() != '\n' && peek() != '\0') {
                    next();
                }
                continue;
            }

            if (Character.isDigit(c)) {
                out.add(number());
                continue;
            }

            if (c == '"') {
                out.add(string());
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                out.add(identifierOrKeyword());
                continue;
            }

            // Operators / punctuation
            switch (c) {
                case '+': next(); out.add(new Token(TokenType.PLUS, "+", line, col)); continue;
                case '-': next(); out.add(new Token(TokenType.MINUS, "-", line, col)); continue;
                case '*': next(); out.add(new Token(TokenType.STAR, "*", line, col)); continue;
                case '/': next(); out.add(new Token(TokenType.SLASH, "/", line, col)); continue;
                case '%': next(); out.add(new Token(TokenType.PERCENT, "%", line, col)); continue;
                case '(' : next(); out.add(new Token(TokenType.LPAREN, "(", line, col)); continue;
                case ')' : next(); out.add(new Token(TokenType.RPAREN, ")", line, col)); continue;
                case ',' : next(); out.add(new Token(TokenType.COMMA, ",", line, col)); continue;
                case '=':
                    next();
                    if (match('=')) out.add(new Token(TokenType.EQEQ, "==", line, col));
                    else out.add(new Token(TokenType.EQUAL, "=", line, col));
                    continue;
                case '!':
                    next();
                    if (match('=')) out.add(new Token(TokenType.BANGEQ, "!=", line, col));
                    else throw new LexerException("Unexpected '!' at " + line + ":" + col);
                    continue;
                case '>':
                    next();
                    if (match('=')) out.add(new Token(TokenType.GTE, ">=", line, col));
                    else out.add(new Token(TokenType.GT, ">", line, col));
                    continue;
                case '<':
                    next();
                    if (match('=')) out.add(new Token(TokenType.LTE, "<=", line, col));
                    else out.add(new Token(TokenType.LT, "<", line, col));
                    continue;
                default:
                    throw new LexerException("Unexpected character '" + c + "' at " + line + ":" + col);
            }
        }

        out.add(new Token(TokenType.EOF, "", line, col));
        return out;
    }

    private Token number() {
        int startCol = col;
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek())) sb.append(next());
        if (peek() == '.') {
            sb.append(next());
            while (Character.isDigit(peek())) sb.append(next());
        }
        return new Token(TokenType.NUMBER, sb.toString(), line, startCol);
    }

    private Token string() {
        int startCol = col;
        next(); // consume opening "
        StringBuilder sb = new StringBuilder();
        while (peek() != '"' && peek() != '\0') {
            char c = next();
            if (c == '\\') {
                char n = peek();
                if (n == 'n') { next(); sb.append('\n'); }
                else if (n == 't') { next(); sb.append('\t'); }
                else sb.append(next());
            } else sb.append(c);
        }
        if (peek() == '\0') throw new LexerException("Unterminated string at " + line + ":" + col);
        next(); // closing "
        return new Token(TokenType.STRING, sb.toString(), line, startCol);
    }

    private Token identifierOrKeyword() {
        int startCol = col;
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(peek()) || peek() == '_' ) sb.append(next());
        String s = sb.toString().toUpperCase();

        // map keywords:
        switch (s) {
            case "PRINT": return new Token(TokenType.PRINT, s, line, startCol);
            case "INPUT": return new Token(TokenType.INPUT, s, line, startCol);
            case "IF": return new Token(TokenType.IF, s, line, startCol);
            case "THEN": return new Token(TokenType.THEN, s, line, startCol);
            case "ELSE": return new Token(TokenType.ELSE, s, line, startCol);
            case "END": return new Token(TokenType.END, s, line, startCol);
            case "ENDIF": return new Token(TokenType.ENDIF, s, line, startCol);
            case "END_IF": return new Token(TokenType.END_IF, s, line, startCol);
            case "WHILE": return new Token(TokenType.WHILE, s, line, startCol);
            case "DO": return new Token(TokenType.DO, s, line, startCol);
            case "ENDWHILE": return new Token(TokenType.ENDWHILE, s, line, startCol);
            case "END_WHILE": return new Token(TokenType.END_WHILE, s, line, startCol);
            case "FOR": return new Token(TokenType.FOR, s, line, startCol);
            case "FROM": return new Token(TokenType.FROM, s, line, startCol);
            case "TO": return new Token(TokenType.TO, s, line, startCol);
            case "ENDFOR": return new Token(TokenType.ENDFOR, s, line, startCol);
            case "END_FOR": return new Token(TokenType.END_FOR, s, line, startCol);
            case "FUNCTION": return new Token(TokenType.FUNCTION, s, line, startCol);
            case "RETURN": return new Token(TokenType.RETURN, s, line, startCol);
            case "AND": return new Token(TokenType.AND, s, line, startCol);
            case "OR": return new Token(TokenType.OR, s, line, startCol);
            case "NOT": return new Token(TokenType.NOT, s, line, startCol);
            default:
                // normal identifier, return original lexeme (case-preserve)
                return new Token(TokenType.IDENTIFIER, sb.toString(), line, startCol);
        }
    }
}
