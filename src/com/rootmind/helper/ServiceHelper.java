package com.rootmind.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.helper.DriverHelper.CustomComparator;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.DriverWrapper;
import com.rootmind.wrapper.FavoriteWrapper;
import com.rootmind.wrapper.ImageWrapper;
import com.rootmind.wrapper.RiderWrapper;
import com.rootmind.wrapper.ServiceListWrapper;
import com.rootmind.wrapper.ServiceWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ServiceHelper extends Helper{
	
	
	//---------insert Rating
		public AbstractWrapper insertService(UsersWrapper usersProfileWrapper, ServiceWrapper[] serviceWrapperArray)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql = null;

			PreparedStatement pstmt = null;

			try {



				
				if(serviceWrapperArray!=null && serviceWrapperArray.length>0)
				{

					con = getConnection();

					
					for(int i=0;i<=serviceWrapperArray.length-1;i++)
					{
				
						ServiceWrapper servWrapper = (ServiceWrapper)verifyService(serviceWrapperArray[i]);
	
	
						if (servWrapper.recordFound == false) 
						{
	
	
							sql = " INSERT INTO Service(RiderRefNo, RiderID, ServiceCode, Status, MakerID, MakerDateTime) "
									+ " Values (?,?,?,?,?,?)";
	
							System.out.println("sql " + sql);
	
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, Utility.trim(serviceWrapperArray[i].riderRefNo));
							pstmt.setString(2, Utility.trim(serviceWrapperArray[i].riderID));
							pstmt.setString(3, Utility.trim(serviceWrapperArray[i].serviceCode));
							pstmt.setString(4, Utility.trim(serviceWrapperArray[i].status));
							pstmt.setString(5, Utility.trim(serviceWrapperArray[i].riderID));
							pstmt.setTimestamp(6, Utility.getCurrentTime()); // date time
	
							pstmt.executeUpdate();
							pstmt.close();
							
							
	
							serviceWrapperArray[i].recordFound = true;
	
							dataArrayWrapper.serviceWrapper = new ServiceWrapper[1];
							dataArrayWrapper.serviceWrapper[0] = serviceWrapperArray[i];
	
							dataArrayWrapper.recordFound = true;
	
							System.out.println("Successfully inserted into insertService1");
	
						}
						else
						{
							sql = " UPDATE Service SET Status=?, ModifierID=?, ModifierDateTime=? "
									+ " WHERE RiderRefNo=? AND RiderID=? AND ServiceCode=?";
	
							System.out.println("sql " + sql);
	
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, Utility.trim(serviceWrapperArray[i].status));
							pstmt.setString(2, Utility.trim(serviceWrapperArray[i].riderID));
							pstmt.setTimestamp(3, Utility.getCurrentTime()); // date time
							pstmt.setString(4, Utility.trim(serviceWrapperArray[i].riderRefNo));
							pstmt.setString(5, Utility.trim(serviceWrapperArray[i].riderID));
							pstmt.setString(6, Utility.trim(serviceWrapperArray[i].serviceCode));
	
							pstmt.executeUpdate();
							pstmt.close();
	
							
							serviceWrapperArray[i].recordFound = true;
	
							dataArrayWrapper.serviceWrapper = new ServiceWrapper[1];
							dataArrayWrapper.serviceWrapper[0] = serviceWrapperArray[i];
	
							dataArrayWrapper.recordFound = true;
	
							System.out.println("Successfully updated into insertService");
							
						}
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
		
		
		public AbstractWrapper fetchService(ServiceWrapper serviceWrapperParam) throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

			Vector<Object> vector = new Vector<Object>();
			String sql = null;
			PreparedStatement pstmt=null;
			
			ServiceWrapper serviceWrapper=null;
//			ServiceListWrapper serviceListWrapper=null;
//			ServiceListWrapper[] serviceListWrapperArray=null;
//			
//			ServiceWrapper[] serviceWrapperArray=null;

			try {
				con = getConnection();
				
//				sql = "SELECT Code, Name FROM ServiceList";
//
//				pstmt = con.prepareStatement(sql);
//				resultSet = pstmt.executeQuery();
//
//				while (resultSet.next()) {
//
//					serviceListWrapper = new ServiceListWrapper();
//
//					serviceListWrapper.code = Utility.trim(resultSet.getString("Code"));
//					serviceListWrapper.name = Utility.trim(resultSet.getString("Name"));
//					
//
//					serviceListWrapper.recordFound = true;
//
//					vector.add(serviceListWrapper);
//
//					System.out.println("serviceListWrapper  successful");
//
//				}
//
//				if (vector.size() > 0) {
//					
//					serviceListWrapperArray = new ServiceListWrapper[vector.size()];
//					vector.copyInto(serviceListWrapperArray);
//					System.out.println("total trn. in fetch " + vector.size());
//	
//				}
//
//				vector.clear();
//				if (resultSet != null)
//					resultSet.close();
//				pstmt.close();
//				
				

				sql = "SELECT RiderRefNo, RiderID, ServiceCode, Status "
						+ " FROM Service WHERE RiderRefNo=? AND RiderID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(serviceWrapperParam.riderRefNo));
				pstmt.setString(2, Utility.trim(serviceWrapperParam.riderID));

				resultSet = pstmt.executeQuery();

				while (resultSet.next()) {

					serviceWrapper = new ServiceWrapper();

					serviceWrapper.riderRefNo = Utility.trim(resultSet.getString("RiderRefNo"));
					serviceWrapper.riderID = Utility.trim(resultSet.getString("RiderID"));
					serviceWrapper.serviceCode = Utility.trim(resultSet.getString("ServiceCode"));
					serviceWrapper.status = Utility.trim(resultSet.getString("Status"));
					

					serviceWrapper.recordFound = true;

					vector.add(serviceWrapper);

					System.out.println("fetchService  successful");

				}

//				if(vector.size()>0)
//				{
//					vector.copyInto(serviceWrapperArray);
//					vector.clear();
//				}
				
				if (resultSet != null)
					resultSet.close();
				pstmt.close();
				

//				boolean contains=false;
//				for(int i=0; i<serviceListWrapperArray.length;i++)
//				{
//
//					contains=false;
//					for(int j=0; j<(serviceWrapperArray!=null ? serviceWrapperArray.length :0);j++)
//					{
//						if(serviceWrapperArray[j].serviceCode.equals(serviceListWrapperArray[i].code))
//						{
//
//							contains=true;
//						}
//						
//					}
//					if(contains==false)
//					{
//
//						serviceWrapper = new ServiceWrapper();
//
//						serviceWrapper.riderRefNo = serviceWrapperParam.riderRefNo;
//						serviceWrapper.riderID =serviceWrapperParam.riderID;
//						serviceWrapper.serviceCode = serviceListWrapperArray[i].code;
//						serviceWrapper.status = Constants.INACTIVE;
//
//						serviceWrapper.recordFound = true;
//
//						vector.add(serviceWrapper);
//
//					}
//					
//				}
				
				if (vector.size() > 0) {
					
					dataArrayWrapper.serviceWrapper = new ServiceWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.serviceWrapper);
					dataArrayWrapper.recordFound = true;
	
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				else
				{
					
					dataArrayWrapper.serviceWrapper = new ServiceWrapper[1];
					serviceWrapperParam.recordFound=false;
					dataArrayWrapper.serviceWrapper[0]=serviceWrapperParam;
					dataArrayWrapper.recordFound = true;
					
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
		
		public AbstractWrapper verifyService(ServiceWrapper serviceWrapper) throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

			//ArrayList<Object> vector = new ArrayList<Object>();
			String sql = null;
			PreparedStatement pstmt=null;
			

			try {
				con = getConnection();
				
				
				

				sql = "SELECT RiderRefNo, RiderID, ServiceCode, Status "
						+ " FROM Service WHERE RiderRefNo=? AND RiderID=? AND ServiceCode=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(serviceWrapper.riderRefNo));
				pstmt.setString(2, Utility.trim(serviceWrapper.riderID));
				pstmt.setString(3, Utility.trim(serviceWrapper.serviceCode));

				resultSet = pstmt.executeQuery();

				if (resultSet.next()) {

					serviceWrapper = new ServiceWrapper();

					serviceWrapper.riderRefNo = Utility.trim(resultSet.getString("RiderRefNo"));
					serviceWrapper.riderID = Utility.trim(resultSet.getString("RiderID"));
					serviceWrapper.serviceCode = Utility.trim(resultSet.getString("ServiceCode"));
					serviceWrapper.status = Utility.trim(resultSet.getString("Status"));
					

					serviceWrapper.recordFound = true;


					System.out.println("verifyService  successful");

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

			return serviceWrapper;
		}


		public AbstractWrapper fetchServiceLocation(UsersWrapper usersProfileWrapper, ServiceWrapper serviceWrapper) throws Exception {

			Connection con = null;
			ResultSet resultSet = null;
			//Vector<DriverWrapper> vector = new Vector<DriverWrapper>();
			
			ArrayList<RiderWrapper> riderDistance= new ArrayList<RiderWrapper>();

			
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
			RiderWrapper riderWrapper=null;
			double driverRadius=0;
			FavoriteWrapper favoriteWrapper=null;
			DataArrayWrapper imageDataArrayWrapper=null;
			ImageWrapper imageWrapper=null;

			try {
				
				
				FavoriteHelper favoriteHelper = new FavoriteHelper();
				ImageHelper imageHelper = new ImageHelper();

				
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


				sql = "SELECT a.RiderRefNo as RiderRefNo, a.RiderID as RiderID, FirstName, LastName, MobileNo, "
						+ " a.Status as Status, CurrentLat, CurrentLng, CurrentLocation, VacantStatus, Service, Rating, Locale, VehicleNo, VehicleType "
						+ " FROM Rider a LEFT JOIN Service b ON a.RiderRefNo=b.RiderRefNo "
						+ " WHERE a.Status='ACTIVE' and a.VacantStatus='VACANT' and b.Status='ACTIVE' and b.ServiceCode=? ";
					
				
				
				if(serviceWrapper.serviceCode.equals(Constants.SERVICE_DRIVER) && serviceWrapper.vehicleType!=null)
				{
					sql = sql + " and VehicleType = ?";
				}

				pstmt = con.prepareStatement(sql);


				pstmt.setString(1, serviceWrapper.serviceCode);

				if(serviceWrapper.serviceCode.equals(Constants.SERVICE_DRIVER) && serviceWrapper.vehicleType!=null)
				{
					pstmt.setString(2, serviceWrapper.vehicleType);
				}


				resultSet = pstmt.executeQuery();

				while (resultSet.next()) {

					riderWrapper = new RiderWrapper();

					riderWrapper.riderRefNo = Utility.trim(resultSet.getString("RiderRefNo"));
					riderWrapper.riderID = Utility.trim(resultSet.getString("RiderID"));
					riderWrapper.firstName = Utility.trim(resultSet.getString("FirstName"));
					riderWrapper.lastName = Utility.trim(resultSet.getString("LastName"));
					riderWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
					riderWrapper.status = Utility.trim(resultSet.getString("Status"));

					riderWrapper.currentLat = resultSet.getBigDecimal("CurrentLat");
					riderWrapper.currentLng = resultSet.getBigDecimal("CurrentLng");
					riderWrapper.currentLocation = Utility.trim(resultSet.getString("CurrentLocation"));
					riderWrapper.vacantStatus = Utility.trim(resultSet.getString("VacantStatus"));
					riderWrapper.serviceCode = serviceWrapper.serviceCode;//Utility.trim(resultSet.getString("Service"));
					

					//------Favorite & Rating------
					favoriteWrapper = new FavoriteWrapper();
					favoriteWrapper.favoriteRefNo = riderWrapper.riderRefNo; //vote receiver
					favoriteWrapper.favoriteID = riderWrapper.riderID; //vote receiver
					favoriteWrapper.riderRefNo = serviceWrapper.riderRefNo; //voted by
					favoriteWrapper.riderID = serviceWrapper.riderID; //voted by
					favoriteWrapper = (FavoriteWrapper) favoriteHelper.fetchFavorite(favoriteWrapper);
					
					if(favoriteWrapper.recordFound==true)
					{
						riderWrapper.favorite = (favoriteWrapper.favorite==null?"N":favoriteWrapper.favorite); //updated favorite
						riderWrapper.yourRating = favoriteWrapper.rating; //individual rating for that rider
					}
					else
					{
						riderWrapper.favorite="N";//favorite not found
						riderWrapper.yourRating = 0.0f; //individual rating for that rider

					}
					//------end of favorite & rating

					//this is avg Rating
					riderWrapper.avgRating = round(resultSet.getFloat("Rating"),1);
					riderWrapper.locale = Utility.trim(resultSet.getString("Locale"));
					riderWrapper.vehicleNo = Utility.trim(resultSet.getString("VehicleNo"));
					riderWrapper.vehicleType = Utility.trim(resultSet.getString("VehicleType"));

					
					
					System.out.println("riderWrapper.avgRating  " + riderWrapper.avgRating);

					
					//double roundOff = Math.round(a * 100.0) / 100.0;
					//to convert meters to kms and to round of two decimals
					riderWrapper.distance= Math.round((Utility.distance(serviceWrapper.currentLat.doubleValue(), serviceWrapper.currentLat.doubleValue(), serviceWrapper.currentLng.doubleValue(), serviceWrapper.currentLng.doubleValue(),0,0)/1000) *100.0) /100.0;
					System.out.println("distance " + serviceWrapper.riderID + "  " +riderWrapper.distance);
					riderWrapper.duration = Math.round(riderWrapper.distance/20.0);
					
					//--------------------
					//get image files names
					imageWrapper = new ImageWrapper();
					imageWrapper.riderRefNo = riderWrapper.riderRefNo;
					imageWrapper.riderID = riderWrapper.riderID;
					imageDataArrayWrapper = (DataArrayWrapper)imageHelper.fetchImage(imageWrapper);
					riderWrapper.imageWrappers = imageDataArrayWrapper.imageWrapper;
					//---------------------
					
					
					riderWrapper.recordFound = true;
					//add only within range of driver radius and do not add self request 
					if( riderWrapper.distance <= driverRadius && !serviceWrapper.riderRefNo.equals(riderWrapper.riderRefNo))
					{
						
						riderDistance.add(riderWrapper);
					}
					
					System.out.println("fetchServiceLocation  successful");

				}

				if (resultSet != null)
					resultSet.close();
				pstmt.close();

				if (riderDistance.size() > 0) {

					Collections.sort(riderDistance, new CustomComparator());
					
					dataArrayWrapper.riderWrapper = new RiderWrapper[riderDistance.size()];
					riderDistance.toArray(dataArrayWrapper.riderWrapper);
					dataArrayWrapper.recordFound = true;;
				}
				else
				{
					dataArrayWrapper.riderWrapper = new RiderWrapper[1];
					riderWrapper = new RiderWrapper();
					riderWrapper.recordFound=false;
					dataArrayWrapper.riderWrapper[0]=riderWrapper;
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
		
		public class CustomComparator implements Comparator<RiderWrapper> {
		    @Override
		    public int compare(RiderWrapper rider1, RiderWrapper rider2) {
		        return rider1.distance > rider2.distance?1: rider1.distance<rider2.distance ?-1:0;
		    }
		}		

		public static float round(float number, int decimalPlace) {
			BigDecimal bd = new BigDecimal(number);
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		}
		
}
