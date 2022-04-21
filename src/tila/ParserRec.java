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


    private Expression program() {
        if (match(BEGIN)) {
            Expression statements = statements();
            consume(END, "Expect 'end' after expression");
            return new Expression.Program(statements);
        }
        throw error(peek(), "Expect expression");
    }

    private Expression statements() {
        if (peek().type == END) {
            return new Expression.Epsilon();
        }
        Expression statement = statement();
        if (match(SEMICOLON)) {
            Expression statements = statements();
            return new Expression.Statements(statement, statements);
        }
        throw error(peek(), "Expect 'end' or ';'");
    }

    private Expression statement() {
        if (match(PRINT)) {
            Token operator = previous();
            Expression right = expr();
            return new Expression.Unary(operator, right);
        } else if (match(WHILE)) {
            return loop();
        } else if (match(IDENTIFIER)) {
            return assignment();
        } else {
            return decl();
        }
//        throw error(peek(), "Expect statement");
    }

    private Expression decl() {
//        if (match(INT)) {
//            Token type = previous();
//            Token identifier = consume(IDENTIFIER, "Expect identifier");
//            Expression decl = decl();
//            return new Expression.Decl(type, identifier, decl);
//        }
//        return new Expression.Epsilon();
        Token type = consume(INT, "Expect type");
        Token identifier = consume(IDENTIFIER, "Expect identifier");
        return new Expression.Decl(type, identifier);
    }

    private Expression assignment() {
        Token identifier = previous();
        consume(EQUAL, "Expect '='");
        Expression expr = expr();
        return new Expression.Assignment(identifier, expr);
    }

    private Expression loop() {
        Expression condition = expr();
        consume(DO, "Expect 'do'");
        consume(BEGIN, "Expect 'begin'");
        Expression body = statements();
        consume(END, "Expect 'end'");
        return new Expression.While(condition, body);
    }

    private Expression expr() {
        return new Expression.Expr(expr1(), expr2());
    }

    private Expression expr1() {
        return new Expression.Expr1(expr3(), expr4());
    }

    private Expression expr2() {
        if (match(MINUS)) {
            Token operator = previous();
            Expression middle = expr1();
            Expression right = expr2();
            return new Expression.Calculation(operator, middle, right);
        }
        return new Expression.Epsilon();
    }

    private Expression expr3() {
        return new Expression.Expr3(expr5(), expr7());
    }

    private Expression expr4() {
        if (match(STAR)) {
            Token operator = previous();
            Expression middle = expr3();
            Expression right = expr4();
            return new Expression.Calculation(operator, middle, right);
        }
        return new Expression.Epsilon();
    }

    private Expression expr5() {
        if (match(LEFT_PAREN)) {
            Expression expr = expr();
            consume(RIGHT_PAREN, "Expect ')' after expression");
            return new Expression.Grouping(expr);
        }
        return expr6();
    }

    private Expression expr6() {
        if (match(NUMBER, IDENTIFIER)) {
            return new Expression.Literal(previous());
        }
        throw error(peek(), "Expect identifier or number");
    }

    private Expression expr7() {
        if (match(CARET)) {
            Token operator = previous();
            Expression right = expr3();
            return new Expression.Unary(operator, right);
        }
        return new Expression.Epsilon();
    }


    Expression parse() {
        try {
            return program();
        } catch (ParseError error) {
            return null;
        }
    }
}
