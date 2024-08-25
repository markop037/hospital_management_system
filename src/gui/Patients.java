package gui;

import controllers.PatientController;
import database.DatabaseConnection;
import entities.Patient;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Patients extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        PatientController patientController = new PatientController(connection);
        List<Patient> patients = patientController.getPatients();

        stage.setTitle("Patients");

        Label lblTitle = new Label("Patients");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnAddPatient = createButton("Add Patient");
        Button btnListPatients = createButton("List of Patients");

        btnAddPatient.setOnAction(e -> {
            handleAddPatient(connection);
        });

        btnListPatients.setOnAction(e -> {
            showPatientsTable(patients);
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(lblTitle, btnAddPatient, btnListPatients);

        Scene scene = new Scene(vBox, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.setPrefSize(200, 60);
        button.setStyle("-fx-font-size: 14px;");

        return button;
    }

    private void handleAddPatient(Connection connection){
        PatientController patientController = new PatientController(connection);

        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Add New Patient");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label lblFirstName = new Label("First Name");
        TextField txtFirstName = new TextField();
        Label lblLastName = new Label("Last Name");
        TextField txtLastName = new TextField();
        Label lblUMCN = new Label("Unique Master Citizen Number");
        TextField txtUMCN = new TextField();
        Label lblDOB = new Label("Date of Birth:");
        DatePicker datePickerDOB = new DatePicker();
        Label lblAddress = new Label("Address");
        TextField txtAddress = new TextField();
        Label lblPhone = new Label("Phone Number");
        TextField txtPhone = new TextField();

        Button btnSubmit = new Button("Submit");

        gridPane.add(lblFirstName, 0, 0);
        gridPane.add(txtFirstName, 1, 0);
        gridPane.add(lblLastName, 0, 1);
        gridPane.add(txtLastName, 1, 1);
        gridPane.add(lblUMCN, 0, 2);
        gridPane.add(txtUMCN, 1, 2);
        gridPane.add(lblDOB, 0, 3);
        gridPane.add(datePickerDOB, 1, 3);
        gridPane.add(lblAddress, 0, 4);
        gridPane.add(txtAddress, 1, 4);
        gridPane.add(lblPhone, 0, 5);
        gridPane.add(txtPhone, 1, 5);
        gridPane.add(btnSubmit, 1, 6);

        btnSubmit.setOnAction(e -> {
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String umcn = txtUMCN.getText();
            LocalDate dob = datePickerDOB.getValue();
            String address = txtAddress.getText();
            String phoneNumber = txtPhone.getText();

            if(firstName.isEmpty() || lastName.isEmpty() || umcn.isEmpty() || dob == null ||
                    address.isEmpty() || phoneNumber.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("All fields must be filled in.");
                alert.showAndWait();
            }
            else {
                Patient patient = new Patient(firstName, lastName, umcn, dob.toString(), address, phoneNumber);

                try {
                    patientController.addPatient(patient);
                    formStage.close();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Patient successfully added!");
                    successAlert.showAndWait();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.show();

    }

    private void showPatientsTable(List<Patient> patients){
        Stage tableStage = new Stage();
        tableStage.setTitle("List of Patients");

        TableView<Patient> tableView = new TableView<>();
        ObservableList<Patient> data = FXCollections.observableArrayList(patients);

        TableColumn<Patient, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<Patient, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        TableColumn<Patient, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        TableColumn<Patient, String> umcnColumn = new TableColumn<>("UMCN");
        umcnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUmcn()));

        TableColumn<Patient, String> dateOfBirthColumn = new TableColumn<>("Date of Birth");
        dateOfBirthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfBirth()));

        TableColumn<Patient, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        TableColumn<Patient, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, umcnColumn,
                dateOfBirthColumn, addressColumn, phoneNumberColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 400);

        tableStage.setScene(scene);
        tableStage.show();
    }
}
