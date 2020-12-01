package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.RideWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class RideHelper extends Helper{
	
	
	//-----------------Start insertRide---------------------
	
	public AbstractWrapper insertRide(UsersWrapper usersProfileWrapper, RideWrapper rideWrapper)throws Exception {
			
			Connection con = null;
			ResultSet resultSet = null;
	
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql=null;
			//String countryCode=null;
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			formatter.applyPattern("###,###,###,##0.00");
			formatter.setDecimalFormatSymbols(symbols);
			PreparedStatement pstmt=null;
			
			
			try {
					con = getConnection();

					sql=" INSERT INTO Ride(RideRefNo, RiderRefNo, RiderID, DriverRefNo, DriverID, VehicleType, "
						+ " RideStartDate, RideEndDate, RideStartPoint, RideEndPoint, PickupLat, PickupLng, DropLat, "
						+ " DropLng, RideStatus, MakerID, MakerDateTime) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					System.out.println("sql " + sql);
					
					pstmt = con.prepareStatement(sql);
					rideWrapper.rideRefNo=generateRideRefNo();
					
					pstmt.setString(1,Utility.trim(rideWrapper.rideRefNo));
					pstmt.setString(2,Utility.trim(rideWrapper.riderRefNo));
					pstmt.setString(3,Utility.trim(rideWrapper.riderID));
					pstmt.setString(4,Utility.trim(rideWrapper.driverRefNo)); 
					pstmt.setString(5,Utility.trim(rideWrapper.driverID));
					pstmt.setString(6,Utility.trim(rideWrapper.vehicleType));  
					//pstmt.setString(7,Utility.trim(rideWrapper.journeyStartDate)); 
					pstmt.setTimestamp(7,Utility.getCurrentTime()); //  journeyStartDate time
					//pstmt.setString(8,Utility.trim(rideWrapper.journeyEndDate)); 
					pstmt.setTimestamp(8,Utility.getCurrentTime()); //  journeyEndDate time
					pstmt.setString(9,Utility.trim(rideWrapper.rideStartPoint)); 
					pstmt.setString(10,Utility.trim(rideWrapper.rideEndPoint));
					
					pstmt.setString(11,Utility.trim(rideWrapper.pickupLat));
					pstmt.setString(12,Utility.trim(rideWrapper.pickupLng));
					pstmt.setString(13,Utility.trim(rideWrapper.dropLat));
					pstmt.setString(14,Utility.trim(rideWrapper.dropLng));
					pstmt.setString(15,Utility.trim(rideWrapper.rideStatus));
					pstmt.setString(16,Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(17,Utility.getCurrentTime()); 
					
					
					pstmt.executeUpdate();
					pstmt.close();
					
					rideWrapper.recordFound=true;
				
					dataArrayWrapper.rideWrapper=new RideWrapper[1];
					dataArrayWrapper.rideWrapper[0]=rideWrapper;
					
					dataArrayWrapper.recordFound=true;
					
					System.out.println("Successfully inserted into insertRide");

					
				
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
		
		//-----------------End insertRide---------------------
	
	
	//-----------------Start updateRide---------------------
	public AbstractWrapper updateRide(UsersWrapper usersProfileWrapper, RideWrapper rideWrapper)throws Exception {
		
			Connection con = null;
			ResultSet resultSet = null;
	
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			formatter.applyPattern("###,###,###,##0.00");
			formatter.setDecimalFormatSymbols(symbols);
	
			PreparedStatement pstmt=null;
			
			
			
			try {
					con = getConnection();
					

					pstmt = con.prepareStatement("SELECT RideRefNo FROM Ride WHERE RideRefNo=?");
					
					System.out.println("Update RideRefNo  is " + rideWrapper.rideRefNo);
					
					pstmt.setString(1,rideWrapper.rideRefNo); //--it may null
					
					resultSet = pstmt.executeQuery();
					
					if (!resultSet.next()) 
					{
						resultSet.close();
						pstmt.close();
						dataArrayWrapper=(DataArrayWrapper)insertRide(usersProfileWrapper,rideWrapper);
					}
					else
					{
						resultSet.close();
						pstmt.close();
						 pstmt = con.prepareStatement("UPDATE Ride SET DriverRefNo=?, DriverID=?, VehicleType =?, "
								 +" RideStartDate=?, RideEndDate=?, RideStartPoint=?, RideEndPoint=?, PickupTime=?, PickupDistance =?, "
								 +" Category=?, ApproxDistance =?, ActualDistance =?, ApproxTime =?, ActualTime =?, ApproxFare =?, "
								 +" BaseFare=?, AdditionalKMFare=?, AdditionalTimeFare=?, TotalFare=?, ServiceTax=?, CouponCode=?, "
								 +" Discount=?, TotalBill=?, FutureRideDate=?, RideStatus=? WHERE RideRefNo=?");
					
						 	pstmt.setString(1,Utility.trim(rideWrapper.driverRefNo)); 
							pstmt.setString(2,Utility.trim(rideWrapper.driverID));
							pstmt.setString(3,Utility.trim(rideWrapper.vehicleType));  
							//pstmt.setString(7,Utility.trim(rideWrapper.journeyStartDate)); 
							pstmt.setTimestamp(4,Utility.getCurrentTime()); //  rideStartDate time
							//pstmt.setString(8,Utility.trim(rideWrapper.journeyEndDate)); 
							pstmt.setTimestamp(5,Utility.getCurrentTime()); //  rideEndDate time
							pstmt.setString(6,Utility.trim(rideWrapper.rideStartPoint)); 
							pstmt.setString(7,Utility.trim(rideWrapper.rideEndPoint));
							
							pstmt.setString(8,Utility.trim(rideWrapper.pickupTime));
							pstmt.setString(9,Utility.trim(rideWrapper.pickupDistance));
							pstmt.setString(10,Utility.trim(rideWrapper.category));
							pstmt.setString(11,Utility.trim(rideWrapper.approxDistance));
							pstmt.setString(12,Utility.trim(rideWrapper.actualDistance));
							pstmt.setString(13,Utility.trim(rideWrapper.approxTime));
							pstmt.setString(14,Utility.trim(rideWrapper.actualTime));
							pstmt.setString(15,Utility.trim(rideWrapper.approxFare));
							pstmt.setString(16,Utility.trim(rideWrapper.baseFare));
							
							pstmt.setString(17,Utility.trim(rideWrapper.additionalKMFare));
							pstmt.setString(18,Utility.trim(rideWrapper.additionalTimeFare));
							pstmt.setString(19,Utility.trim(rideWrapper.totalFare));
							pstmt.setString(20,Utility.trim(rideWrapper.serviceTax));
							pstmt.setString(21,Utility.trim(rideWrapper.couponCode));
							
							pstmt.setString(22,Utility.trim(rideWrapper.discount));
							pstmt.setString(23,Utility.trim(rideWrapper.totalBill));
							pstmt.setString(24,Utility.trim(rideWrapper.futureRideDate));
							pstmt.setString(25,Utility.trim(rideWrapper.rideStatus));
						
							pstmt.setString(26,Utility.trim(rideWrapper.rideRefNo));
						
							pstmt.executeUpdate();
							pstmt.close();

							rideWrapper.recordFound=true;
							dataArrayWrapper.rideWrapper=new RideWrapper[1];
							dataArrayWrapper.rideWrapper[0]=rideWrapper;
							dataArrayWrapper.recordFound=true;
						
							System.out.println("Successfully Ride details Updated");
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
	//-----------------End updateRide--------------------
	
	//-----------------Start fetchRide---------------------
	
	public AbstractWrapper fetchRide(UsersWrapper usersProfileWrapper, RideWrapper rideWrapper)throws Exception {

			Connection con = null;
			ResultSet resultSet = null;
			
			DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
			
			Vector<Object> vector = new Vector<Object>();
			String sql=null;
			
			try {
				
					con = getConnection();
					
					sql="SELECT RideRefNo, RiderRefNo, RiderID, DriverRefNo, DriverID, VehicleType, "
						+ " RideStartDate, RideEndDate, RideStartPoint, RideEndPoint, PickupTime, PickupDistance, "
						+ " Category, ApproxDistance , ActualDistance , ApproxTime , ActualTime , ApproxFare, "
						+ " BaseFare, AdditionalKMFare, AdditionalTimeFare, TotalFare, ServiceTax, CouponCode, "
						+ " Discount, TotalBill, FutureRideDate, RideStatus FROM Ride";
					
					if(rideWrapper.rideRefNo !=null && !rideWrapper.rideRefNo.equals("")){
						
						sql = sql + " WHERE RideRefNo=?";
					}
					
					
					PreparedStatement pstmt = con.prepareStatement(sql);
					
					if(rideWrapper.rideRefNo !=null && !rideWrapper.rideRefNo.equals("")){
						
						pstmt.setString(1,Utility.trim(rideWrapper.rideRefNo));
					}
					
					resultSet = pstmt.executeQuery();
					
					while (resultSet.next()) 
					{
						
						 rideWrapper=new RideWrapper();
						
						  rideWrapper.rideRefNo =Utility.trim(resultSet.getString("RideRefNo"));
						  rideWrapper.riderRefNo =Utility.trim(resultSet.getString("RiderRefNo"));
						  rideWrapper.riderID =Utility.trim(resultSet.getString("RiderID"));
						  rideWrapper.driverRefNo =Utility.trim(resultSet.getString("DriverRefNo"));
						  rideWrapper.driverID =Utility.trim(resultSet.getString("DriverID"));
						  rideWrapper.vehicleType =Utility.trim(resultSet.getString("VehicleType"));
						  rideWrapper.rideStartDate =Utility.setDate(resultSet.getString("RideStartDate"));
						  rideWrapper.rideEndDate  =Utility.setDate(resultSet.getString("RideEndDate")); 
						  rideWrapper.rideStartPoint=Utility.trim(resultSet.getString("RideStartPoint"));
						  rideWrapper.rideEndPoint=Utility.trim(resultSet.getString("RideEndPoint"));
						   
						  rideWrapper.pickupTime =Utility.setDate(resultSet.getString("PickupTime"));
						  rideWrapper.pickupDistance =Utility.trim(resultSet.getString("PickupDistance"));  
						  rideWrapper.category =Utility.trim(resultSet.getString("Category"));
						  rideWrapper.approxDistance =Utility.trim(resultSet.getString("ApproxDistance"));
						  rideWrapper.actualDistance =Utility.trim(resultSet.getString("ActualDistance"));
						  rideWrapper.approxTime =Utility.setDate(resultSet.getString("ApproxTime"));
						  rideWrapper.actualTime =Utility.setDate(resultSet.getString("ActualTime"));
						  rideWrapper.approxFare =Utility.trim(resultSet.getString("ApproxFare"));
						  rideWrapper.baseFare=Utility.trim(resultSet.getString("BaseFare"));
						  rideWrapper.additionalKMFare=Utility.trim(resultSet.getString("AdditionalKMFare"));
						  rideWrapper.additionalTimeFare=Utility.trim(resultSet.getString("AdditionalTimeFare"));
						  rideWrapper.totalFare=Utility.trim(resultSet.getString("TotalFare"));
						  rideWrapper.serviceTax=Utility.trim(resultSet.getString("ServiceTax"));
						  rideWrapper.couponCode=Utility.trim(resultSet.getString("CouponCode"));
						  rideWrapper.discount=Utility.trim(resultSet.getString("Discount"));
						  rideWrapper.totalBill=Utility.trim(resultSet.getString("TotalBill"));
						  rideWrapper.futureRideDate=Utility.setDate(resultSet.getString("FutureRideDate"));
						  rideWrapper.rideStatus=Utility.trim(resultSet.getString("RideStatus"));
						
						rideWrapper.recordFound=true;

						
						
						System.out.println("fetchRide  successful");
		
						vector.addElement(rideWrapper);
		
				}
				
				if (vector.size()>0)
				{
					dataArrayWrapper.rideWrapper = new RideWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.rideWrapper);
					dataArrayWrapper.recordFound=true;
		
					System.out.println("total trn. in fetch " + vector.size());
		
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
	//-----------------End fetchRide---------------------
	
	
	//-----------------Start fetchRidesearch---------------------
	
			public AbstractWrapper fetchRideSearch(UsersWrapper usersProfileWrapper,RideWrapper rideWrapper)throws Exception {

					Connection con = null;
					ResultSet resultSet = null;
					String sql=null;
					DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
					
					
					Vector<Object> vector = new Vector<Object>();
					
					try {
							
						    //PopoverHelper popoverHelper=new PopoverHelper();
							con = getConnection();
							

							if(rideWrapper.riderID !=null && !rideWrapper.riderID.equals(""))
							{
								sql= " WHERE RiderID =?";
								
								System.out.println("rideWrapper RiderID " + sql);
								
							}
							
							else if(rideWrapper.driverID !=null && !rideWrapper.driverID.equals(""))
							{
								
								
								sql=" WHERE DriverID =?";
								
								
								System.out.println(" driverID " +rideWrapper.driverID);
								
								
							}
							
						
							PreparedStatement pstmt = con.prepareStatement("SELECT RideRefNo, RiderRefNo, RiderID, DriverRefNo, DriverID, VehicleType, "
									+ " RideStartDate, RideEndDate, RideStartPoint, RideEndPoint, PickupTime, PickupDistance, "
									+ " Category, ApproxDistance , ActualDistance , ApproxTime , ActualTime , ApproxFare, "
									+ " BaseFare, AdditionalKMFare, AdditionalTimeFare, TotalFare, ServiceTax, CouponCode, "
									+ " Discount, TotalBill, FutureRideDate, RideStatus FROM Ride "+sql);
						
							
							
							if(rideWrapper.riderID !=null && !rideWrapper.riderID.trim().isEmpty())
							{
								pstmt.setString(1, rideWrapper.riderID.trim());
								
								
							}
							
							else if(rideWrapper.driverID !=null && !rideWrapper.driverID.trim().isEmpty())
							{
								
									pstmt.setString(1, rideWrapper.driverID.trim());
								
									
							}

		
							resultSet = pstmt.executeQuery();
							
							while (resultSet.next()) 
							{
								rideWrapper=new RideWrapper();
								
								
								  rideWrapper.rideRefNo =Utility.trim(resultSet.getString("RideRefNo"));
								  rideWrapper.riderRefNo =Utility.trim(resultSet.getString("RiderRefNo"));
								  rideWrapper.riderID =Utility.trim(resultSet.getString("RiderID"));
								  rideWrapper.driverRefNo =Utility.trim(resultSet.getString("DriverRefNo"));
								  rideWrapper.driverID =Utility.trim(resultSet.getString("DriverID"));
								  rideWrapper.vehicleType =Utility.trim(resultSet.getString("VehicleType"));
								  rideWrapper.rideStartDate =Utility.setDate(resultSet.getString("RideStartDate"));
								  rideWrapper.rideEndDate  =Utility.setDate(resultSet.getString("RideEndDate")); 
								  rideWrapper.rideStartPoint=Utility.trim(resultSet.getString("RideStartPoint"));
								  rideWrapper.rideEndPoint=Utility.trim(resultSet.getString("RideEndPoint"));
								   
								  rideWrapper.pickupTime =Utility.setDate(resultSet.getString("PickupTime"));
								  rideWrapper.pickupDistance =Utility.trim(resultSet.getString("PickupDistance"));  
								  rideWrapper.category =Utility.trim(resultSet.getString("Category"));
								  rideWrapper.approxDistance =Utility.trim(resultSet.getString("ApproxDistance"));
								  rideWrapper.actualDistance =Utility.trim(resultSet.getString("ActualDistance"));
								  rideWrapper.approxTime =Utility.setDate(resultSet.getString("ApproxTime"));
								  rideWrapper.actualTime =Utility.setDate(resultSet.getString("ActualTime"));
								  rideWrapper.approxFare =Utility.trim(resultSet.getString("ApproxFare"));
								  rideWrapper.baseFare=Utility.trim(resultSet.getString("BaseFare"));
								  rideWrapper.additionalKMFare=Utility.trim(resultSet.getString("AdditionalKMFare"));
								  rideWrapper.additionalTimeFare=Utility.trim(resultSet.getString("AdditionalTimeFare"));
								  rideWrapper.totalFare=Utility.trim(resultSet.getString("TotalFare"));
								  rideWrapper.serviceTax=Utility.trim(resultSet.getString("ServiceTax"));
								  rideWrapper.couponCode=Utility.trim(resultSet.getString("CouponCode"));
								  rideWrapper.discount=Utility.trim(resultSet.getString("Discount"));
								  rideWrapper.totalBill=Utility.trim(resultSet.getString("TotalBill"));
								  rideWrapper.futureRideDate=Utility.setDate(resultSet.getString("FutureRideDate"));
								  rideWrapper.rideStatus=Utility.trim(resultSet.getString("RideStatus"));
								  
								  
								
								rideWrapper.recordFound=true;
								
								System.out.println("Ride Details Queue fetch successful");
				
								vector.addElement(rideWrapper);
				
						}
						
						if (vector.size()>0)
						{
							dataArrayWrapper.rideWrapper = new RideWrapper[vector.size()];
							vector.copyInto(dataArrayWrapper.rideWrapper);
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
	
	//-----------------generateRideRefNo-------------------------------
	public String generateRideRefNo()throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;

		//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql=null;
		
		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");
	
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		int rideRefNo=0;
		String finalRideRefNo=null;
		String rideCode=null;
		
		try {
			
			
			con = getConnection();
			
			
			sql="SELECT RideRefNo, RideCode from MST_Parameter";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
		
			resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) 
			{
				
				rideRefNo=resultSet.getInt("RideRefNo");
				System.out.println("RideRefNo " + rideRefNo);
				rideCode=resultSet.getString("RideCode");
				
			}
			
			resultSet.close();
			pstmt.close();
			
			if(rideRefNo==0)
			{
				rideRefNo=1;
				
			}
			else
			{
				
				rideRefNo=rideRefNo+1;
			}
				
			sql="UPDATE MST_Parameter set RideRefNo=?";
			
			
			System.out.println("sql " + sql);
			
			pstmt = con.prepareStatement(sql);
	
			pstmt.setInt(1,rideRefNo);
			
			pstmt.executeUpdate();
			pstmt.close();
			
			int paddingSize=6;

			finalRideRefNo=rideCode+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" + paddingSize +"d",rideRefNo);
			
			System.out.println("Successfully generated RideRefNo " + finalRideRefNo);
			
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

		return finalRideRefNo;
	}
	
	//-----------------End generateRideRefNo---------------------------

}
