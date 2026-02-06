package com.tutorialseu.todoapp.controller;

import com.tutorialseu.todoapp.dto.TaskDTO;
import com.tutorialseu.todoapp.managers.TaskList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ToDoController {
    @FXML
    public MFXTextField taskTitle;
    @FXML
    public VBox taskListVBox;

    private TaskList taskList;
    @FXML
    public MFXComboBox statusComboBox;

    public void initialize() {
        taskList = new TaskList();
        statusComboBox.getItems().addAll("All", "To Do", "In Progress", "Done");
        statusComboBox.setValue("To Do");
        statusComboBox.valueProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue,
                                        String oldValue, String newValue) {
                        filterTasksByStatus(newValue);
                    }
                }
        );
        redrawTaskList();
        //addTask("Create a JavaFX Project", "Build a cool JavaFX Project", LocalDateTime.now().minusMinutes(30), "In Progress");
        //addTask("Learn JavaFX", "Learn how to program with JavaFX", LocalDateTime.now().minusMinutes(4), "Done");
        //addTask("Create a ToDo Project", "Build a To-Do list Project", LocalDateTime.now().minusMinutes(10), "In Progress");
        //addTask("Work on Springboot", "Springboot is important", LocalDateTime.now().minusMinutes(1), "To Do");
    }

    private void filterTasksByStatus(String status) {
        taskListVBox.getChildren().clear();
        List<TaskDTO> filteredTasks;
        if("All".equals(status)) filteredTasks = taskList.getTasks();
        else {
            filteredTasks = taskList.getTasks().stream().filter(
                    task -> task.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        for (TaskDTO task: filteredTasks) displayTask(task);
    }

    public void handleAddTask(ActionEvent actionEvent) {
        showAddTaskDialog();
    }
    private void showAddTaskDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tutorialseu/todoapp/task-add-dialog.fxml"));
            VBox dialogPane = loader.load();

            TaskAddDialogController dialogController = loader.getController();
            dialogController.setMainController(this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Task");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(dialogPane);
            String css = Objects.requireNonNull(this.getClass().getResource("/com/tutorialseu/todoapp/addtaskstyles.css").toExternalForm());
            scene.getStylesheets().add(css);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTaskFromDialog(String title, String description) {
        addTask(title, description, LocalDateTime.now(), "To Do");
    }

    private void addTask(String title, String description, LocalDateTime dateAdded, String status) {
        TaskDTO newTask = new TaskDTO(title, description, dateAdded, status);
        taskList.addTask(newTask);
        redrawTaskList();
    }

    private void displayTask(TaskDTO task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/tutorialseu/todoapp/task-card.fxml"));
            HBox taskCard = loader.load();
            TaskCardController taskCardController = loader.getController();
            taskCardController.setTaskDetails(task.getTitle(), task.getDateAdded(), task.getStatus(), task.getId(), this);
            taskCardController.setTaskList(taskList);
            taskListVBox.getChildren().add(taskCard);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void redrawTaskList() {
        taskListVBox.getChildren().clear();
        for (TaskDTO task: taskList.getTasks()) {
            displayTask(task);
        }
        statusComboBox.setValue("All");
    }

}
