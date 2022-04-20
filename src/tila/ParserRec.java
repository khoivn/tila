package tila;

import java.util.List;

import static tila.TokenType.*;

public class ParserRec {

    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    ParserRec(List<Token> tokens) {
        this.tokens = tokens;
    }


    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }


    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Tila.error(token, message);
        return new ParseError();
    }


    private Expr program() {
        if (match(BEGIN)) {
            Expr statements = statements();
            consume(END, "Expect 'end' after expression");
            return new Expr.Program(statements);
        }
        throw error(peek(), "Expect expression");
    }

    private Expr statements() {
        if (peek().type == END) {
            return new Expr.Epsilon();
        }
        Expr statement = statement();
        if (match(SEMICOLON)) {
            Expr statements = statements();
            return new Expr.Statements(statement, statements);
        }
        throw error(peek(), "Expect 'end' or ';'");
    }

    private Expr statement() {
        if (match(PRINT)) {
            Token operator = previous();
            Expr right = expr();
            return new Expr.Unary(operator, right);
        } else if (match(WHILE)) {
            return loop();
        } else if (match(IDENTIFIER)) {
            return assignment();
        } else {
            return decl();
        }
//        throw error(peek(), "Expect statement");
    }

    private Expr decl() {
        if (peek().type == INT) {
            Token type = peek();
            advance();
            Token identifier = consume(IDENTIFIER, "Expect identifier");
            Expr decl = decl();
            return new Expr.Decl(type, identifier, decl);
        }
        return new Expr.Epsilon();
    }

    private Expr assignment() {
        Token identifier = previous();
        consume(EQUAL, "Expect '='");
        Expr expr = expr();
        return new Expr.Assignment(identifier, expr);
    }

    private Expr loop() {
        Expr condition = expr();
        consume(DO, "Expect 'do'");
        consume(BEGIN, "Expect 'begin'");
        Expr body = statements();
        consume(END, "Expect 'end'");
        return new Expr.While(condition, body);
    }

    private Expr expr() {
        return new Expr.Pair(expr1(), expr2());
    }

    private Expr expr1() {
        return new Expr.Pair(expr3(), expr4());
    }

    private Expr expr2() {
        if (match(MINUS)) {
            Token operator = previous();
            Expr middle = expr1();
            Expr right = expr2();
            return new Expr.Calculation(operator, middle, right);
        }
        return new Expr.Epsilon();
    }

    private Expr expr3() {
        return new Expr.Pair(expr5(), expr7());
    }

    private Expr expr4() {
        if (match(STAR)) {
            Token operator = previous();
            Expr middle = expr3();
            Expr right = expr4();
            return new Expr.Calculation(operator, middle, right);
        }
        return new Expr.Epsilon();
    }

    private Expr expr5() {
        if (match(LEFT_PAREN)) {
            Expr expr = expr();
            consume(RIGHT_PAREN, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }
        return new Expr.Grouping(expr6());
    }

    private Expr expr6() {
        if (match(NUMBER, IDENTIFIER)) {
            return new Expr.Literal(previous().literal);
        }
        throw error(peek(), "Expect identifier or number");
    }

    private Expr expr7() {
        if (match(CARET)) {
            Token operator = previous();
            Expr right = expr3();
            return new Expr.Unary(operator, right);
        }
        return new Expr.Epsilon();
    }


    Expr parse() {
        try {
            return program();
        } catch (ParseError error) {
            return null;
        }
    }
}
