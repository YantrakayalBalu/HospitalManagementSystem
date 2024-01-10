package com.hospitaManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void addPatient() {
		System.out.println("Add patient Name : ");
		String name = scanner.next();
		System.out.println("Add Patient Age : ");
		int age = scanner.nextInt();
		System.out.println("Add Patient Gender : ");
		String gender = scanner.next();

		try {
			String query = "INSERT into patients(name,age,gender) VALUES(?,?,?)";
			PreparedStatement preparestatement = connection.prepareStatement(query);
			preparestatement.setString(1, name);
			preparestatement.setInt(2, age);
			preparestatement.setString(3, gender);

			int affectedRows = preparestatement.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Patient Added Successfully......!");
			} else {
				System.out.println("Failed to add Patient..");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void viewPatients() {

		String query = "SELECT * from patients";

		try {

			PreparedStatement preparestatement = connection.prepareStatement(query);
			ResultSet resultSet = preparestatement.executeQuery();
			System.out.println("Patients : ");
			System.out.println("+-------------+-----------------+------------+------------+");
			System.out.println("| Pateint ID  | Name            | Age        | Gender     |");
			System.out.println("+-------------+-----------------+------------+------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				System.out.printf("| %-11s | %-15s | %-10s | %-10s |\n", id, name, age, gender);
			    System.out.println("+-------------+-----------------+------------+------------+");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean getPatientByID(int id) {

		String query = "SELECT * from patients WHERE id = ?";
		try {
			PreparedStatement preparestatement = connection.prepareStatement(query);
			preparestatement.setInt(1, id);
			ResultSet resultset = preparestatement.executeQuery();
			if (resultset.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
