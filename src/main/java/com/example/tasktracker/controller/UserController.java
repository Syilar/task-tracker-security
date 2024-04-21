package com.example.tasktracker.controller;

import com.example.tasktracker.dto.UpsertUserRequest;
import com.example.tasktracker.dto.UserResponse;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<UserResponse> findAll() {
        return userService.findAll()
                .map(userMapper::userToResponse);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok);

    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UpsertUserRequest request) {
        return userService.save(userMapper.requestToUser(request))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable String id, @RequestBody UpsertUserRequest request) {
        return userService.update(id, userMapper.requestToUser(id, request))
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return userService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
