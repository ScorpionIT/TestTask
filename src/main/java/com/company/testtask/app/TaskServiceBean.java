package com.company.testtask.app;

import com.company.testtask.entity.TaskTypeEnum;
import com.company.testtask.entity.TestEntity;
import io.jmix.core.DataManager;
import liquibase.pro.packaged.T;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;

@Service
public class TaskServiceBean {

    static class FirstTask {
        private String[] firstRow;
        private String[] secondRow;
        private String[] result;

        public FirstTask(String[] f, String[] s) {
            firstRow = f;
            secondRow = s;
            result = new String[f.length];
        }

        public FirstTask() {
            firstRow = new String[]{"test"};
            secondRow = new String[]{"test", "tesw", "teset"};
        }

        public void calculate() {
            if (firstRow.length == 0 || secondRow.length == 0) {
                return;
            }
            int iter = 0;
            for (String substr : firstRow) {
                for (String word : secondRow) {
                    if (word.contains(substr)) {
                        result[iter++] = substr;
                        break;
                    }
                }
            }
            //Having not sorted arrays of substrings
            if (result.length != 0) {//is result null
                List<String> list = new ArrayList<>(Arrays.asList(result));
                list.removeAll(Collections.singletonList(null));
                Collections.sort(list);
                result = list.toArray(new String[0]);
            } else {
                result = new String[]{};
            }
        }

    }

    static class SecondTask {
        private int[][] table = new int[3][3];
        private int M = 15;
        private Set<Integer> possibleNumbers = new HashSet<Integer>();
        private int minimalSum = 100;
        private int[][] minimalSumTable = new int[3][3];

        SecondTask(int[][] arr) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    table[i][j] = arr[i][j];
                    possibleNumbers.add(arr[i][j]);
                }
            }
        }

        private boolean checkValues() {
            int sum = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sum += table[i][j];
                }
            }
            if (sum % 3 != 0) {
                return false;
            }
            M = sum / 3;
            return true;
        }

        private void checkValid(int[][] arr) {

            Set<Integer> set = new HashSet<Integer>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (arr[i][j] < 0) return;
                    set.add(arr[i][j]);
                }
            }
            if (set.size() != 9) {
                return;
            }

            int localsum = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    localsum += abs(table[i][j] - arr[i][j]);
                }
            }
            if (localsum < minimalSum) {
                minimalSum = localsum;
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(arr[i], 0, minimalSumTable[i], 0, 3);
                }
            }
        }


        void calulate() {
            if (!checkValues()) {
                return;
            }
            int[][] newSquare = new int[3][3];
            for (int firstNum : possibleNumbers) {
                Set<Integer> array1 = new HashSet<Integer>(possibleNumbers);
                array1.remove(firstNum); //creating new set without first value

                for (int secondNum : array1) {
                    int thirdNum = M - firstNum - secondNum; //calculating third number by knowing the first two
                    if (!array1.contains(thirdNum)) continue; //if number negative or out of possible range
                    newSquare[0][0] = firstNum;
                    newSquare[0][1] = secondNum;
                    newSquare[0][2] = thirdNum;
                    Set<Integer> array2 = new HashSet<Integer>(array1);
                    array2.remove(secondNum);
                    array2.remove(thirdNum);

                    for (int fourth : array2) {
                        newSquare[1][0] = fourth;
                        newSquare[2][0] = M - newSquare[0][0] - newSquare[1][0];
                        if (!array2.contains(newSquare[2][0])) continue;
                        Set<Integer> array3 = new HashSet<Integer>(array2);
                        array3.remove(fourth);
                        array3.remove(newSquare[2][0]);

                        for (int fifth : array3) {
                            newSquare[1][1] = fifth;
                            newSquare[2][1] = M - newSquare[1][1] - newSquare[0][1];
                            if (!array3.contains(newSquare[2][1]) || newSquare[2][1] < 0) continue;

                            newSquare[1][2] = M - newSquare[1][1] - newSquare[1][0];
                            if (!array3.contains(newSquare[1][2]) || newSquare[1][2] < 0) continue;

                            newSquare[2][2] = M - newSquare[0][2] - newSquare[1][2];
                            if (!array3.contains(newSquare[2][2]) || newSquare[2][2] < 0) continue;

                            if (array3.contains(newSquare[2][1]) && array3.contains(newSquare[1][2]) && array3.contains(newSquare[2][2])) {
                                checkValid(newSquare);
                            }
                        }
                    }
                }
            }
        }

    }

    @Autowired
    private DataManager dataManager;

    public String calculateTask1(String a, String b) {
        FirstTask firstTask = new FirstTask(a.split(","), b.split(","));
        firstTask.calculate();
        return Arrays.toString(firstTask.result);
    }

    public Pair<int[][], Integer> calculateTask2(int[][] input) {
        SecondTask secondTask = new SecondTask(input);
        secondTask.calulate();
        return Pair.of(secondTask.minimalSumTable, secondTask.minimalSum);
    }

    public void saveTask(String data, TaskTypeEnum taskType){
        TestEntity entity = dataManager.create(TestEntity.class);
        entity.setConditionInput(data);
        entity.setTaskType(taskType);
        dataManager.save(entity);
    }

    public void exportTask(String data, TaskTypeEnum taskType,String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.write('\n');
        writer.write(taskType.toString());
        writer.flush();
        writer.close();
    }

    public TestEntity importTask(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        Scanner scanner = new Scanner(fr);
        String data = scanner.nextLine();
        TaskTypeEnum taskType = TaskTypeEnum.valueOf(scanner.nextLine());
        fr.close();
        TestEntity testEntity = dataManager.create(TestEntity.class);
        testEntity.setConditionInput(data);
        testEntity.setTaskType(taskType);
        return testEntity;
    }

}

