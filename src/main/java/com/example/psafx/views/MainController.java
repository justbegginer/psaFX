package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;
import com.example.psafx.system.Task;
import com.example.psafx.util.Action;
import com.example.psafx.util.TimeScale;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.stream.Stream;

public class MainController {

    @FXML
    public HBox labelsContainer;
    @FXML
    private HBox testContainer;

    enum LineType {
        TASK,
        DEVICE,
        BUFFER,
        DENY
    }

    @FXML
    private VBox mainContainer;

    @FXML
    private Label logText;

    @FXML
    private List<HBox> boxes;

    private List<HBox> taskBoxes;

    private List<HBox> devicesBoxes;

    private List<HBox> buffersBoxes;

    private HBox denyContainer;


    private Label taskStatistic;

    private Label deviceStatistic;

    private Label bufferStatistic;

    private List<Label> taskLabelsStatistic;

    private List<Label> deviceLabelsStatistic;

    private List<Label> bufferLabelsStatistic;

    private final ComplexManager complexManager;

    public MainController() {
        this.complexManager = new ComplexManager(4, 2, 3, 0.8, 1.1);
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
            HBox tempContainer = new HBox();
            Label tempLabel = new Label("Task " + (i + 1));
            tempLabel.setTextFill(Color.BLACK);
            tempLabel.setMaxWidth(50);
            tempLabel.setMinWidth(50);
            tempContainer.getChildren().add(tempLabel);
            taskBoxes.add(tempContainer);
            boxes.add(tempContainer);
            mainContainer.getChildren().add(tempContainer);
        }
        for (int i = 0; i < complexManager.getBufferCount(); i++) {
            HBox tempContainer = new HBox();
            Label tempLabel = new Label("Buff " + (i + 1));
            tempLabel.setTextFill(Color.BLACK);
            tempLabel.setMaxWidth(50);
            tempLabel.setMinWidth(50);
            tempContainer.getChildren().add(tempLabel);
            buffersBoxes.add(tempContainer);
            boxes.add(tempContainer);
            mainContainer.getChildren().add(tempContainer);
        }
        for (int i = 0; i < complexManager.getDeviceCount(); i++) {
            HBox tempContainer = new HBox();
            Label tempLabel = new Label("Dev  " + (i + 1));
            tempLabel.setTextFill(Color.BLACK);
            tempLabel.setMaxWidth(50);
            tempLabel.setMinWidth(50);
            tempContainer.getChildren().add(tempLabel);
            devicesBoxes.add(tempContainer);
            boxes.add(tempContainer);
            mainContainer.getChildren().add(tempContainer);
        }
        HBox tempContainer = new HBox();
        Label denyLabel = new Label("Deny");
        denyLabel.setTextFill(Color.BLACK);
        denyLabel.setMaxWidth(50);
        denyLabel.setMinWidth(50);
        tempContainer.getChildren().add(denyLabel);
        boxes.add(tempContainer);
        mainContainer.getChildren().add(tempContainer);
        denyContainer = tempContainer;
        // add black lines to every hbox
        for (int i = 0; i < boxes.size(); i++) {
            lineSegmentsMap.put(boxes.get(i), new ArrayList<Line>(100));
            for (int j = 0; j < 100; j++) {
                Line temp = new Line(0, 0, 0, 0);
                temp.setStrokeWidth(10);
                temp.setStroke(Color.BLACK);
                boxes.get(i).getChildren().add(temp);
                lineSegmentsMap.get(boxes.get(i)).add(temp);
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

    private void bindLineEnd(Line line, HBox taskContainer) {
        line.endXProperty().bind(taskContainer.widthProperty().subtract(80)); // Adjust the subtract value as needed
    }

    @FXML
    protected void onHelloButtonClick() {
        List<Action> actions = this.complexManager.iterateAction();
        for (Action action : actions) {
            drawNew(action);
        }
        getStatistic();
    }

    private final Map<HBox, List<Line>> lineSegmentsMap = new HashMap<>();

    public void createColoredSegmentOnLine(HBox box, double segmentStartX, double segmentEndX, Color color) {
        // scaling coordinates
        for (int i = (int) (segmentStartX * 10); i < (int) (segmentEndX * 10); i++) {
            lineSegmentsMap.get(box).get(i).setStroke(color);
        }
    }

    public void createColoredSegmentOnLineBadVersion(Line baseLine, double segmentStartX, double segmentEndX, Color color) {
        // scaling coordinates
        segmentStartX *= 100;
        segmentEndX *= 100;
        System.out.println(segmentStartX);
        baseLine.setStroke(new LinearGradient(segmentStartX, baseLine.getStartY(), segmentEndX, baseLine.getEndY(), false,
                CycleMethod.NO_CYCLE,
                new Stop(0, color),
                new Stop(0.001, Color.BLACK),
                new Stop(1, color)));


    }


    @FXML
    protected void drawNew(Action action) {
        StringBuilder stringBuilder = new StringBuilder(action.toString());
        switch (action.getType()) {
            case TASK_DENY:
                TimeScale timeScale = action.getTaskTimeScale();
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1, Color.GREEN);
                createColoredSegmentOnLine(denyContainer,
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1, Color.RED);
                denyTasks.set(action.getTaskGroup() - 1, denyTasks.get(action.getTaskGroup() - 1) + 1);
                completedTasks.set(action.getTaskGroup() - 1, completedTasks.get(action.getTaskGroup() - 1) + 1);
                break;
            case BUFFER_RELEASE:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime(), Color.GREENYELLOW);
                createColoredSegmentOnLine(buffersBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getEndTime(), Color.ORANGERED);
                break;
            case DEVICE_RELEASE:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1, Color.GREEN);
                completedTasks.set(action.getTaskGroup() - 1, completedTasks.get(action.getTaskGroup() - 1) + 1);
                createColoredSegmentOnLine(devicesBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getEndTime(), Color.BLUE);
                break;
            case BUFFER_ADD:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1, Color.GREENYELLOW);
                createColoredSegmentOnLine(buffersBoxes.get(action.getEntityNumber().get() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1, Color.ORANGE);
                break;
            case DEVICE_ADD:
                createColoredSegmentOnLine(taskBoxes.get(action.getTaskGroup() - 1),
                        action.getTaskTimeScale().getStartTime(), action.getTaskTimeScale().getStartTime() + 0.1, Color.GREENYELLOW);
                createColoredSegmentOnLine(devicesBoxes.get(action.getEntityNumber().get() - 1),
                        action.getEntityTimeScale().getStartTime(), action.getEntityTimeScale().getStartTime() + 0.1, Color.AQUAMARINE);
                break;
        }
        stringBuilder
                .append(" Package:")
                .append(complexManager.getBufferManager().getLastTaken())
                .append(" Current time:")
                .append(complexManager.getCurrentTime());
        logText.setText(stringBuilder.toString());
        logText.setVisible(true);
    }

    List<Integer> completedTasks;
    List<Integer> denyTasks;

    private void getStatistic() {
        List<Double> deviceBusyRatio = getBusyRatio((int) (complexManager.getCurrentTime() * 10), devicesBoxes);
        System.out.println(devicesBoxes.size());
        System.out.println(deviceBusyRatio.size());
        System.out.println(deviceLabelsStatistic.size());
        for (int i = 0; i < deviceBusyRatio.size(); i++) {
            Label tempLabel = deviceLabelsStatistic.get(i);
            tempLabel.setText("Busy Ratio of Device "+(i+1)+" = " +String.format("%.1f", deviceBusyRatio.get(i)));
        }
        List<Double> bufferBusyRatio = getBusyRatio((int) (complexManager.getCurrentTime() * 10), buffersBoxes);
        for (int i = 0; i < bufferBusyRatio.size(); i++) {
            Label tempLabel = bufferLabelsStatistic.get(i);
            tempLabel.setText("Busy Ratio of Buffer "+(i+1)+" = " + String.format("%.1f", bufferBusyRatio.get(i)));
        }
        List<Double> taskDenyProbability = getDenyProbability();
        taskStatistic.setText("Average Deny Probability = " + (double)denyTasks.stream().reduce(Integer::sum).orElse(0) / completedTasks.stream().reduce(Integer::sum).orElse(0));
        for (int i = 0; i < taskLabelsStatistic.size(); i++) {
            Label tempLabel = taskLabelsStatistic.get(i);
            tempLabel.setText("Task "+(i+1)+" Deny Probability = " + taskDenyProbability.get(i));
        }
    }

    private List<Double> getBusyRatio(int time, List<HBox> boxes) {
        List<Double> busyPercents = new ArrayList<Double>();
        for (int i = 0; i < boxes.size(); i++) {
            double sum = 0;
            for (int j = 0; j < time; j++) {
                if (lineSegmentsMap.get(boxes.get(i)).get(j).getStroke() == Color.BLACK) {
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