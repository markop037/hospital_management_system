package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class ApplicationMenu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Button btnPatients = new Button("Patient Record");
        Button btnAppointments = new Button("Schedule Appointments");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(btnPatients, btnAppointments);

        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
