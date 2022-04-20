package tila1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tila1.Expression.*;
import static tila1.TokenType.*;

public class Parser {
    private int current = 0;


    private final List<Token> tokens;

    static List<ITokenType> TERMINATES = List.of(LEFT_PAREN, RIGHT_PAREN,
            MINUS, STAR, SLASH,
            EPSILON,
            NUMBER,
            NAME, PLUS, EOF);
    static List<ITokenType> NON_TERMINATES = List.of(Goal, Expr, Term, Expr_, Factor, Term_);
    static List<Grammar> GRAMMAR = new ArrayList<>() {{
        add(new Grammar(Goal, List.of(Expr)));
        add(new Grammar(Expr, List.of(Term, Expr_)));
        add(new Grammar(Expr_, List.of(PLUS, Term, Expr_)));
        add(new Grammar(Expr_, List.of(MINUS, Term, Expr_)));
        add(new Grammar(Expr_, List.of(EPSILON)));
        add(new Grammar(Term, List.of(Factor, Term_)));
        add(new Grammar(Term_, List.of(STAR, Factor, Term_)));
        add(new Grammar(Term_, List.of(SLASH, Factor, Term_)));
        add(new Grammar(Term_, List.of(EPSILON)));
        add(new Grammar(Factor, List.of(LEFT_PAREN, Expr, RIGHT_PAREN)));
        add(new Grammar(Factor, List.of(NUMBER)));
        add(new Grammar(Factor, List.of(NAME)));

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
//                if (TERMINATES.contains(firstRight) || NON_TERMINATES.contains(firstRight)) {
                if (true) {
                    rhs = FIRST.get(firstRight).stream().filter(a -> a != EPSILON).collect(Collectors.toList());
                    i = 1;
                    while (FIRST.get(right.get(i - 1)).contains(EPSILON) && i <= right.size() - 1) {
                        rhs.addAll(FIRST.get(right.get(i)).stream().filter(a -> a != EPSILON).collect(Collectors.toList()));
                        i = i + 1;
                    }
                }
                if (i == right.size() && FIRST.get(right.get(right.size() - 1)).contains(EPSILON)) {
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

        FOLLOW.put(Goal, new ArrayList<>() {{
            add(EOF);
        }});

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Grammar grammar : GRAMMAR) {
                List<ITokenType> right = grammar.right;
                List<ITokenType> trailer = FOLLOW.get(grammar.left);
                for (int i = right.size(); i >= 1; i--) {
                    ITokenType beta = right.get(i - 1);
                    if (NON_TERMINATES.contains(beta)) {
                        changed = changed || trailer.stream().anyMatch(e -> !FOLLOW.get(beta).contains(e));
                        FOLLOW.get(beta).addAll(trailer);
                        FOLLOW.put(beta, new ArrayList<>(new HashSet<>(FOLLOW.get(beta))));

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
//        firstPlus();

//        FIRST.entrySet().stream().forEach(e -> {
//            System.out.print(e.getKey());
//            System.out.println(e.getValue());
//        });
        System.out.println(FIRST);
        System.out.println(FOLLOW);

        Parser parser = new Parser(List.of());
        Token word = parser.peek();


    }
//
//    private ParseError error(Token token, String message) {
//        Tila.error(token, message);
//        return new ParseError();
//    }

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


    boolean statements() {
        return false;
    }


}
