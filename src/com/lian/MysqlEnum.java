package com.lian;

public enum MysqlEnum {
    A("VARCHAR", "String"),
    B("CHAR", "String"),
    C("BLOB", "Byte"),
    D("VARCHAR", "String"),
    E("INTEGER", "Long"),
    F("TINYINT", "Integer"),
    G("SMALLINT", "Integer"),
    H("MEDIUMINT", "Integer"),
    I("BIT", "Boolean"),
    J("BIGINT", "BigInteger"),
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
            if (value.code == code) {
                return value.java;
            }
        }
        return "String";
    }

}
