package com.example.todo.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitLimitHandler;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super(DatabaseVersion.make(3));
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl() {
            @Override
            public boolean supportsIdentityColumns() {
                return true;
            }

            @Override
            public String getIdentitySelectString(String table, String column, int type) {
                return "select last_insert_rowid()";
            }

            @Override
            public String getIdentityColumnString(int type) {
                return "integer";
            }
        };
    }

    @Override
    public boolean supportsTemporaryTables() {
        return true;
    }


    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LimitLimitHandler.INSTANCE;
    }

//    @Override
//    public boolean supportsJoinFetch() {
//        return false;
//    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

}
