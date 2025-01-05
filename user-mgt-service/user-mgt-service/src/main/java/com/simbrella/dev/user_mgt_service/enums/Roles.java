package com.simbrella.dev.user_mgt_service.enums;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.simbrella.dev.user_mgt_service.enums.UserPermissions.*;

public enum Roles {
    ROLE_USER(Sets.newHashSet(USER_EDIT, USER_READ)),
    ROLE_ADMIN(Sets.newHashSet(USER_DELETE, USER_READ, USER_EDIT));
    private final Set<UserPermissions> permissions;

    Roles(Set<UserPermissions> permissions){
        this.permissions = permissions;
    }
    public Set<UserPermissions> getPermissions(){
        return permissions;
    }
}
