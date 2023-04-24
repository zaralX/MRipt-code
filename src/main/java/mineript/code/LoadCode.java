package mineript.code;

import mineript.code.statements.Statement;
import mineript.code.values.Token;
import mineript.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class LoadCode {
    public static void load(List<File> files) throws IOException {
        for (File file : files) {
            final String code = Files.readString(file.toPath());
            final Boolean debug = (Boolean) config.get().get("debug");

            if (Boolean.TRUE.equals(debug)) System.out.println("<--- LEXER -->");

            final List<Token> tokens = new Lexer(code).tokenize();

            if (debug) {
                for (Token token : tokens) {
                    System.out.println(token.toString());
                }
            }

            if (debug) System.out.println("<--- PARSER -->");

            final Statement program = new Parser(tokens).parse();
            if (debug) System.out.println(program.toString());

            if (debug) System.out.println("<--- PROGRAM -->\n");

            program.execute();
        }
    }
}
