package com.br.mom.ms.common.enums;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum PersistentType {

    CUSTOME(1, "自定义消费端"),
    MYSQL(2, "MYSQL"),
    HADOOP(3, "HADOOP"),
    ELASTICSEARCH(4, "ELASTICSEARCH"),
    HBASE(5, "HBASE"),
    NEO4J(6, "NEO4J");

    private int v;
    private String name;

    private PersistentType(int v, String name) {
        this.v = v;
        this.name = name;
    }

    public int getV() {
        return v;
    }

    public String getName() {
        return name;
    }

    public static PersistentType fromInt(int v) {
        for (PersistentType p : values()) {
            if (p.getV() == v) {
                return p;
            }
        }
        return null;
    }

}
