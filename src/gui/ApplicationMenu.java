package gui;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;

import database.DatabaseConnection;

import entities.*;

import controllers.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        PatientController patientController = new PatientController(connection);
        AppointmentController appointmentController = new AppointmentController(connection);

        Label titleLabel = new Label("Hospital Management System");

        Button btnListPatients = new Button("List of Patients");
        Button btnAddPatient = new Button("Add Patient");
        Button btnListAppointments = new Button("List of Appointments");
        Button btnScheduleAppointment = new Button("Schedule Appointment");

        btnListPatients.setOnAction(e -> {
            try{
                List<Patient> patients = patientController.getPatients();
                showPatientsTable(patients);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnAddPatient.setOnAction(e -> {
            handleAddPatient(connection);
        });

        btnListAppointments.setOnAction(e -> {
            try{
                List<Appointment> appointments = appointmentController.getAppointments();
                showAppointmentsTable(appointments);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnScheduleAppointment.setOnAction(e -> {
            try {
                handleAddAppointment(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                formStage.close();
            }


        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.show();

    }

    private void showAppointmentsTable(List<Appointment> appointments){
        Stage tableStage = new Stage();
        tableStage.setTitle("List of Appointments");

        TableView<Appointment> tableView = new TableView<>();
        ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);

        TableColumn<Appointment, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<Appointment, Number> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPatientId()));

        TableColumn<Appointment, Number> doctorIdColumn = new TableColumn<>("Doctor ID");
        doctorIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDoctorId()));

        TableColumn<Appointment, String> dateTimeColumn = new TableColumn<>("Date");
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime()));

        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, patientIdColumn, doctorIdColumn, dateTimeColumn, statusColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 400);

        tableStage.setScene(scene);
        tableStage.show();
    }

    private void handleAddAppointment(Connection connection) throws SQLException {
        AppointmentController appointmentController = new AppointmentController(connection);
        PatientController patientController = new PatientController(connection);
        List<Patient> patients = patientController.getPatients();
        DoctorController doctorController = new DoctorController(connection);
        List<Doctor> doctors =doctorController.getDoctors();

        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Add New Appointment");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label lblPatient = new Label("Patient:");
        ComboBox<Patient> comboBoxPatient = new ComboBox<>();
        comboBoxPatient.setItems(FXCollections.observableArrayList(patients));

        Label lblDoctor = new Label("Doctor:");
        ComboBox<Doctor> comboBoxDoctor = new ComboBox<>();
        comboBoxDoctor.setItems(FXCollections.observableArrayList(doctors));

        Label lblDate = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        Label lblStatus = new Label("Status:");
        ComboBox<String> comboBoxStatus = new ComboBox<>();
        comboBoxStatus.setItems(FXCollections.observableArrayList(
                "Hospitalized",
                "Discharged",
                "Awaiting Examination"
        ));

        Button btnSubmit = new Button("Submit");

        gridPane.add(lblPatient, 0, 0);
        gridPane.add(comboBoxPatient, 1, 0);
        gridPane.add(lblDoctor, 0, 1);
        gridPane.add(comboBoxDoctor, 1, 1);
        gridPane.add(lblDate, 0, 2);
        gridPane.add(datePicker, 1, 2);
        gridPane.add(lblStatus, 0, 3);
        gridPane.add(comboBoxStatus, 1, 3);
        gridPane.add(btnSubmit, 1, 5);

        btnSubmit.setOnAction(e -> {
            Patient selectedPatient = comboBoxPatient.getValue();
            Doctor selectedDoctor = comboBoxDoctor.getValue();
            LocalDate selectedDate = datePicker.getValue();
            String selectedStatus = comboBoxStatus.getValue();

            if(selectedPatient == null || selectedDoctor == null || selectedDate == null ||
                    selectedStatus==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setContentText("All fields must be filled in.");
                alert.showAndWait();
            }
            else{
                int idPatient = selectedPatient.getId();
                int idDoctor = selectedDoctor.getId();
                Appointment appointment = new Appointment(idPatient, idDoctor, selectedDate.toString(),
                        selectedStatus);
                try {
                    appointmentController.scheduleAppointment(appointment);
                    formStage.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.showAndWait();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
