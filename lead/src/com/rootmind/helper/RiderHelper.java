package com.rootmind.helper;

import java.math.BigDecimal;
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
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import com.rootmind.controller.AES128Crypto;
import com.rootmind.helper.DriverHelper.CustomComparator;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.DriverWrapper;
import com.rootmind.wrapper.FavoriteWrapper;
import com.rootmind.wrapper.ImageWrapper;
import com.rootmind.wrapper.RiderWrapper;
import com.rootmind.wrapper.ServiceWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class RiderHelper extends Helper {
	
	//-----------------Start insertTraveller---------------------

		public AbstractWrapper insertRider(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)throws Exception {
				
				Connection con = null;
				ResultSet resultSet = null;
		
				DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				String sql=null;
				//String countryCode=null;
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator(',');
//				formatter.applyPattern("###,###,###,##0.00");
//				formatter.setDecimalFormatSymbols(symbols);

				PreparedStatement pstmt=null;
				
				
				try {
					
					
						dataArrayWrapper = (DataArrayWrapper)verifyRider(usersProfileWrapper, riderWrapper);
						
						if(dataArrayWrapper.recordFound==true && dataArrayWrapper.riderWrapper!=null && dataArrayWrapper.riderWrapper.length>0)
						{
							RiderWrapper verifyRiderWrapper = dataArrayWrapper.riderWrapper[0];
							if(verifyRiderWrapper.recordFound==false)
							{
								
								con = getConnection();

								sql=" INSERT INTO Rider(RiderRefNo, RiderID, FirstName, LastName, MobileNo, Email, "
										+ " Gender, City, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
										+ " RegisterDate, Userid, RCRegNo, VehicleNo, VehicleType, PANNo, Address1, Address2, Address3, CityID, "
										+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
										+ " PermPINCode, PermDistrictID, PermStateID, Status, Service, FCMToken, Locale, VacantStatus,"
										+ " MakerID, MakerDateTime ) "
										+ " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
								

								
								System.out.println("sql " + sql);
								
								pstmt = con.prepareStatement(sql);
								riderWrapper.riderRefNo=generateRiderRefNo();
								riderWrapper.riderID=generateRiderID(riderWrapper.riderRefNo);
								riderWrapper.status=Constants.ACTIVE_STATUS;
								riderWrapper.vacantStatus=Constants.VACANT_CODE;
								pstmt.setString(1,Utility.trim(riderWrapper.riderRefNo));
								pstmt.setString(2,Utility.trim(riderWrapper.riderID));
								pstmt.setString(3,Utility.trim(riderWrapper.firstName));
								pstmt.setString(4,Utility.trim(riderWrapper.lastName)); 
								pstmt.setString(5,Utility.trim(riderWrapper.mobileNo));
								pstmt.setString(6,Utility.trim(riderWrapper.email));  
								pstmt.setString(7,Utility.trim(riderWrapper.gender)); 
								pstmt.setString(8,Utility.trim(riderWrapper.city));
								pstmt.setString(9, Utility.trim(riderWrapper.drivingLicence));
								pstmt.setString(10, Utility.trim(riderWrapper.aadhaarNo));
								pstmt.setString(11, Utility.trim(riderWrapper.passportNo));
								pstmt.setString(12, Utility.trim(riderWrapper.emiratesID));
								pstmt.setString(13, Utility.trim(riderWrapper.visaNo));
								pstmt.setTimestamp(14,Utility.getCurrentTime()); //  date time
								pstmt.setString(15,Utility.trim(riderWrapper.riderID));  
								pstmt.setString(16, Utility.trim(riderWrapper.rcRegNo));
								pstmt.setString(17, Utility.trim(riderWrapper.vehicleNo));
								pstmt.setString(18, Utility.trim(riderWrapper.vehicleType));
								pstmt.setString(19, Utility.trim(riderWrapper.panNo));
								pstmt.setString(20, Utility.trim(riderWrapper.address1));
								pstmt.setString(21, Utility.trim(riderWrapper.address2));
								pstmt.setString(22, Utility.trim(riderWrapper.address3));
								pstmt.setString(23, Utility.trim(riderWrapper.cityID));
								pstmt.setString(24, Utility.trim(riderWrapper.pinCode));
								pstmt.setString(25, Utility.trim(riderWrapper.districtID));
								pstmt.setString(26, Utility.trim(riderWrapper.stateID));
								pstmt.setString(27, Utility.trim(riderWrapper.permAddress1));
								pstmt.setString(28, Utility.trim(riderWrapper.permAddress2));
								pstmt.setString(29, Utility.trim(riderWrapper.permAddress3));
								pstmt.setString(30, Utility.trim(riderWrapper.permCityID));
								pstmt.setString(31, Utility.trim(riderWrapper.permPINCode));
								pstmt.setString(32, Utility.trim(riderWrapper.permDistrictID));
								pstmt.setString(33, Utility.trim(riderWrapper.permStateID));
								pstmt.setString(34, Utility.trim(riderWrapper.status));
								pstmt.setString(35, Utility.trim(riderWrapper.serviceCode));
								pstmt.setString(36, Utility.trim(riderWrapper.fcmToken));
								pstmt.setString(37, Utility.trim(riderWrapper.locale));
								pstmt.setString(38, Utility.trim(riderWrapper.vacantStatus));
								pstmt.setString(39,Utility.trim(usersProfileWrapper.userid)); 
								pstmt.setTimestamp(40,Utility.getCurrentTime()); //  date time

								pstmt.executeUpdate();
								pstmt.close();
								
//								//------Create Login Profile
//								UsersHelper usersHelper=new UsersHelper();
//								UsersWrapper usersWrapper=new UsersWrapper();
//								usersWrapper.userid=riderWrapper.riderID;
//								usersWrapper.password=new AES128Crypto().md5DB(riderWrapper.password); //password encrypt
//								usersWrapper.riderID=riderWrapper.riderID;
//								usersWrapper.riderRefNo=riderWrapper.riderRefNo;
//								usersWrapper.email=riderWrapper.email;
//								usersWrapper.mobileNo=riderWrapper.mobileNo;
//								usersHelper.insertLoginProfile(usersProfileWrapper,usersWrapper,Constants.RIDER_CODE);
//								//------					
								
								
								riderWrapper.recordFound=true;
							
								
								System.out.println("Successfully inserted into insertRider");
								
							}
							else
							{
								//since data comes with mobile number
								riderWrapper.riderRefNo = verifyRiderWrapper.riderRefNo;
								riderWrapper.riderID = verifyRiderWrapper.riderID;
								riderWrapper = (RiderWrapper)updateLoginRider(usersProfileWrapper, riderWrapper);
								
								
							}
							
							dataArrayWrapper.riderWrapper=new RiderWrapper[1];
							dataArrayWrapper.riderWrapper[0]=riderWrapper;
							dataArrayWrapper.recordFound=true;

						}
					
					
				} catch (SQLException se) {
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				} catch (NamingException ne) {
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}
		
				return dataArrayWrapper;
			}
			
			//-----------------End inserttravellerDetails---------------------
		
		
		//-----------------Start updateTraveller---------------------
		public AbstractWrapper updateRider(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)throws Exception {
			
				Connection con = null;
				ResultSet resultSet = null;
		
				//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator(',');
//				formatter.applyPattern("###,###,###,##0.00");
//				formatter.setDecimalFormatSymbols(symbols);
		
				PreparedStatement pstmt=null;
				
				System.out.println("Rider Update userid " +usersProfileWrapper.userid);
				
				try {
						con = getConnection();
						
						 pstmt = con.prepareStatement("UPDATE Rider SET FirstName=?,  Locale=?,  " //LastName=?, MobileNo=?,
						 		+ "  ModifierID=?, ModifierDateTime=?  WHERE RiderRefNo=? AND RiderID=?"); //Email=?, Gender=?, City=?,
					
						 //VehicleNo=?, VehicleType=?,
								 
					    pstmt.setString(1,Utility.trim(riderWrapper.firstName));
						//pstmt.setString(2, Utility.trim(riderWrapper.vehicleNo));
						//pstmt.setString(3, Utility.trim(riderWrapper.vehicleType));

					    pstmt.setString(2,Utility.trim(riderWrapper.locale));
						//pstmt.setString(2,Utility.trim(riderWrapper.lastName));  
						//pstmt.setString(3,Utility.trim(riderWrapper.mobileNo)); 
						//pstmt.setString(4,Utility.trim(riderWrapper.email)); 
						//pstmt.setString(5,Utility.trim(riderWrapper.gender));  
						//pstmt.setString(6,Utility.trim(riderWrapper.city));
						pstmt.setString(3,Utility.trim(usersProfileWrapper.userid));
						pstmt.setTimestamp(4,Utility.getCurrentTime()); //  date time
						pstmt.setString(5,Utility.trim(riderWrapper.riderRefNo));
						pstmt.setString(6,Utility.trim(riderWrapper.riderID));
					
						pstmt.executeUpdate();
						pstmt.close();
						
						//------Create Login Profile
						UsersHelper usersHelper=new UsersHelper();
						UsersWrapper usersWrapper=new UsersWrapper();
						usersWrapper.userid=riderWrapper.riderID;
						usersWrapper.riderID=riderWrapper.riderID;
						usersWrapper.riderRefNo=riderWrapper.riderRefNo;
						usersWrapper.email=riderWrapper.email;
						usersWrapper.mobileNo=riderWrapper.mobileNo;
						usersHelper.updateLoginProfile(usersProfileWrapper,usersWrapper,Constants.RIDER_CODE);
						//------					

						riderWrapper.recordFound=true;
						
//						FCMNotification fcmNotification = new FCMNotification("RIDER_REGISTER_MESSAGE", 
//								riderWrapper.riderRefNo, riderWrapper.riderID, "Rider","");
//						fcmNotification.run();

						//Messaging.sendCommonMessage(riderWrapper.fcmToken);
						
						System.out.println("Successfully Rider details Updated");
						
						
				} catch (SQLException se) {
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				} catch (NamingException ne) {
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}
		
				return riderWrapper;
		}
		//-----------------End updateTraveller--------------------
		
		//-----------------Start fetchTraveller---------------------
		
		public AbstractWrapper fetchRider(RiderWrapper riderWrapper)throws Exception {

				Connection con = null;
				ResultSet resultSet = null;
				
				//DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
				
				//Vector<Object> vector = new Vector<Object>();
				String sql=null;
				
				try {
					
						con = getConnection();
						
						sql="SELECT RiderRefNo, RiderID, FirstName, LastName, MobileNo, Email, "
								+ " Gender, City, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
								+ " RegisterDate, Userid, RCRegNo, VehicleNo, VehicleType, PANNo, Address1, Address2, Address3, CityID, "
								+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
								+ " PermPINCode, PermDistrictID, PermStateID, Status, Service, Locale "
								+ " FROM Rider WHERE RiderRefNo=? AND RiderID=? AND Status=?";
						
						
						PreparedStatement pstmt = con.prepareStatement(sql);
						
						pstmt.setString(1,Utility.trim(riderWrapper.riderRefNo));
						pstmt.setString(2,Utility.trim(riderWrapper.riderID));
						pstmt.setString(3,Constants.ACTIVE_STATUS);
						
						
						resultSet = pstmt.executeQuery();
						
						if(resultSet.next()) 
						{
							
							riderWrapper=new RiderWrapper();
							
							riderWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
							riderWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
							riderWrapper.firstName=Utility.trim(resultSet.getString("FirstName"));  
							riderWrapper.lastName=Utility.trim(resultSet.getString("LastName"));
							riderWrapper.mobileNo=Utility.trim(resultSet.getString("MobileNo"));  
							riderWrapper.email=Utility.trim(resultSet.getString("Email"));
							riderWrapper.gender=Utility.trim(resultSet.getString("Gender"));
							riderWrapper.city=Utility.trim(resultSet.getString("City"));  
							riderWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
							riderWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
							riderWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
							riderWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
							riderWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));
							
							riderWrapper.registerDate=Utility.setDate(resultSet.getString("RegisterDate"));
							riderWrapper.userid=Utility.trim(resultSet.getString("Userid")); 
							riderWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
							riderWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
							riderWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));

							riderWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));
							riderWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
							riderWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
							riderWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
							riderWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
							riderWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
							riderWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
							riderWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
							riderWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
							riderWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
							riderWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
							riderWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
							riderWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
							riderWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
							riderWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));
							
							riderWrapper.status=Utility.trim(resultSet.getString("Status"));
							riderWrapper.serviceCode=Utility.trim(resultSet.getString("Service"));
							riderWrapper.locale=Utility.trim(resultSet.getString("Locale"));
							
							riderWrapper.recordFound=true;

							System.out.println("fetchRider  successful");
			
							System.out.println("inside riderWrapper.vehicleNo" + riderWrapper.vehicleNo);

							//get image files names
							ImageHelper imageHelper = new ImageHelper();
							ImageWrapper imageWrapper = new ImageWrapper();
							imageWrapper.riderRefNo = riderWrapper.riderRefNo;
							imageWrapper.riderID = riderWrapper.riderID;
							imageWrapper.imageID = riderWrapper.imageID;
							DataArrayWrapper dataArrayWrapper = (DataArrayWrapper)imageHelper.fetchImage(imageWrapper);
							riderWrapper.imageWrappers = dataArrayWrapper.imageWrapper;
							
							System.out.println("after image riderWrapper.vehicleNo" + riderWrapper.vehicleNo);

			
						}
						
						//dataArrayWrapper.riderWrapper = new RiderWrapper[1];
						//dataArrayWrapper.riderWrapper[0]=riderWrapper;
						//dataArrayWrapper.recordFound=true;

					   if (resultSet!=null)  resultSet.close();
					   pstmt.close();

				} catch (SQLException se) {
				
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				
				} catch (NamingException ne) {
					
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
				
					 ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
			
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}

				return riderWrapper;
		}
		//-----------------End fetch---------------------
		
		//-----------------Start fetchTravellersearch---------------------
		
		public AbstractWrapper fetchRiderSearch(UsersWrapper usersProfileWrapper,DriverWrapper driverWrapper)throws Exception {

				Connection con = null;
				ResultSet resultSet = null;
				String sql=null;
				DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	
				Vector<Object> vector = new Vector<Object>();
				
				RiderWrapper riderWrapper=null;
				
				try {
						
					    //PopoverHelper popoverHelper=new PopoverHelper();
						con = getConnection();
						

//						if(!Utility.isEmpty(riderWrapper.riderID))
//						{
//							sql= " WHERE RiderID =?";
//							
//							System.out.println("riderWrapper RiderID " + sql);
//							
//						}
//						
//						else if(!Utility.isEmpty(riderWrapper.firstName))
//						{
//							
//							
//							sql=" WHERE UPPER(FirstName) LIKE ?";
//							
//							
//							System.out.println(" first Name " +riderWrapper.firstName);
//							
//							
//						}
//						else if(!Utility.isEmpty(riderWrapper.lastName))
//						{
//							
//							
//							sql=" WHERE  UPPER(LastName) LIKE ?";
//							
//							
//							System.out.println(" lastName  " +riderWrapper.lastName);
//							
//							
//						}
//						
//						else if(!Utility.isEmpty(riderWrapper.mobileNo))
//						{
//							
//								sql= " WHERE MobileNo LIKE ?";
//								
//						}
						
					
						sql="SELECT RiderRefNo, RiderID, FirstName, LastName, MobileNo, Email, "
								+ " Gender, City, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
								+ " RegisterDate, Userid, RCRegNo, VehicleNo, VehicleType, PANNo, Address1, Address2, Address3, CityID, "
								+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
								+ " PermPINCode, PermDistrictID, PermStateID, Status, Service, Locale, MakerID, MakerDateTime "
								+ " FROM Rider WHERE MakerID=?";

						PreparedStatement pstmt = con.prepareStatement(sql);
					
						pstmt.setString(1, Utility.trim(driverWrapper.driverID));

						
						
//						if(!Utility.isEmpty(riderWrapper.riderID))
//						{
//							pstmt.setString(1, riderWrapper.riderID.trim());
//							
//							
//						}
//						
//						else if(!Utility.isEmpty(riderWrapper.firstName))
//						{
//							
//								pstmt.setString(1, '%' + riderWrapper.firstName.trim().toUpperCase() + '%');
//							
//								
//						}
//						else if(!Utility.isEmpty(riderWrapper.lastName))
//						{
//							
//							
//								pstmt.setString(1, '%' + riderWrapper.lastName.trim().toUpperCase() + '%');
//								
//						}
//						
//						else if(!Utility.isEmpty(riderWrapper.mobileNo))
//						{
//							
//								pstmt.setString(1, riderWrapper.mobileNo.trim());
//						
//						}
						
						
	
						resultSet = pstmt.executeQuery();
						
						while (resultSet.next()) 
						{
							riderWrapper=new RiderWrapper();
							
							
							riderWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
							riderWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
							riderWrapper.firstName=Utility.trim(resultSet.getString("FirstName"));  
							riderWrapper.lastName=Utility.trim(resultSet.getString("LastName"));
							riderWrapper.mobileNo=Utility.trim(resultSet.getString("MobileNo"));  
							riderWrapper.email=Utility.trim(resultSet.getString("Email"));
							riderWrapper.gender=Utility.trim(resultSet.getString("Gender"));
							riderWrapper.city=Utility.trim(resultSet.getString("City"));  
							riderWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
							riderWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
							riderWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
							riderWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
							riderWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));
							
							riderWrapper.registerDate=Utility.setDate(resultSet.getString("RegisterDate"));
							riderWrapper.userid=Utility.trim(resultSet.getString("Userid")); 
							riderWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
							riderWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
							riderWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));

							riderWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));
							riderWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
							riderWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
							riderWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
							riderWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
							riderWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
							riderWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
							riderWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
							riderWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
							riderWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
							riderWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
							riderWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
							riderWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
							riderWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
							riderWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));
							
							riderWrapper.status=Utility.trim(resultSet.getString("Status"));
							riderWrapper.serviceCode=Utility.trim(resultSet.getString("Service"));
							riderWrapper.locale=Utility.trim(resultSet.getString("Locale"));


