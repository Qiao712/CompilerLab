package github.qiao712.lexical.function;

public class IsLetter implements Function{

    @Override
    public boolean func(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }
}
