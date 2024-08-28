package gui;


import controllers.MedicalRecordController;
import controllers.PatientController;
import database.DatabaseConnection;
import entities.MedicalRecord;
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

public class MedicalRecords extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        MedicalRecordController medicalRecordController = new MedicalRecordController(connection);
        List<MedicalRecord> medicalRecords = medicalRecordController.getMedicalRecords();

        stage.setTitle("Medical Records");

        Label lblTitle = new Label("Medical Records");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnAddMedicalRecord = createButton("Add Medical Record");
        Button btnListMedicalRecords = createButton("List of Medical Records");

        btnAddMedicalRecord.setOnAction(e -> {
            try {
                addMedicalRecord(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnListMedicalRecords.setOnAction(e -> {
            showMedicalRecordTable(medicalRecords);
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(lblTitle,btnAddMedicalRecord, btnListMedicalRecords);

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

    private void addMedicalRecord(Connection connection) throws SQLException {
        MedicalRecordController medicalRecordController = new MedicalRecordController(connection);
        PatientController patientController = new PatientController(connection);
        List<Patient> patients = patientController.getPatients();

        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("New Medical Record");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label lblPatient = new Label("Patient:");
        ComboBox<Patient> comboBoxPatient = new ComboBox<>();
        comboBoxPatient.setItems(FXCollections.observableArrayList(patients));

        Label lblDiagnosis = new Label("Diagnosis:");
        ComboBox<String> comboBoxDiagnosis = new ComboBox<>();
        comboBoxDiagnosis.setItems(FXCollections.observableArrayList(
                "Hypertension",
                "Diabetes Mellitus",
                "Chronic Kidney Disease",
                "Pneumonia",
                "Fractured Femur",
                "Myocardial Infarction"
        ));

        Label lblTreatment = new Label("Treatment:");
        ComboBox<String> comboBoxTreatment = new ComboBox<>();
        comboBoxTreatment.setItems(FXCollections.observableArrayList(
                "Antihypertensive Medication",
                "Insulin Therapy",
                "Dialysis",
                "Antibiotic Therapy",
                "Surgical Repair",
                "Cardiac Rehabilitation"
        ));

        Label lblDate = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        Button btnSubmit = new Button("Submit");

        gridPane.add(lblPatient, 0, 0);
        gridPane.add(comboBoxPatient, 1, 0);
        gridPane.add(lblDiagnosis, 0, 1);
        gridPane.add(comboBoxDiagnosis, 1, 1);
        gridPane.add(lblTreatment, 0, 2);
        gridPane.add(comboBoxTreatment, 1, 2);
        gridPane.add(lblDate, 0, 3);
        gridPane.add(datePicker, 1, 3);
        gridPane.add(btnSubmit, 1, 5);

        btnSubmit.setOnAction(e -> {
            Patient selectedPatient = comboBoxPatient.getValue();
            String selectedDiagnosis = comboBoxDiagnosis.getValue();
            String selectedTreatment = comboBoxTreatment.getValue();
            LocalDate selectedDate = datePicker.getValue();

            if(selectedPatient == null || selectedDiagnosis == null || selectedTreatment == null ||
                    selectedDate == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setContentText("All fields must be filled in.");
                alert.showAndWait();
            }
            else{
                int idPatient = selectedPatient.getId();
                MedicalRecord medicalRecord = new MedicalRecord(idPatient, selectedDiagnosis,
                        selectedTreatment, selectedDate.toString());
                medicalRecordController.addMedicalRecord(medicalRecord);
                formStage.close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Medical Record successfully added!");
                successAlert.showAndWait();
            }
        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.showAndWait();

    }

    private void showMedicalRecordTable(List<MedicalRecord> medicalRecords){
        Stage tableStage = new Stage();
        tableStage.setTitle("List od Medical Records");

        TableView<MedicalRecord> tableView = new TableView<>();
        ObservableList<MedicalRecord> data = FXCollections.observableArrayList(medicalRecords);

        TableColumn<MedicalRecord, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<MedicalRecord, Number> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPatientId()));

        TableColumn<MedicalRecord, String> diagnosisColumn = new TableColumn<>("Diagnosis");
        diagnosisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiagnosis()));

        TableColumn<MedicalRecord, String> treatmentColumn = new TableColumn<>("Treatment");
        treatmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTreatment()));

        TableColumn<MedicalRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, patientIdColumn, diagnosisColumn, treatmentColumn, dateColumn);

        VBox vBox = new VBox(tableView);
        vBox.setPadding(new Insets(20));

        Scene scene = new Scene(vBox, 800, 400);

        tableStage.setScene(scene);
        tableStage.show();
    }
}
