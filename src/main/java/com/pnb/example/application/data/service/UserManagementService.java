package com.pnb.example.application.data.service;

import com.pnb.example.application.data.entity.UserManagement;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    private final UserManagementRepository repository;

    public UserManagementService(UserManagementRepository repository) {
        this.repository = repository;
    }

    public Optional<UserManagement> get(Long id) {
        return repository.findById(id);
    }

    public UserManagement update(UserManagement entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UserManagement> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UserManagement> list(Pageable pageable, Specification<UserManagement> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
