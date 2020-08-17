package com.rootmind.helper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.google.firebase.database.utilities.encoding.CustomClassMapper;
import com.rootmind.controller.AES128Crypto;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.DriverLocationWrapper;
import com.rootmind.wrapper.DriverWrapper;
import com.rootmind.wrapper.FavoriteWrapper;
import com.rootmind.wrapper.RiderWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class DriverHelper extends Helper {

	// -----------------Start insertDriver---------------------

	public AbstractWrapper insertDriver(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// String countryCode=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {

			dataArrayWrapper = (DataArrayWrapper) verifyDriver(usersProfileWrapper, driverWrapper);

			if (dataArrayWrapper.recordFound == true && dataArrayWrapper.driverWrapper != null
					&& dataArrayWrapper.driverWrapper.length > 0) {
				DriverWrapper verifyDriverWrapper = dataArrayWrapper.driverWrapper[0];
				if (verifyDriverWrapper.recordFound == false) 
				{

					con = getConnection();

					sql = " INSERT INTO Driver(DriverRefNo, DriverID, FirstName, LastName, MobileNo, Email, "
							+ " Gender, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
							+ " RegisterDate, Userid, RCRegNo, VehicleNo, PANNo, Address1, Address2, Address3, CityID, "
							+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
							+ " PermPINCode, PermDistrictID, PermStateID, Status, VehicleType, Service, FCMToken, Locale) "
							+ " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);
					driverWrapper.driverRefNo = generateDriverRefNo();
					driverWrapper.driverID = generateDriverID(driverWrapper.driverRefNo);
					driverWrapper.status = Constants.ACTIVE_STATUS;
					pstmt.setString(1, Utility.trim(driverWrapper.driverRefNo));
					pstmt.setString(2, Utility.trim(driverWrapper.driverID));
					pstmt.setString(3, Utility.trim(driverWrapper.firstName));
					pstmt.setString(4, Utility.trim(driverWrapper.lastName));
					pstmt.setString(5, Utility.trim(driverWrapper.mobileNo));
					pstmt.setString(6, Utility.trim(driverWrapper.email));
					pstmt.setString(7, Utility.trim(driverWrapper.gender));

					pstmt.setString(8, Utility.trim(driverWrapper.drivingLicence));
					pstmt.setString(9, Utility.trim(driverWrapper.aadhaarNo));
					pstmt.setString(10, Utility.trim(driverWrapper.passportNo));
					pstmt.setString(11, Utility.trim(driverWrapper.emiratesID));
					pstmt.setString(12, Utility.trim(driverWrapper.visaNo));
					pstmt.setTimestamp(13, Utility.getCurrentTime()); // date time
					// pstmt.setDate(11,Utility.getDate(driverWrapper.registerDate));
					pstmt.setString(14, Utility.trim(driverWrapper.driverID));
					pstmt.setString(15, Utility.trim(driverWrapper.rcRegNo));
					pstmt.setString(16, Utility.trim(driverWrapper.vehicleNo));
					pstmt.setString(17, Utility.trim(driverWrapper.panNo));
					pstmt.setString(18, Utility.trim(driverWrapper.address1));
					pstmt.setString(19, Utility.trim(driverWrapper.address2));
					pstmt.setString(20, Utility.trim(driverWrapper.address3));
					pstmt.setString(21, Utility.trim(driverWrapper.cityID));
					pstmt.setString(22, Utility.trim(driverWrapper.pinCode));
					pstmt.setString(23, Utility.trim(driverWrapper.districtID));
					pstmt.setString(24, Utility.trim(driverWrapper.stateID));
					pstmt.setString(25, Utility.trim(driverWrapper.permAddress1));
					pstmt.setString(26, Utility.trim(driverWrapper.permAddress2));
					pstmt.setString(27, Utility.trim(driverWrapper.permAddress3));
					pstmt.setString(28, Utility.trim(driverWrapper.permCityID));
					pstmt.setString(29, Utility.trim(driverWrapper.permPINCode));
					pstmt.setString(30, Utility.trim(driverWrapper.permDistrictID));
					pstmt.setString(31, Utility.trim(driverWrapper.permStateID));
					pstmt.setString(32, Utility.trim(driverWrapper.status));
					pstmt.setString(33, Utility.trim(driverWrapper.vehicleType));
					pstmt.setString(34, Utility.trim(driverWrapper.service));
					pstmt.setString(35, Utility.trim(driverWrapper.fcmToken));
					pstmt.setString(36, Utility.trim(driverWrapper.locale));

					// ------Create Login Profile
					UsersHelper usersHelper = new UsersHelper();
					UsersWrapper usersWrapper = new UsersWrapper();
					usersWrapper.userid = driverWrapper.driverID;
					usersWrapper.password = new AES128Crypto().md5DB(driverWrapper.password); // password encrypt
					usersWrapper.driverID = driverWrapper.driverID;
					usersWrapper.driverRefNo = driverWrapper.driverRefNo;
					usersWrapper.email = driverWrapper.email;
					usersWrapper.mobileNo = driverWrapper.mobileNo;
					usersHelper.insertLoginProfile(usersProfileWrapper, usersWrapper, Constants.DRIVER_CODE);
					// ------

					pstmt.executeUpdate();
					pstmt.close();

					driverWrapper.recordFound = true;

					dataArrayWrapper.driverWrapper = new DriverWrapper[1];
					dataArrayWrapper.driverWrapper[0] = driverWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("Successfully inserted into insertDriver");

				}
				else
				{
					//since data comes with mobile number
					driverWrapper.driverRefNo = verifyDriverWrapper.driverRefNo;
					driverWrapper.driverID = verifyDriverWrapper.driverID;
					updateLoginDriver(usersProfileWrapper, driverWrapper);
				}
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

		return dataArrayWrapper;
	}

	// -----------------End insertDriver---------------------

	// -----------------Start updateDriver---------------------
	public AbstractWrapper updateDriver(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			System.out.println("Update DriverRefNo  is " + driverWrapper.driverRefNo);

//			pstmt = con.prepareStatement("UPDATE Driver SET FirstName=?, LastName=?, MobileNo=?, "
//					+ " Email=?, Gender=?, DrivingLicence=?, AadhaarNo=?, PassportNo=?, "
//					+ " EmiratesID=?, VisaNo=?, RCRegNo=?, VehicleNo=?, VehicleType=?, PANNo=?, Address1=?, Address2=?, "
//					+ " Address3=?, CityID=?, PINCode=?, DistrictID=?, StateID=?, PermAddress1=?, "
//					+ " PermAddress2=?, PermAddress3=?, PermCityID=?, PermPINCode=?, PermDistrictID=?, "
//					+ " PermStateID=? WHERE DriverRefNo=? AND DriverID=?");
			
			pstmt = con.prepareStatement("UPDATE Driver SET FirstName=?,  "
					+ " VehicleNo=?, VehicleType=?, Locale=?, ModifierID=?, ModifierDateTime=? "
					+ " WHERE DriverRefNo=? AND DriverID=?");

			
			pstmt.setString(1, Utility.trim(driverWrapper.firstName));
//			pstmt.setString(2, Utility.trim(driverWrapper.lastName));
//			pstmt.setString(3, Utility.trim(driverWrapper.mobileNo));
//			pstmt.setString(4, Utility.trim(driverWrapper.email));
//			pstmt.setString(5, Utility.trim(driverWrapper.gender));
//			pstmt.setString(6, Utility.trim(driverWrapper.drivingLicence));
//			pstmt.setString(7, Utility.trim(driverWrapper.aadhaarNo));
//			pstmt.setString(8, Utility.trim(driverWrapper.passportNo));
//			pstmt.setString(9, Utility.trim(driverWrapper.emiratesID));
//			pstmt.setString(10, Utility.trim(driverWrapper.visaNo));
//
//			pstmt.setString(11, Utility.trim(driverWrapper.rcRegNo));
			pstmt.setString(2, Utility.trim(driverWrapper.vehicleNo));
			pstmt.setString(3, Utility.trim(driverWrapper.vehicleType));
			pstmt.setString(4, Utility.trim(driverWrapper.locale));
			
//			pstmt.setString(13, Utility.trim(driverWrapper.panNo));
//
//			pstmt.setString(14, Utility.trim(driverWrapper.address1));
//			pstmt.setString(15, Utility.trim(driverWrapper.address2));
//			pstmt.setString(16, Utility.trim(driverWrapper.address3));
//			pstmt.setString(17, Utility.trim(driverWrapper.cityID));
//			pstmt.setString(18, Utility.trim(driverWrapper.pinCode));
//
//			pstmt.setString(19, Utility.trim(driverWrapper.districtID));
//			pstmt.setString(20, Utility.trim(driverWrapper.stateID));
//			pstmt.setString(21, Utility.trim(driverWrapper.permAddress1));
//			pstmt.setString(22, Utility.trim(driverWrapper.permAddress2));
//			pstmt.setString(23, Utility.trim(driverWrapper.permAddress3));
//			pstmt.setString(24, Utility.trim(driverWrapper.permCityID));
//			pstmt.setString(25, Utility.trim(driverWrapper.permPINCode));
//			pstmt.setString(26, Utility.trim(driverWrapper.permDistrictID));
//			pstmt.setString(27, Utility.trim(driverWrapper.permStateID));

			pstmt.setString(5,Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(6,Utility.getCurrentTime()); //  date time

			pstmt.setString(7, Utility.trim(driverWrapper.driverRefNo));
			pstmt.setString(8, Utility.trim(driverWrapper.driverID));

			pstmt.executeUpdate();
			pstmt.close();

			// ------Create Login Profile
			UsersHelper usersHelper = new UsersHelper();
			UsersWrapper usersWrapper = new UsersWrapper();
			usersWrapper.userid = driverWrapper.driverID;
			usersWrapper.riderID = driverWrapper.driverID;
			usersWrapper.riderRefNo = driverWrapper.driverRefNo;
			usersWrapper.email = driverWrapper.email;
			usersWrapper.mobileNo = driverWrapper.mobileNo;
			usersHelper.updateLoginProfile(usersProfileWrapper, usersWrapper, Constants.DRIVER_CODE);
			// ------

			driverWrapper.recordFound = true;
			dataArrayWrapper.driverWrapper = new DriverWrapper[1];
			dataArrayWrapper.driverWrapper[0] = driverWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully Driver details Updated");

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
	// -----------------End updateDriver--------------------

	// -----------------Start fetchDriver---------------------

	public AbstractWrapper fetchDriver(DriverWrapper driverWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

		// Vector<Object> vector = new Vector<Object>();
		String sql = null;

		try {
			//PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			sql = "SELECT DriverRefNo, DriverID, FirstName, LastName, MobileNo, Email, "
					+ " Gender, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
					+ " RegisterDate, Userid, RCRegNo, VehicleNo, PANNo, Address1, Address2, Address3, CityID, "
					+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
					+ " PermPINCode, PermDistrictID, PermStateID, Status, VehicleType, Locale  "
					+ " FROM Driver WHERE DriverRefNo=? AND DriverID=? AND Status=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(driverWrapper.driverRefNo));
			pstmt.setString(2, Utility.trim(driverWrapper.driverID));
			pstmt.setString(3, Constants.ACTIVE_STATUS);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				driverWrapper = new DriverWrapper();

				driverWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
				driverWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
				driverWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
				driverWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
				driverWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
				driverWrapper.email = Utility.trim(resultSet.getString("Email"));
				driverWrapper.gender = Utility.trim(resultSet.getString("Gender"));

				driverWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
				driverWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
				driverWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
				driverWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
				driverWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));

				driverWrapper.registerDate = Utility.setDate(resultSet.getString("RegisterDate"));
				driverWrapper.userid = Utility.trim(resultSet.getString("Userid"));

				driverWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
				driverWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
				driverWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));

				driverWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
				driverWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
				driverWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
				driverWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
				driverWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
				driverWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
				driverWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				driverWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
				driverWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
				driverWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
				driverWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
				driverWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
				driverWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
				driverWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));
				driverWrapper.status = Utility.trim(resultSet.getString("Status"));
				driverWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));
				driverWrapper.locale = Utility.trim(resultSet.getString("Locale"));

