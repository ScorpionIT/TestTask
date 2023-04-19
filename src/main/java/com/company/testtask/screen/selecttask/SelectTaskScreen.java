package com.company.testtask.screen.selecttask;

import com.company.testtask.app.TaskServiceBean;
import com.company.testtask.entity.TaskTypeEnum;
import com.company.testtask.entity.TestEntity;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.*;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.List;

@UiController("SelectTaskScreen")
@UiDescriptor("select-task-screen.xml")
public class SelectTaskScreen extends Screen {
    @Autowired
    private ComboBox<TaskTypeEnum> taskcombobox;
    @Autowired
    private HBoxLayout task1box;
    @Autowired
    private HBoxLayout task2box;
    @Autowired
    private TaskServiceBean taskServiceBean;
    @Autowired
    private TextField<Integer> n1;
    @Autowired
    private TextField<Integer> n2;
    @Autowired
    private TextField<Integer> n3;
    @Autowired
    private TextField<Integer> n4;
    @Autowired
    private TextField<Integer> n5;
    @Autowired
    private TextField<Integer> n6;
    @Autowired
    private TextField<Integer> n7;
    @Autowired
    private TextField<Integer> n8;
    @Autowired
    private TextField<Integer> n9;
    @Autowired
    private TextField<Integer> an1;
    @Autowired
    private TextField<Integer> an2;
    @Autowired
    private TextField<Integer> an3;
    @Autowired
    private TextField<Integer> an4;
    @Autowired
    private TextField<Integer> an5;
    @Autowired
    private TextField<Integer> an6;
    @Autowired
    private TextField<Integer> an7;
    @Autowired
    private TextField<Integer> an8;
    @Autowired
    private TextField<Integer> an9;
    @Autowired
    private TextField<Integer> outputConditionTextArea2;
    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private TextArea a;
    @Autowired
    private TextArea b;
    @Autowired
    private TextArea outputConditionTextField;
    @Autowired
    private Dialogs dialogs;

    @Subscribe("taskcombobox")
    public void onTaskcomboboxValueChange(HasValue.ValueChangeEvent<TaskTypeEnum> event) {
        if (event.getValue() == TaskTypeEnum.TASK_1) {
            task1box.setVisible(true);
            task2box.setVisible(false);
        } else {
            task1box.setVisible(false);
            task2box.setVisible(true);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        taskcombobox.setValue(TaskTypeEnum.TASK_1);

    }

    @Subscribe("calculateBtn")
    public void onCalculateBtnClick(Button.ClickEvent event) {
        calculate();
    }


    private void calculate() {
        if (taskcombobox.getValue() == TaskTypeEnum.TASK_1) {
            //task 1
            if (checkFirst()) {
                //noinspection DataFlowIssue
                String res = taskServiceBean.calculateTask1(a.getValue().toString(), b.getValue().toString());
                outputConditionTextField.setValue(res);
            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }

        } else { // task2
            if (checkSecond()) {
                //noinspection DataFlowIssue
                Pair<int[][], Integer> pair = taskServiceBean.calculateTask2(new int[][]{
                        {n1.getValue(), n2.getValue(), n3.getValue()},
                        {n4.getValue(), n5.getValue(), n6.getValue()},
                        {n7.getValue(), n8.getValue(), n9.getValue()}});
                if (pair.getSecond() == 100) {
                    notifications.create(Notifications.NotificationType.TRAY)
                            .withCaption(messageBundle.getMessage("CalculateError"))
                            .withDescription(messageBundle.getMessage("CalculateErrorDescription"))
                            .show();

                } else {
                    an1.setValue(pair.getFirst()[0][0]);
                    an2.setValue(pair.getFirst()[0][1]);
                    an3.setValue(pair.getFirst()[0][2]);
                    an4.setValue(pair.getFirst()[1][0]);
                    an5.setValue(pair.getFirst()[1][1]);
                    an6.setValue(pair.getFirst()[1][2]);
                    an7.setValue(pair.getFirst()[2][0]);
                    an8.setValue(pair.getFirst()[2][1]);
                    an9.setValue(pair.getFirst()[2][2]);
                    outputConditionTextArea2.setValue(pair.getSecond());
                }
            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }
        }
    }

    private boolean checkFirst() {
        return (a.getValue() != null && b.getValue() != null);
    }

    private boolean checkSecond() {
        return (n1.getValue() != null && n2.getValue() != null && n3.getValue() != null
                && n4.getValue() != null && n5.getValue() != null && n6.getValue() != null
                && n7.getValue() != null && n8.getValue() != null && n9.getValue() != null);
    }

    private String prepareStringData1(){
        return a.getValue().toString() + ';' + b.getValue().toString();
    }

    private String prepareStringData2(){
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                n1.getValue().toString(), n2.getValue().toString(), n3.getValue().toString(),
                n4.getValue().toString(), n5.getValue().toString(), n6.getValue().toString(),
                n7.getValue().toString(), n8.getValue().toString(), n9.getValue().toString());
    }

