package com.novus.novusbank.role.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novus.novusbank.response.Response;
import com.novus.novusbank.role.entity.Role;
import com.novus.novusbank.role.services.RoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthjority('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<Response<Role>> createRole(@RequestBody Role roleRequest) {
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Response<Role>> updateRole(@RequestBody Role roleRequest) {
        return ResponseEntity.ok(roleService.updateRole(roleRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Role>>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }

}
