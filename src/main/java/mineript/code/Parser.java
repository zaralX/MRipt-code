package mineript.code;

import mineript.code.expressions.*;
import mineript.code.statements.*;
import mineript.code.values.Token;
import mineript.code.values.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private final int size;
    private int pos;

    private static final Token END = new Token(TokenType.END, "");

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
    }

    public Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.END)) {
            result.add(statement());
        }
        return result;
    }

    private Statement block() {
        final BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.add(statement());
        }
        return block;
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }
        if (match(TokenType.BOOLEAN)) {
            return new ValueExpression(Boolean.valueOf (current.getText()));
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAR)) {
            return function();
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRAKE)) {
            return element();
        }
        if (lookMatch(0, TokenType.LBRAKE)) {
            return array();
        }
        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }
        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getText());
        }
        if (match(TokenType.LPAR)) {
            Expression result = expression();
            match(TokenType.RPAR);
            return result;
        }
        throw new RuntimeException("Unknown expression " + current);
    }

    private ArrayAccessExpression element() {
        final String variable = consume(TokenType.WORD).getText();
        List<Expression> indexes = new ArrayList<>();
        do {
            consume(TokenType.LBRAKE);
            indexes.add(expression());
            consume(TokenType.RBRAKE);
        } while (lookMatch(0, TokenType.LBRAKE));
        return new ArrayAccessExpression(variable, indexes);
    }

    private Expression array() {
        consume(TokenType.LBRAKE);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRAKE)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private FunctionExpression function() {
        final String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAR);
        final FunctionExpression function = new FunctionExpression(name);
        while (!match(TokenType.RPAR)) {
            function.add(expression());
            match(TokenType.COMMA);
        }
        return function;
    }


    private FunctionDefineStatement functionDefine() {
        final String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAR);
        final List<String> args = new ArrayList<>();
        while (!match(TokenType.RPAR)) {
            args.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        final Statement body = statementOrBlock();
        return new FunctionDefineStatement(name, args, body);
    }

    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }
        return primary();
    }

    private  Expression multery() {
        Expression expression = unary();

        while (true) {
            if (match(TokenType.STAR)) {
                expression = new BinaryExpression(expression, unary(), '*');
                continue;
            }
            if (match(TokenType.SLASH)) {
                expression = new BinaryExpression(expression, unary(), '/');
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression additive() {
        Expression expression = multery();

        while (true) {
            if (match(TokenType.PLUS)) {
                expression = new BinaryExpression(expression, multery(), '+');
                continue;
            }
            if (match(TokenType.MINUS)) {
                expression = new BinaryExpression(expression, multery(), '-');
                continue;
            }
            break;
        }
        return expression;
    }

    private Statement statementOrBlock() {
        if (get(0).getType() == TokenType.LBRACE) {
            return block();
        }
        return statement();
    }

    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }
        if (match(TokenType.PRINTLN)) {
            return new PrintlnStatement(expression());
        }
        if (match(TokenType.IF)) {
            return ifElse();
        }
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        if (match(TokenType.DO)) {
            return doWhileStatement();
        }
        if (match(TokenType.BREAK)) {
            return new BreakStatement();
        }
        if (match(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }
        if (match(TokenType.RETURN)) {
            return new ReturnStatement(expression());
        }
        if (match(TokenType.FOR)) {
            return forStatement();
        }
        if (match(TokenType.DEF)) {
            return functionDefine();
        }
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAR) {
            return new FunctionStatement(function());
        }
        return assigmentStatement();
    }

    private Statement assigmentStatement() {
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.SET)) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.SET);
            return new AssigmentStatement(variable, expression());
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRAKE)) {
            ArrayAccessExpression array = element();
            consume(TokenType.SET);
            return new ArrayAssignmentStatement(array, expression());
        }
        throw new RuntimeException("Unknown Statement " + get(0));
    }

    private Statement ifElse() {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }
        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement whileStatement() {
        final Expression condition = expression();
        final Statement statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }

    private Statement doWhileStatement() {
        final Statement statement = statementOrBlock();
        consume(TokenType.WHILE);
        final Expression condition = expression();
        return new DoWhileStatement(condition, statement);
    }

    private Statement forStatement() {
        match(TokenType.LPAR);

        final Statement init = assigmentStatement();
        consume(TokenType.COMMA);
        final Expression termination = expression();
        consume(TokenType.COMMA);
        final Statement increment = assigmentStatement();

        match(TokenType.RPAR);

        final Statement block = statementOrBlock();
        return new ForStatement(init, termination, increment, block);
    }

    private Expression expression() {
        return logicOr();
    }

    private Expression logicAnd() {
        Expression result = equality();

        while (true) {
            if (match(TokenType.ANDAND)) {
                result = new ConditionalExpression(result, equality(), ConditionalExpression.Operator.AND);
                continue;
            }
            break;
        }

        if (match(TokenType.ANDAND)) {
            return new ConditionalExpression(result, equality(), ConditionalExpression.Operator.AND);
        }

        return result;
    }

    private Expression logicOr() {
        Expression result = logicAnd();

        while (true) {
            if (match(TokenType.OROR)) {
                result = new ConditionalExpression(result, logicAnd(), ConditionalExpression.Operator.OR);
                continue;
            }
            break;
        }

        if (match(TokenType.OROR)) {
            return new ConditionalExpression(result, logicAnd(), ConditionalExpression.Operator.OR);
        }

        return result;
    }

    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.EQUALS)) {
            return new ConditionalExpression(result, conditional(), ConditionalExpression.Operator.EQUALS);
        }
        if (match(TokenType.NOTEQUALS)) {
            return new ConditionalExpression(result, conditional(), ConditionalExpression.Operator.NOTEQUALS);
        }

        return result;
    }

    private Expression conditional() {
        Expression expression = additive();

        while (true) {
            if (match(TokenType.LT)) {
                expression = new ConditionalExpression(expression, additive(), ConditionalExpression.Operator.LT);
                continue;
            }
            if (match(TokenType.LTEQUALS)) {
                expression = new ConditionalExpression(expression, additive(), ConditionalExpression.Operator.LTEQUALS);
                continue;
            }
            if (match(TokenType.GT)) {
                expression = new ConditionalExpression(expression, additive(), ConditionalExpression.Operator.GT);
                continue;
            }
            if (match(TokenType.GTEQUALS)) {
                expression = new ConditionalExpression(expression, additive(), ConditionalExpression.Operator.GTEQUALS);
                continue;
            }
            break;
        }
        return expression;
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) throw new RuntimeException("Token " + current + " doesn't match " + type);
        pos++;
        return current;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    private Token get(int rPosition) {
        final int position = pos + rPosition;
        if (position >= size) return END;
        return tokens.get(position);
    }
}
