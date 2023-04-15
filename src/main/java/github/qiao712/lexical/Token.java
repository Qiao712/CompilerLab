package github.qiao712.lexical;

public class Token {
    public TokenType type;
    public String value;

    @Override
    public String toString() {
        return "(" + type.getName() + ", " + value + ")";
    }
}
