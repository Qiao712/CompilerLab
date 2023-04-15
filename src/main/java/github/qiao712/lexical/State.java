package github.qiao712.lexical;

import github.qiao712.lexical.function.Function;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 状态
 */
public class State {
    private String name = "";           //状态名
    private boolean terminal = false;   //是否是终态
    private TokenType type;             //尽在终态中设置

    /**
     * 非终态
     */
    public State(){

    }

    /**
     * 非终态
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * 终态
     */
    public State(TokenType type){
        this.terminal = true;
        this.type = type;
    }

    /**
     * 状态转换函数和其下一个状态
     */
    private final Map<Function, State> table = new LinkedHashMap<>();
    /**
     * 匹配单个字符
     */
    private final Map<Character, State> charTable = new HashMap<>();
    /**
     * 默认转移
     */
    private State defaultState;

    /**
     * 添加一个状态转移
     */
    public void addTransition(char c, State nextState){
        charTable.put(c, nextState);
    }

    /**
     * 添加多个状态转移
     */
    public void addTransition(char[] cs, State nextState){
        for (char c : cs) {
            addTransition(c, nextState);
        }
    }

    /**
     * 添加一个状态转移
     */
    public void addTransition(Function function, State nextState){
        table.put(function, nextState);
    }

    /**
     * 设置默认状态转移
     */
    public void setDefaultTransition(State state){
        this.defaultState = state;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public TokenType getType() {
        return type;
    }

    public State transfer(char c){
        for (Map.Entry<Function, State> entry : table.entrySet()) {
            if(entry.getKey().func(c)){
                return entry.getValue();
            }
        }

        return charTable.getOrDefault(c, defaultState);
    }

    @Override
    public String toString() {
        return name;
    }
}
