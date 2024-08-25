package controllers;
import entities.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    private Connection connection;

    public AppointmentController(Connection connection) {
        this.connection = connection;
    }

    public void scheduleAppointment(Appointment appointment) throws SQLException{
        String sql = "INSERT INTO Appointment (patientId, doctorId, dateTime, status) " +
                "VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setString(3, appointment.getDateTime());
            pstmt.setString(4, appointment.getStatus());
            pstmt.executeUpdate();
        }
    }

    public List<Appointment> getAppointments() throws SQLException{
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointment";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setPatientId(rs.getInt("patientId"));
                appointment.setDoctorId(rs.getInt("doctorId"));
                appointment.setDateTime(rs.getString("dateTime"));
                appointment.setStatus(rs.getString("status"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }
}
