package com.rootmind.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;

public class GCMNotification extends Helper implements Runnable {

	private Thread thread;
	private String threadName = null;
	private String schoolID = null;

	GCMNotification(String name, String schoolID) {

		threadName = name;
		this.schoolID = schoolID;
		System.out.println("Creating Thread " + threadName);
	}

	public void run() {
		System.out.println("Running Thread " + threadName);

		try {

			if (threadName.equals("SCHOOL_MESSAGE")) {
				sendSchoolMessage(schoolID);

			}
			if (threadName.equals("DIARY_MESSAGE")) {
				sendDiaryMessage(schoolID);
			}

			if (threadName.equals("ATTENDANCE_MESSAGE")) {
				sendAttendanceMessage(schoolID);
			}
			if (threadName.equals("BIRTHDAY_MESSAGE")) {
				sendBirthdayMessage(schoolID);
			}

		} catch (InterruptedException ex) {
			System.out.println("Thread " + threadName + " interruped");
			ex.printStackTrace();

		} catch (Exception ex) {
			ex.printStackTrace();

		}

		System.out.println("Existing Thread " + threadName);
	}

	public void start() {

		System.out.println("Starting thread " + threadName);

		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
			System.out.println("Started thread " + threadName);

		}

	}

	// ----------------------- Start sendSchoolMessage-------------------
	public AbstractWrapper sendSchoolMessage(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("sendSchoolMessage ");

		Vector<String> vector = new Vector<String>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords=null;

		PreparedStatement pstmtSub = null;
		String sql = null;
		String gradeAllCode = null;
		//SchoolMessageWrapper schoolMessageWrapper = null;

		try {

			con = getConnection();

			// ----Grade all code--

			pstmt = con.prepareStatement("SELECT GradeAllCode from MST_Parameter where SchoolID=?");

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				gradeAllCode = Utility.trim(resultSet.getString("GradeAllCode"));
				System.out.println(" GradeAllCode is " + gradeAllCode);
			}

			resultSet.close();
			pstmt.close();
			// --------

			pstmt = con.prepareStatement("SELECT MessageID, Message, MessageDateTime, Delivered, GradeList, MakerID, "
					+ " MakerDateTime, SchoolID FROM SchoolMessage WHERE Delivered <>'Y' and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
//				schoolMessageWrapper = new SchoolMessageWrapper();
//
//				schoolMessageWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
//				schoolMessageWrapper.message = Utility.trim(resultSet.getString("Message"));
//				schoolMessageWrapper.messageDateTime = Utility.trim(resultSet.getString("MessageDateTime"));
//				schoolMessageWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
//				schoolMessageWrapper.gradeList = Utility.trim(resultSet.getString("GradeList"));
//				schoolMessageWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
//				schoolMessageWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
//				schoolMessageWrapper.schoolID = Utility.setDate(resultSet.getString("SchoolID"));
//
//				schoolMessageWrapper.recordFound = true;
//
//				if (schoolMessageWrapper.gradeList.toUpperCase().trim().equals(gradeAllCode)) {
//					sql = "SELECT a.StudentID, b.DeviceToken as DeviceToken FROM StudentProfile a, Users b WHERE a.StudentId=b.StudentId and a.Status<>'INACTIVE' and b.UserGroup='STUDENT' and a.SchoolID=?";
//				} else {
//
//					sql = "SELECT a.StudentID, b.DeviceToken as DeviceToken FROM StudentProfile a, Users b WHERE a.StudentId=b.StudentId and a.Status<>'INACTIVE' and b.UserGroup='STUDENT' and a.GradeId=? and a.SchoolID=?";
//				}
//
//				pstmtSub = con.prepareStatement(sql);
//
//				if (!schoolMessageWrapper.gradeList.toUpperCase().trim().equals(gradeAllCode)) {
//					pstmtSub.setString(1, Utility.trim(schoolMessageWrapper.gradeList));
//					pstmtSub.setString(2, Utility.trim(schoolMessageWrapper.schoolID));
//
//				} else {
//					pstmtSub.setString(1, Utility.trim(schoolMessageWrapper.schoolID));
//
//				}

				ResultSet resultSetSub = pstmtSub.executeQuery();
				String deviceToken = null;

				while (resultSetSub.next()) {

					deviceToken = Utility.trim(resultSetSub.getString("DeviceToken"));

					System.out.println("deviceToken is " + deviceToken);

					if (deviceToken != null && !deviceToken.trim().equals("") && !vector.contains(deviceToken.trim())) {
						vector.addElement(deviceToken);
					}
				}
				resultSetSub.close();
				pstmtSub.close();

				String result = null;
				if (vector.size() > 0) {
					if (vector.size() == 1) {
						// single device id
						String toTokenId = vector.get(0);
//						result = sendMessage(toTokenId, null, schoolMessageWrapper.message, "School Message", "myicon",
//								schoolMessageWrapper.schoolID);
					} else {
						// multiple devices at the same time
						String[] registration_ids = new String[vector.size()];
						vector.copyInto(registration_ids);
//						result = sendMessage(null, registration_ids, schoolMessageWrapper.message, "School Message",
//								"myicon", schoolMessageWrapper.schoolID);

					}

					pstmtSub = con
							.prepareStatement("UPDATE SchoolMessage SET Delivered=?, MessageDateTime=?,GCMResponse=? "
									+ " WHERE Delivered <>'Y' and MessageID=? and SchoolID=?");
					pstmtSub.setString(1, "Y");
					pstmtSub.setTimestamp(2, Utility.getCurrentTime());
					pstmtSub.setString(3, result);
//					pstmtSub.setString(4, Utility.trim(schoolMessageWrapper.messageID));
					pstmtSub.setString(5, schoolID);

					pstmtSub.executeUpdate();
					pstmtSub.close();
				}

				System.out.println("send School Message  successful");

				// vector.addElement(schoolMessageWrapper);

			}

			/*
			 * if (vector.size()>0) { dataArrayWrapper.schoolMessageWrapper = new
			 * SchoolMessageWrapper[vector.size()];
			 * vector.copyInto(dataArrayWrapper.schoolMessageWrapper);
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * System.out.println("total trn. in fetch " + vector.size());
			 * 
			 * } else
			 * 
			 * { dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
			 * dataArrayWrapper.schoolMessageWrapper[0]= schoolMessageWrapper;
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * }
			 */
			if (resultSet != null)
				resultSet.close();
			pstmt.close();

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// ----------------------- End sendSchoolMessage-------------------

	// ----------------------- Start sendDiaryMessage-------------------
	public AbstractWrapper sendDiaryMessage(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("Send Grade Diary Message ");

		Vector<String> vector = new Vector<String>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords=null;

		PreparedStatement pstmtSub = null;
		String sql = null;
		//StudentDiaryWrapper studentDiaryWrapper = null;

		try {

			con = getConnection();

			pstmt = con.prepareStatement(
					"SELECT AcademicYearID,RefNo,StudentID,MessageID, Message, MessageDateTime, Delivered, GradeID,SectionID, MakerID, "
							+ " MakerDateTime FROM StudentDiary WHERE Delivered <>'Y' and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
//				studentDiaryWrapper = new StudentDiaryWrapper();
//				studentDiaryWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
//				studentDiaryWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
//				studentDiaryWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
//				studentDiaryWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
//				studentDiaryWrapper.message = Utility.trim(resultSet.getString("Message"));
//				studentDiaryWrapper.messageDateTime = Utility.trim(resultSet.getString("MessageDateTime"));
//				studentDiaryWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
//				studentDiaryWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
//				studentDiaryWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
//				studentDiaryWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
//				studentDiaryWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
//				studentDiaryWrapper.schoolID = Utility.setDate(resultSet.getString("SchoolID"));
//
//				studentDiaryWrapper.recordFound = true;
//
//				sql = "SELECT a.StudentID , b.DeviceToken as DeviceToken FROM StudentProfile a, Users b WHERE a.Status<>'INACTIVE' and b.UserGroup='STUDENT'"
//						+ " and a.AcademicYearID=? and a.GradeId=? and a.sectionID=? and a.SchoolID=?";
//
//				System.out.println("StudentID in send diary message " + studentDiaryWrapper.studentID);
//				System.out.println("RefNo in send diary message " + studentDiaryWrapper.refNo);
//
//				if (studentDiaryWrapper.studentID != null && !studentDiaryWrapper.studentID.trim().equals("")) {
//					sql = sql + " and a.RefNo=? and a.StudentID=? and b.StudentID=? ";
//				}
//
//				System.out.println(" sql in send diary message " + sql);
//
//				pstmtSub = con.prepareStatement(sql);
//
//				pstmtSub.setString(1, Utility.trim(studentDiaryWrapper.academicYearID));
//				pstmtSub.setString(2, Utility.trim(studentDiaryWrapper.gradeID));
//				pstmtSub.setString(3, Utility.trim(studentDiaryWrapper.sectionID));
//				pstmtSub.setString(4, Utility.trim(schoolID));
//
//				if (studentDiaryWrapper.studentID != null && !studentDiaryWrapper.studentID.trim().equals("")) {
//					System.out.println("if StudentID in send diary message " + studentDiaryWrapper.studentID);
//					System.out.println("if RefNo in send diary message " + studentDiaryWrapper.refNo);
//					pstmtSub.setString(5, Utility.trim(studentDiaryWrapper.refNo));
//					pstmtSub.setString(6, Utility.trim(studentDiaryWrapper.studentID));
//					pstmtSub.setString(7, Utility.trim(studentDiaryWrapper.studentID));
//				}

				ResultSet resultSetSub = pstmtSub.executeQuery();
				String deviceToken = null;

				while (resultSetSub.next()) {

					deviceToken = Utility.trim(resultSetSub.getString("DeviceToken"));

					System.out.println("deviceToken is " + deviceToken);

					if (deviceToken != null && !deviceToken.trim().equals("") && !vector.contains(deviceToken.trim())) {
						vector.addElement(deviceToken);
					}
				}
				resultSetSub.close();
				pstmtSub.close();

				String result = null;

				if (vector.size() > 0) {
					if (vector.size() == 1) {
						// single device id
						String toTokenId = vector.get(0);
//						result = sendMessage(toTokenId, null, studentDiaryWrapper.message, "School Diary", "myicon",
//								studentDiaryWrapper.schoolID);
					} else {
						// multiple devices at the same time
						String[] registration_ids = new String[vector.size()];
						vector.copyInto(registration_ids);
//						result = sendMessage(null, registration_ids, studentDiaryWrapper.message, "School Diary",
//								"myicon", studentDiaryWrapper.schoolID);

					}

					pstmtSub = con
							.prepareStatement("UPDATE StudentDiary SET Delivered=?, MessageDateTime=?,GCMResponse=? "
									+ " WHERE Delivered <>'Y' and MessageID=? and SchoolID=?");
					pstmtSub.setString(1, "Y");
					pstmtSub.setTimestamp(2, Utility.getCurrentTime());
					pstmtSub.setString(3, result);
//					pstmtSub.setString(4, Utility.trim(studentDiaryWrapper.messageID));
					pstmtSub.setString(5, Utility.trim(schoolID));

					pstmtSub.executeUpdate();
					pstmtSub.close();
				}

				System.out.println("send Student Diary Message  successful");

				// vector.addElement(schoolMessageWrapper);

			}

			/*
			 * if (vector.size()>0) { dataArrayWrapper.schoolMessageWrapper = new
			 * SchoolMessageWrapper[vector.size()];
			 * vector.copyInto(dataArrayWrapper.schoolMessageWrapper);
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * System.out.println("total trn. in fetch " + vector.size());
			 * 
			 * } else
			 * 
			 * { dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
			 * dataArrayWrapper.schoolMessageWrapper[0]= schoolMessageWrapper;
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * }
			 */
			if (resultSet != null)
				resultSet.close();
			pstmt.close();

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// ----------------------- End sendDiaryMessage-------------------

	// ----------------------- Start sendAttendanceMessage-------------------
	public AbstractWrapper sendAttendanceMessage(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("sendAttendanceMessage ");

		Vector<String> vector = new Vector<String>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords=null;

		PreparedStatement pstmtSub = null;
		String sql = null;

		String absentMessage = null;
		String studentName = null;
		//StudentAttendanceWrapper studentAttendanceWrapper = null;

		try {

			con = getConnection();

			// -----AbsentMessage code--

			PopoverHelper popoverHelper = new PopoverHelper();
			//ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper.fetchParameters(schoolID);
			//absentMessage = parameterWrapper.absentMessage;

			// ------------

			pstmt = con.prepareStatement(
					"SELECT AcademicYearID,RefNo,StudentID,CalendarDate,MessageID, Message, Delivered, GradeID,SectionID, MorningStatus, "
							+ " EveningStatus, SchoolID FROM StudentAttenMessage WHERE Delivered <>'Y' and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
//				studentAttendanceWrapper = new StudentAttendanceWrapper();
//				studentAttendanceWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
//				studentAttendanceWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
//				studentAttendanceWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
//				studentAttendanceWrapper.calendarDate = Utility.setDate(resultSet.getString("CalendarDate"));
//				studentAttendanceWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
//				studentAttendanceWrapper.message = Utility.trim(resultSet.getString("Message"));
//				studentAttendanceWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
//				studentAttendanceWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
//				studentAttendanceWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
//				studentAttendanceWrapper.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
//				studentAttendanceWrapper.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
//				studentAttendanceWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
//
//				studentAttendanceWrapper.recordFound = true;
//
//				sql = "SELECT a.StudentID, a.StudentName as StudentName, a.Surname as Surname, b.DeviceToken as DeviceToken FROM StudentProfile a, Users b WHERE a.StudentId=b.StudentId and a.Status<>'INACTIVE' and b.UserGroup='STUDENT' "
//						+ " and a.AcademicYearID=? and a.RefNo=? and a.StudentID=? and b.StudentID=? and a.SchoolID=?";
//
//				pstmtSub = con.prepareStatement(sql);
//
//				pstmtSub.setString(1, Utility.trim(studentAttendanceWrapper.academicYearID));
//				pstmtSub.setString(2, Utility.trim(studentAttendanceWrapper.refNo));
//				pstmtSub.setString(3, Utility.trim(studentAttendanceWrapper.studentID));
//				pstmtSub.setString(4, Utility.trim(studentAttendanceWrapper.studentID));
//				pstmtSub.setString(5, Utility.trim(schoolID));
//
//				ResultSet resultSetSub = pstmtSub.executeQuery();
//				String deviceToken = null;
//				String message = null;
//				if (resultSetSub.next()) {
//
//					deviceToken = Utility.trim(resultSetSub.getString("DeviceToken"));
//					studentName = Utility.trim(resultSetSub.getString("StudentName")) + " "
//							+ Utility.trim(resultSetSub.getString("Surname"));
//
//					System.out.println("studentName is " + studentName + studentAttendanceWrapper.studentID);
//
//					message = absentMessage.replace("{{studentName}}", studentName);
//
//					message = message.replace("{{calendarDate}}", studentAttendanceWrapper.calendarDate);
//
//					if (studentAttendanceWrapper.morningStatus.equals("A")
//							&& studentAttendanceWrapper.eveningStatus.equals("A")) {
//						message = message.replace("{{session}}", " day session");
//					} else if (studentAttendanceWrapper.morningStatus.equals("A")) {
//						message = message.replace("{{session}}", " morning session");
//					} else if (studentAttendanceWrapper.eveningStatus.equals("A")) {
//						message = message.replace("{{session}}", " evening session");
//					}
//
//					System.out.println("deviceToken is " + deviceToken);
//					System.out.println("message is " + message);
//
//					if (deviceToken != null && !deviceToken.trim().equals("") && !vector.contains(deviceToken.trim())) {
//						vector.addElement(deviceToken);
//					}
				}
				//resultSetSub.close();
				pstmtSub.close();

				String result = null;
				if (vector.size() > 0) {
					if (vector.size() == 1) {
						// single device id
						String toTokenId = vector.get(0);
//						result = sendMessage(toTokenId, null, message, "Student Attendance", "myicon",
//								studentAttendanceWrapper.schoolID);
					} else {
						// multiple devices at the same time
						String[] registration_ids = new String[vector.size()];
						vector.copyInto(registration_ids);
//						result = sendMessage(null, registration_ids, message, "Student Attendance", "myicon",
//								studentAttendanceWrapper.schoolID);

					}

					pstmtSub = con.prepareStatement(
							"UPDATE StudentAttenMessage SET Message=?, Delivered=?, MessageDateTime=?, GCMResponse=? "
									+ " WHERE Delivered <>'Y' and MessageID=? and RefNo=? and StudentID=? and SchoolID=?");

//					pstmtSub.setString(1, message);
//					pstmtSub.setString(2, "Y");
//					pstmtSub.setTimestamp(3, Utility.getCurrentTime());
//					pstmtSub.setString(4, result);
//					pstmtSub.setString(5, Utility.trim(studentAttendanceWrapper.messageID));
//					pstmtSub.setString(6, Utility.trim(studentAttendanceWrapper.refNo));
//					pstmtSub.setString(7, Utility.trim(studentAttendanceWrapper.studentID));
//					pstmtSub.setString(8, Utility.trim(studentAttendanceWrapper.schoolID));

					pstmtSub.executeUpdate();
					pstmtSub.close();
				}

				System.out.println("send Student Attendance  successful");

				// vector.addElement(schoolMessageWrapper);

			//}

			/*
			 * if (vector.size()>0) { dataArrayWrapper.schoolMessageWrapper = new
			 * SchoolMessageWrapper[vector.size()];
			 * vector.copyInto(dataArrayWrapper.schoolMessageWrapper);
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * System.out.println("total trn. in fetch " + vector.size());
			 * 
			 * } else
			 * 
			 * { dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
			 * dataArrayWrapper.schoolMessageWrapper[0]= schoolMessageWrapper;
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * }
			 */

			if (resultSet != null)
				resultSet.close();
			pstmt.close();

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// ----------------------- End sendAttendanceMessage-------------------

	// ----------------------- Start sendBirthdayMessage-------------------
	public AbstractWrapper sendBirthdayMessage(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("sendBirthdayMessage ");

		Vector<String> vector = new Vector<String>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords=null;

		PreparedStatement pstmtSub = null;
		String sql = null;

		String birthdayMessage = null;
		String studentName = null;
		//StudentDiaryWrapper studentDiaryWrapper = null;

		/*
		 * Date date; // your date Calendar cal = Calendar.getInstance();
		 * cal.setTime(date); int year = cal.get(Calendar.YEAR); int month =
		 * cal.get(Calendar.MONTH); int day = cal.get(Calendar.DAY_OF_MONTH);
		 */

		try {

			con = getConnection();

			// -----AbsentMessage code--

			sql = "SELECT BirthdayMessage from MST_Parameter where SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				birthdayMessage = Utility.trim(resultSet.getString("BirthdayMessage"));

			}

			System.out.println("birthdayMessage " + birthdayMessage);

			resultSet.close();
			pstmt.close();

			// ------------

			pstmt = con.prepareStatement(
					"SELECT AcademicYearID,RefNo,StudentID,MessageID, Message, Delivered,GradeID,SectionID FROM StudentNotification WHERE Delivered <>'Y' and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
//			while (resultSet.next()) {
//				studentDiaryWrapper = new StudentDiaryWrapper();
//				studentDiaryWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
//				studentDiaryWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
//				studentDiaryWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
//
//				studentDiaryWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
//				studentDiaryWrapper.message = Utility.trim(resultSet.getString("Message"));
//				studentDiaryWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
//				studentDiaryWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
//				studentDiaryWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
//				studentDiaryWrapper.schoolID = Utility.trim(resultSet.getString("schoolID"));
//
//				studentDiaryWrapper.recordFound = true;
//
//				sql = "SELECT a.StudentID, a.StudentName as StudentName, a.Surname as Surname, b.DeviceToken as DeviceToken FROM StudentProfile a, Users b WHERE a.StudentId=b.StudentId and a.Status<>'INACTIVE' and b.UserGroup='STUDENT' "
//						+ " and a.AcademicYearID=? and a.RefNo=? and a.StudentID=? and b.StudentID=? and a.SchoolID=?";
//
//				pstmtSub = con.prepareStatement(sql);
//
//				pstmtSub.setString(1, Utility.trim(studentDiaryWrapper.academicYearID));
//				pstmtSub.setString(2, Utility.trim(studentDiaryWrapper.refNo));
//				pstmtSub.setString(3, Utility.trim(studentDiaryWrapper.studentID));
//				pstmtSub.setString(4, Utility.trim(studentDiaryWrapper.studentID));
//				pstmtSub.setString(5, Utility.trim(studentDiaryWrapper.schoolID));
//
//				ResultSet resultSetSub = pstmtSub.executeQuery();
//				String deviceToken = null;
//				String message = null;
//				if (resultSetSub.next()) {
//
//					deviceToken = Utility.trim(resultSetSub.getString("DeviceToken"));
//					studentName = Utility.trim(resultSetSub.getString("StudentName")) + " "
//							+ Utility.trim(resultSetSub.getString("Surname"));
//
//					// System.out.println("studentName is " +studentName
//					// +studentDiaryWrapper.studentID );
//
//					message = birthdayMessage.replace("{{studentName}} {{surname}}", studentName);
//
//					System.out.println("deviceToken is " + deviceToken);
//					System.out.println("message is " + message);
//
//					if (deviceToken != null && !deviceToken.trim().equals("") && !vector.contains(deviceToken.trim())) {
//						vector.addElement(deviceToken);
//					}
//				}
//				resultSetSub.close();
//				pstmtSub.close();
//
//				String result = null;
//				if (vector.size() > 0) {
//					if (vector.size() == 1) {
//						// single device id
//						String toTokenId = vector.get(0);
//						result = sendMessage(toTokenId, null, message, "Birthday Message", "myicon",
//								studentDiaryWrapper.schoolID);
//					} else {
//						// multiple devices at the same time
//						String[] registration_ids = new String[vector.size()];
//						vector.copyInto(registration_ids);
//						result = sendMessage(null, registration_ids, message, "Birthday Message", "myicon",
//								studentDiaryWrapper.schoolID);
//
//					}
//
//					pstmtSub = con.prepareStatement(
//							"UPDATE StudentNotification SET Message=?, Delivered=?, MessageDateTime=?, GCMResponse=? "
//									+ " WHERE Delivered <>'Y' and MessageID=? and RefNo=? and StudentID=? and SchoolID=?");
//
//					pstmtSub.setString(1, message);
//					pstmtSub.setString(2, "Y");
//					pstmtSub.setTimestamp(3, Utility.getCurrentTime());
//					pstmtSub.setString(4, result);
//					pstmtSub.setString(5, Utility.trim(studentDiaryWrapper.messageID));
//					pstmtSub.setString(6, Utility.trim(studentDiaryWrapper.refNo));
//					pstmtSub.setString(7, Utility.trim(studentDiaryWrapper.studentID));
//					pstmtSub.setString(8, Utility.trim(studentDiaryWrapper.schoolID));
//
//					pstmtSub.executeUpdate();
//					pstmtSub.close();
//				}
//
//				System.out.println("send Birthday Message  successful");
//
//				// vector.addElement(schoolMessageWrapper);
//
//			}

			/*
			 * if (vector.size()>0) { dataArrayWrapper.schoolMessageWrapper = new
			 * SchoolMessageWrapper[vector.size()];
			 * vector.copyInto(dataArrayWrapper.schoolMessageWrapper);
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * System.out.println("total trn. in fetch " + vector.size());
			 * 
			 * } else
			 * 
			 * { dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
			 * dataArrayWrapper.schoolMessageWrapper[0]= schoolMessageWrapper;
			 * dataArrayWrapper.recordFound=true;
			 * 
			 * }
			 */
			if (resultSet != null)
				resultSet.close();
			pstmt.close();

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// ----------------------- End sendBirthdayMessage-------------------

	public String sendMessage(String toTokenId, String[] registration_ids, String body, String title, String icon,
			String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;

		String gcmKey = null;
		String gcmActivate = null;
		String gcmURL = null;
		String result = "";

		try {
			con = getConnection();

			// -----GCMKey GCMActivate code--

			sql = "SELECT GCMKey,GCMActivate,GCMURL from MST_Parameter where SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				gcmKey = Utility.trim(resultSet.getString("GCMKey"));
				gcmActivate = Utility.trim(resultSet.getString("GCMActivate"));
				gcmURL = Utility.trim(resultSet.getString("GCMURL"));
			}

			resultSet.close();
			pstmt.close();

			// ----------
			if (gcmActivate != null && gcmActivate.equals("Y")) {
				JsonObject mainJsonObj = new JsonObject();
				JsonObject childJsonObj = new JsonObject();
				childJsonObj.addProperty("body", body);
				childJsonObj.addProperty("title", title);
				childJsonObj.addProperty("icon", icon);
				mainJsonObj.add("notification", childJsonObj);

				if (toTokenId != null && !toTokenId.equals("")) {
					mainJsonObj.addProperty("to", toTokenId);
				} else {

					Gson gson = new GsonBuilder().create();
					JsonArray registration_ids_array = gson.toJsonTree(registration_ids).getAsJsonArray();

					System.out.println("registration_ids_array is = " + registration_ids_array);

					mainJsonObj.add("registration_ids", registration_ids_array);

				}

				// "d7WX4aaCHIA:APA91bE1vbhCltXu7BDaul7EcPV8XKY5RuP05rUNsJjzakZJ1h8eEXrOZAqyb-1q3-2hyCYXuc69_sA85V5JOi7maPeoBodCSnurn1E4lygvOib7ZWmAPouPrOOaP7eDiYWomtGqB6EH");

				String jsonFormattedString = mainJsonObj.toString();
				String urlParameters = jsonFormattedString.replaceAll("\\\\", "");

				System.out.println("urlParameters is = " + urlParameters);

				byte[] postData = urlParameters.getBytes("UTF-8");
				int postDataLength = postData.length;
				String request = gcmURL; // "https://gcm-http.googleapis.com/gcm/send";
				URL url = new URL(request);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setInstanceFollowRedirects(false);
				conn.setRequestProperty("Authorization", "key=" + gcmKey); // AIzaSyChya7IJ7KWKIlJbQIxv-apjxtcuStIBUg
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("charset", "UTF-8");
				conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				conn.setUseCaches(false);
				// POST
				DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
				System.out.println("before write " + postData.toString());
				writer.write(postData);
				System.out.println("after write ");
				writer.flush();

				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				while ((line = reader.readLine()) != null) {
					result += line;
				}

				writer.close();
				reader.close();
				System.out.println("result " + result);

			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return result;
	}

}
