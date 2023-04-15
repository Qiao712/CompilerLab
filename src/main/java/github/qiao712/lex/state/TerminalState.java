package github.qiao712.lex.state;

import github.qiao712.lex.TokenType;

public class TerminalState implements State {
    //词的类型
    public TokenType type;

    //是否归还最后一个字符
    public boolean back;

    public TerminalState(TokenType type, boolean back) {
        this.type = type;
        this.back = back;
    }

    @Override
    public State transfer(char c) {
        throw new UnsupportedOperationException();
    }
}
