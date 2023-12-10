package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;
import com.example.psafx.util.Action;
import com.example.psafx.util.TimeScale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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

    private final ComplexManager complexManager;

    public StatisticController() {
        // testing tasks 3 2 3(Main)
        // testing devices 3 2 4
        this.complexManager = new ComplexManager(5, 2, 5, 1, 1.1);
        this.completedTasks = new ArrayList<>(Collections.nCopies(this.complexManager.getTaskCount(), 0));
        this.denyTasks = new ArrayList<>(Collections.nCopies(this.complexManager.getTaskCount(), 0));
    }

    @FXML
    public void initialize() {
        taskBoxes = new ArrayList<>();
        devicesBoxes = new ArrayList<>();
        buffersBoxes = new ArrayList<>();
        boxes = new ArrayList<>();
        for (int i = 0; i < complexManager.getTaskCount(); i++) {
            String tempString = "Task " + (i + 1);
            taskBoxes.add(tempString);
            boxes.add(tempString);
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
        taskStatistic = new Label();
        taskStatistic.setTextFill(Color.RED);
        mainContainer.getChildren().add(taskStatistic);
        taskLabelsStatistic = new ArrayList<>();
        for (int i = 0; i < complexManager.getTaskCount(); i++) {
            Label tempLabel = new Label();
            tempLabel.setTextFill(Color.RED);
            taskLabelsStatistic.add(tempLabel);
            mainContainer.getChildren().add(tempLabel);
        }
        bufferLabelsStatistic = new ArrayList<>();
        for (int i = 0; i < complexManager.getBufferCount(); i++) {
            Label tempLabel = new Label();
            tempLabel.setTextFill(Color.ORANGE);
            bufferLabelsStatistic.add(tempLabel);
            mainContainer.getChildren().add(tempLabel);
        }
        deviceLabelsStatistic = new ArrayList<>();
        for (int i = 0; i < complexManager.getDeviceCount(); i++) {
            Label tempLabel = new Label();
            tempLabel.setTextFill(Color.BLUE);
            deviceLabelsStatistic.add(tempLabel);
            mainContainer.getChildren().add(tempLabel);
        }
        // add statistics field
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
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime());
                createColoredSegmentOnLine(buffersBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getEndTime());
                break;
            case DEVICE_RELEASE:
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

    List<Integer> completedTasks;
    List<Integer> denyTasks;

    private void getStatistic() {
        List<Double> deviceBusyRatio = getBusyRatio((int) (complexManager.getCurrentTime() * 10), devicesBoxes);
        for (int i = 0; i < deviceBusyRatio.size(); i++) {
            Label tempLabel = deviceLabelsStatistic.get(i);
            tempLabel.setText("Busy Ratio of Device "+(i+1)+" = " +String.format("%.7f", deviceBusyRatio.get(i)));
        }
        List<Double> bufferBusyRatio = getBusyRatio((int) (complexManager.getCurrentTime() * 10), buffersBoxes);
        for (int i = 0; i < bufferBusyRatio.size(); i++) {
            Label tempLabel = bufferLabelsStatistic.get(i);
            tempLabel.setText("Busy Ratio of Buffer "+(i+1)+" = " + String.format("%.7f", bufferBusyRatio.get(i)));
        }
        List<Double> taskDenyProbability = getDenyProbability();
        taskStatistic.setText("Average Deny Probability = " + (double)denyTasks.stream().reduce(Integer::sum).orElse(0) / completedTasks.stream().reduce(Integer::sum).orElse(0));

        for (int i = 0; i < taskLabelsStatistic.size(); i++) {
            Label tempLabel = taskLabelsStatistic.get(i);
            tempLabel.setText("Task "+(i+1)+" Deny Probability = " + taskDenyProbability.get(i));
        }

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
            busyPercents.add(((double)(complexManager.getCurrentTime()*10) - sum) / (complexManager.getCurrentTime()*10));
        }
        return busyPercents;
    }

    private List<Double> getDenyProbability() {
        List<Double> taskDenyProbabilities = new ArrayList<Double>();
        for (int i = 0; i < completedTasks.size(); i++) {
            if (completedTasks.get(i) == 0) {
                taskDenyProbabilities.add(0.);
            } else {
                taskDenyProbabilities.add(denyTasks.get(i) / (double) completedTasks.get(i));
            }
        }
        return taskDenyProbabilities;
    }
}
