/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pseudocode.parser;

/**
 *
 * @author Josh
 */
import pseudocode.lexer.*;
import pseudocode.errors.ParserException;
import pseudocode.parser.expressions.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private Token next() {
        if (!isAtEnd()) pos++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private boolean match(TokenType... types) {
        for (TokenType t : types) {
            if (check(t)) { next(); return true; }
        }
        return false;
    }

    private void consume(TokenType type, String message) {
        if (check(type)) { next(); return; }
        throw error(peek(), message);
    }

    private ParserException error(Token token, String message) {
        return new ParserException("Parse error at " + token.line + ":" + token.col + " - " + message + " (found '" + token.lexeme + "')");
    }

    public ProgramNode parseProgram() {
        List<ASTNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            // ignore NEWLINE tokens between statements
            if (match(TokenType.NEWLINE)) continue;
            if (check(TokenType.EOF)) break;
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private ASTNode parseStatement() {
        if (match(TokenType.PRINT)) {
            ASTNode expr = parseExpression();
            // optional newline consumed by top loop
            return new PrintNode(expr);
        }

        if (match(TokenType.INPUT)) {
            if (!check(TokenType.IDENTIFIER))
                throw error(peek(), "Expected variable name after INPUT");
            String name = next().lexeme;
            return new InputNode(name, null);
        }

        if (check(TokenType.IDENTIFIER) && peekAheadIs(TokenType.EQUAL)) {
            String name = next().lexeme;
            consume(TokenType.EQUAL, "Expected '=' for assignment");
            ASTNode expr = parseExpression();
            return new AssignmentNode(name, expr);
        }

        if (match(TokenType.IF)) {
            ASTNode cond = parseExpression();
            consume(TokenType.THEN, "Expected THEN after IF condition");
            BlockNode thenBlock = parseBlockUntil(TokenType.ELSE, TokenType.ENDIF, TokenType.END_IF, TokenType.END);
            BlockNode elseBlock = null;
            if (match(TokenType.ELSE)) {
                elseBlock = parseBlockUntil(TokenType.ENDIF, TokenType.END_IF, TokenType.END);
            }
            // Handle END IF (two tokens) or ENDIF/END_IF (single token)
            if (check(TokenType.ENDIF) || check(TokenType.END_IF)) {
                next();
            } else if (check(TokenType.END)) {
                next();
                // Check if next token is IF (for "END IF" syntax)
                if (check(TokenType.IF)) {
                    next();
                }
            }
            return new IfNode(cond, thenBlock, elseBlock);
        }

        if (match(TokenType.WHILE)) {
            ASTNode cond = parseExpression();
            consume(TokenType.DO, "Expected DO after WHILE condition");
            BlockNode body = parseBlockUntil(TokenType.ENDWHILE, TokenType.END_WHILE, TokenType.END);
            // Handle END WHILE (two tokens) or ENDWHILE/END_WHILE (single token)
            if (check(TokenType.ENDWHILE) || check(TokenType.END_WHILE)) {
                next();
            } else if (check(TokenType.END)) {
                next();
                // Check if next token is WHILE (for "END WHILE" syntax)
                if (check(TokenType.WHILE)) {
                    next();
                }
            }
            return new WhileNode(cond, body);
        }

        if (match(TokenType.FOR)) {
            if (!check(TokenType.IDENTIFIER)) throw error(peek(), "Expected iterator name after FOR");
            String it = next().lexeme;
            consume(TokenType.FROM, "Expected FROM in FOR loop");
            ASTNode start = parseExpression();
            consume(TokenType.TO, "Expected TO in FOR loop");
            ASTNode end = parseExpression();
            consume(TokenType.DO, "Expected DO in FOR loop");
            BlockNode body = parseBlockUntil(TokenType.ENDFOR, TokenType.END_FOR, TokenType.END);
            // Handle END FOR (two tokens) or ENDFOR/END_FOR (single token)
            if (check(TokenType.ENDFOR) || check(TokenType.END_FOR)) {
                next();
            } else if (check(TokenType.END)) {
                next();
                // Check if next token is FOR (for "END FOR" syntax)
                if (check(TokenType.FOR)) {
                    next();
                }
            }
            return new ForNode(it, start, end, body);
        }

        if (match(TokenType.FUNCTION)) {
            if (!check(TokenType.IDENTIFIER)) throw error(peek(), "Expected function name after FUNCTION");
            String name = next().lexeme;
            consume(TokenType.LPAREN, "Expected '(' after function name");
            List<String> params = new ArrayList<>();
            if (!check(TokenType.RPAREN)) {
                do {
                    if (!check(TokenType.IDENTIFIER)) throw error(peek(), "Expected parameter name");
                    params.add(next().lexeme);
                } while (match(TokenType.COMMA));
            }
            consume(TokenType.RPAREN, "Expected ')' after parameters");
            BlockNode body = parseBlockUntil(TokenType.END);
            if (check(TokenType.END)) next();
            return new FunctionNode(name, params, body);
        }

        if (match(TokenType.RETURN)) {
            ASTNode expr = null;
            if (!check(TokenType.NEWLINE) && !check(TokenType.EOF)) {
                expr = parseExpression();
            }
            return new ReturnNode(expr);
        }

        // If none matched, maybe an expression statement
        ASTNode expr = parseExpression();
        return expr;
    }

    private BlockNode parseBlockUntil(TokenType... endTokens) {
        List<ASTNode> statements = new ArrayList<>();
        while (!isAtEnd() && !isOneOf(peek().type, endTokens)) {
            if (match(TokenType.NEWLINE)) continue;
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private boolean isOneOf(TokenType t, TokenType[] list) {
        for (TokenType tt : list) if (tt == t) return true;
        return false;
    }

    private boolean peekAheadIs(TokenType t) {
        if (pos + 1 >= tokens.size()) return false;
        return tokens.get(pos + 1).type == t;
    }

    // Expression grammar:
    // expression → logicalOr
    // logicalOr → logicalAnd ( ( "OR" ) logicalAnd )*
    // logicalAnd → equality ( ( "AND" ) equality )*
    // equality → comparison ( ( "==" | "!=" ) comparison )*
    // comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )*
    // term → factor ( ( "+" | "-" ) factor )*
    // factor → unary ( ( "*" | "/" | "%" ) unary )*
    // unary → ( "-" | "NOT") unary | primary
    // primary → NUMBER | STRING | IDENTIFIER | "(" expression ")"

    private ASTNode parseExpression() {
        return parseLogicalOr();
    }

    private ASTNode parseLogicalOr() {
        ASTNode node = parseLogicalAnd();
        while (match(TokenType.OR)) {
            String op = previous().lexeme;
            ASTNode right = parseLogicalAnd();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseLogicalAnd() {
        ASTNode node = parseEquality();
        while (match(TokenType.AND)) {
            String op = previous().lexeme;
            ASTNode right = parseEquality();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseEquality() {
        ASTNode node = parseComparison();
        while (match(TokenType.EQEQ) || match(TokenType.BANGEQ)) {
            String op = previous().lexeme;
            ASTNode right = parseComparison();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseComparison() {
        ASTNode node = parseTerm();
        while (match(TokenType.GT) || match(TokenType.GTE) || match(TokenType.LT) || match(TokenType.LTE)) {
            String op = previous().lexeme;
            ASTNode right = parseTerm();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseTerm() {
        ASTNode node = parseFactor();
        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            String op = previous().lexeme;
            ASTNode right = parseFactor();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseFactor() {
        ASTNode node = parseUnary();
        while (match(TokenType.STAR) || match(TokenType.SLASH) || match(TokenType.PERCENT)) {
            String op = previous().lexeme;
            ASTNode right = parseUnary();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNode parseUnary() {
        if (match(TokenType.MINUS)) {
            String op = previous().lexeme;
            ASTNode right = parseUnary();
            return new UnaryOpNode(op, right);
        }
        if (match(TokenType.NOT)) {
            String op = previous().lexeme;
            ASTNode right = parseUnary();
            return new UnaryOpNode(op, right);
        }
        return parsePrimary();
    }

    private ASTNode parsePrimary() {
        if (match(TokenType.NUMBER)) {
            String v = previous().lexeme;
            return new LiteralNode(Double.parseDouble(v));
        }

        if (match(TokenType.STRING)) {
            return new LiteralNode(previous().lexeme);
        }

        if (match(TokenType.IDENTIFIER)) {
            String name = previous().lexeme;
            // Check if it's a function call
            if (match(TokenType.LPAREN)) {
                List<ASTNode> args = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        args.add(parseExpression());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments");
                return new pseudocode.parser.expressions.CallNode(name, args);
            }
            return new VariableNode(name);
        }

        if (match(TokenType.LPAREN)) {
            ASTNode expr = parseExpression();
            consume(TokenType.RPAREN, "Expected closing ')'");
            return expr;
        }

        throw error(peek(), "Expected expression");
    }
}
