package gui;

import controllers.AppointmentController;
import controllers.DoctorController;
import controllers.PatientController;
import database.DatabaseConnection;
import entities.Appointment;
import entities.Doctor;
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

public class Appointments extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        AppointmentController appointmentController = new AppointmentController(connection);
        List<Appointment> appointments = appointmentController.getAppointments();

        stage.setTitle("Appointments");

        Label lblTitle = new Label("Appointments");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnScheduleAppointment = createButton("Schedule Appointment");
        Button btnListAppointments = createButton("List of Appointments");

        btnScheduleAppointment.setOnAction(e -> {
            try {
                scheduleAppointment(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnListAppointments.setOnAction(e -> {
            showAppointmentsTable(appointments);
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(lblTitle,btnScheduleAppointment, btnListAppointments);

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

    private void scheduleAppointment(Connection connection) throws SQLException {
        AppointmentController appointmentController = new AppointmentController(connection);
        PatientController patientController = new PatientController(connection);
        List<Patient> patients = patientController.getPatients();
        DoctorController doctorController = new DoctorController(connection);
        List<Doctor> doctors =doctorController.getDoctors();

        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("New Appointment");

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

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Appointment successfully scheduled!");
                    successAlert.showAndWait();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.showAndWait();

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
}
