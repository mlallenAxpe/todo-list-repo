module com.tutorialseu.todoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires javafx.graphics;

    opens com.tutorialseu.todoapp to javafx.fxml;
    exports com.tutorialseu.todoapp;
    exports com.tutorialseu.todoapp.controller;
    opens com.tutorialseu.todoapp.controller to javafx.fxml;
}