package gui;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

import database.DatabaseConnection;

import entities.Patient;

import controllers.PatientController;

import java.sql.Connection;
import java.util.List;

public class ApplicationMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        PatientController patientController = new PatientController(connection);

        Label titleLabel = new Label("Hospital Management System");

        Button btnListPatients = new Button("List of Patients");
        Button btnAddPatient = new Button("Add Patient");
        Button btnListAppointments = new Button("List of Appointments");
        Button btnScheduleAppointment = new Button("Schedule Appointment");

        btnListPatients.setOnAction(e -> {
            try{
                List<Patient> patients = patientController.getPatients();
                showPatientsTable(patients);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(btnListPatients, 0, 0);
        grid.add(btnAddPatient, 1, 0);
        grid.add(btnListAppointments, 0, 1);
        grid.add(btnScheduleAppointment, 1, 1);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, grid);

        Scene scene = new Scene(root, 400, 250);

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void showPatientsTable(List<Patient> patients) {
        Stage tableStage = new Stage();
        tableStage.setTitle("List of Patients");

        TableView<Patient> tableView = new TableView<>();
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);

        TableColumn<Patient, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<Patient, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFirstName()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, firstNameColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox, 800, 400);
        tableStage.setScene(scene);
        tableStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
