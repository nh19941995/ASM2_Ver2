package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumRole {
    USER("ROLE_USER", "badge badge-info"),
    MANAGER("ROLE_MANAGER", "badge badge-success");

    private final String name;
    private final String classCss;
}



