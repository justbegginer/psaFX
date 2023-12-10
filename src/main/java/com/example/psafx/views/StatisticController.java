package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;
import com.example.psafx.util.Action;
import com.example.psafx.util.TimeScale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class StatisticController {

    @FXML
    private VBox mainContainer;

    @FXML
    private List<String> boxes;

    private List<String> taskBoxes;

    private List<String> devicesBoxes;

    private List<String> buffersBoxes;

    private HBox denyContainer;

    private Label taskStatistic;

    private Label deviceStatistic;

    private Label bufferStatistic;

    private List<Label> taskLabelsStatistic;

    private List<Label> deviceLabelsStatistic;

    private List<Label> bufferLabelsStatistic;

    private TableView<TaskStatistic> taskTable;

    private TableView<DeviceStatistic> deviceTable;

    private final ComplexManager complexManager;

    public StatisticController() {
        // testing tasks 5 2 4(3)
        // testing devices 5 2 10
        this.complexManager = new ComplexManager(5, 2, 3, 1, 1.1);
        this.completedTasks = new ArrayList<>(Collections.nCopies(this.complexManager.getTaskCount(), 0));
        this.denyTasks = new ArrayList<>(Collections.nCopies(this.complexManager.getTaskCount(), 0));
    }

    @FXML
    public void initialize() {
        this.taskBoxes = new ArrayList<>();
        this.devicesBoxes = new ArrayList<>();
        this.buffersBoxes = new ArrayList<>();
        this.timeInSystem = new ArrayList<>();
        this.waitTime = new ArrayList<>();
        boxes = new ArrayList<>();
        for (int i = 0; i < complexManager.getTaskCount(); i++) {
            String tempString = "Task " + (i + 1);
            taskBoxes.add(tempString);
            boxes.add(tempString);
            timeInSystem.add(new ArrayList<>());
            waitTime.add(new ArrayList<>());
        }
        for (int i = 0; i < complexManager.getBufferCount(); i++) {
            String tempString = "Buffer " + (i + 1);
            buffersBoxes.add(tempString);
            boxes.add(tempString);
        }
        for (int i = 0; i < complexManager.getDeviceCount(); i++) {
            String tempString = "Device " + (i + 1);
            devicesBoxes.add(tempString);
            boxes.add(tempString);
        }
        boxes.add("Deny");
        // add black lines to every hbox
        for (int i = 0; i < boxes.size(); i++) {
            lineSegmentsMap.put(boxes.get(i), new ArrayList<Boolean>(10000));
            for (int j = 0; j < 10000000; j++) {
                lineSegmentsMap.get(boxes.get(i)).add(false);
            }
        }
        this.taskTable = new TableView<TaskStatistic>();
        TableColumn<TaskStatistic, Number> taskNumber = new TableColumn<>("Task Number");
        taskNumber.setCellValueFactory(new PropertyValueFactory<>("taskNumber"));
        TableColumn<TaskStatistic, Number> denyProbability = new TableColumn<>("Deny Probability");
        denyProbability.setCellValueFactory(new PropertyValueFactory<>("denyProbability"));
        TableColumn<TaskStatistic, Number> countOfTasks = new TableColumn<>("Count of Tasks");
        countOfTasks.setCellValueFactory(new PropertyValueFactory<>("completedTaskCount"));
        TableColumn<TaskStatistic, Number> countOfDeniedTask = new TableColumn<>("Count of Denied");
        countOfDeniedTask.setCellValueFactory(new PropertyValueFactory<>("deniedTaskCount"));
        TableColumn<TaskStatistic, Number> averageTimeInSystem = new TableColumn<>("Average Time in System");
        averageTimeInSystem.setCellValueFactory(new PropertyValueFactory<>("averageTimeInSystem"));
        TableColumn<TaskStatistic, Number> averageWaitTimeInSystem = new TableColumn<>("Average Wait Time in System");
        averageWaitTimeInSystem.setCellValueFactory(new PropertyValueFactory<>("averageWaitTimeInSystem"));
        TableColumn<TaskStatistic, Number> dispersionTimeInSystem = new TableColumn<>("Dispersion Time in System");
        dispersionTimeInSystem.setCellValueFactory(new PropertyValueFactory<>("dispersionTimeInSystem"));
        TableColumn<TaskStatistic, Number> dispersionWaitTimeInSystem = new TableColumn<>("Dispersion Wait Time in System");
        dispersionWaitTimeInSystem.setCellValueFactory(new PropertyValueFactory<>("dispersionWaitTimeInSystem"));
        taskNumber.setPrefWidth(100);
        denyProbability.setPrefWidth(150);
        countOfDeniedTask.setPrefWidth(150);
        countOfTasks.setPrefWidth(150);
        averageTimeInSystem.setPrefWidth(200);
        averageWaitTimeInSystem.setPrefWidth(200);
        dispersionTimeInSystem.setPrefWidth(200);
        dispersionWaitTimeInSystem.setPrefWidth(200);
        this.taskTable.getColumns().addAll(
                taskNumber, denyProbability,
                countOfTasks, countOfDeniedTask,
                averageTimeInSystem, averageWaitTimeInSystem,
                dispersionTimeInSystem, dispersionWaitTimeInSystem);
        this.mainContainer.getChildren().add(this.taskTable);

        this.deviceTable = new TableView<DeviceStatistic>();
        TableColumn<DeviceStatistic, Number> deviceNumber = new TableColumn<>("Device Number");
        deviceNumber.setCellValueFactory(new PropertyValueFactory<>("deviceNumber"));
        TableColumn<DeviceStatistic, Number> deviceTableColumn = new TableColumn<>("Busy Ratio");
        deviceTableColumn.setCellValueFactory(new PropertyValueFactory<>("busyRatio"));
        deviceTableColumn.setPrefWidth(150);
        deviceNumber.setPrefWidth(300);
        this.deviceTable.getColumns().addAll(deviceNumber, deviceTableColumn);
        this.mainContainer.getChildren().add(this.deviceTable);
    }

    @FXML
    protected void onHelloButtonClick() {
        for (int i = 0; i < 1000000; i++) {
            List<Action> actions = this.complexManager.iterateAction();
            for (Action action : actions) {
                drawNew(action);
            }
        }
        getStatistic();
    }

    protected void drawNew(Action action) {
        switch (action.getType()) {
            case TASK_DENY:
                TimeScale timeScale = action.getTaskTimeScale();
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1);
                createColoredSegmentOnLine("Deny",
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1);
                denyTasks.set(action.getTaskGroup() - 1, denyTasks.get(action.getTaskGroup() - 1) + 1);
                completedTasks.set(action.getTaskGroup() - 1, completedTasks.get(action.getTaskGroup() - 1) + 1);
                break;
            case BUFFER_RELEASE:
                waitTime.get(action.getTaskGroup() - 1)
                        .add(action.getEntityTimeScale().getEndTime() - action.getEntityTimeScale().getStartTime());
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime());
                createColoredSegmentOnLine(buffersBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getEndTime());
                break;
            case DEVICE_RELEASE:
                timeInSystem.get(action.getTaskGroup() - 1)
                        .add(action.getTaskTimeScale().getEndTime() - action.getEntityTimeScale().getStartTime());
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1);
                completedTasks.set(action.getTaskGroup() - 1, completedTasks.get(action.getTaskGroup() - 1) + 1);
                createColoredSegmentOnLine(devicesBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getEndTime());
                break;
            case BUFFER_ADD:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1);
                createColoredSegmentOnLine(buffersBoxes.get(action.getEntityNumber().get() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1);
                break;
            case DEVICE_ADD:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1);
                createColoredSegmentOnLine(devicesBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getStartTime() + 0.1);
                break;
        }
        //System.out.println(completedTasks);
        //System.out.println(denyTasks);
    }

    private final Map<String, List<Boolean>> lineSegmentsMap = new HashMap<>();

    public void createColoredSegmentOnLine(String name, double segmentStartX, double segmentEndX) {
        // scaling coordinates
        for (int i = (int) (segmentStartX * 10); i < (int) (segmentEndX * 10); i++) {
            lineSegmentsMap.get(name).set(i, true);
        }
    }

    private List<Integer> completedTasks;
    private List<Integer> denyTasks;
    private List<List<Double>> timeInSystem;
    private List<List<Double>> waitTime;
    private List<Double> dispersionTimeInSystem;
    private List<Double> dispersionWaitTime;

    private void getStatistic() {
        //taskStatistic.setText("Average Deny Probability = " + (double)denyTasks.stream().reduce(Integer::sum).orElse(0) / completedTasks.stream().reduce(Integer::sum).orElse(0));
        ObservableList<TaskStatistic> taskData = FXCollections.observableArrayList();
        List<Double> taskDenyProbability = getDenyProbability();
        List<Double> averageTime = getAverageTime(timeInSystem);
        List<Double> averageWaitTime = getAverageTime(waitTime);
        List<Double> dispersionTime = getDispersionTime(timeInSystem, averageTime);
        List<Double> dispersionWait = getDispersionTime(waitTime, averageWaitTime);
        for (int i = 0; i < taskDenyProbability.size(); i++) {
            taskData.add(new TaskStatistic(i + 1, taskDenyProbability.get(i), completedTasks.get(i),
                    denyTasks.get(i), averageTime.get(i), averageWaitTime.get(i),
                    dispersionTime.get(i), dispersionWait.get(i)));
        }
        this.taskTable.setItems(taskData);
        List<Double> deviceBusyRatio = getBusyRatio((int) (complexManager.getCurrentTime() * 10), devicesBoxes);
        ObservableList<DeviceStatistic> deviceData = FXCollections.observableArrayList();
        for (int i = 0; i < deviceBusyRatio.size(); i++) {
            deviceData.add(new DeviceStatistic(i + 1, deviceBusyRatio.get(i)));
        }
        this.deviceTable.setItems(deviceData);
    }

    private List<Double> getBusyRatio(int time, List<String> boxes) {
        List<Double> busyPercents = new ArrayList<Double>();
        for (int i = 0; i < boxes.size(); i++) {
            double sum = 0;
            for (int j = 0; j < time; j++) {
                if (!lineSegmentsMap.get(boxes.get(i)).get(j)) {
                    sum++;
                }
            }
            busyPercents.add(((double) (complexManager.getCurrentTime() * 10) - sum) / (complexManager.getCurrentTime() * 10));
        }
        return busyPercents;
    }

    private List<Double> getDenyProbability() {
        List<Double> taskDenyProbabilities = new ArrayList<>();
        for (int i = 0; i < completedTasks.size(); i++) {
            if (completedTasks.get(i) == 0) {
                taskDenyProbabilities.add(0.);
            } else {
                taskDenyProbabilities.add(denyTasks.get(i) / (double) completedTasks.get(i));
            }
        }
        return taskDenyProbabilities;
    }

    private List<Double> getAverageTime(List<List<Double>> all) {
        List<Double> averageTimeDispersion = new ArrayList<Double>();
        for (int i = 0; i < completedTasks.size(); i++) {
            averageTimeDispersion.add(all.get(i).stream().reduce(Double::sum).orElse(0.) / (double) completedTasks.get(i));
        }
        return averageTimeDispersion;
    }

    private List<Double> getDispersionTime(List<List<Double>> all, List<Double> averages){
        List<Double> average = new ArrayList<Double>();
        for (int i = 0; i < all.size(); i++) {
            int finalI = i;
            average.add(all.get(i).stream().mapToDouble(d -> Math.pow(d-averages.get(finalI), 2)).sum()/completedTasks.get(i));
        }
        return average;
    }


}
