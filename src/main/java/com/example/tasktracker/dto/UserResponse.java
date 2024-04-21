package com.example.tasktracker.dto;

import com.example.tasktracker.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;

    private String userName;

    private String email;

    private Set<RoleType> roles;
}
