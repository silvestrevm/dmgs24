package com.pnb.example.application.data.service;

import com.pnb.example.application.data.entity.ConfigurationParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfigurationParametersRepository
        extends
            JpaRepository<ConfigurationParameters, Long>,
            JpaSpecificationExecutor<ConfigurationParameters> {

}
