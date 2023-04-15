package github.qiao712.lex;

import github.qiao712.lex.function.IsLetter;
import github.qiao712.lex.state.NonTerminalState;
import github.qiao712.lex.state.State;
import github.qiao712.lex.state.TerminalState;

import java.util.List;
import java.util.Set;

public class LexicalAnalyzer {
    /**
     * 起始状态
     */
    private final NonTerminalState start = new NonTerminalState("start");

    /**
     * 保留字
     */
    private Set<String> words;

    public LexicalAnalyzer(){
        //起始状态，过滤空白符
        start.addTransition(Character::isWhitespace, start);
        //标识符的识别
        NonTerminalState identifier = new NonTerminalState("identifier");
        start.addTransition(new IsLetter(), identifier);
        start.addTransition('_', identifier);
        identifier.addTransition(Character::isLetter, identifier);
        identifier.addTransition(Character::isDigit, identifier);
        identifier.addTransition('_', identifier);
        identifier.setDefaultTransition(new TerminalState(TokenType.IDENTIFIER, true)); //为true，表示将最后次读入的字符，留给下一轮的检查
        //无符号数
        NonTerminalState num = new NonTerminalState("num");
        start.addTransition(Character::isDigit, num);
        num.addTransition(Character::isDigit, num);
        num.setDefaultTransition(new TerminalState(TokenType.NUMBER, true));


        //+ 或 ++
        NonTerminalState plus = new NonTerminalState("+");
        start.addTransition('+', plus);
        plus.addTransition('+', new TerminalState(TokenType.OPERATOR1, false));
        plus.setDefaultTransition(new TerminalState(TokenType.OPERATOR1, true));
        //- 或 --
        NonTerminalState minus = new NonTerminalState("-");
        start.addTransition('-', minus);
        minus.addTransition('-', new TerminalState(TokenType.OPERATOR1, false));
        minus.setDefaultTransition(new TerminalState(TokenType.OPERATOR1, true));
        // | 和 ||
        NonTerminalState or = new NonTerminalState("|");
        start.addTransition('|', or);
        or.addTransition('|', new TerminalState(TokenType.OPERATOR1, false));   //||
        or.setDefaultTransition(new TerminalState(TokenType.OPERATOR1, true));     //|
        // & 和 &&
        NonTerminalState and = new NonTerminalState("&");
        start.addTransition('&', and);
        and.addTransition('&', new TerminalState(TokenType.OPERATOR1, false));  //&&
        and.setDefaultTransition(new TerminalState(TokenType.OPERATOR1, true));    //&
        // 乘、除、取模、按位取反
        start.addTransition("*/%^".toCharArray(), new TerminalState(TokenType.OPERATOR1, false));
        // ! 和 !=
        NonTerminalState not = new NonTerminalState("!");
        start.addTransition('!', not);
        not.addTransition('=', new TerminalState(TokenType.OPERATOR3, false));  //!=
        not.setDefaultTransition(new TerminalState(TokenType.OPERATOR2, true));    //!
        //= 和 ==
        NonTerminalState equal = new NonTerminalState("=");
        start.addTransition('=', equal);
        equal.addTransition('=', new TerminalState(TokenType.OPERATOR3, false));    //==
        equal.setDefaultTransition(new TerminalState(TokenType.OPERATOR4, true));      //=
        //< 和 <=
        NonTerminalState less = new NonTerminalState("<");
        start.addTransition('<', less);
        less.addTransition('=', new TerminalState(TokenType.OPERATOR3, false));     //<=
        less.setDefaultTransition(new TerminalState(TokenType.OPERATOR3, true));        //<
        //> 和 >=
        NonTerminalState great = new NonTerminalState(">");
        start.addTransition('>', great);
        great.addTransition('=', new TerminalState(TokenType.OPERATOR3, false));     //>=
        great.setDefaultTransition(new TerminalState(TokenType.OPERATOR3, true));        //>
        //，和 ；
        start.addTransition(",:".toCharArray(), new TerminalState(TokenType.SPLIT, false)); //;,
        //匹配失败
        start.setDefaultTransition(new TerminalState(TokenType.UNKWON, false));
    }

    public List<Token> analyze(String text){
        char[] source = (text + '\0').toCharArray();

        int i = 0;
        StringBuilder value = new StringBuilder();      //当前持有的字符
        State state = start;                            //当前状态

        while(i < source.length){
            value.append(source[i]);
            state = state.transfer(source[i]);
            i++;

            //到达终结符
            if(state instanceof TerminalState){
                TerminalState ts = (TerminalState) state;

                //需要归还最后一个字符，使其进入下一轮匹配
                if(ts.back){
                    value.deleteCharAt(value.length()-1);
                    i--;
                }

                System.out.printf("(%s, %s)\n", ts.type, value);

                //重置到初始状态
                state = start;
                value = new StringBuilder();
            }
        }

        return null;
    }

    public void setReservedWords(Set<String> words){

    }

    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

//        String text = "abc123+abc--123++-----+-+-&&&|||**//%%";
//        lexicalAnalyzer.analyze(text);

        String text2 = "a!!===123我12;<<=>>=<<<>>,123,";
        lexicalAnalyzer.analyze(text2);
    }
}
