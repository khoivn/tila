package tila1;

enum TokenType implements ITokenType {
    LEFT_PAREN, RIGHT_PAREN,
    MINUS, STAR, SLASH,
    EPSILON,
    NUMBER,
    NAME, PLUS, EOF
}