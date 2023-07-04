package com.zerobase.yogizogi.global.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MysqlCustomDialect extends MySQL57Dialect {

    public MysqlCustomDialect() {
        super();
        registerFunction(
            "get_distance",
            new StandardSQLFunction("get_distance", StandardBasicTypes.DOUBLE)
        );
    }
}
