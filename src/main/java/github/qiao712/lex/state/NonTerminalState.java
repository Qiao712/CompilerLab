package github.qiao712.lex.state;

import github.qiao712.lex.function.Function;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 可以设置函数作为状态转移的条件
 */
public class NonTerminalState implements State {
    private String name;    //状态名

    public NonTerminalState(String name){
        this.name = name;
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
    public NonTerminalState addTransition(char c, State nextState){
        charTable.put(c, nextState);
        return this;
    }

    /**
     * 添加多个状态转移
     */
    public NonTerminalState addTransition(char[] cs, State nextState){
        for (char c : cs) {
            addTransition(c, nextState);
        }
        return this;
    }

    /**
     * 添加一个状态转移
     */
    public NonTerminalState addTransition(Function function, State nextState){
        table.put(function, nextState);
        return this;
    }

    /**
     * 设置默认状态转移
     */
    public NonTerminalState setDefaultTransition(State state){
        this.defaultState = state;
        return this;
    }

    @Override
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
