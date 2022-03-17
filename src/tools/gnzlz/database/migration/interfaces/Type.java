package tools.gnzlz.database.migration.interfaces;

import java.sql.Types;

public enum Type {
    INTEGER,
    INT,
    TINYINT,
    BOOLEAN,
    BIT,
    SMALLINTEGER,
    SMALLINT,
    BIGINT,
    BIGINTEGER,
    LONG,
    FLOAT,
    REAL,
    DECIMAL,
    DOUBLE,
    NUMERIC,
    VARBINARY,
    BINARY,
    DATETIME,
    DATE,
    TIME,
    TIMESTAMP,
    VARCHAR,
    CHAR,
    LONGVARCHAR,
    TEXT,
    CLOB,
    BLOB,
    ARRAY,
    REF,
    STRUCT;

    @Override
    public String toString() {
        return super.toString();
    }
}
