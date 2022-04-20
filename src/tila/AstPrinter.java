package tila;

class AstPrinter implements Expression.Visitor<String> {
    String print(Expression expr) {
        return expr.accept(this);
    }


    @Override
    public String visitAssignExpr(Expression.Assignment expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expression.Binary expr) {
        return parenthesize(expr.operator.lexeme,
                expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Expression.Call expr) {
        return null;
    }

    @Override
    public String visitGetExpr(Expression.Get expr) {
        return null;
    }

    @Override
    public String visitGroupingExpr(Expression.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitProgramExpr(Expression.Program expr) {
        return null;
    }

    @Override
    public String visitStatementExpr(Expression.Statements expr) {
        return null;
    }

    @Override
    public String visitLiteralExpr(Expression.Literal expr) {
        if (expr.value == null) return "null";
        return expr.value.toString();
    }

    @Override
    public String visitEpsilonExpr(Expression.Epsilon expr) {
        return "";
    }

    @Override
    public String visitLogicalExpr(Expression.Logical expr) {
        return null;
    }

    @Override
    public String visitSetExpr(Expression.Set expr) {
        return null;
    }

    @Override
    public String visitSuperExpr(Expression.Super expr) {
        return null;
    }

    @Override
    public String visitThisExpr(Expression.This expr) {
        return null;
    }

    @Override
    public String visitUnaryExpr(Expression.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitDoubleUnaryExpr(Expression.Calculation expr) {
        return null;
    }

    @Override
    public String visitVariableExpr(Expression.Variable expr) {
        return null;
    }

    @Override
    public String visitPairExpr(Expression.Pair expr) {
        return null;
    }

    @Override
    public String visitBlockStmt(Expression.Block stmt) {
        return null;
    }

    @Override
    public String visitClassStmt(Expression.Class stmt) {
        return null;
    }

    @Override
    public String visitExpressionStmt(Expression.Expr stmt) {
        return null;
    }

    @Override
    public String visitFunctionStmt(Expression.Function stmt) {
        return null;
    }

    @Override
    public String visitIfStmt(Expression.If stmt) {
        return null;
    }

    @Override
    public String visitPrintStmt(Expression.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public String visitReturnStmt(Expression.Return stmt) {
        return null;
    }

    @Override
    public String visitVarStmt(Expression.Var stmt) {
        return null;
    }

    @Override
    public String visitWhileStmt(Expression.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public String visitDecl(Expression.Decl stmt) {
        return null;
    }

    private String parenthesize(String name, Expression... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expression expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    private String stringify(Object object) {
        if (object == null) return "null";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private void execute(Expression stmt) {
        stmt.accept(this);
    }
}