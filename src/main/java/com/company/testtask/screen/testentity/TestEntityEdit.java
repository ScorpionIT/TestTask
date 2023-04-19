package com.company.testtask.screen.testentity;

import io.jmix.ui.screen.*;
import com.company.testtask.entity.TestEntity;

@UiController("TestEntity.edit")
@UiDescriptor("test-entity-edit.xml")
@EditedEntityContainer("testEntityDc")
public class TestEntityEdit extends StandardEditor<TestEntity> {
}