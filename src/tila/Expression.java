package tila;

import java.util.List;

abstract class Expression {
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

        R visitExpressionStmt(Expr stmt);

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

    static class Assignment extends tila.Expression {
        Assignment(Token id, tila.Expression expr) {
            this.id = id;
            this.expr = expr;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        final Token id;
        final tila.Expression expr;

        @Override
        public String toString() {
            return String.format("Assignment{%s = %s}", id, expr);
        }
    }

    static class Binary extends tila.Expression {
        Binary(tila.Expression left, Token operator, tila.Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final tila.Expression left;
        final Token operator;
        final tila.Expression right;
    }

    static class Call extends tila.Expression {
        Call(tila.Expression callee, Token paren, List<tila.Expression> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        final tila.Expression callee;
        final Token paren;
        final List<tila.Expression> arguments;
    }

    static class Get extends tila.Expression {
        Get(tila.Expression object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }

        final tila.Expression object;
        final Token name;
    }

    static class Grouping extends tila.Expression {
        Grouping(tila.Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final tila.Expression expression;

        @Override
        public String toString() {
            return String.format("Grouping{%s}", expression);
        }
    }


    static class Program extends tila.Expression {
        Program(tila.Expression statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }

        final tila.Expression statements;

        @Override
        public String toString() {
            return "Program{begin " + statements + " end EOF}";
        }
    }

    static class Statements extends tila.Expression {
        Statements(tila.Expression statement, tila.Expression statements) {
            this.statement = statement;
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementExpr(this);
        }

        final tila.Expression statements;
        final tila.Expression statement;

        @Override
        public String toString() {
            return String.format("Statements{%s; %s}", statement, statements);
        }
    }

    static class Literal extends tila.Expression {
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
            return value.toString();
        }
    }

    static class Epsilon extends tila.Expression {
        Epsilon() {
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitEpsilonExpr(this);
        }

        @Override
        public String toString() {
            return "ε";
        }
    }

    static class Logical extends tila.Expression {
        Logical(tila.Expression left, Token operator, tila.Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

        final tila.Expression left;
        final Token operator;
        final tila.Expression right;
    }

    static class Set extends tila.Expression {
        Set(tila.Expression object, Token name, tila.Expression value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }

        final tila.Expression object;
        final Token name;
        final tila.Expression value;
    }

    static class Super extends tila.Expression {
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


    static class This extends tila.Expression {
        This(Token keyword) {
            this.keyword = keyword;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpr(this);
        }

        final Token keyword;
    }

    static class Unary extends tila.Expression {
        Unary(Token operator, tila.Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final tila.Expression right;

        @Override
        public String toString() {
            return String.format("(%s %s)", operator, right);
        }
    }

    static class Calculation extends tila.Expression {
        Calculation(Token operator, tila.Expression middle, tila.Expression right) {
            this.operator = operator;
            this.middle = middle;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDoubleUnaryExpr(this);
        }

        final Token operator;
        final tila.Expression middle;
        final tila.Expression right;

        @Override
        public String toString() {
            return String.format("Calculation{%s %s %s}", operator, middle, right);
        }
    }

    static class Variable extends tila.Expression {
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        final Token name;
    }

    static class Pair extends tila.Expression {
        Pair(tila.Expression left, tila.Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPairExpr(this);
        }

        final tila.Expression left;
        final tila.Expression right;

        @Override
        public String toString() {
            return String.format("Pair{%s %s}", left, right);
        }
    }

    static class Expr extends Pair {
        Expr(Expression left, Expression right) {
            super(left, right);
        }
        @Override
        public String toString() {
            return String.format("Expr{%s %s}", left, right);
        }
    }

    static class Expr1 extends Pair {
        Expr1(Expression left, Expression right) {
            super(left, right);
        }
        @Override
        public String toString() {
            return String.format("Expr1{%s %s}", left, right);
        }
    }

    static class Expr3 extends Pair {
        Expr3(Expression left, Expression right) {
            super(left, right);
        }
        @Override
        public String toString() {
            return String.format("Expr3{%s %s}", left, right);
        }
    }


    static class Block extends tila.Expression {
        Block(List<tila.Expression> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        final List<tila.Expression> statements;
    }

    static class Class extends tila.Expression {
        Class(Token name,
              tila.Expression.Variable superclass,
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
        final tila.Expression.Variable superclass;
        final List<Function> methods;
    }

//    static class Expr extends Expression {
//        Expr(Expression expression) {
//            this.expression = expression;
//        }
//
//        @Override
//        <R> R accept(Visitor<R> visitor) {
//            return visitor.visitExpressionStmt(this);
//        }
//
//        final Expression expression;
//    }

    static class Function extends tila.Expression {
        Function(Token name, List<Token> params, List<tila.Expression> body) {
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
        final List<tila.Expression> body;
    }

    static class If extends tila.Expression {
        If(tila.Expression condition, tila.Expression thenBranch, tila.Expression elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        final tila.Expression condition;
        final tila.Expression thenBranch;
        final tila.Expression elseBranch;
    }

    static class Print extends tila.Expression {
        Print(tila.Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        final tila.Expression expression;
    }

    static class Return extends tila.Expression {
        Return(Token keyword, tila.Expression value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        final Token keyword;
        final tila.Expression value;
    }

    static class Var extends tila.Expression {
        Var(Token name, tila.Expression initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        final Token name;
        final tila.Expression initializer;
    }

    static class While extends tila.Expression {
        While(tila.Expression condition, tila.Expression body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        final tila.Expression condition;
        final tila.Expression body;

        @Override
        public String toString() {
            return String.format("Loop{while %s do begin %s end}", condition, body);
        }
    }

    static class Decl extends tila.Expression {
        Decl(Token type, Token identifier, tila.Expression decl) {
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
        final tila.Expression decl;

        @Override
        public String toString() {
            return String.format("Decl{%s %s; %s}", type, identifier, decl);
        }
    }
}
