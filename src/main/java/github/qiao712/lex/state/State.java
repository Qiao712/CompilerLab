package github.qiao712.lex.state;

public interface State {
    State transfer(char c);
}
