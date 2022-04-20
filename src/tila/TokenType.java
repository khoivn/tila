package tila;

enum TokenType implements ITokenType {
    LEFT_PAREN, RIGHT_PAREN,
    MINUS, SEMICOLON, STAR, CARET,
    EQUAL, EPSILON,
    IDENTIFIER, STRING, NUMBER,
    PRINT, WHILE, DO, BEGIN, END, INT,
    EOF
}