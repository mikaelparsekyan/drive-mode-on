package com.project.drivemodeon.service.impl.role;

import com.project.drivemodeon.model.entity.Role;
import com.project.drivemodeon.repository.RoleRepository;
import com.project.drivemodeon.service.api.role.RoleService;
import com.project.drivemodeon.validation.constant.enumeration.RolesEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seedRoles() {
        if (this.roleRepository.count() == 0) {
            Arrays.stream(RolesEnum.values())
                    .forEach(role -> this.roleRepository.saveAndFlush(
                            new Role(role.name())));
        }
    }

    @Override
    public void getAll() {
        //TODO 
    }
}
