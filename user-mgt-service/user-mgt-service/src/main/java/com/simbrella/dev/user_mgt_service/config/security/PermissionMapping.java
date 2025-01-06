package com.simbrella.dev.user_mgt_service.config.security;

import lombok.Data;

import java.util.List;

@Data
public class PermissionMapping {
    private String permission;
    private List<String> methods;
    private List<String> patterns;
}

