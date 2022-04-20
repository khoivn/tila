package tila;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tila.ExpressionEnum.Expr;
import static tila.ExpressionEnum.*;
import static tila.TokenType.*;

public class Parser {
    private int current = 0;


    private final List<Token> tokens;

    static List<ITokenType> TERMINATES = List.of(LEFT_PAREN, RIGHT_PAREN, MINUS, SEMICOLON, STAR, CARET, EQUAL, IDENTIFIER, STRING, NUMBER, PRINT, WHILE, DO, BEGIN, END, INT, EOF, EPSILON);
    static List<ITokenType> NON_TERMINATES = List.of(Program, Statements, Statement, Decl, Assigment, Loop, Type, Expr, Expr1, Expr2, Expr3, Expr4, Expr5, Expr6, Expr7);
    static List<Grammar> GRAMMAR = new ArrayList<>() {{
        add(new Grammar(Program, List.of(BEGIN, Statements, END, EOF)));
        add(new Grammar(Statements, List.of(Statement, SEMICOLON, Statements)));
        add(new Grammar(Statements, List.of(EPSILON)));
        add(new Grammar(Statement, List.of(Decl)));
        add(new Grammar(Statement, List.of(Assigment)));
        add(new Grammar(Statement, List.of(Loop)));
        add(new Grammar(Statement, List.of(PRINT, Expr)));
        add(new Grammar(Decl, List.of(Type, IDENTIFIER, SEMICOLON, Decl)));
        add(new Grammar(Decl, List.of(EPSILON)));
        add(new Grammar(Type, List.of(INT)));
        add(new Grammar(Assigment, List.of(IDENTIFIER, EQUAL, Expr)));
        add(new Grammar(Expr, List.of(Expr1, Expr2)));
        add(new Grammar(Expr2, List.of(MINUS, Expr1, Expr2)));
        add(new Grammar(Expr2, List.of(EPSILON)));
        add(new Grammar(Expr1, List.of(Expr3, Expr4)));
        add(new Grammar(Expr4, List.of(STAR, Expr3, Expr4)));
        add(new Grammar(Expr4, List.of(EPSILON)));
        add(new Grammar(Expr3, List.of(Expr5, Expr7)));
        add(new Grammar(Expr7, List.of(CARET, Expr3)));
        add(new Grammar(Expr7, List.of(EPSILON)));
        add(new Grammar(Expr5, List.of(LEFT_PAREN, Expr6, RIGHT_PAREN)));
        add(new Grammar(Expr5, List.of(Expr6)));
        add(new Grammar(Expr6, List.of(IDENTIFIER)));
        add(new Grammar(Expr6, List.of(NUMBER)));
        add(new Grammar(Loop, List.of(WHILE, Expr, DO, BEGIN, Statements, END)));
    }};

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    static Map<ITokenType, List<ITokenType>> FIRST = new HashMap<>();
    static Map<ITokenType, List<ITokenType>> FOLLOW = new HashMap<>();
    static Map<ITokenType, List<ITokenType>> FIRST_PLUS = new HashMap<>();

    private static class ParseError extends RuntimeException {
    }

    static void first() {
        for (ITokenType terminate : Stream.concat(TERMINATES.stream(), Stream.of(EOF, EPSILON)).collect(Collectors.toList())) {
            FIRST.put(terminate, new ArrayList<>() {{
                add(terminate);
            }});
        }
        for (ITokenType nt : NON_TERMINATES) {
            FIRST.put(nt, new ArrayList<>());
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Grammar grammar : GRAMMAR) {
                List<ITokenType> right = grammar.right;
                ITokenType firstRight = right.get(0);
                List<ITokenType> rhs = new ArrayList<>();
                int i = -1;
                if (TERMINATES.contains(firstRight) || NON_TERMINATES.contains(firstRight)) {
                    rhs = FIRST.get(firstRight).stream().filter(a -> a != EPSILON).collect(Collectors.toList());
                    i = 0;
                    while (FIRST.get(right.get(i)).contains(EPSILON) && i <= right.size() - 2) {
                        rhs.addAll(FIRST.get(right.get(i + 1)).stream().filter(a -> a != EPSILON).collect(Collectors.toList()));
                        i = i + 1;
                    }
                }
                if (i == right.size() - 1 && FIRST.get(right.get(right.size() - 1)).contains(EPSILON)) {
                    rhs.add(EPSILON);
                }
                changed = changed || rhs.stream().anyMatch(e -> !FIRST.get(grammar.left).contains(e));
                FIRST.get(grammar.left).addAll(rhs);
                FIRST.put(grammar.left, new ArrayList<>(new HashSet<>(FIRST.get(grammar.left))));
            }
        }
    }

    static void follow() {
        for (ITokenType nt : NON_TERMINATES) {
            FOLLOW.put(nt, new ArrayList<>());
        }
        FOLLOW.put(Program, new ArrayList<>() {{
            add(EOF);
        }});

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Grammar grammar : GRAMMAR) {
                List<ITokenType> right = grammar.right;
                List<ITokenType> trailer = FOLLOW.get(grammar.left);
                for (int i = right.size() - 1; i >= 0; i--) {
                    ITokenType beta = right.get(i);
                    if (NON_TERMINATES.contains(beta)) {
                        changed = changed || trailer.stream().anyMatch(e -> !FOLLOW.get(beta).contains(e));
                        FOLLOW.get(beta).addAll(trailer);
                        FOLLOW.put(beta, new ArrayList<>(new HashSet<>(FOLLOW.get(beta))));
//                        FOLLOW.put(beta, FOLLOW.get(beta));


                        if (FIRST.get(beta).contains(EPSILON)) {
                            trailer.addAll(FIRST.get(beta).stream().filter(a -> a != EPSILON).collect(Collectors.toList()));
                        } else {
                            trailer = FIRST.get(beta);
                        }
                    } else {
                        trailer = FIRST.get(beta);
                    }
                }
            }
        }
    }

    static void firstPlus() {
        for (Grammar grammar : GRAMMAR) {
            List<ITokenType> first = FIRST.get(grammar.right.get(0));
            List<ITokenType> follow = FOLLOW.get(grammar.left);

            if (first.contains(EPSILON)) {
                grammar.firstPlus = first;
            } else {
                grammar.firstPlus = Stream.concat(first.stream(), follow.stream()).collect(Collectors.toList());
            }

            System.out.print(grammar.left);
            System.out.print(grammar.right);
            System.out.println(grammar.firstPlus);
        }
    }

    public static void main(String[] args) {
        first();
        follow();
        firstPlus();

//        FIRST.entrySet().stream().forEach(e -> {
//            System.out.print(e.getKey());
//            System.out.println(e.getValue());
//        });
        System.out.println(FIRST);
        System.out.println(FOLLOW);

        Parser parser = new Parser(List.of());
        Token word = parser.peek();
        if (parser.program()) {

        }


    }

    private ParseError error(Token token, String message) {
        Tila.error(token, message);
        return new ParseError();
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


    boolean program() {
        //Program	-> 	begin Statements end EOF
        if (check(BEGIN)) {
            advance();
            if (!statements()) {
                throw error(peek(), "Error");

            }
            if (!check(END)) {
                throw error(peek(), "Error");
            }
            advance();
            return true;
        } else {
            throw error(peek(), "Error");
        }
    }

    boolean statements() {
        return false;
    }


}