//							riderWrapper.cityValue=popoverHelper.fetchPopoverDesc(riderWrapper.city, "MST_City");
//							riderWrapper.genderValue=popoverHelper.fetchPopoverDesc(riderWrapper.gender, "MST_Gender");
							
							riderWrapper.recordFound=true;
							
							System.out.println("Rider Details Queue fetch successful");
			
							vector.addElement(riderWrapper);
			
					}
					
					if (vector.size()>0)
					{
						dataArrayWrapper.riderWrapper = new RiderWrapper[vector.size()];
						vector.copyInto(dataArrayWrapper.riderWrapper);
						dataArrayWrapper.recordFound=true;
			
						System.out.println("total trn. in fetch " + vector.size());
			
					}
					
					resultSet.close();
					pstmt.close();
	
				} catch (SQLException se) {
				
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				
				} catch (NamingException ne) {
					
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
				
					 ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
			
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}
	
				return dataArrayWrapper;
		}
		//-----------------End fetchtravellerSearch---------------------
		
		//-----------------generateTravellerRefNo-------------------------------
		public String generateRiderRefNo()throws Exception {
			
			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql=null;
			
			SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");
		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
			
			int riderRefNo=0;
			String finalRiderRefNo=null;
			String riderCode=null;
			System.out.println("generateRiderRefNo ");
			
			try {
				
				
				con = getConnection();
				
				
				sql="SELECT RiderRefNo, RiderCode from Parameter";
				
				PreparedStatement pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) 
				{
					
					riderRefNo=resultSet.getInt("RiderRefNo");
					System.out.println("RiderRefNo " + riderRefNo);
					riderCode=resultSet.getString("RiderCode");
					
				}
				
				resultSet.close();
				pstmt.close();
				
				if(riderRefNo==0)
				{
					riderRefNo=1;
					
				}
				else
				{
					
					riderRefNo=riderRefNo+1;
				}
					
				sql="UPDATE Parameter set RiderRefNo=?";
				
				
				System.out.println("sql " + sql);
				
				pstmt = con.prepareStatement(sql);
		
				pstmt.setInt(1,riderRefNo);
				
				pstmt.executeUpdate();
				pstmt.close();
				
				int paddingSize=5;

				finalRiderRefNo=riderCode+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" + paddingSize +"d",riderRefNo);
				
				System.out.println("Successfully generated RiderRefNo " + finalRiderRefNo);
				
			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			}
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				try
				{
					releaseConnection(resultSet, con);
				} 
				catch (SQLException se)
				{
					se.printStackTrace();
					throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
				}
			}

			return finalRiderRefNo;
		}
		
		//-----------------End generateTravellerRefNo---------------------------
		
		
		//-----------------generateTravellerID-------------------------------

		
		public String generateRiderID(String refNo)throws Exception {
			
			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql=null;
			
//					SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy");
		
//					DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//					DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//					symbols.setGroupingSeparator(',');
//					formatter.applyPattern("###,###,###,##0.00");
//					formatter.setDecimalFormatSymbols(symbols);
			
			int riderRefNo=0;
			String finalRiderID=null;
			String riderCode=null;
			
			System.out.println("generateRiderID ");
			
			try {
				con = getConnection();
				
				
				sql="SELECT RiderRefNo,RiderCode from Parameter";
				
				PreparedStatement pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					
					riderRefNo=resultSet.getInt("RiderRefNo");
					System.out.println("RiderRefNo " + riderRefNo);
					riderCode=resultSet.getString("RiderCode");
					
				}
				
				resultSet.close();
				pstmt.close();
				
//						if(riderRefNo==0)
//						{
//							riderRefNo=1;
//							
//						}
//						else
//						{
//							
//							riderRefNo=riderRefNo+1;
//						}
//							
//						sql="UPDATE Parameter set RiderRefNo=?";
//						
//						
//						System.out.println("sql " + sql);
//						
//						pstmt = con.prepareStatement(sql);
//				
//						pstmt.setInt(1,riderRefNo);
//						
//						pstmt.executeUpdate();
//						pstmt.close();
				
				//int paddingSize=5;

				//int paddingSize=6-String.valueOf(studentID).length();
				

				
				
				//finalRiderID=riderCode+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" +paddingSize +"d",riderRefNo);
				finalRiderID=riderCode+refNo.substring(7);
				
				
				
				System.out.println("Successfully generated finalRiderID " + finalRiderID);
				
			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			}
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				try
				{
					releaseConnection(resultSet, con);
				} 
				catch (SQLException se)
				{
					se.printStackTrace();
					throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
				}
			}

			return finalRiderID;
		}
		
				
		//-----------------End generateTravellerID---------------------------
				
				
		//-----------------Start verifyRider---------------------
				
		public AbstractWrapper verifyRider(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)throws Exception {

						Connection con = null;
						ResultSet resultSet = null;
						
						DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
						
						
						String sql=null;
						//boolean filterFound=false;
						//int n=0;
						
						try {
							
								con = getConnection();
								
								sql="SELECT RiderRefNo, RiderID, FirstName, LastName, MobileNo, Email, "
										+ " Gender, City, DrivingLicence, AadhaarNo, PassportNo, EmiratesID, VisaNo, "
										+ " RegisterDate, Userid, RCRegNo, VehicleNo, VehicleType, PANNo, Address1, Address2, Address3, CityID, "
										+ " PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, "
										+ " PermPINCode, PermDistrictID, PermStateID, Status, Service, Locale "
										+ " FROM Rider WHERE (MobileNo=? OR Email=?) AND Status=?"; 
								
								PreparedStatement pstmt = con.prepareStatement(sql);
								pstmt.setString(1, Utility.trim(riderWrapper.mobileNo));
								pstmt.setString(2, Utility.trim(riderWrapper.email));
								pstmt.setString(3,Constants.ACTIVE_STATUS);
								
								resultSet = pstmt.executeQuery();
								
								if(resultSet.next()) 
								{
									riderWrapper = new RiderWrapper();
									
									riderWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
									riderWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
									riderWrapper.firstName=Utility.trim(resultSet.getString("FirstName"));  
									riderWrapper.lastName=Utility.trim(resultSet.getString("LastName"));
									riderWrapper.mobileNo=Utility.trim(resultSet.getString("MobileNo"));  
									riderWrapper.email=Utility.trim(resultSet.getString("Email"));
									riderWrapper.gender=Utility.trim(resultSet.getString("Gender"));
									riderWrapper.city=Utility.trim(resultSet.getString("City"));  
									riderWrapper.drivingLicence = Utility.trim(resultSet.getString("DrivingLicence"));
									riderWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
									riderWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
									riderWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
									riderWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));
									
									riderWrapper.registerDate=Utility.setDate(resultSet.getString("RegisterDate"));
									riderWrapper.userid=Utility.trim(resultSet.getString("Userid")); 
									riderWrapper.rcRegNo = Utility.trim(resultSet.getString("RCRegNo"));
									riderWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
									riderWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));

									riderWrapper.panNo = Utility.trim(resultSet.getString("PANNo"));
									riderWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
									riderWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
									riderWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
									riderWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
									riderWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
									riderWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
									riderWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
									riderWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
									riderWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
									riderWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
									riderWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
									riderWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
									riderWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
									riderWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));
									
									riderWrapper.status=Utility.trim(resultSet.getString("Status"));
									riderWrapper.serviceCode=Utility.trim(resultSet.getString("Service"));
									riderWrapper.locale=Utility.trim(resultSet.getString("Locale"));
									
									FCMNotification fcmNotification = new FCMNotification();
									fcmNotification.firebaseInit();

									riderWrapper.customToken=FirebaseAuth.getInstance().createCustomTokenAsync(riderWrapper.riderID).get();

									System.out.println("riderWrapper.customToken " + riderWrapper.customToken);

							
									riderWrapper.recordFound=true;

									dataArrayWrapper.riderWrapper = new RiderWrapper[1];
									dataArrayWrapper.riderWrapper[0]=riderWrapper;
									dataArrayWrapper.recordFound=true;
									
									System.out.println("Mobile/Email Found ");
					
									
						
								}
								else
								{
									riderWrapper.emobileFound=false;
									riderWrapper.recordFound=false;

									dataArrayWrapper.riderWrapper = new RiderWrapper[1];
									dataArrayWrapper.riderWrapper[0]=riderWrapper;
									dataArrayWrapper.recordFound=true;
									
									System.out.println("Mobile/Email Not Found ");
						
								}
								
								if (resultSet!=null)  resultSet.close();
								pstmt.close();

						} catch (SQLException se) {
						
							se.printStackTrace();
							throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
						
						} catch (NamingException ne) {
							
							ne.printStackTrace();
							throw new NamingException(ne.getMessage());
						}
						 catch (Exception ex) {
						
							 ex.printStackTrace();
							throw new Exception(ex.getMessage());
						}
					
						finally
						{
							try
							{
								releaseConnection(resultSet, con);
							} 
							catch (SQLException se)
							{
								se.printStackTrace();
								throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
							}
						}

						return dataArrayWrapper;
				}
			//-----------------End verifyRider---------------------

