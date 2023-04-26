package com.pnb.example.application.data.service;

import com.pnb.example.application.data.entity.UserManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserManagementRepository
        extends
            JpaRepository<UserManagement, Long>,
            JpaSpecificationExecutor<UserManagement> {

}
