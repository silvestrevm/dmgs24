package com.pnb.example.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class ConfigurationParameters extends AbstractEntity {

    private Integer parameterId;
    private String parameterName;
    private String value;

    public Integer getParameterId() {
        return parameterId;
    }
    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }
    public String getParameterName() {
        return parameterName;
    }
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