//				driverWrapper.cityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.cityID, "MST_City");
//				driverWrapper.districtIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.districtID,
//						"MST_District");
//				driverWrapper.stateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.stateID, "MST_State");
//				driverWrapper.permCityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permCityID, "MST_City");
//				driverWrapper.permDistrictIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permDistrictID,
//						"MST_District");
//				driverWrapper.permStateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permStateID, "MST_State");

				driverWrapper.recordFound = true;

				System.out.println("fetchDriver  successful");

			}

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
		}

		finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return driverWrapper;
	}
	// -----------------End fetchDetails---------------------

	// -----------------Start fetchDriversearch---------------------

	public AbstractWrapper fetchDriverSearch(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		String sql = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {

			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			if (driverWrapper.driverID != null && !driverWrapper.driverID.equals("")) {
				sql = " WHERE DriverID =?";

				System.out.println("driverWrapper travellerID " + sql);

			}

			else if (driverWrapper.firstName != null && !driverWrapper.firstName.equals("")) {

				sql = " WHERE UPPER(FirstName) LIKE ?";

				System.out.println(" first Name " + driverWrapper.firstName);

			} else if (driverWrapper.lastName != null && !driverWrapper.lastName.equals("")) {

				sql = " WHERE  UPPER(LastName) LIKE ?";

				System.out.println(" lastName  " + driverWrapper.lastName);

			}

			else if (driverWrapper.mobileNo != null && !driverWrapper.mobileNo.equals("")) {

				sql = " WHERE MobileNo LIKE ?";

			} else if (driverWrapper.aadhaarNo != null && !driverWrapper.aadhaarNo.equals("")) {
				sql = " WHERE AadhaarNo =?";

				System.out.println("driverWrapper AadhaarNo " + sql);

			}

			PreparedStatement pstmt = con
					.prepareStatement("DriverRefNo, DriverID, FirstName, LastName, MobileNo, Email, "
							+ " Gender, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
							+ " RegisterDate, Userid, RCRegNo, VehicleNo, PANNo, Address1, Address2, Address3, CityID, "
							+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
							+ " PermPINCode, PermDistrictID, PermStateID FROM Traveller " + sql);

			if (driverWrapper.driverID != null && !driverWrapper.driverID.trim().isEmpty()) {
				pstmt.setString(1, driverWrapper.driverID.trim());

			}

			else if (driverWrapper.firstName != null && !driverWrapper.firstName.trim().isEmpty()) {

				pstmt.setString(1, '%' + driverWrapper.firstName.trim().toUpperCase() + '%');

			} else if (driverWrapper.lastName != null && !driverWrapper.lastName.trim().isEmpty()) {

				pstmt.setString(1, '%' + driverWrapper.lastName.trim().toUpperCase() + '%');

			}

			else if (driverWrapper.mobileNo != null && !driverWrapper.mobileNo.trim().isEmpty()) {

				pstmt.setString(1, driverWrapper.mobileNo.trim());

			}

			else if (driverWrapper.aadhaarNo != null && !driverWrapper.aadhaarNo.trim().isEmpty()) {
				pstmt.setString(1, driverWrapper.aadhaarNo.trim());

			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				driverWrapper = new DriverWrapper();

				driverWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
				driverWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
				driverWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
				driverWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
				driverWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
				driverWrapper.email = Utility.trim(resultSet.getString("Email"));
				driverWrapper.gender = Utility.trim(resultSet.getString("Gender"));

				driverWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
				driverWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
				driverWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
				driverWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
				driverWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));

				driverWrapper.registerDate = Utility.setDate(resultSet.getString("RegisterDate"));
				driverWrapper.userid = Utility.trim(resultSet.getString("Userid"));

				driverWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
				driverWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
				driverWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));

				driverWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
				driverWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
				driverWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
				driverWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
				driverWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
				driverWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
				driverWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				driverWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
				driverWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
				driverWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
				driverWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
				driverWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
				driverWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
				driverWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));

				driverWrapper.cityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.cityID, "MST_City");
				driverWrapper.districtIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.districtID,
						"MST_District");
				driverWrapper.stateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.stateID, "MST_State");
				driverWrapper.permCityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permCityID, "MST_City");
				driverWrapper.permDistrictIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permDistrictID,
						"MST_District");
				driverWrapper.permStateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permStateID, "MST_State");

				driverWrapper.recordFound = true;

				System.out.println("Driver Details Queue fetch successful");

				vector.addElement(driverWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.driverWrapper = new DriverWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.driverWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

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
		}

		finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// -----------------End fetchtravellerSearch---------------------

	// -----------------generateDriverRefNo-------------------------------
	public String generateDriverRefNo() throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		int driverRefNo = 0;
		String finalDriverRefNo = null;
		String driverCode = null;

		try {

			con = getConnection();

			sql = "SELECT DriverRefNo, DriverCode from Parameter";

			PreparedStatement pstmt = con.prepareStatement(sql);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				driverRefNo = resultSet.getInt("DriverRefNo");
				System.out.println("DriverRefNo " + driverRefNo);
				driverCode = resultSet.getString("DriverCode");

			}

			resultSet.close();
			pstmt.close();

			if (driverRefNo == 0) {
				driverRefNo = 1;

			} else {

				driverRefNo = driverRefNo + 1;
			}

			sql = "UPDATE Parameter set DriverRefNo=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, driverRefNo);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 5;

			finalDriverRefNo = driverCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", driverRefNo);

			System.out.println("Successfully generated DriverRefNo " + finalDriverRefNo);

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

		return finalDriverRefNo;
	}

	// -----------------End generateDriverRefNo---------------------------

	// -----------------generateDriverID-------------------------------

	public String generateDriverID(String refNo) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

