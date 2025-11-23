/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package pseudocode.lexer;

/**
 *
 * @author Josh
 */
public enum TokenType {
    // single-char
    PLUS, MINUS, STAR, SLASH, PERCENT,
    LPAREN, RPAREN, COMMA,

    // one or two char
    EQUAL, EQEQ, BANGEQ, GT, LT, GTE, LTE,

    // literals
    NUMBER, STRING, IDENTIFIER,

    // keywords
    PRINT, INPUT,
    IF, THEN, ELSE, ENDIF, END_IF, END, WHILE, DO, ENDWHILE, END_WHILE,
    FOR, FROM, TO, ENDFOR, END_FOR,
    FUNCTION, RETURN,
    AND, OR, NOT,

    // special
    NEWLINE, EOF
}
