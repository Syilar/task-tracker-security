package com.example.tasktracker.dto;

import com.example.tasktracker.entity.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UpsertUserRequest {

    private String userName;

    private String email;

    private String password;

    private Set<RoleType> roles;
}