//		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		int driverRefNo = 0;
		String finalDriverID = null;
		String driverCode = null;

		try {
			con = getConnection();

			sql = "SELECT DriverRefNo,DriverCode from Parameter";

			PreparedStatement pstmt = con.prepareStatement(sql);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				driverRefNo = resultSet.getInt("DriverRefNo");
				System.out.println("driverRefNo" + driverRefNo);
				driverCode = resultSet.getString("DriverCode");

			}

			resultSet.close();
			pstmt.close();

			// if(driverRefNo==0)
			// {
			// driverRefNo=1;
			//
			// }
			// else
			// {
			//
			// driverRefNo=driverRefNo+1;
			// }
			//
			// sql="UPDATE Parameter set DriverRefNo=?";
			//
			//
			// System.out.println("sql " + sql);
			//
			// pstmt = con.prepareStatement(sql);
			//
			// pstmt.setInt(1,driverRefNo);
			//
			// pstmt.executeUpdate();
			// pstmt.close();

			//int paddingSize = 5;

			// int paddingSize=6-String.valueOf(studentID).length();

			// finalDriverID=driverCode+dmyFormat.format(new
			// java.util.Date()).toUpperCase()+String.format("%0" +paddingSize
			// +"d",driverRefNo);
			finalDriverID = driverCode + refNo.substring(7);

			System.out.println("Successfully generated finalDriverID " + finalDriverID);

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

		return finalDriverID;
	}

	// -----------------End generateDriverID---------------------------

	// -----------------Start verifyDriver---------------------

	public AbstractWrapper verifyDriver(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		String sql = null;
		// boolean filterFound=false;
		// int n=0;

		try {

			//PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			sql = "SELECT DriverRefNo, DriverID, FirstName, LastName, MobileNo, Email, "
					+ " Gender, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
					+ " RegisterDate, Userid, RCRegNo, VehicleNo, PANNo, Address1, Address2, Address3, CityID, "
					+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
					+ " PermPINCode, PermDistrictID, PermStateID, Status, VehicleType, Locale "
					+ " FROM Driver WHERE (MobileNo=? OR Email=?) AND Status=?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Utility.trim(driverWrapper.mobileNo));
			pstmt.setString(2, Utility.trim(driverWrapper.email));
			pstmt.setString(3, Constants.ACTIVE_STATUS);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				driverWrapper = new DriverWrapper();
				
				driverWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
				driverWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
				driverWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
				driverWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
				driverWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
				driverWrapper.email = Utility.trim(resultSet.getString("Email"));
				driverWrapper.gender = Utility.trim(resultSet.getString("Gender"));

				driverWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
				driverWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
				driverWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
				driverWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
				driverWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));

				driverWrapper.registerDate = Utility.setDate(resultSet.getString("RegisterDate"));
				driverWrapper.userid = Utility.trim(resultSet.getString("Userid"));

				driverWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
				driverWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
				driverWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));

				driverWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
				driverWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
				driverWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
				driverWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
				driverWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
				driverWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
				driverWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				driverWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
				driverWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
				driverWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
				driverWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
				driverWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
				driverWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
				driverWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));
				driverWrapper.status = Utility.trim(resultSet.getString("Status"));
				driverWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));
				driverWrapper.locale = Utility.trim(resultSet.getString("Locale"));

