package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatus {
    ACTIVE("ACTIVE", "badge badge-info"),
    BLOCK("BLOCK", "badge badge-primary"),
    INACTIVE("INACTIVE", "badge badge-danger");

    private final String name;
    private final String classCss;
}
