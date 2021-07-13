package com.lian;

public enum MysqlEnum {
    A("VARCHAR", "String"),
    B("CHAR", "Char"),
    C("BLOB", "Byte"),
    D("VARCHAR", "String"),
    E("INTEGER", "Integer"),
    F("TINYINT", "Integer"),
    G("SMALLINT", "Integer"),
    H("MEDIUMINT", "Integer"),
    I("BIT", "Boolean"),
    J("BIGINT", "Long"),
    K("FLOAT", "Float"),
    L("DOUBLE", "Double"),
    M("DECIMAL", "BigDecimal"),
    N("PK", "Long"),
    O("DATE", "Date"),
    P("TIME", "Date"),
    Q("DATETIME", "Date"),
    R("TIMESTAMP", "Date"),
    S("YEAR", "Date");

    private String code;
    private String java;

    MysqlEnum(String code, String java) {
        this.code = code;
        this.java = java;
    }

    public static String getJavaByCode(String code) {
        for (MysqlEnum value : MysqlEnum.values()) {
            if (value.code.toLowerCase().trim().equals(code.toLowerCase().trim())) {
                return value.java;
            }
        }
        return "String";
    }

}
