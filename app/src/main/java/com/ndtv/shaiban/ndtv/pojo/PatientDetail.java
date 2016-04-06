package com.ndtv.shaiban.ndtv.pojo;

/**
 * Created by shaiban on 5/4/16.
 */
public class PatientDetail {
    public String name, diagnosis, symptoms, medication, toBetakon, comments, knownDisease, doctor, specialty;
    public int id;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKnownDisease() {
        return knownDisease;
    }

    public void setKnownDisease(String knownDisease) {
        this.knownDisease = knownDisease;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getToBetakon() {
        return toBetakon;
    }

    public void setToBetakon(String toBetakon) {
        this.toBetakon = toBetakon;
    }
}