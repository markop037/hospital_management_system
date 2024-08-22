package controllers;

import entities.Doctor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {
    private Connection connection;

    public DoctorController(Connection connection) {
        this.connection = connection;
    }

    public List<Doctor> getDoctors(){
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFirstName(rs.getString("firstName"));
                doctor.setLastName(rs.getString("lastName"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhoneNumber(rs.getString("phoneNumber"));
                doctors.add(doctor);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return doctors;
    }


}
