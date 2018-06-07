package com.br.mom.ms.disconf;

/**
 *
 * @author xin.cao@100credit.com
 */
public class Configure {

    private static int CD = 1000 * 30;

    public static void setCD(int CD) {
        Configure.CD = CD;
    }

    public static int getCD() {
        return CD;
    }

}
