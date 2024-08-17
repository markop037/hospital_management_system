package controllers;
import entities.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private Connection connection;

    public PatientController(Connection connection) {
        this.connection = connection;
    }

    public void addPatient(Patient patient) throws SQLException{
        String sql = "INSERT INTO Patient (firstName, lastName, umcn, dateOfBirth, address, phoneNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getUmcn());
            pstmt.setString(4, patient.getDateOfBirth());
            pstmt.setString(5, patient.getAddress());
            pstmt.setString(6, patient.getPhoneNumber());
            pstmt.executeUpdate();
        }
    }

    public List<Patient> getPatients() throws SQLException{
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setFirstName(rs.getString("firstName"));
                patient.setLastName(rs.getString("lastName"));
                patient.setUmcn(rs.getString("umcn"));
                patient.setDateOfBirth(rs.getString("dateOfBirth"));
                patient.setAddress(rs.getString("address"));
                patient.setPhoneNumber(rs.getString("phoneNumber"));
                patients.add(patient);
            }
        }
        return patients;
    }
}
