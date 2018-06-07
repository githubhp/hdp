package com.br.mom.ms.constent;

/**
 *
 * @author xin.cao@100credit.com
 */
public enum UserRole {

    COMMON(1, "common", "普通用户"),
    ADMIN(2, "admin", "管理员"),
    SUPER_ADMIN(3, "super_admin", "超级管理员"),
    COMMON_ADMIN(4, "common:admin", "管理员"),
    COMMON_ADMIN_SUPER_ADMIN(5, "common:admin:super_admin", "超级管理员");

    private int id;
    private String key;
    private String name;

    private UserRole(int id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public static UserRole fromId(int id) {
        for (UserRole userRole : values()) {
            if (userRole.getId() == id) {
                return userRole;
            }
        }
        return null;
    }
}
