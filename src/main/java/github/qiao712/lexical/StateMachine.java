package github.qiao712.lexical;

import github.qiao712.lexical.function.IsLetter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateMachine {
    /**
     * 起始状态
     */
    private final State start = new State("start");

    /**
     * 保留字表
     */
    private Set<String> keywords = new HashSet<>();

    public StateMachine(Set<String> keywords){
        this.keywords = keywords;
        build();
    }

    private void build(){
        //起始状态，过滤空白符
        start.addTransition(Character::isWhitespace, start);

        //标识符的识别
        State identifier = new State(TokenType.IDENTIFIER);
        start.addTransition(new IsLetter(), identifier);
        start.addTransition('_', identifier);
        identifier.addTransition(Character::isLetter, identifier);
        identifier.addTransition(Character::isDigit, identifier);
        identifier.addTransition('_', identifier);
        //无符号数
        State num = new State(TokenType.NUMBER);
        start.addTransition(Character::isDigit, num);
        num.addTransition(Character::isDigit, num);
        //+ 或 ++
        State plus = new State(TokenType.OPERATOR1);
        State plusPlus = new State(TokenType.OPERATOR1);
        start.addTransition('+', plus);
        plus.addTransition('+', plusPlus);
        //- 或 --
        State minus = new State(TokenType.OPERATOR1);
        State minusMinus = new State(TokenType.OPERATOR1);
        start.addTransition('-', minus);
        minus.addTransition('-', minusMinus);
        // | 和 ||
        State or = new State(TokenType.OPERATOR2);
        State orOr = new State(TokenType.OPERATOR2);
        start.addTransition('|', or);
        or.addTransition('|', orOr);
        // & 和 &&
        State and = new State(TokenType.OPERATOR2);
        State andAnd = new State(TokenType.OPERATOR2);
        start.addTransition('&', and);
        and.addTransition('&', andAnd);
        // 乘、除、取模、按位取反、按位异或
        start.addTransition("*/%~^".toCharArray(), new State(TokenType.OPERATOR1));
        // ! 和 !=
        State not = new State(TokenType.OPERATOR2);
        State notNot = new State(TokenType.OPERATOR3);
        start.addTransition('!', not);
        not.addTransition('=', notNot);
        //= 和 ==
        State equal = new State(TokenType.OPERATOR4);
        State euqalEqual = new State(TokenType.OPERATOR3);
        start.addTransition('=', equal);        //=
        equal.addTransition('=', euqalEqual);   //==
        //< 和 <=
        State less = new State(TokenType.OPERATOR3);
        State lessEqual = new State(TokenType.OPERATOR3);
        start.addTransition('<', less);
        less.addTransition('=', lessEqual);
        //> 和 >=
        State great = new State(TokenType.OPERATOR3);
        State greatEqual = new State(TokenType.OPERATOR3);
        start.addTransition('>', great);
        great.addTransition('=', greatEqual);
        //识别字符串
        State str = new State("str");
        State escapeChar = new State("esc");
        start.addTransition('\"', str);               //“ 进入字符串
        str.addTransition(c-> c!=0 && c!= '\\' && c!='\"', str);    //不断匹配字符串中的字符
        str.addTransition('\\', escapeChar);         //匹配字符串中的转移字符 \x
        escapeChar.addTransition(c->c!=0, str);         //匹配字符串中的转移字符 \x
        str.addTransition('\"', new State(TokenType.STRING));          //" 退出字符串
        //分隔符 )(,;:?{}#
        start.addTransition("(),;:?{}[]#".toCharArray(), new State(TokenType.SPLIT)); //;,
        //匹配失败
        start.setDefaultTransition(new State(TokenType.UNKWON));
    }

    public List<Token> analyze(String text){
        List<Token> tokens = new ArrayList<>();
        char[] source = (text + '\0').toCharArray();

        int i = 0;
        StringBuilder value = new StringBuilder();      //当前持有的字符
        State state = start;                            //当前状态

        //使用状态机进行匹配
        while(i < source.length){
            State nextState = state.transfer(source[i]);

            //是终结符，且无法继续推进，则记录
            if(state.isTerminal() && nextState == null){
                Token token = new Token(state.getType(), value.toString().trim());
                tokens.add(token);

                //区别出保留字
                if(keywords.contains(token.value)){
                    token.type = TokenType.KEYWORD;
                }

                //重置到初始状态
                state = start;
                value = new StringBuilder();
            }else{
                value.append(source[i]);
                state = nextState;
                i++;
            }
        }

        //如果没有到达终态，则非法
        if(!start.isTerminal() && !value.toString().trim().isEmpty()){
            tokens.add(new Token(TokenType.UNKWON, value.toString().trim()));
        }

        return tokens;
    }
}
