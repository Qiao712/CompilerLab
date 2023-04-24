package github.qiao712.lexical;

public enum TokenType {
    IDENTIFIER("标识符"),
    KEYWORD("保留字"),
    NUMBER("数字"),
    OPERATOR1("算数运算符"),
    OPERATOR2("逻辑运算符"),
    OPERATOR3("关系运算符"),
    OPERATOR4("赋值运算符"),
    SPLIT("分割符"),
    STRING("字符串"),
    CHAR("字符"),
    UNKWON("非法字符");  //不合法

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
