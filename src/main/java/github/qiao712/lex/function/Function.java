package github.qiao712.lex.function;

/**
 * 用作状态转移函数的函数接口
 */
@FunctionalInterface
public interface Function {
    boolean func(char c);
}
