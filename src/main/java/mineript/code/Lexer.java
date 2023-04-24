package mineript.code;

import mineript.code.values.Token;
import mineript.code.values.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private static final String charsOperator = "+-*/(){}[]=<>!&|,";

    private static final Map<String, TokenType> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);

        OPERATORS.put("(", TokenType.LPAR);
        OPERATORS.put(")", TokenType.RPAR);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("[", TokenType.LBRAKE);
        OPERATORS.put("]", TokenType.RBRAKE);

        OPERATORS.put("=", TokenType.SET);
        OPERATORS.put(",", TokenType.COMMA);

        OPERATORS.put("==", TokenType.EQUALS);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(">=", TokenType.GTEQUALS);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put("<=", TokenType.LTEQUALS);
        OPERATORS.put("!", TokenType.NOT);
        OPERATORS.put("!=", TokenType.NOTEQUALS);
        OPERATORS.put("|", TokenType.OR);
        OPERATORS.put("||", TokenType.OROR);
        OPERATORS.put("&", TokenType.AND);
        OPERATORS.put("&&", TokenType.ANDAND);
    }

    private final String input;
    private final int input_len;
    private final List<Token> tokens;

    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.input_len = input.length();

        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (pos < input_len) {
            final char current = peek(0);
            if (Character.isDigit(current)) {
                tokenizeNumber();
            } else if (current == '"') {
                tokenizeText();
            } else if (charsOperator.indexOf(current) != -1) {
                tokenizeOperator();
            } else if (Character.isLetter(current) || current == '.') {
                tokenizeWord();
            } else {
                next();
            }
        }
        return tokens;
    }

    private void tokenizeNumber() {
        // Getting full number
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) {
                    throw new RuntimeException("Invalid float number");
                }
            } else if (!Character.isDigit(current)) {
                break;
            }
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }
    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (!Character.isLetterOrDigit(current) && current != '_' && current != '$') {
                break;
            }
            buffer.append(current);
            current = next();
        }
        final String word = buffer.toString();
        switch (word) {
            case "print": addToken(TokenType.PRINT); break;
            case "println": addToken(TokenType.PRINTLN); break;
            case "if": addToken(TokenType.IF); break;
            case "else": addToken(TokenType.ELSE); break;
            case "while": addToken(TokenType.WHILE); break;
            case "for": addToken(TokenType.FOR); break;
            case "do": addToken(TokenType.DO); break;
            case "break": addToken(TokenType.BREAK); break;
            case "continue": addToken(TokenType.CONTINUE); break;
            case "def": addToken(TokenType.DEF); break;
            case "return": addToken(TokenType.RETURN); break;
            case "class": addToken(TokenType.CLASS); break;
            case "true": addToken(TokenType.BOOLEAN, "true"); break;
            case "false": addToken(TokenType.BOOLEAN, "false"); break;
            default:
                addToken(TokenType.WORD, buffer.toString());
                break;
        }
    }

    private void tokenizeText() {
        next(); // SKIP "
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"': current = next(); buffer.append('"'); continue;
                    case 'n': current = next(); buffer.append('\n'); continue;
                    case 't': current = next(); buffer.append('\t'); continue;
                }
                buffer.append("\\");
                continue;
            }
            if (current ==  '"') {
                break;
            }
            buffer.append(current);
            current = next();
        }
        next(); // skip closing "
        addToken(TokenType.TEXT, buffer.toString());
    }

    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeMultiComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text+current) && !text.isEmpty()) {
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeComment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeMultiComment() {
        char current = peek(0);
        while (true) {
            if (current == '\0') throw new RuntimeException("Missing comment close");
            if (current == '*' && peek(1) == '/') {
                break;
            }
            current = next();
        }
        next(); // *
        next(); // /
    }


    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int rPosition) {
        final int position = pos + rPosition;
        if (position >= input_len) return '\0';
        return input.charAt(position);
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text));
    }
}
