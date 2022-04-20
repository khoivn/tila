//package tila1;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class Tila {
//    static boolean hadError = false;
//    static String inputFile = null;
//
//    public static void main(String[] args) throws IOException {
//        if (args.length > 1) {
//            System.out.println("Error: Illegal input");
//            System.exit(64);
//        } else if (args.length == 1) {
//            inputFile = args[0];
//            runFile(args[0]);
//        } else {
//            runPrompt();
//        }
//    }
//
//    private static void runFile(String path) throws IOException {
//        byte[] bytes = Files.readAllBytes(Paths.get(path));
//        run(new String(bytes, Charset.defaultCharset()));
//        if (hadError) System.exit(65);
//    }
//
//    private static void runPrompt() throws IOException {
//        InputStreamReader input = new InputStreamReader(System.in);
//        BufferedReader reader = new BufferedReader(input);
//        for (; ; ) {
//            System.out.print("> ");
//            String line = reader.readLine();
//            if (line == null) break;
//            run(line);
//            hadError = false;
//        }
//    }
//
//    private static void run(String source) {
//        Scanner scanner = new Scanner(source);
//        List<Token> tokens = scanner.scanTokens();
//        // For now, just print the tokens.
//        for (Token token : tokens) {
//            System.out.println(token);
//        }
//
//        Parser1 parser = new Parser1(tokens);
//        Expr expression = parser.parse();
//        // Stop if there was a syntax error.
//        if (hadError) return;
//        System.out.println(expression);
////        System.out.println(new AstPrinter().print(expression));
//    }
//
//    static void error(int line, int column, String message) {
//        report(line, column, "", message);
//    }
//
//    static void error(Token token, String message) {
//        System.err.println(message);
////        report(line, column, "", message);
//    }
//
//    private static String getFilePath() {
//        if (inputFile == null) return "";
//        return Paths.get(inputFile).toAbsolutePath().toString();
//    }
//
//    private static void report(int line, int column, String where, String message) {
//        if (inputFile != null) System.err.println(getFilePath() + ":" + line + ":" + column);
//        System.err.println("Error" + where + ": " + message);
//        hadError = true;
//    }
//}