package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.RoleType;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role getRoleByAuthority(String authority) {
        try {
            RoleType roleType = RoleType.valueOf(authority);
            return roleRepository.findByAuthority(roleType)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + authority));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role type: " + authority);
        }
    }

    @Override
    public Role getOrCreateRole(RoleType roleType) {
        return roleRepository.findByAuthority(roleType)
                .orElseGet(() -> {
                    Role newRole = new Role(roleType);
                    return roleRepository.save(newRole);
                });
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}