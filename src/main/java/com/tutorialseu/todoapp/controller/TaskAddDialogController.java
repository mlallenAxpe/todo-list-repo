package com.tutorialseu.todoapp.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class TaskAddDialogController {
    public MFXTextField taskTitleField;
    public TextArea taskDescriptionField;

    private ToDoController mainController;

    public void setMainController(ToDoController mainController) {
        this.mainController = mainController;
    }

    public void handleCancel(ActionEvent actionEvent) {
        closeDialog();
    }

    public void handleSubmit(ActionEvent actionEvent) {
        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        if (!title.isEmpty()) {
            this.mainController.addTaskFromDialog(title, description);
            closeDialog();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Title is necessary");
            alert.show();
            System.out.println("Write a title");
        }
    }
    private void closeDialog() {
        Stage stage = (Stage) taskTitleField.getScene().getWindow();
        stage.close();
    }
}
