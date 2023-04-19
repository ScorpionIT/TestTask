package com.company.testtask.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum TaskTypeEnum implements EnumClass<String> {

    TASK_1("TASK_1"),
    TASK_2("TASK_2");

    private String id;

    TaskTypeEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TaskTypeEnum fromId(String id) {
        for (TaskTypeEnum at : TaskTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}