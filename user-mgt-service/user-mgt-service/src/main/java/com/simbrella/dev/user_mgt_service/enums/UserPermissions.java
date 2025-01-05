package com.simbrella.dev.user_mgt_service.enums;

public enum UserPermissions {
    USER_READ("user.read"),
    USER_EDIT("user.edit"),
    USER_DELETE("user.delete");

    private final String permission;
    UserPermissions(String permission){
        this.permission=permission;
    }

    public String getPermission(){
        return permission;
    }
}
