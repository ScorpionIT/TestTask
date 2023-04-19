package com.company.testtask.screen.testentity;

import com.company.testtask.entity.TaskTypeEnum;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.screen.*;
import com.company.testtask.entity.TestEntity;

@UiController("TestEntity.browse")
@UiDescriptor("test-entity-browse.xml")
@LookupComponent("testEntitiesTable")
public class TestEntityBrowse extends StandardLookup<TestEntity> {
}