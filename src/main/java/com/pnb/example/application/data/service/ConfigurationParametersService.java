package com.pnb.example.application.data.service;

import com.pnb.example.application.data.entity.ConfigurationParameters;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationParametersService {

    private final ConfigurationParametersRepository repository;

    public ConfigurationParametersService(ConfigurationParametersRepository repository) {
        this.repository = repository;
    }

    public Optional<ConfigurationParameters> get(Long id) {
        return repository.findById(id);
    }

    public ConfigurationParameters update(ConfigurationParameters entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ConfigurationParameters> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ConfigurationParameters> list(Pageable pageable, Specification<ConfigurationParameters> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