//		public AbstractWrapper fetchDriverLocation(UsersWrapper usersProfileWrapper,
//				RiderWrapper riderWrapper) throws Exception {
//
//			Connection con = null;
//			ResultSet resultSet = null;
//			//Vector<DriverWrapper> vector = new Vector<DriverWrapper>();
//			
//			ArrayList<DriverWrapper> driverDistance= new ArrayList<DriverWrapper>();
//
//			
//			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//			String sql = null;
//			// String countryCode=null;
//
//			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
//
////			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
////			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
////			symbols.setGroupingSeparator(',');
////			formatter.applyPattern("###,###,###,##0.00");
////			formatter.setDecimalFormatSymbols(symbols);
//
//			PreparedStatement pstmt = null;
//			DriverWrapper driverWrapper=null;
//			double driverRadius=0;
//			FavoriteWrapper favoriteWrapper=null;
//
//			try {
//				
//				
//				FavoriteHelper favoriteHelper = new FavoriteHelper();
//
//				con = getConnection();
//				
//
//				//---------Get parameter------
//				sql="SELECT DriverRadius from Parameter";
//				
//				pstmt = con.prepareStatement(sql);
//			
//				resultSet = pstmt.executeQuery();
//				if (resultSet.next()) 
//				{
//					
//					driverRadius=resultSet.getDouble("DriverRadius");
//					
//				}
//				
//				if (resultSet != null)
//					resultSet.close();
//				pstmt.close();
//				//----------end get parameter
//
//
//				sql = "SELECT DriverRefNo, DriverID, FirstName, LastName, MobileNo, "
//						+ " VehicleNo, Status, VehicleType, CurrentLat, CurrentLng, CurrentLocation, VacantStatus, Service, Rating, Locale "
//						+ " FROM Driver WHERE Status='ACTIVE' and VacantStatus='VACANT' ";
//				
//				if(riderWrapper.serviceCode.equals(Constants.TRANSPORT_BUSINESS))
//				{
//					sql = sql + " and VehicleType = ?";
//				}
//
//				pstmt = con.prepareStatement(sql);
//
//				
//				if(riderWrapper.serviceCode.equals(Constants.TRANSPORT_BUSINESS))
//				{
//					pstmt.setString(1, riderWrapper.vehicleType);
//				}
//
//				resultSet = pstmt.executeQuery();
//
//				while (resultSet.next()) {
//
//					driverWrapper = new DriverWrapper();
//
//					driverWrapper.driverRefNo = Utility.trim(resultSet.getString("DriverRefNo"));
//					driverWrapper.driverID = Utility.trim(resultSet.getString("DriverID"));
//					driverWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
//					driverWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
//					driverWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
//					driverWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
//					driverWrapper.status = Utility.trim(resultSet.getString("Status"));
//					driverWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));
//
//					driverWrapper.currentLat = resultSet.getBigDecimal("CurrentLat");
//					driverWrapper.currentLng = resultSet.getBigDecimal("CurrentLng");
//					driverWrapper.currentLocation = Utility.trim(resultSet.getString("CurrentLocation"));
//					driverWrapper.vacantStatus = Utility.trim(resultSet.getString("VacantStatus"));
//					driverWrapper.service = Utility.trim(resultSet.getString("Service"));
//					
//
//					//------Favorite & Rating------
//					favoriteWrapper = new FavoriteWrapper();
//					favoriteWrapper.driverRefNo = driverWrapper.driverRefNo;
//					favoriteWrapper.driverID = driverWrapper.driverID;
//					favoriteWrapper.riderRefNo = riderWrapper.riderRefNo;
//					favoriteWrapper.riderID = riderWrapper.riderID;
//					favoriteWrapper = (FavoriteWrapper) favoriteHelper.fetchFavorite(favoriteWrapper);
//					
//					if(favoriteWrapper.recordFound==true)
//					{
//						driverWrapper.favorite = (favoriteWrapper.favorite==null?"N":favoriteWrapper.favorite); //updated favorite
//						driverWrapper.yourRating = favoriteWrapper.rating; //individual rating for that rider
//					}
//					else
//					{
//						driverWrapper.favorite="N";//favorite not found
//						driverWrapper.yourRating = 0.0f; //individual rating for that rider
//
//					}
//					//------end of favorite & rating
//
//					//this is avg Rating
//					driverWrapper.avgRating = round(resultSet.getFloat("Rating"),1);
//					driverWrapper.locale = Utility.trim(resultSet.getString("Locale"));
//
//					
//					
//					System.out.println("driverWrapper.avgRating  " + driverWrapper.avgRating);
//
//					
//					//double roundOff = Math.round(a * 100.0) / 100.0;
//					//to convert meters to kms and to round of two decimals
//					driverWrapper.distance= Math.round((Utility.distance(riderWrapper.currentLat.doubleValue(), driverWrapper.currentLat.doubleValue(), riderWrapper.currentLng.doubleValue(), driverWrapper.currentLng.doubleValue(),0,0)/1000) *100.0) /100.0;
//					System.out.println("distance " + driverWrapper.driverID + "  " +driverWrapper.distance);
//					driverWrapper.duration = Math.round(driverWrapper.distance/20.0);
//					
//					driverWrapper.recordFound = true;
//
//					//add only within range of driver radius
//					if(  driverWrapper.distance <= driverRadius)
//					{
//						
//						driverDistance.add(driverWrapper);
//					}
//					
//					System.out.println("fetchDriverLocation  successful");
//
//				}
//
//				if (resultSet != null)
//					resultSet.close();
//				pstmt.close();
//
//				if (driverDistance.size() > 0) {
//
//					Collections.sort(driverDistance, new CustomComparator());
//					
//					dataArrayWrapper.driverWrapper = new DriverWrapper[driverDistance.size()];
//					driverDistance.toArray(dataArrayWrapper.driverWrapper);
//					dataArrayWrapper.recordFound = true;;
//				}
//				else
//				{
//					dataArrayWrapper.driverWrapper = new DriverWrapper[1];
//					driverWrapper = new DriverWrapper();
//					driverWrapper.recordFound=false;
//					dataArrayWrapper.driverWrapper[0]=driverWrapper;
//					dataArrayWrapper.recordFound = true;;
//					
//				}
//
//			} catch (SQLException se) {
//				se.printStackTrace();
//				throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
//			} catch (NamingException ne) {
//				ne.printStackTrace();
//				throw new NamingException(ne.getMessage());
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				throw new Exception(ex.getMessage());
//			} finally {
//				try {
//					releaseConnection(resultSet, con);
//				} catch (SQLException se) {
//					se.printStackTrace();
//					throw new Exception(se.getSQLState() + " ; " + se.getMessage());
//				}
//			}
//
//			return dataArrayWrapper;
//		}		
//		
		// -----------------Start updateRider Location---------------------
		public AbstractWrapper updateRiderLocation(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

			PreparedStatement pstmt = null;

			try {
				con = getConnection();

				System.out.println("Update RiderRefNo  is " + riderWrapper.riderRefNo);

				updateLocation(usersProfileWrapper, riderWrapper);
								
				pstmt = con.prepareStatement("UPDATE Rider SET CurrentLat=?, CurrentLng=?, CurrentLocation=?   "
						+ " WHERE RiderRefNo=? AND RiderID=?");

				pstmt.setBigDecimal(1, riderWrapper.currentLat);
				pstmt.setBigDecimal(2, riderWrapper.currentLng);
				pstmt.setString(3, Utility.trim(riderWrapper.currentLocation));
				pstmt.setString(4, Utility.trim(riderWrapper.riderRefNo));
				pstmt.setString(5, Utility.trim(riderWrapper.riderID));

				pstmt.executeUpdate();
				pstmt.close();

				riderWrapper.recordFound = true;
				dataArrayWrapper.riderWrapper = new RiderWrapper[1];
				dataArrayWrapper.riderWrapper[0] = riderWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Rider location details Updated");

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
				

		public void updateLocation(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

			PreparedStatement pstmt = null;

			try {
				con = getConnection();

				System.out.println("Insert RiderRefNo  is " + riderWrapper.riderRefNo);

				
				pstmt = con.prepareStatement("INSERT INTO Location(RiderRefNo, RiderID, CurrentLat, CurrentLng, "
						+ " CurrentLocation, VacantStatus, MakerID, MakerDateTime) values (?,?,?,?,?,?,?,?)");

				pstmt.setString(1,Utility.trim(riderWrapper.riderRefNo));
				pstmt.setString(2,Utility.trim(riderWrapper.riderID));
				pstmt.setBigDecimal(3, riderWrapper.currentLat);
				pstmt.setBigDecimal(4, riderWrapper.currentLng);
				pstmt.setString(5, Utility.trim(riderWrapper.currentLocation));
				pstmt.setString(6, Utility.trim(riderWrapper.vacantStatus));
				pstmt.setString(7,Utility.trim(riderWrapper.riderID)); 
				pstmt.setTimestamp(8,Utility.getCurrentTime()); //  date time

				pstmt.executeUpdate();
				pstmt.close();


				System.out.println("Successfully  location details Inserted");

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

			
		}
		// -----------------End updateDriverVacantStatus--------------------
		
		
	
		//-----------------Start updateLogin---------------------
		public AbstractWrapper updateLoginRider(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapperParam)throws Exception {
			
				Connection con = null;
				ResultSet resultSet = null;
		
				PreparedStatement pstmt=null;
				
				//String locale=null;
				
				RiderWrapper riderWrapper=null;
				
				try {
						con = getConnection();
						
						riderWrapper = (RiderWrapper)fetchRider(riderWrapperParam); //take all data from database and update only from client
						
						//----don't overwrite exisitng locale while logging...if not available then default----
						if(!Utility.isEmpty(riderWrapper.locale))
						{
							riderWrapperParam.locale = riderWrapper.locale;
						}
						
						
//						pstmt = con.prepareStatement("SELECT Locale, status FROM Rider WHERE RiderRefNo=? AND RiderID=?");
//						pstmt.setString(1,Utility.trim(riderWrapper.riderRefNo));
//						pstmt.setString(2,Utility.trim(riderWrapper.riderID));
//						resultSet = pstmt.executeQuery();
//						if(resultSet.next()) 
//						{
//							locale=Utility.trim(resultSet.getString("Locale"));
//							riderWrapper.status=Utility.trim(resultSet.getString("status"));
//							
//							if(!Utility.isEmpty(locale))
//							{
//								riderWrapper.locale = locale;
//							}
//						}
//						resultSet.close();
//						pstmt.close();
						//-------end of local retrive
						
						
						 pstmt = con.prepareStatement("UPDATE Rider SET FCMToken=?,  " 
						 		+ "  LastloginDateTime=?, Locale=? WHERE RiderRefNo=? AND RiderID=?");
					
					    pstmt.setString(1,Utility.trim(riderWrapperParam.fcmToken));  
						pstmt.setTimestamp(2,Utility.getCurrentTime()); //  date time
						pstmt.setString(3, Utility.trim(riderWrapperParam.locale));
						pstmt.setString(4,Utility.trim(riderWrapperParam.riderRefNo));
						pstmt.setString(5,Utility.trim(riderWrapperParam.riderID));
					
						pstmt.executeUpdate();
						pstmt.close();

						riderWrapper.fcmToken= riderWrapperParam.fcmToken; //take latest fcmToken
						riderWrapper.recordFound=true;

						
						
						
						//Messaging.sendOverrideMessage(riderWrapper.fcmToken);
						
						System.out.println("Successfully Rider details Updated");
						
						
				} catch (SQLException se) {
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				} catch (NamingException ne) {
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}
		
				return riderWrapper;
		}
		//-----------------End updateTraveller--------------------
		
		// -----------------Start updateDriverVacantStatus---------------------
		public AbstractWrapper updateVacantStatus(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

			PreparedStatement pstmt = null;

			try {
				con = getConnection();

				System.out.println("Update RiderRefNo  is " + riderWrapper.riderRefNo);

				updateLocation(usersProfileWrapper, riderWrapper);

				pstmt = con.prepareStatement("UPDATE Rider SET CurrentLat=?, CurrentLng=?, CurrentLocation=?, VacantStatus=?  "
						+ " WHERE RiderRefNo=? AND RiderID=?");

				pstmt.setBigDecimal(1, riderWrapper.currentLat);
				pstmt.setBigDecimal(2, riderWrapper.currentLng);
				pstmt.setString(3, Utility.trim(riderWrapper.currentLocation));
				pstmt.setString(4, Utility.trim(riderWrapper.vacantStatus));
				pstmt.setString(5, Utility.trim(riderWrapper.riderRefNo));
				pstmt.setString(6, Utility.trim(riderWrapper.riderID));

				pstmt.executeUpdate();
				pstmt.close();

				riderWrapper.recordFound = true;
				dataArrayWrapper.riderWrapper = new RiderWrapper[1];
				dataArrayWrapper.riderWrapper[0] = riderWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Rider Vacant Status details Updated");

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

		public static float round(float number, int decimalPlace) {
			BigDecimal bd = new BigDecimal(number);
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		}


	
		// -----------------End insertDriver---------------------
		
		public class CustomComparator implements Comparator<DriverWrapper> {
		    @Override
		    public int compare(DriverWrapper driver1, DriverWrapper driver2) {
		        return driver1.distance > driver2.distance?1: driver1.distance<driver2.distance ?-1:0;
		    }
		}
		
		// -----------------Start updateDriver---------------------
		public AbstractWrapper updateDriver(UsersWrapper usersProfileWrapper, RiderWrapper riderWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);

			PreparedStatement pstmt = null;

			try {
				con = getConnection();

				System.out.println("Update DriverRefNo  is " + riderWrapper.riderRefNo);

//				pstmt = con.prepareStatement("UPDATE Driver SET FirstName=?, LastName=?, MobileNo=?, "
//						+ " Email=?, Gender=?, DrivingLicence=?, AadhaarNo=?, PassportNo=?, "
//						+ " EmiratesID=?, VisaNo=?, RCRegNo=?, VehicleNo=?, VehicleType=?, PANNo=?, Address1=?, Address2=?, "
//						+ " Address3=?, CityID=?, PINCode=?, DistrictID=?, StateID=?, PermAddress1=?, "
//						+ " PermAddress2=?, PermAddress3=?, PermCityID=?, PermPINCode=?, PermDistrictID=?, "
//						+ " PermStateID=? WHERE DriverRefNo=? AND DriverID=?");
				
				pstmt = con.prepareStatement("UPDATE Rider SET  "
						+ " VehicleNo=?, VehicleType=?, ModifierID=?, ModifierDateTime=? "
						+ " WHERE RiderRefNo=? AND RiderID=?");

				
				//pstmt.setString(1, Utility.trim(driverWrapper.firstName));
//				pstmt.setString(2, Utility.trim(driverWrapper.lastName));
//				pstmt.setString(3, Utility.trim(driverWrapper.mobileNo));
//				pstmt.setString(4, Utility.trim(driverWrapper.email));
//				pstmt.setString(5, Utility.trim(driverWrapper.gender));
//				pstmt.setString(6, Utility.trim(driverWrapper.drivingLicence));
//				pstmt.setString(7, Utility.trim(driverWrapper.aadhaarNo));
//				pstmt.setString(8, Utility.trim(driverWrapper.passportNo));
//				pstmt.setString(9, Utility.trim(driverWrapper.emiratesID));
//				pstmt.setString(10, Utility.trim(driverWrapper.visaNo));
	//
//				pstmt.setString(11, Utility.trim(driverWrapper.rcRegNo));
				pstmt.setString(1, Utility.trim(riderWrapper.vehicleNo));
				pstmt.setString(2, Utility.trim(riderWrapper.vehicleType));
				//pstmt.setString(4, Utility.trim(driverWrapper.locale));
				
//				pstmt.setString(13, Utility.trim(driverWrapper.panNo));
	//
//				pstmt.setString(14, Utility.trim(driverWrapper.address1));
//				pstmt.setString(15, Utility.trim(driverWrapper.address2));
//				pstmt.setString(16, Utility.trim(driverWrapper.address3));
//				pstmt.setString(17, Utility.trim(driverWrapper.cityID));
//				pstmt.setString(18, Utility.trim(driverWrapper.pinCode));
	//
//				pstmt.setString(19, Utility.trim(driverWrapper.districtID));
//				pstmt.setString(20, Utility.trim(driverWrapper.stateID));
//				pstmt.setString(21, Utility.trim(driverWrapper.permAddress1));
//				pstmt.setString(22, Utility.trim(driverWrapper.permAddress2));
//				pstmt.setString(23, Utility.trim(driverWrapper.permAddress3));
//				pstmt.setString(24, Utility.trim(driverWrapper.permCityID));
//				pstmt.setString(25, Utility.trim(driverWrapper.permPINCode));
//				pstmt.setString(26, Utility.trim(driverWrapper.permDistrictID));
//				pstmt.setString(27, Utility.trim(driverWrapper.permStateID));

				pstmt.setString(3,Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(4,Utility.getCurrentTime()); //  date time

				pstmt.setString(5, Utility.trim(riderWrapper.riderRefNo));
				pstmt.setString(6, Utility.trim(riderWrapper.riderID));

				pstmt.executeUpdate();
				pstmt.close();

				// ------Create Login Profile
//				UsersHelper usersHelper = new UsersHelper();
//				UsersWrapper usersWrapper = new UsersWrapper();
//				usersWrapper.userid = driverWrapper.driverID;
//				usersWrapper.riderID = driverWrapper.driverID;
//				usersWrapper.riderRefNo = driverWrapper.driverRefNo;
//				usersWrapper.email = driverWrapper.email;
//				usersWrapper.mobileNo = driverWrapper.mobileNo;
//				usersHelper.updateLoginProfile(usersProfileWrapper, usersWrapper, Constants.DRIVER_CODE);
				// ------

				riderWrapper.recordFound = true;
				dataArrayWrapper.riderWrapper = new RiderWrapper[1];
				dataArrayWrapper.riderWrapper[0] = riderWrapper;
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
}
