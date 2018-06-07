package com.br.mom.ms.common.enums;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum Status {

    OK(1, "成功"),
    FAILURE(2, "失败"),
    NOT_FOUND(3, "没有找到"),
    NOT_EXIST(4, "不存在"),
    NOT_PERMISSION(5, "没有权限"),
    DEBUG(6, "调试状态")
    ;
    private int v;
    private String name;

    private Status(int v, String name) {
        this.v = v;
        this.name = name;
    }

    public int getV() {
        return v;
    }

    public String getName() {
        return name;
    }

    public static Status fromInt(int v) {
        for (Status s : values()) {
            if (s.getV() == v) {
                return s;
            }
        }
        return null;
    }
}
