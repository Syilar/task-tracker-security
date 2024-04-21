package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.dto.UpsertTaskRequest;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<TaskResponse> findAll() {
        return taskService.findAll()
                .map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> createTask(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody UpsertTaskRequest request) {
        return taskService.save(userDetails.getUsername(), taskMapper.requestToTask(request))
                .map(taskMapper::taskToResponse)
                .map(taskResponse -> ResponseEntity.created(URI.create("/api/v1/task")).body(taskResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> updateTask(@PathVariable String id, @RequestBody UpsertTaskRequest request) {
        return taskService.update(taskMapper.requestToTask(id, request))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/observer/{observerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String observerId, @RequestParam String taskId) {
        return taskService.addObserver(observerId, taskId)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
