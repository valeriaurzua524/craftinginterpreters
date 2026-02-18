package com.craftinginterpreters.lox;

import java.util.List;




public class RpnPrinter implements Expr.Visitor<String>{
    public String print(Expr e){return e==null?"":e.accept(this);}

    private String j(String...p){
        StringBuilder b=new StringBuilder();
        for(String s:p){
            if(s==null)continue;
            s=s.trim();
            if(s.isEmpty())continue;
            if(b.length()>0)b.append(' ');
            b.append(s);
        }
        return b.toString();
    }

    @Override public String visitBinaryExpr(Expr.Binary e){
        return j(e.left.accept(this),e.right.accept(this),e.operator.lexeme);
    }

    @Override public String visitGroupingExpr(Expr.Grouping e){
        return e.expression.accept(this);
    }

    @Override public String visitLiteralExpr(Expr.Literal e){
        return e.value==null?"nil":e.value.toString();
    }

    @Override public String visitUnaryExpr(Expr.Unary e){
        return j(e.right.accept(this),e.operator.lexeme);
    }

    @Override public String visitVariableExpr(Expr.Variable e){
        return e.name.lexeme;
    }

    @Override public String visitAssignExpr(Expr.Assign e){
        return j(e.value.accept(this),e.name.lexeme,"=");
    }

    @Override public String visitLogicalExpr(Expr.Logical e){
        return j(e.left.accept(this),e.right.accept(this),e.operator.lexeme);
    }

    @Override public String visitCallExpr(Expr.Call e){
        StringBuilder b=new StringBuilder();
        b.append(e.callee.accept(this));
        for(Expr a:e.arguments)b.append(' ').append(a.accept(this));
        b.append(" call");
        return b.toString().trim();
    }

    @Override public String visitGetExpr(Expr.Get e){
        return j(e.object.accept(this),e.name.lexeme,"get");
    }

    @Override public String visitSetExpr(Expr.Set e){
        return j(e.object.accept(this),e.name.lexeme,e.value.accept(this),"set");
    }

    @Override public String visitThisExpr(Expr.This e){
        return "this";
    }

    @Override
    public String visitTernaryExpr(Expr.Ternary expr) {
        // RPN style: condition thenBranch elseBranch ?:
        return j(
                expr.condition.accept(this),
                expr.thenBranch.accept(this),
                expr.elseBranch.accept(this),
                "?:"
        );
    }


    @Override public String visitSuperExpr(Expr.Super e){
        return j("super",e.method.lexeme);
    }

    public static void main(String[] args){
        Expr ex=new Expr.Binary(
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(1.0),new Token(TokenType.PLUS,"+",null,1),new Expr.Literal(2.0))),
                new Token(TokenType.STAR,"*",null,1),
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(4.0),new Token(TokenType.MINUS,"-",null,1),new Expr.Literal(3.0)))
        );
        System.out.println(new RpnPrinter().print(ex));
    }
}
