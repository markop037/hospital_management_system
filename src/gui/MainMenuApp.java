package gui;

import controllers.DoctorController;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

import database.DatabaseConnection;

import controllers.DoctorController;

import entities.Doctor;

public class MainMenuApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        DoctorController doctorController = new DoctorController(connection);
        List<Doctor> doctors = doctorController.getDoctors();

        stage.setTitle("Hospital Management System");

        Label lblTitle = new Label("Hospital Management System");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnDoctors = createButton("Doctors");
        Button btnAppointments = createButton("Appointments");
        Button btnPatients = createButton("Patients");
        Button btnMedicalRecords = createButton("Medical Records");
        Button btnExpenses = createButton("Expenses");

        btnDoctors.setOnAction(e -> {
            doctorsTable(doctors);
        });

        btnAppointments.setOnAction(e -> {
            try {
                Appointments appointmentsWindow = new Appointments();
                Stage appointmentsStage = new Stage();
                appointmentsWindow.start(appointmentsStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(lblTitle,btnDoctors, btnAppointments ,btnPatients,
                btnMedicalRecords, btnExpenses);

        Scene scene = new Scene(vBox, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setPrefSize(200, 60);
        button.setStyle("-fx-font-size: 14px;");

        return button;
    }

    private void doctorsTable(List<Doctor> doctors){
        Stage tableStage = new Stage();
        tableStage.setTitle("Doctors");

        TableView<Doctor> tableView = new TableView<>();
        ObservableList<Doctor> data = FXCollections.observableArrayList(doctors);

        TableColumn<Doctor, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellDate -> new SimpleObjectProperty<>(cellDate.getValue().getId()));

        TableColumn<Doctor, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellDate ->
                new SimpleStringProperty(cellDate.getValue().getFirstName()));

        TableColumn<Doctor, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellDate ->
                new SimpleStringProperty(cellDate.getValue().getLastName()));

        TableColumn<Doctor, String> specializationColumn = new TableColumn<>("Specialization");
        specializationColumn.setCellValueFactory(cellDate ->
                new SimpleStringProperty(cellDate.getValue().getSpecialization()));

        TableColumn<Doctor, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(cellDate ->
                new SimpleStringProperty(cellDate.getValue().getPhoneNumber()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn,
                specializationColumn, phoneNumberColumn);

        VBox vBox = new VBox(tableView);
        vBox.setPadding(new Insets(20));

        Scene scene = new Scene(vBox, 700, 400);

        tableStage.setScene(scene);
        tableStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
