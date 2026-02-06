package com.tutorialseu.todoapp.controller;

import com.tutorialseu.todoapp.dto.TaskDTO;
import com.tutorialseu.todoapp.managers.TaskList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TaskCardController {
    @FXML
    Label taskName;

    @FXML
    Label taskTimeStamp;

    @FXML
    Label taskStatus;

    public String taskId;
    private TaskList taskList = new TaskList();

    private ToDoController toDoController;

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    @FXML
    private void handleViewTask() {
        TaskDTO task = taskList.getTaskById(taskId);
        showViewTaskDialog(task);
    }

    private void showViewTaskDialog(TaskDTO task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tutorialseu/todoapp/task-view-dialog.fxml"));
            VBox dialogPane = loader.load();

            TaskViewDialogController dialogController = loader.getController();
            dialogController.setTaskDetails(task, this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(taskName.getText());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(dialogPane);
            String css = Objects.requireNonNull(this.getClass().getResource("/com/tutorialseu/todoapp/viewtaskstyles.css").toExternalForm());
            scene.getStylesheets().add(css);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTaskDetails(String name, LocalDateTime timestamp, String status, String id, ToDoController controller) {
        this.taskId = id;
        taskName.setText(name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd-MM-yyyy");
        taskTimeStamp.setText(formatter.format(timestamp));
        taskStatus.setText(status);
        applyStatusColor(status);
        this.toDoController = controller;
    }

    private void applyStatusColor(String status) {
        String color;
        switch(status) {
            case "To Do":
                color = "red";
                break;
            case "In Progress":
                color = "orange";
                break;
            case "Done":
                color = "green";
                break;
            default:
                color = "gray";
        }
        taskStatus.setStyle("-fx-text-fill: " + color);
    }
    public void updateTask(TaskDTO task) {
        taskList.updateTask(task);
        taskName.setText(task.getTitle());
        taskStatus.setText(task.getStatus());
        applyStatusColor(task.getStatus());
        toDoController.redrawTaskList();
    }

    public void deleteTask(TaskDTO task) {
        taskList.removeTask(task);
        toDoController.redrawTaskList();
    }
}
