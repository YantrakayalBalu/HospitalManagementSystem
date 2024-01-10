package org.hospital.HospitalSystemManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.hospitaManagement.Doctor;
import com.hospitaManagement.Patient;

/**
 * Hello world!
 *
 */
public class HospitalManagementHome 
{
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "balu";
	

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Connection connection = DriverManager.getConnection(url,username,password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection);
			while (true) {
				System.out.println("HOSPITAL MANAGEMNET SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View  Patient");
				System.out.println("3. View Doctors");
				System.out.println("4. Book appoitment");
				System.out.println("5. EXIT");

				System.out.println("Enter your choice = ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					// add patients
					patient.addPatient();
					System.out.println();
					break;

				case 2:
					// view patients
					patient.viewPatients();
					System.out.println();
					break;

				case 3:
					// view doctors
					doctor.viewDoctors();
					System.out.println();
					break;

				case 4:
					// book appoitment
					bookAppoitment(patient, doctor, connection, scanner);
					System.out.println();
					break;
				case 5:
					System.out.println("THANK YOU ! for using Hospital Management System.....!");
					return;
				default:
					System.out.println("Enter Valid Choice !!!!!!!!!!");
					break;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	public static void bookAppoitment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {

		System.out.println("Enter Patient id : ");
		int patientId = scanner.nextInt();
		System.out.println("Enter Doctor id : ");
		int doctorId = scanner.nextInt();
		System.out.println("Enter appoitment date (YYYY-MM-DD) : ");
		String appoitmentDate = scanner.next();
		if (patient.getPatientByID(patientId) && doctor.getDoctorByID(doctorId)) {
			if (checkDoctorAvalability(doctorId, appoitmentDate, connection)) {

				String appoitmentQuery = "INSERT into APPOINTMENTS(PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE) VALUES(? ,? ,?)";
				try {
					PreparedStatement preparestatement = connection.prepareStatement(appoitmentQuery);
					preparestatement.setInt(1, patientId);
					preparestatement.setInt(2, doctorId);
					preparestatement.setString(3, appoitmentDate);
					int rowsAffected = preparestatement.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Appoitment booked....!!");
					} else {
						System.out.println("Failed To Appoitment Booked!!!!!!!!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Doctor Not available on this DATE......");
			}
		} else {
			System.out.println("Either Doctor OR Patient doesn't Exit!!!!!!!");
		}

	}

	private static boolean checkDoctorAvalability(int doctorId, String appoitmentDate, Connection connection) {
		String query = "SELECT Count(*) FROM APPOINTMENTS WHERE DOCTOR_ID = ? AND APPOINTMENT_DATE = ?";
		try {

			PreparedStatement preparestatement = connection.prepareStatement(query);
			preparestatement.setInt(1, doctorId);
			preparestatement.setString(2, appoitmentDate);

			ResultSet resultset = preparestatement.executeQuery();
			if (resultset.next()) {
				int count = resultset.getInt(1);
				if (count == 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