    @Subscribe("saveBtn")
    public void onSaveBtnClick(Button.ClickEvent event) {
        if (taskcombobox.getValue() == TaskTypeEnum.TASK_1) {
            if (checkFirst()) {
                taskServiceBean.saveTask(prepareStringData1(), taskcombobox.getValue());
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("SavedSuccessfully"))
                        .show();
            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }
        } else {
            if (checkSecond()) {
                taskServiceBean.saveTask(prepareStringData2(),taskcombobox.getValue());
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("SavedSuccessfully"))
                        .show();
            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }
        }
    }

    @Subscribe("loadBtn")
    public void onLoadBtnClick(Button.ClickEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Select values")
                .withParameter(InputParameter.entityParameter("Task", TestEntity.class))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        TestEntity testEntity = closeEvent.getValue("Task");
                        assert testEntity != null;//idk
                        fillFields(testEntity);
                    }
                })
                .show();
    }

    private void fillFields(TestEntity testEntity) {
        if (testEntity.getTaskType() == TaskTypeEnum.TASK_1) {
            String[] ab = testEntity.getConditionInput().split(";");
            taskcombobox.setValue(testEntity.getTaskType());
            a.setValue(ab[0]);
            b.setValue(ab[1]);
            calculate();
        } else {
            String[] numbers = testEntity.getConditionInput().split(",");
            taskcombobox.setValue(testEntity.getTaskType());
            n1.setValue(Integer.parseInt(numbers[0]));
            n2.setValue(Integer.parseInt(numbers[1]));
            n3.setValue(Integer.parseInt(numbers[2]));
            n4.setValue(Integer.parseInt(numbers[3]));
            n5.setValue(Integer.parseInt(numbers[4]));
            n6.setValue(Integer.parseInt(numbers[5]));
            n7.setValue(Integer.parseInt(numbers[6]));
            n8.setValue(Integer.parseInt(numbers[7]));
            n9.setValue(Integer.parseInt(numbers[8]));
            calculate();
        }
    }

    @Subscribe("exportBtn")
    public void onExportBtnClick(Button.ClickEvent event) throws IOException {
        if (taskcombobox.getValue() == TaskTypeEnum.TASK_1) {
            if (checkFirst()) {
                dialogs.createInputDialog(this)
                        .withCaption("Input file name")
                        .withParameter(InputParameter.stringParameter("filename"))
                        .withActions(DialogActions.OK_CANCEL)
                        .withCloseListener(closeEvent ->{
                            if(closeEvent.closedWith(DialogOutcome.OK)) {
                                String file = closeEvent.getValue("filename");
                                try {
                                    taskServiceBean.exportTask(prepareStringData1(),taskcombobox.getValue(),file);
                                    notifications.create(Notifications.NotificationType.TRAY)
                                            .withCaption(messageBundle.getMessage("SavedSuccessfully"))
                                            .show();
                                } catch (IOException e) {
                                    throw new RuntimeException("Error with file");
                                }
                            }
                        })
                        .show();
            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }
        } else {
            if (checkSecond()) {
                dialogs.createInputDialog(this)
                        .withCaption("Input file name")
                        .withParameter(InputParameter.stringParameter("filename"))
                        .withActions(DialogActions.OK_CANCEL)
                        .withCloseListener(closeEvent ->{
                            if(closeEvent.closedWith(DialogOutcome.OK)) {
                                String file = closeEvent.getValue("filename");
                                try {
                                    taskServiceBean.exportTask(prepareStringData2(),taskcombobox.getValue(),file);
                                    notifications.create(Notifications.NotificationType.TRAY)
                                            .withCaption(messageBundle.getMessage("SavedSuccessfully"))
                                            .show();
                                } catch (IOException e) {
                                    throw new RuntimeException("Error with file");
                                }
                            }
                        })
                        .show();

            } else {
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption(messageBundle.getMessage("ErrorCaption"))
                        .withDescription(messageBundle.getMessage("ErrorDescription"))
                        .show();
            }
        }
    }

    @Subscribe("importBtn")
    public void onImportBtnClick(Button.ClickEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Input file name(e.g. test.txt)")
                .withParameter(InputParameter.stringParameter("filename"))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent ->{
                    if(closeEvent.closedWith(DialogOutcome.OK)) {
                        String file = closeEvent.getValue("filename");
                        try {
                            TestEntity testEntity = taskServiceBean.importTask(file);
                            fillFields(testEntity);
                            notifications.create(Notifications.NotificationType.TRAY)
                                    .withCaption(messageBundle.getMessage("SavedSuccessfully"))
                                    .show();
                        } catch (IOException e) {
                            throw new RuntimeException("Cannot open file");
                        }
                    }
                })
                .show();
    }
}