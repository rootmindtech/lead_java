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
import com.rootmind.wrapper.FareWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class FareHelper extends Helper{
	
	
	//-----------------Start insertTraveller---------------------
	
			public AbstractWrapper insertFare(UsersWrapper usersProfileWrapper, FareWrapper fareWrapper)throws Exception {
					
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

							sql=" INSERT INTO Fare(FareRefNo, Category, BaseFare, DistanceFare, WaitTimeFare, "
									+ " RideTimeFare, CancellationFee, FareType, AdditionalKMFare, NightTimeCharge, "
									+ " AdditionalDistFare, AdditionalTimeFare, CityID) Values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
							
							System.out.println("sql " + sql);
							
							pstmt = con.prepareStatement(sql);
							fareWrapper.fareRefNo=generateFareRefNo();
							
							pstmt.setString(1,Utility.trim(fareWrapper.fareRefNo));
							pstmt.setString(2,Utility.trim(fareWrapper.category));
							pstmt.setString(3,Utility.trim(fareWrapper.baseFare));
							pstmt.setString(4,Utility.trim(fareWrapper.distanceFare)); 
							pstmt.setString(5,Utility.trim(fareWrapper.waitTimeFare));
							pstmt.setString(6,Utility.trim(fareWrapper.rideTimeFare));  
							pstmt.setString(7,Utility.trim(fareWrapper.cancellationFee)); 
							pstmt.setString(8,Utility.trim(fareWrapper.fareType));
							pstmt.setString(9,Utility.trim(fareWrapper.additionalKMFare)); 
							pstmt.setString(10,Utility.trim(fareWrapper.nightTimeCharge));
							pstmt.setString(11,Utility.trim(fareWrapper.additionalDistFare));  
							pstmt.setString(12,Utility.trim(fareWrapper.additionalTimeFare)); 
							pstmt.setString(13,Utility.trim(fareWrapper.cityID)); 
						  
							pstmt.executeUpdate();
							pstmt.close();
							
							fareWrapper.recordFound=true;
						
							dataArrayWrapper.fareWrapper=new FareWrapper[1];
							dataArrayWrapper.fareWrapper[0]=fareWrapper;
							
							dataArrayWrapper.recordFound=true;
							
							System.out.println("Successfully inserted into insertFare");
							
							
							
							
						
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
				
				//-----------------End insertDetails---------------------
			
			
			//-----------------Start updateFare---------------------
			public AbstractWrapper updateFare(UsersWrapper usersProfileWrapper, FareWrapper fareWrapper)throws Exception {
				
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
							

							pstmt = con.prepareStatement("SELECT FareRefNo FROM Fare WHERE FareRefNo=?");
							
							System.out.println("Update Traveller Details fareRefNo is " + fareWrapper.fareRefNo);
							
							pstmt.setString(1,fareWrapper.fareRefNo); //--it may null
							
							resultSet = pstmt.executeQuery();
							
							if (!resultSet.next()) 
							{
								resultSet.close();
								pstmt.close();
								dataArrayWrapper=(DataArrayWrapper)insertFare(usersProfileWrapper,fareWrapper);
							}
							else
							{
								resultSet.close();
								 pstmt.close();
								 pstmt = con.prepareStatement("UPDATE Fare SET Category=?, BaseFare=?, "
								 		+ " DistanceFare=?, WaitTimeFare=?, RideTimeFare=?, CancellationFee=?, FareType=?, "
								 		+ " AdditionalKMFare=?, NightTimeCharge=?, AdditionalDistFare=?, AdditionalTimeFare=?, CityID=?  "
								 		+ " WHERE fareRefNo=?");
							
								
									pstmt.setString(1,Utility.trim(fareWrapper.category));
									pstmt.setString(2,Utility.trim(fareWrapper.baseFare));
									pstmt.setString(3,Utility.trim(fareWrapper.distanceFare)); 
									pstmt.setString(4,Utility.trim(fareWrapper.waitTimeFare));
									pstmt.setString(5,Utility.trim(fareWrapper.rideTimeFare));  
									pstmt.setString(6,Utility.trim(fareWrapper.cancellationFee)); 
									pstmt.setString(7,Utility.trim(fareWrapper.fareType));
									pstmt.setString(8,Utility.trim(fareWrapper.additionalKMFare)); 
									pstmt.setString(9,Utility.trim(fareWrapper.nightTimeCharge));
									pstmt.setString(10,Utility.trim(fareWrapper.additionalDistFare));  
									pstmt.setString(11,Utility.trim(fareWrapper.additionalTimeFare));
									pstmt.setString(12,Utility.trim(fareWrapper.cityID));
									pstmt.setString(13,Utility.trim(fareWrapper.fareRefNo));
								
									pstmt.executeUpdate();
									pstmt.close();

									fareWrapper.recordFound=true;
									dataArrayWrapper.fareWrapper=new FareWrapper[1];
									dataArrayWrapper.fareWrapper[0]=fareWrapper;
									dataArrayWrapper.recordFound=true;
								
									System.out.println("Successfully Fare details Updated");
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
			//-----------------End update--------------------
			
			//-----------------Start fetchFare---------------------
			
			public AbstractWrapper fetchFare(UsersWrapper usersProfileWrapper, FareWrapper fareWrapper)throws Exception {

					Connection con = null;
					ResultSet resultSet = null;
					
					DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
					
					Vector<Object> vector = new Vector<Object>();
					String sql=null;
					
					try {
						
							con = getConnection();
							
							sql="SELECT FareRefNo, Category, BaseFare, DistanceFare, WaitTimeFare, "
									+ " RideTimeFare, CancellationFee, FareType, AdditionalKMFare, NightTimeCharge, "
									+ " AdditionalDistFare, AdditionalTimeFare FROM Fare WHERE 1=1";
							
							if(fareWrapper.fareRefNo !=null && !fareWrapper.fareRefNo.equals("")){
								
								sql = sql + " AND FareRefNo=?";
							}
							else if(fareWrapper.cityID !=null && !fareWrapper.cityID.equals("")){
								
								sql = sql + " AND CityID=?";
							}
							
							PreparedStatement pstmt = con.prepareStatement(sql);
							
							if(fareWrapper.fareRefNo !=null && !fareWrapper.fareRefNo.equals("")){
								
								pstmt.setString(1,Utility.trim(fareWrapper.fareRefNo));
							}
							if(fareWrapper.cityID !=null && !fareWrapper.cityID.equals("")){
								
								pstmt.setString(2,Utility.trim(fareWrapper.cityID));
							}
							
							resultSet = pstmt.executeQuery();
							
							while (resultSet.next()) 
							{
								
								fareWrapper=new FareWrapper();
								
								fareWrapper.fareRefNo=Utility.trim(resultSet.getString("fareRefNo"));
								fareWrapper.category=Utility.trim(resultSet.getString("category"));
								fareWrapper.baseFare=Utility.trim(resultSet.getString("baseFare"));
								fareWrapper.distanceFare= Utility.trim(resultSet.getString("distanceFare"));
								fareWrapper.waitTimeFare=Utility.trim(resultSet.getString("waitTimeFare"));
								fareWrapper.rideTimeFare=  Utility.trim(resultSet.getString("rideTimeFare"));
								fareWrapper.cancellationFee= Utility.trim(resultSet.getString("cancellationFee"));
								fareWrapper.fareType=Utility.trim(resultSet.getString("fareType"));
								fareWrapper.additionalKMFare= Utility.trim(resultSet.getString("additionalKMFare"));
								fareWrapper.nightTimeCharge=Utility.trim(resultSet.getString("nightTimeCharge"));
								fareWrapper.additionalDistFare= Utility.trim(resultSet.getString("additionalDistFare")); 
								fareWrapper.additionalDistFare=Utility.trim(resultSet.getString("additionalDistFare"));
								
								
								
								fareWrapper.recordFound=true;

								
								
								System.out.println("fetchTraveller  successful");
				
								vector.addElement(fareWrapper);
				
						}
						
						if (vector.size()>0)
						{
							dataArrayWrapper.fareWrapper = new FareWrapper[vector.size()];
							vector.copyInto(dataArrayWrapper.fareWrapper);
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
			//-----------------End fetch---------------------
			
			//-----------------generateFareRefNo-------------------------------
			public String generateFareRefNo()throws Exception {
				
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
				
				int fareRefNo=0;
				String finalFareRefNo=null;
				String fareCode=null;
				
				try {
					
					
					con = getConnection();
					
					
					sql="SELECT FareRefNo, FareCode from MST_Parameter";
					
					PreparedStatement pstmt = con.prepareStatement(sql);
				
					resultSet = pstmt.executeQuery();
					
					if (resultSet.next()) 
					{
						
						fareRefNo=resultSet.getInt("FareRefNo");
						System.out.println("FareRefNo " + fareRefNo);
						fareCode=resultSet.getString("FareCode");
						
					}
					
					resultSet.close();
					pstmt.close();
					
					if(fareRefNo==0)
					{
						fareRefNo=1;
						
					}
					else
					{
						
						fareRefNo=fareRefNo+1;
					}
						
					sql="UPDATE MST_Parameter set FareRefNo=?";
					
					
					System.out.println("sql " + sql);
					
					pstmt = con.prepareStatement(sql);
			
					pstmt.setInt(1,fareRefNo);
					
					pstmt.executeUpdate();
					pstmt.close();
					
					int paddingSize=6;

					finalFareRefNo=fareCode+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" + paddingSize +"d",fareRefNo);
					
					System.out.println("Successfully generated FareRefNo " + finalFareRefNo);
					
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

				return finalFareRefNo;
			}
			
			//-----------------End generateFareRefNo---------------------------

}
