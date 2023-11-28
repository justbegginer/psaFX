package com.example.psafx.views;

import com.example.psafx.system.ComplexManager;
import com.example.psafx.util.Action;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

import java.util.List;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private HBox taskContainer1, taskContainer2, taskContainer3;

    @FXML
    private Line taskLine1, taskLine2, taskLine3;

    @FXML
    private Line deviceLine1, deviceLine2, deviceLine3;

    @FXML
    private HBox deviceContainer1, deviceContainer2, deviceContainer3;

    @FXML
    private Line bufferLine1, bufferLine2, bufferLine3;

    @FXML
    private HBox bufferContainer1, bufferContainer2, bufferContainer3;

    @FXML
    private Line denyLine;

    @FXML
    private HBox denyContainer;

    private final ComplexManager complexManager;

    public MainController() {
        this.complexManager = new ComplexManager(3, 3, 3);
    }

    @FXML
    public void initialize() {
        // Assuming all task containers are direct children of the same VBox
        bindLineEnd(taskLine1, taskContainer1);
        bindLineEnd(taskLine2, taskContainer2);
        bindLineEnd(taskLine3, taskContainer3);
        bindLineEnd(bufferLine1, bufferContainer1);
        bindLineEnd(bufferLine2, bufferContainer2);
        bindLineEnd(bufferLine3, bufferContainer3);

        // Bind device lines
        bindLineEnd(deviceLine1, deviceContainer1);
        bindLineEnd(deviceLine2, deviceContainer2);
        bindLineEnd(deviceLine3, deviceContainer3);
        // Do the same for the other lines...
        bindLineEnd(denyLine, denyContainer);
    }

    private void bindLineEnd(Line line, HBox taskContainer) {
        line.endXProperty().bind(taskContainer.widthProperty().subtract(100)); // Adjust the subtract value as needed
    }

    @FXML
    protected void onHelloButtonClick() {
        List<Action> actions = this.complexManager.iterateAction();
        for (Action action : actions) {
            drawNew(action);
        }
    }


    @FXML
    protected void drawNew(Action action){

    }
}