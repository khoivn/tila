package tila;

import java.util.List;

abstract class Expr {
    interface Visitor<R> {
        R visitAssignExpr(Assignment expr);

        R visitBinaryExpr(Binary expr);

        R visitCallExpr(Call expr);

        R visitGetExpr(Get expr);

        R visitGroupingExpr(Grouping expr);

        R visitProgramExpr(Program expr);

        R visitStatementExpr(Statements expr);

        R visitLiteralExpr(Literal expr);

        R visitEpsilonExpr(Epsilon expr);

        R visitLogicalExpr(Logical expr);

        R visitSetExpr(Set expr);

        R visitSuperExpr(Super expr);

        R visitThisExpr(This expr);

        R visitUnaryExpr(Unary expr);

        R visitDoubleUnaryExpr(Calculation expr);

        R visitVariableExpr(Variable expr);

        R visitPairExpr(Pair expr);

        R visitBlockStmt(Block stmt);

        R visitClassStmt(Class stmt);

        R visitExpressionStmt(Expression stmt);

        R visitFunctionStmt(Function stmt);

        R visitIfStmt(If stmt);

        R visitPrintStmt(Print stmt);

        R visitReturnStmt(Return stmt);

        R visitVarStmt(Var stmt);

        R visitWhileStmt(While stmt);

        R visitDecl(Decl stmt);

    }

    // Nested Expr classes here...
    abstract <R> R accept(Visitor<R> visitor);

    static class Assignment extends Expr {
        Assignment(Token id, Expr expr) {
            this.id = id;
            this.expr = expr;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        final Token id;
        final Expr expr;

        @Override
        public String toString() {
            return String.format("\nAssignment{%s = %s}", id, expr);
        }
    }

    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Call extends Expr {
        Call(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        final Expr callee;
        final Token paren;
        final List<Expr> arguments;
    }

    static class Get extends Expr {
        Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }

        final Expr object;
        final Token name;
    }

    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expr expression;

        @Override
        public String toString() {
            return "Grouping{" +
                    "expression=" + expression +
                    '}';
        }
    }


    static class Program extends Expr {
        Program(Expr statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }

        final Expr statements;

        @Override
        public String toString() {
            return "Program{begin " + statements + " end EOF}";
        }
    }

    static class Statements extends Expr {
        Statements(Expr statement, Expr statements) {
            this.statement = statement;
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementExpr(this);
        }

        final Expr statements;
        final Expr statement;

        @Override
        public String toString() {
            return String.format("\nStatements{%s; %s}", statement, statements);
        }
    }

    static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;

        @Override
        public String toString() {
            return String.format("\nLiteral{%s}", value);
        }
    }

    static class Epsilon extends Expr {
        Epsilon() {
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitEpsilonExpr(this);
        }

        @Override
        public String toString() {
            return "Epsilon{}";
        }
    }

    static class Logical extends Expr {
        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Set extends Expr {
        Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }

        final Expr object;
        final Token name;
        final Expr value;
    }

    static class Super extends Expr {
        Super(Token keyword, Token method) {
            this.keyword = keyword;
            this.method = method;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSuperExpr(this);
        }

        final Token keyword;
        final Token method;
    }


    static class This extends Expr {
        This(Token keyword) {
            this.keyword = keyword;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpr(this);
        }

        final Token keyword;
    }

    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expr right;
    }

    static class Calculation extends Expr {
        Calculation(Token operator, Expr middle, Expr right) {
            this.operator = operator;
            this.middle = middle;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDoubleUnaryExpr(this);
        }

        final Token operator;
        final Expr middle;
        final Expr right;

        @Override
        public String toString() {
            return String.format("\nCalculation{%s %s %s}", operator, middle, right);
        }
    }

    static class Variable extends Expr {
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        final Token name;
    }

    static class Pair extends Expr {
        Pair(Expr left, Expr right) {
            this.left = left;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPairExpr(this);
        }

        final Expr left;
        final Expr right;

        @Override
        public String toString() {
            return String.format("\nPair{%s %s}", left, right);
        }
    }


    static class Block extends Expr {
        Block(List<Expr> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        final List<Expr> statements;
    }

    static class Class extends Expr {
        Class(Token name,
              Expr.Variable superclass,
              List<Function> methods) {
            this.name = name;
            this.superclass = superclass;
            this.methods = methods;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStmt(this);
        }

        final Token name;
        final Expr.Variable superclass;
        final List<Function> methods;
    }

    static class Expression extends Expr {
        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        final Expr expression;
    }

    static class Function extends Expr {
        Function(Token name, List<Token> params, List<Expr> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }

        final Token name;
        final List<Token> params;
        final List<Expr> body;
    }

    static class If extends Expr {
        If(Expr condition, Expr thenBranch, Expr elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        final Expr condition;
        final Expr thenBranch;
        final Expr elseBranch;
    }

    static class Print extends Expr {
        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        final Expr expression;
    }

    static class Return extends Expr {
        Return(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        final Token keyword;
        final Expr value;
    }

    static class Var extends Expr {
        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        final Token name;
        final Expr initializer;
    }

    static class While extends Expr {
        While(Expr condition, Expr body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        final Expr condition;
        final Expr body;
    }

    static class Decl extends Expr {
        Decl(Token type, Token identifier, Expr decl) {
            this.type = type;
            this.identifier = identifier;
            this.decl = decl;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDecl(this);
        }

        final Token type;
        final Token identifier;
        final Expr decl;

        @Override
        public String toString() {
            return String.format("\nDecl{%s %s; %s}", type, identifier, decl);
        }
    }
}
