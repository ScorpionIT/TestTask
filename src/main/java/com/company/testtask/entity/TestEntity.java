package com.company.testtask.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "TEST_ENTITY")
@Entity
public class TestEntity {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "CONDITION_INPUT", nullable = false)
    @NotNull
    private String conditionInput;

    @Column(name = "CONDITION_OUTPUT")
    private String conditionOutput;

    @NotNull
    @Column(name = "TASK_TYPE", nullable = false)
    private String taskType;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType == null ? null : taskType.getId();
    }

    public TaskTypeEnum getTaskType() {
        return taskType == null ? null : TaskTypeEnum.fromId(taskType);
    }

    public void setConditionOutput(String conditionOutput) {
        this.conditionOutput = conditionOutput;
    }

    public String getConditionOutput() {
        return conditionOutput;
    }

    public String getConditionInput() {
        return conditionInput;
    }

    public void setConditionInput(String conditionInput) {
        this.conditionInput = conditionInput;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}