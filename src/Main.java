import database.DatabaseConnection;
import controllers.*;
import gui.ApplicationMenu;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = DatabaseConnection.getConnection()){
            PatientController patientController = new PatientController(connection);
            AppointmentController appointmentController = new AppointmentController(connection);

            ApplicationMenu.main(args);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
