package com.br.mom.ms.constent;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum ResultStatus {

    YES(1, "是"),
    NO(2, "否");

    private final int no;
    private final String name;

    private ResultStatus(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return this.no;
    }

    public String getName() {
        return this.name;
    }

    public static ResultStatus fromInt(int no) {
        for (ResultStatus r : values()) {
            if (r.getNo() == no) {
                return r;
            }
        }
        return null;
    }

    public static boolean isYES(int no) {
        for (ResultStatus rs : values()) {
            if (rs.getNo() == no) {
                return rs == YES;
            }
        }
        return false;
    }
}
