package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;
import com.example.psafx.util.Action;
import com.example.psafx.util.TimeScale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;

import java.util.*;

public class MainController {

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

    private final ComplexManager complexManager;

    public MainController() {
        this.complexManager = new ComplexManager(3, 3, 3, 0.8, 1.1);
    }

    @FXML
    public void initialize() {
        taskBoxes = new ArrayList<>();
        devicesBoxes = new ArrayList<>();
        buffersBoxes = new ArrayList<>();
        boxes = new ArrayList<>();
        for (int i = 0; i < complexManager.getTaskCount(); i++) {
            HBox tempContainer = new HBox();
            Label tempLabel = new Label("Task " + (i+1));
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
            Label tempLabel = new Label("Buff " + (i+1));
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
            Label tempLabel = new Label("Dev  " + (i+1));
            tempLabel.setTextFill(Color.BLACK);
            tempLabel.setMaxWidth(50);
            tempLabel.setMinWidth(50);
            tempContainer.getChildren().add(tempLabel);
            devicesBoxes.add(tempContainer);
            boxes.add(tempContainer);
            mainContainer.getChildren().add(tempContainer);
        }
        HBox tempContainer = new HBox();
        Label tempLabel = new Label("Deny");
        tempLabel.setTextFill(Color.BLACK);
        tempLabel.setMaxWidth(50);
        tempLabel.setMinWidth(50);
        tempContainer.getChildren().add(tempLabel);
        devicesBoxes.add(tempContainer);
        boxes.add(tempContainer);
        mainContainer.getChildren().add(tempContainer);
        denyContainer = tempContainer;
        // Assuming all task containers are direct children of the same VBox
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

        //createColoredSegmentOnLine(taskLine1, 10, 20, Color.BLACK);
    }

    private Map<HBox, List<Line>> lineSegmentsMap = new HashMap<>();

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
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1, Color.GREENYELLOW);
                createColoredSegmentOnLine(denyContainer,
                        timeScale.getStartTime(), timeScale.getStartTime() + 0.1, Color.RED);
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

    public void getStatistic(){
        List<Double> deviceBusyRatio = getBusyRatio((int)(complexManager.getCurrentTime()*10), devicesBoxes);
        List<Double> bufferBusyRatio = getBusyRatio((int)(complexManager.getCurrentTime()*10), buffersBoxes);
    }

    public List<Double> getBusyRatio(int time, List<HBox> boxes){
        List<Double> busyPercents = new ArrayList<Double>();
        for (int i = 0; i < devicesBoxes.size(); i++) {
            double sum = 0;
            for (int j = 0; j < time; j++) {
                if (lineSegmentsMap.get(boxes.get(i)).get(j).getStroke() == Color.BLACK){
                    sum++;
                }
            }
            busyPercents.add((complexManager.getCurrentTime() - sum)/ complexManager.getCurrentTime());
        }
        return busyPercents;
    }



}