//				driverWrapper.cityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.cityID, "MST_City");
//				driverWrapper.districtIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.districtID,
//						"MST_District");
//				driverWrapper.stateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.stateID, "MST_State");
//				driverWrapper.permCityIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permCityID, "MST_City");
//				driverWrapper.permDistrictIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permDistrictID,
//						"MST_District");
//				driverWrapper.permStateIDValue = popoverHelper.fetchPopoverDesc(driverWrapper.permStateID, "MST_State");

				driverWrapper.recordFound = true;

				dataArrayWrapper.driverWrapper = new DriverWrapper[1];
				dataArrayWrapper.driverWrapper[0] = driverWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Driver Mobile/Email Found ");

			} else {
				driverWrapper.emobileFound = false;
				driverWrapper.recordFound = false;

				dataArrayWrapper.driverWrapper = new DriverWrapper[1];
				dataArrayWrapper.driverWrapper[0] = driverWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Driver Mobile/Email Not Found ");

			}

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
		}

		finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// -----------------End verify---------------------

	// -----------------Start insertDriverlocation---------------------

	public AbstractWrapper insertDriverLocation(UsersWrapper usersProfileWrapper,
			DriverLocationWrapper[] driverLocationWrapperArray) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		Vector<Object> vector = new Vector<Object>();
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// String countryCode=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("DELETE FROM DriverLocation WHERE RiderRefNo=? AND RiderID=?");
			pstmt.setString(1, Utility.trim(driverLocationWrapperArray[0].riderRefNo));
			pstmt.setString(2, Utility.trim(driverLocationWrapperArray[0].riderID));
			pstmt.executeUpdate();
			pstmt.close();

			for (int i = 0; i <= driverLocationWrapperArray.length - 1; i++) {

				sql = " INSERT INTO DriverLocation(DriverRefNo, DriverID, RiderRefNo, RiderID, DriverLat, DriverLng, Distance, "
						+ " MakerID, MakerDateTime) Values (?,?,?,?,?,?,?,?,?)";

				System.out.println("sql " + sql);

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(driverLocationWrapperArray[i].driverRefNo));
				pstmt.setString(2, Utility.trim(driverLocationWrapperArray[i].driverID));
				pstmt.setString(3, Utility.trim(driverLocationWrapperArray[i].riderRefNo));
				pstmt.setString(4, Utility.trim(driverLocationWrapperArray[i].riderID));
				pstmt.setString(5, Utility.trim(driverLocationWrapperArray[i].driverLat));
				pstmt.setString(6, Utility.trim(driverLocationWrapperArray[i].driverLng));
				pstmt.setString(7, Utility.trim(driverLocationWrapperArray[i].distance));
				pstmt.setString(8, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(9, Utility.getCurrentTime()); // date time

				pstmt.executeUpdate();
				pstmt.close();

				driverLocationWrapperArray[i].recordFound = true;

			}

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into driver Location");

			// ------------Get Neareast Locations using stored procedure----//
			// -----Using The Haversine Formula--//

			System.out.println("before SP_DriverGeolocation ");
			System.out.println("driverLocationWrapperArray[0].riderRefNo " + driverLocationWrapperArray[0].riderRefNo);
			System.out.println("driverLocationWrapperArray[0].riderID " + driverLocationWrapperArray[0].riderID);
			System.out.println("driverLocationWrapperArray[0].riderLat " + driverLocationWrapperArray[0].riderLat);
			System.out.println("driverLocationWrapperArray[0].riderLng " + driverLocationWrapperArray[0].riderLng);

			String query = "{CALL SP_DriverGeolocation(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(query);
			cstmt.setString(1, Utility.trim(driverLocationWrapperArray[0].riderRefNo));
			cstmt.setString(2, Utility.trim(driverLocationWrapperArray[0].riderID));
			cstmt.setDouble(3, Double.parseDouble(driverLocationWrapperArray[0].riderLat));
			cstmt.setDouble(4, Double.parseDouble(driverLocationWrapperArray[0].riderLng));
			cstmt.setInt(5, 10); // radius of the drivers to locate

			resultSet = cstmt.executeQuery();

			DriverLocationWrapper driverLocationWrapper = null;
			vector.clear();

			while (resultSet.next()) {

				driverLocationWrapper = new DriverLocationWrapper();

				driverLocationWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
				driverLocationWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
				driverLocationWrapper.riderRefNo = Utility.trim(resultSet.getString("RiderRefNo"));
				driverLocationWrapper.riderID = Utility.trim(resultSet.getString("RiderID"));
				driverLocationWrapper.driverLat = String.valueOf(resultSet.getDouble("DriverLat"));
				driverLocationWrapper.driverLng = String.valueOf(resultSet.getDouble("DriverLng"));
				driverLocationWrapper.distance = resultSet.getBigDecimal("Distance").toString();

				System.out.println("driverLocationWrapper.distance " + driverLocationWrapper.distance);

				driverLocationWrapper.recordFound = true;
				vector.addElement(driverLocationWrapper);
			}

			dataArrayWrapper.driverLocationWrapper = new DriverLocationWrapper[vector.size()];
			vector.copyInto(dataArrayWrapper.driverLocationWrapper);
			dataArrayWrapper.recordFound = true;

			resultSet.close();
			cstmt.close();

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

	// -----------------End insertDriverlocation---------------------
	
	// -----------------Start updateDriverVacantStatus---------------------
		public AbstractWrapper updateVacantStatus(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

			PreparedStatement pstmt = null;

			try {
				con = getConnection();

				System.out.println("Update DriverRefNo  is " + driverWrapper.driverRefNo);

				
				pstmt = con.prepareStatement("UPDATE Driver SET CurrentLat=?, CurrentLng=?, CurrentLocation=?, VacantStatus=?  "
						+ " WHERE DriverRefNo=? AND DriverID=?");

				pstmt.setBigDecimal(1, driverWrapper.currentLat);
				pstmt.setBigDecimal(2, driverWrapper.currentLng);
				pstmt.setString(3, Utility.trim(driverWrapper.currentLocation));
				pstmt.setString(4, Utility.trim(driverWrapper.vacantStatus));
				pstmt.setString(5, Utility.trim(driverWrapper.driverRefNo));
				pstmt.setString(6, Utility.trim(driverWrapper.driverID));

				pstmt.executeUpdate();
				pstmt.close();

				driverWrapper.recordFound = true;
				dataArrayWrapper.driverWrapper = new DriverWrapper[1];
				dataArrayWrapper.driverWrapper[0] = driverWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Driver Vacant Status details Updated");

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
		// -----------------End updateDriverVacantStatus--------------------

		
		
		/**
		 * Calculate distance between two points in latitude and longitude taking
		 * into account height difference. If you are not interested in height
		 * difference pass 0.0. Uses Haversine method as its base.
		 * 
		 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
		 * el2 End altitude in meters
		 * @returns Distance in Meters
		 */
		
		
		// -----------------Start insertDriver---------------------

		public AbstractWrapper fetchDriverLocation(UsersWrapper usersProfileWrapper,
				RiderWrapper riderWrapper) throws Exception {

			Connection con = null;
			ResultSet resultSet = null;
			//Vector<DriverWrapper> vector = new Vector<DriverWrapper>();
			
			ArrayList<DriverWrapper> driverDistance= new ArrayList<DriverWrapper>();

			
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql = null;
			// String countryCode=null;

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);

			PreparedStatement pstmt = null;
			DriverWrapper driverWrapper=null;
			double driverRadius=0;
			FavoriteWrapper favoriteWrapper=null;

			try {
				
				
				FavoriteHelper favoriteHelper = new FavoriteHelper();

				con = getConnection();
				

				//---------Get parameter------
				sql="SELECT DriverRadius from Parameter";
				
				pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					
					driverRadius=resultSet.getDouble("DriverRadius");
					
				}
				
				if (resultSet != null)
					resultSet.close();
				pstmt.close();
				//----------end get parameter


				sql = "SELECT DriverRefNo, DriverID, FirstName, LastName, MobileNo, "
						+ " VehicleNo, Status, VehicleType, CurrentLat, CurrentLng, CurrentLocation, VacantStatus, Service, Rating, Locale "
						+ " FROM Driver WHERE Status='ACTIVE' and VacantStatus='VACANT' ";
				
				if(riderWrapper.serviceCode.equals(Constants.TRANSPORT_BUSINESS))
				{
					sql = sql + " and VehicleType = ?";
				}

				pstmt = con.prepareStatement(sql);

				
				if(riderWrapper.serviceCode.equals(Constants.TRANSPORT_BUSINESS))
				{
					pstmt.setString(1, riderWrapper.vehicleType);
				}

				resultSet = pstmt.executeQuery();

				while (resultSet.next()) {

					driverWrapper = new DriverWrapper();

					driverWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
					driverWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
					driverWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
					driverWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
					driverWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
					driverWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
					driverWrapper.status = Utility.trim(resultSet.getString("Status"));
					driverWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));

					driverWrapper.currentLat = resultSet.getBigDecimal("CurrentLat");
					driverWrapper.currentLng = resultSet.getBigDecimal("CurrentLng");
					driverWrapper.currentLocation = Utility.trim(resultSet.getString("CurrentLocation"));
					driverWrapper.vacantStatus = Utility.trim(resultSet.getString("VacantStatus"));
					driverWrapper.service = Utility.trim(resultSet.getString("Service"));
					

					//------Favorite & Rating------
					favoriteWrapper = new FavoriteWrapper();
					favoriteWrapper.favoriteRefNo = driverWrapper.driverRefNo;
					favoriteWrapper.favoriteID = driverWrapper.driverID;
					favoriteWrapper.riderRefNo = riderWrapper.riderRefNo;
					favoriteWrapper.riderID = riderWrapper.riderID;
					favoriteWrapper = (FavoriteWrapper) favoriteHelper.fetchFavorite(favoriteWrapper);
					
					if(favoriteWrapper.recordFound==true)
					{
						driverWrapper.favorite = (favoriteWrapper.favorite==null?"N":favoriteWrapper.favorite); //updated favorite
						driverWrapper.yourRating = favoriteWrapper.rating; //individual rating for that rider
					}
					else
					{
						driverWrapper.favorite="N";//favorite not found
						driverWrapper.yourRating = 0.0f; //individual rating for that rider

					}
					//------end of favorite & rating

					//this is avg Rating
					driverWrapper.avgRating = round(resultSet.getFloat("Rating"),1);
					driverWrapper.locale = Utility.trim(resultSet.getString("Locale"));

					
					
					System.out.println("driverWrapper.avgRating  " + driverWrapper.avgRating);

					
					//double roundOff = Math.round(a * 100.0) / 100.0;
					//to convert meters to kms and to round of two decimals
					driverWrapper.distance= Math.round((Utility.distance(riderWrapper.currentLat.doubleValue(), driverWrapper.currentLat.doubleValue(), riderWrapper.currentLng.doubleValue(), driverWrapper.currentLng.doubleValue(),0,0)/1000) *100.0) /100.0;
					System.out.println("distance " + driverWrapper.driverID + "  " +driverWrapper.distance);
					driverWrapper.duration = Math.round(driverWrapper.distance/20.0);
					
					driverWrapper.recordFound = true;

					//add only within range of driver radius
					if(  driverWrapper.distance <= driverRadius)
					{
						
						driverDistance.add(driverWrapper);
					}
					
					System.out.println("fetchDriverLocation  successful");

				}

				if (resultSet != null)
					resultSet.close();
				pstmt.close();

				if (driverDistance.size() > 0) {

					Collections.sort(driverDistance, new CustomComparator());
					
					dataArrayWrapper.driverWrapper = new DriverWrapper[driverDistance.size()];
					driverDistance.toArray(dataArrayWrapper.driverWrapper);
					dataArrayWrapper.recordFound = true;;
				}
				else
				{
					dataArrayWrapper.driverWrapper = new DriverWrapper[1];
					driverWrapper = new DriverWrapper();
					driverWrapper.recordFound=false;
					dataArrayWrapper.driverWrapper[0]=driverWrapper;
					dataArrayWrapper.recordFound = true;;
					
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

			return dataArrayWrapper;
		}

		// -----------------End insertDriver---------------------
		
		public class CustomComparator implements Comparator<DriverWrapper> {
		    @Override
		    public int compare(DriverWrapper driver1, DriverWrapper driver2) {
		        return driver1.distance > driver2.distance?1: driver1.distance<driver2.distance ?-1:0;
		    }
		}
		
		
		// -----------------Start updateLoginDriver---------------------
		public AbstractWrapper updateLoginDriver(UsersWrapper usersProfileWrapper, DriverWrapper driverWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			

			PreparedStatement pstmt = null;
			String locale=null;


			try {
				con = getConnection();

				System.out.println("Update DriverRefNo  is " + driverWrapper.driverRefNo + " locale " + driverWrapper.locale);

				//----don't overwrite exisitng locale while logging...if not available then default----
				pstmt = con.prepareStatement("SELECT Locale FROM Driver WHERE DriverRefNo=? AND DriverID=?");
				pstmt.setString(1,Utility.trim(driverWrapper.driverRefNo));
				pstmt.setString(2,Utility.trim(driverWrapper.driverID));
				resultSet = pstmt.executeQuery();
				if(resultSet.next()) 
				{
					locale=Utility.trim(resultSet.getString("Locale"));
					
					if(!Utility.isEmpty(locale))
					{
						driverWrapper.locale = locale;
					}
				}
				resultSet.close();
				pstmt.close();
				//-------end of local retrive

				
				pstmt = con.prepareStatement("UPDATE Driver SET FCMToken=?,  "
						+ " LastloginDateTime=?, Locale=? "
						+ " WHERE DriverRefNo=? AND DriverID=?");

				
				pstmt.setString(1, Utility.trim(driverWrapper.fcmToken));
				pstmt.setTimestamp(2,Utility.getCurrentTime()); //  date time
				pstmt.setString(3, Utility.trim(driverWrapper.locale));
				pstmt.setString(4, Utility.trim(driverWrapper.driverRefNo));
				pstmt.setString(5, Utility.trim(driverWrapper.driverID));

				pstmt.executeUpdate();
				pstmt.close();

				driverWrapper.recordFound = true;
				dataArrayWrapper.driverWrapper = new DriverWrapper[1];
				dataArrayWrapper.driverWrapper[0] = driverWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Driver details Updated");

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
		// -----------------End updateDriver--------------------
		
		public static float round(float number, int decimalPlace) {
			BigDecimal bd = new BigDecimal(number);
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		}

}
