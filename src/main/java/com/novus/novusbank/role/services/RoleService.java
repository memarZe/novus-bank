package com.novus.novusbank.role.services;

import java.util.List;

import com.novus.novusbank.response.Response;
import com.novus.novusbank.role.entity.Role;

public interface RoleService {

    Response<Role> createRole(Role request);

    Response<Role> updateRole(Role request);

    Response<List<Role>> getAllRoles();

    Response<?> deleteRole(Long id);

}
