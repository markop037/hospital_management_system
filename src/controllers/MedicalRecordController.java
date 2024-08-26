package controllers;

import entities.MedicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordController {
    private Connection connection;

    public MedicalRecordController(Connection connection) {
        this.connection = connection;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord){
            String sql = "INSERT INTO medicalrecord (patientId, diagnosis, treatment, date)" +
                    "VALUES (?, ?, ?, ?)";
            try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, medicalRecord.getPatientId());
                pstmt.setString(2, medicalRecord.getDiagnosis());
                pstmt.setString(3, medicalRecord.getTreatment());
                pstmt.setString(4, medicalRecord.getDate());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public List<MedicalRecord> getMedicalRecords(){
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        String sql = "SELECT * FROM medicalrecord";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                MedicalRecord medicalRecord = new MedicalRecord();
                medicalRecord.setId(rs.getInt("id"));
                medicalRecord.setPatientId(rs.getInt("patientId"));
                medicalRecord.setDiagnosis(rs.getString("diagnosis"));
                medicalRecord.setTreatment(rs.getString("treatment"));
                medicalRecord.setDate(rs.getString("date"));
                medicalRecords.add(medicalRecord);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }
}
