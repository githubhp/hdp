package com.br.mom.ms.common.enums;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum ContentFormat {

    JSON(1, "JSON"),
    CSV(2, "CSV逗号分割"),
    CSV_TAB(3, "CSV制表符分割");

    private int v;
    private String name;

    private ContentFormat() {}

    private ContentFormat(int v, String name) {
        this.v = v;
        this.name = name;
    }

    public int getV() {
        return v;
    }

    public String getName() {
        return name;
    }

    public static ContentFormat fromInt(int v) {
        for (ContentFormat c : values()) {
            if (c.getV() == v) {
                return c;
            }
        }
        return null;
    }
}
