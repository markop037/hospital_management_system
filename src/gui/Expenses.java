package gui;

import controllers.ExpenseController;
import controllers.PatientController;
import database.DatabaseConnection;
import entities.Expense;
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

public class Expenses extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        ExpenseController expenseController = new ExpenseController(connection);
        List<Expense> expenses = expenseController.expenseList();

        stage.setTitle("Expenses");

        Label lblTitle = new Label("Expenses");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnAddExpense = createButton("Add Expense");
        Button btnListExpensess = createButton("List of Expenses");

        btnAddExpense.setOnAction(e -> {
            try {
                addExpense(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnListExpensess.setOnAction(e -> {
            showExpenseTable(expenses);
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(lblTitle, btnAddExpense, btnListExpensess);

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

    private void addExpense(Connection connection) throws SQLException {
        ExpenseController expenseController = new ExpenseController(connection);
        PatientController patientController = new PatientController(connection);
        List<Patient> patients = patientController.getPatients();

        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("New Expense");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label lblPatient = new Label("Patient:");
        ComboBox<Patient> comboBoxPatient = new ComboBox<>();
        comboBoxPatient.setItems(FXCollections.observableArrayList(patients));

        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();

        Label lblDate = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        Button btnSubmit = new Button("Submit");

        gridPane.add(lblPatient, 0, 0);
        gridPane.add(comboBoxPatient, 1, 0);
        gridPane.add(lblAmount, 0, 1);
        gridPane.add(txtAmount, 1, 1);
        gridPane.add(lblDate, 0, 2);
        gridPane.add(datePicker, 1, 2);
        gridPane.add(btnSubmit, 1, 4);

        btnSubmit.setOnAction(e -> {
            Patient selectedPatient = comboBoxPatient.getValue();
            String amount = txtAmount.getText();
            LocalDate selectedDate = datePicker.getValue();

            if(selectedPatient == null || amount.isEmpty() ||
                    selectedDate == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setContentText("All fields must be filled in.");
                alert.showAndWait();
            }
            else{
                int idPatient = selectedPatient.getId();
                Expense expense = new Expense(idPatient,Double.parseDouble(amount),selectedDate.toString());
                expenseController.addExpense(expense);
                formStage.close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Expense successfully added!");
                successAlert.showAndWait();
            }
        });

        Scene scene = new Scene(gridPane, 400, 300);
        formStage.setScene(scene);
        formStage.showAndWait();
    }

    private void showExpenseTable(List<Expense> expenses){
        Stage tableStage = new Stage();
        tableStage.setTitle("List of Expenses");

        TableView<Expense> tableView = new TableView<>();
        ObservableList<Expense> data = FXCollections.observableArrayList(expenses);

        TableColumn<Expense, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<Expense, Number> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPatientId()));

        TableColumn<Expense, Number> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getAmount()));

        TableColumn<Expense, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));

        tableView.setItems(data);
        tableView.getColumns().addAll(idColumn, patientIdColumn, amountColumn, dateColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 400);

        tableStage.setScene(scene);
        tableStage.show();
    }
}
