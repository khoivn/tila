package tila;

class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }


    @Override
    public String visitAssignExpr(Expr.Assignment expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme,
                expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return null;
    }

    @Override
    public String visitGetExpr(Expr.Get expr) {
        return null;
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitProgramExpr(Expr.Program expr) {
        return null;
    }

    @Override
    public String visitStatementExpr(Expr.Statements expr) {
        return null;
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "null";
        return expr.value.toString();
    }

    @Override
    public String visitEpsilonExpr(Expr.Epsilon expr) {
        return "";
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return null;
    }

    @Override
    public String visitSetExpr(Expr.Set expr) {
        return null;
    }

    @Override
    public String visitSuperExpr(Expr.Super expr) {
        return null;
    }

    @Override
    public String visitThisExpr(Expr.This expr) {
        return null;
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitDoubleUnaryExpr(Expr.Calculation expr) {
        return null;
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    @Override
    public String visitPairExpr(Expr.Pair expr) {
        return null;
    }

    @Override
    public String visitBlockStmt(Expr.Block stmt) {
        return null;
    }

    @Override
    public String visitClassStmt(Expr.Class stmt) {
        return null;
    }

    @Override
    public String visitExpressionStmt(Expr.Expression stmt) {
        return null;
    }

    @Override
    public String visitFunctionStmt(Expr.Function stmt) {
        return null;
    }

    @Override
    public String visitIfStmt(Expr.If stmt) {
        return null;
    }

    @Override
    public String visitPrintStmt(Expr.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public String visitReturnStmt(Expr.Return stmt) {
        return null;
    }

    @Override
    public String visitVarStmt(Expr.Var stmt) {
        return null;
    }

    @Override
    public String visitWhileStmt(Expr.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public String visitDecl(Expr.Decl stmt) {
        return null;
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    private Object evaluate(Expr expr) {
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

    private void execute(Expr stmt) {
        stmt.accept(this);
    }
}