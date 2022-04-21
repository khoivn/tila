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

    static class Assignment extends Expression {
        Assignment(Token id, Expression expr) {
            this.id = id;
            this.expr = expr;
        }

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        final Token id;
        final Expression expr;

        @Override
        public String toString() {
            return String.format("Assignment{%s = %s}", id, expr);
        }
    }

    static class Binary extends Expression {
        Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expression left;
        final Token operator;
        final Expression right;
    }

    static class Call extends Expression {
        Call(Expression callee, Token paren, List<Expression> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        final Expression callee;
        final Token paren;
        final List<Expression> arguments;
    }

    static class Get extends Expression {
        Get(Expression object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }

        final Expression object;
        final Token name;
    }

    static class Grouping extends Expression {
        Grouping(Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expression expression;

        @Override
        public String toString() {
            return String.format("Grouping{%s}", expression);
        }
    }


    static class Program extends Expression {
        Program(Expression statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitProgramExpr(this);
        }

        final Expression statements;

        @Override
        public String toString() {
            return "Program{begin " + statements + " end EOF}";
        }
    }

    static class Statements extends Expression {
        Statements(Expression statement, Expression statements) {
            this.statement = statement;
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitStatementExpr(this);
        }

        final Expression statements;
        final Expression statement;

        @Override
        public String toString() {
            return String.format("Statements{%s; %s}", statement, statements);
        }
    }

    static class Literal extends Expression {
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

    static class Epsilon extends Expression {
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

    static class Logical extends Expression {
        Logical(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

        final Expression left;
        final Token operator;
        final Expression right;
    }

    static class Set extends Expression {
        Set(Expression object, Token name, Expression value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }

        final Expression object;
        final Token name;
        final Expression value;
    }

    static class Super extends Expression {
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


    static class This extends Expression {
        This(Token keyword) {
            this.keyword = keyword;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpr(this);
        }

        final Token keyword;
    }

    static class Unary extends Expression {
        Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expression right;

        @Override
        public String toString() {
            return String.format("(%s %s)", operator, right);
        }
    }

    static class Calculation extends Expression {
        Calculation(Token operator, Expression middle, Expression right) {
            this.operator = operator;
            this.middle = middle;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDoubleUnaryExpr(this);
        }

        final Token operator;
        final Expression middle;
        final Expression right;

        @Override
        public String toString() {
            return String.format("Calculation{%s %s %s}", operator, middle, right);
        }
    }

    static class Variable extends Expression {
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        final Token name;
    }

    static class Pair extends Expression {
        Pair(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPairExpr(this);
        }

        final Expression left;
        final Expression right;

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


    static class Block extends Expression {
        Block(List<Expression> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        final List<Expression> statements;
    }

    static class Class extends Expression {
        Class(Token name,
              Expression.Variable superclass,
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
        final Expression.Variable superclass;
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

    static class Function extends Expression {
        Function(Token name, List<Token> params, List<Expression> body) {
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
        final List<Expression> body;
    }

    static class If extends Expression {
        If(Expression condition, Expression thenBranch, Expression elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        final Expression condition;
        final Expression thenBranch;
        final Expression elseBranch;
    }

    static class Print extends Expression {
        Print(Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        final Expression expression;
    }

    static class Return extends Expression {
        Return(Token keyword, Expression value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }

        final Token keyword;
        final Expression value;
    }

    static class Var extends Expression {
        Var(Token name, Expression initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        final Token name;
        final Expression initializer;
    }

    static class While extends Expression {
        While(Expression condition, Expression body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        final Expression condition;
        final Expression body;

        @Override
        public String toString() {
            return String.format("Loop{while %s do begin %s end}", condition, body);
        }
    }

    static class Decl extends Expression {
        Decl(Token type, Token identifier) {
            this.type = type;
            this.identifier = identifier;
//            this.decl = decl;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDecl(this);
        }

        final Token type;
        final Token identifier;
//        final Expression decl;

        @Override
        public String toString() {
            return String.format("Decl{%s %s}", type, identifier);
        }
    }
}
