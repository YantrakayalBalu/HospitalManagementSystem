package com.hospitaManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
	private Connection connection;

	public Doctor(Connection connection) {
		this.connection = connection;

	}

	public void viewDoctors() {

		String query = "SELECT * from doctors";

		try {

			PreparedStatement preparestatement = connection.prepareStatement(query);
			ResultSet resultSet = preparestatement.executeQuery();
			System.out.println("Doctors : ");
			System.out.println("+------------+----------------+-------------------+");
			System.out.println("| Doctor ID  | Name           | Specialization    |");
			System.out.println("+------------+----------------+-------------------+");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String specialization = resultSet.getString("specialization");
				System.out.printf("|%-12s|%-16s|%-19s|\n", id, name, specialization);
				System.out.println("+------------+----------------+-------------------+");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean getDoctorByID(int id) {

		String query = "SELECT * from doctors WHERE id = ?";
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
