package com.ssm.service;

import com.ssm.domain.Permission;
import com.ssm.domain.Role;

import java.util.List;

public interface IRoleService {
    public List<Role> findAll() throws Exception;
    public void save(Role role) throws Exception;
    public Role findById(String id) throws Exception;
    public void deleteRoleById(String id) throws Exception;
    //角色添加权限
    public List<Permission> findOtherPermissions(String roleId);
    public void addPermissionToRole(String roleId, String[] permissionIds);
}
