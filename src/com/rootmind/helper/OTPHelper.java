package com.rootmind.helper;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.OTPWrapper;

public class OTPHelper extends Helper{
	
	public AbstractWrapper insertOTP(OTPWrapper otpWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql=null;
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		
		PreparedStatement pstmt=null;
		
		int otpExpLmit=0;
			
		try {
				
				con = getConnection();
				
				//-------- Fetch OTP Expiry Time Parameter
				pstmt = con.prepareStatement("SELECT OtpExpLimit from MST_Parameter");
				
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{	
					otpExpLmit=resultSet.getInt("OtpExpLimit");
					System.out.println("Parameter " + otpExpLmit);
					
				}
				
				resultSet.close();
				pstmt.close();
				
				//-------
				
				//Insert OTP Details
			
				sql=" INSERT INTO OTP(RiderRefNo, RiderID, DriverRefNo, DriverID, UserGroup, MobileNo, OTP, OTPGenDateTime, "
						+ "OTPExpDateTime, Status, Email) Values(?,?,?,?,?,?,?,?,?,?,?)";
				
				
				System.out.println("sql " + sql);
				
				 pstmt = con.prepareStatement(sql);
				 
				if(otpWrapper.userGroup.equals("RIDER") && otpWrapper.riderRefNo !=null){
					pstmt.setString(1,Utility.trim(otpWrapper.riderRefNo));
					pstmt.setString(2,Utility.trim(otpWrapper.riderID));
					pstmt.setString(3,"DRIVERREFNO");
					pstmt.setString(4,"DRIVERID");
				}
				else if(otpWrapper.userGroup.equals("DRIVER") && otpWrapper.driverRefNo !=null){
					pstmt.setString(1,"RIDERREFNO");
					pstmt.setString(2,"RIDERID");
					pstmt.setString(3,Utility.trim(otpWrapper.driverRefNo));
					pstmt.setString(4,Utility.trim(otpWrapper.driverID));
					
				}
				
				pstmt.setString(5,Utility.trim(otpWrapper.userGroup));
				pstmt.setString(6,Utility.trim(otpWrapper.mobileNo));	
				
				otpWrapper.otp=generateOTP();
				System.out.println("Generated OTP "+ otpWrapper.otp);
				
				pstmt.setString(7,otpWrapper.otp);
				
			
				pstmt.setTimestamp(8,new java.sql.Timestamp(System.currentTimeMillis()));
				
				System.out.println("Generated Date Time "+ new java.sql.Timestamp(System.currentTimeMillis()));
				
				pstmt.setTimestamp(9,new java.sql.Timestamp(System.currentTimeMillis()+otpExpLmit*60*1000));
				
				System.out.println("Expiry Date Time "+ new java.sql.Timestamp(System.currentTimeMillis()+otpExpLmit*60*1000));
				
				pstmt.setString(10,Utility.trim("CREATED"));
				pstmt.setString(11,Utility.trim(otpWrapper.email));
				
				
				
				pstmt.executeUpdate();
				if (resultSet!=null)  resultSet.close();
				pstmt.close();
				

				otpWrapper.generatedOTP=true;
	
				otpWrapper.recordFound=true;
				
				
				dataArrayWrapper.otpWrapper=new OTPWrapper[1];
				dataArrayWrapper.otpWrapper[0]=otpWrapper;
				dataArrayWrapper.recordFound=true;
				
				System.out.println("OTP Inserted");
		
			
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
	
	public AbstractWrapper validateOTP(OTPWrapper otpWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		
		PreparedStatement pstmt=null;
		
	
		String sql=null;
		Timestamp expTimestamp=null;
		OTPWrapper otpWrapperSub=null;
		
		try 
		{
			con = getConnection();
			
			
			
			sql="SELECT OTP, OTPGenDateTime, OTPExpDateTime FROM OTP "; 
			
			if(otpWrapper.riderRefNo !=null && !otpWrapper.riderRefNo.trim().isEmpty())
			{
				sql=sql + " WHERE otpexpdatetime=(select max(a.otpexpdatetime) as maxexpdate from OTP a where a.riderRefNo=? AND a.riderID=?) and riderRefNo=? AND riderID=?";
				
				
			}
			else if(otpWrapper.driverRefNo!=null && !otpWrapper.driverRefNo.trim().isEmpty())
			{
				sql=sql + " WHERE otpexpdatetime=(select max(a.otpexpdatetime) as maxexpdate from OTP a where a.driverRefNo=? AND a.driverID=?) and driverRefNo=? AND driverID=?";
				
			}
			/*else if(otpWrapper.accountNumber!=null && !otpWrapper.accountNumber.trim().isEmpty())
			{
				sql=sql + " WHERE otpexpdatetime=(select max(a.otpexpdatetime) as maxexpdate from RMT_OTP a where a.accountNumber=?) and accountNumber=?";

			}
			else if(otpWrapper.creditCardNumber!=null && !otpWrapper.creditCardNumber.trim().isEmpty())
			{
				sql=sql +  "WHERE otpexpdatetime=(select max(a.otpexpdatetime) as maxexpdate from RMT_OTP a where a.creditCardNumber=?) and creditCardNumber=?";

			}*/
			
			System.out.println("validateOTP sql  " + sql);
			
			pstmt = con.prepareStatement(sql);
			
			if(otpWrapper.userGroup.equals("RIDER"))
			{
				pstmt.setString(1, otpWrapper.riderRefNo.trim());
				pstmt.setString(2, otpWrapper.riderID.trim());
				pstmt.setString(3, otpWrapper.riderRefNo.trim());
				pstmt.setString(4, otpWrapper.riderID.trim());
				/*if(otpWrapper.riderRefNo !=null && !otpWrapper.riderRefNo.trim().isEmpty())
				{
					pstmt.setString(1, otpWrapper.riderRefNo.trim());
				}
				if(otpWrapper.riderID !=null && !otpWrapper.riderID.trim().isEmpty())
				{
					pstmt.setString(2, otpWrapper.riderID.trim());
				}
				if(otpWrapper.riderRefNo !=null && !otpWrapper.riderRefNo.trim().isEmpty())
				{
					pstmt.setString(3, otpWrapper.riderRefNo.trim());
				}
				if(otpWrapper.riderID !=null && !otpWrapper.riderID.trim().isEmpty())
				{
					pstmt.setString(4, otpWrapper.riderID.trim());
				}*/
				
			}
			else if(otpWrapper.userGroup.equals("DRIVER"))
			{
				pstmt.setString(1, otpWrapper.driverRefNo.trim());
				pstmt.setString(2, otpWrapper.driverID.trim());
				pstmt.setString(3, otpWrapper.driverRefNo.trim());
				pstmt.setString(4, otpWrapper.driverID.trim());
				/*if(otpWrapper.driverRefNo!=null && !otpWrapper.driverRefNo.trim().isEmpty()){
					pstmt.setString(1, otpWrapper.driverRefNo.trim());
					
				}
				if(otpWrapper.driverID!=null && !otpWrapper.driverID.trim().isEmpty()){
					pstmt.setString(2, otpWrapper.driverID.trim());
					
				}
				if(otpWrapper.driverRefNo!=null && !otpWrapper.driverRefNo.trim().isEmpty()){
					pstmt.setString(3, otpWrapper.driverRefNo.trim());
					
				}
				if(otpWrapper.driverID!=null && !otpWrapper.driverID.trim().isEmpty()){
					pstmt.setString(4, otpWrapper.driverID.trim());
					
				}*/
				
				
				System.out.println("OTP CIF no " + otpWrapper.driverRefNo);
				
			}
			/*else if(otpWrapper.accountNumber!=null && !otpWrapper.accountNumber.trim().isEmpty())
			{
				pstmt.setString(1, otpWrapper.accountNumber.trim());
				pstmt.setString(2, otpWrapper.accountNumber.trim());
				
			}
			else if(otpWrapper.creditCardNumber!=null && !otpWrapper.creditCardNumber.trim().isEmpty())
			{
				pstmt.setString(1, otpWrapper.creditCardNumber.trim());
				pstmt.setString(2, otpWrapper.creditCardNumber.trim());
			}*/
			resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) 
			{
				otpWrapperSub= new OTPWrapper();	
				otpWrapperSub.otp=Utility.trim(resultSet.getString("OTP"));
				
				System.out.println("OTP frm DB "+ otpWrapperSub.otp);
				
				otpWrapperSub.otpExpDateTime=Utility.trim(resultSet.getString("OTPExpDateTime"));
				
				 expTimestamp=resultSet.getTimestamp("OTPExpDateTime");
				 
				 System.out.println("expTimestamp "+ expTimestamp);
				
			}
			
			resultSet.close();
			pstmt.close();

			
			//Date otpExpDateTime  = dmyFormat.parse(otpWrapper.otpExpDateTime);
	
			
			if(expTimestamp.after(new java.sql.Timestamp(System.currentTimeMillis())))
			{
				System.out.println("System Time stamp "+ new java.sql.Timestamp(System.currentTimeMillis()));
				System.out.println("Time stamp validation success ");
				System.out.println("OTP frm user "+otpWrapper.otp);
				
				if(otpWrapper.otp.equals(otpWrapperSub.otp))
				{
					otpWrapper.verifiedOTP=true;
					
					System.out.println("OTP Matched");
					
					pstmt = con.prepareStatement("UPDATE OTP SET STATUS=? WHERE OTP=? and OTPExpDateTime=?");
					pstmt.setString(1,"VERIFIED");
					pstmt.setString(2,Utility.trim(otpWrapperSub.otp));
					pstmt.setTimestamp(3,expTimestamp);
					pstmt.executeUpdate();
					pstmt.close();
					
					//----Active The Rider STATUS
					if(otpWrapper.userGroup.equals("RIDER"))
					{   
						System.out.println("OTP Matched Rider Status Active");
						pstmt = con.prepareStatement("UPDATE Rider SET STATUS=? WHERE RiderRefNo=? and RiderID=?");
						pstmt.setString(1,"ACTIVE");
						pstmt.setString(2, Utility.trim(otpWrapper.riderRefNo));
						pstmt.setString(3, Utility.trim(otpWrapper.riderID));
						pstmt.executeUpdate();
						pstmt.close();
						
						//------ACTIVE THE USER
						pstmt = con.prepareStatement("UPDATE Users SET STATUS=? WHERE RiderRefNo=? and RiderID=?");
						pstmt.setString(1,"ACTIVE");
						pstmt.setString(2, Utility.trim(otpWrapper.riderRefNo));
						pstmt.setString(3, Utility.trim(otpWrapper.riderID));
						pstmt.executeUpdate();
						pstmt.close();
						
					}
					//----Active The Driver STATUS
					if(otpWrapper.userGroup.equals("DRIVER"))
					{
						System.out.println("OTP Matched Driver Status Active");
						pstmt = con.prepareStatement("UPDATE Driver SET STATUS=? WHERE DriverRefNo=? and DriverID=?");
						pstmt.setString(1,"ACTIVE");
						pstmt.setString(2, Utility.trim(otpWrapper.driverRefNo));
						pstmt.setString(3, Utility.trim(otpWrapper.driverID));
						pstmt.executeUpdate();
						pstmt.close();
						
						//------ACTIVE THE USER
						pstmt = con.prepareStatement("UPDATE Users SET STATUS=? WHERE DriverRefNo=? and DriverID=?");
						pstmt.setString(1,"ACTIVE");
						pstmt.setString(2, Utility.trim(otpWrapper.driverRefNo));
						pstmt.setString(3, Utility.trim(otpWrapper.driverID));
						pstmt.executeUpdate();
						pstmt.close();
					}
				
				}
				
			
			}
/*			else{
				
				pstmt.close();
				
				pstmt = con.prepareStatement("UPDATE RMT_OTP SET STATUS=? WHERE OTP=?");
				pstmt.setString(1,"Expired");
				pstmt.setString(2,Utility.trim(otpWrapperSub.otp));
				pstmt.executeUpdate();
				pstmt.close();
			
				
			}*/
			


			otpWrapper.recordFound=true;
			dataArrayWrapper.otpWrapper=new OTPWrapper[1];
			dataArrayWrapper.otpWrapper[0]=otpWrapper;
			dataArrayWrapper.recordFound=true;
			
			
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

	
	
	
	public String generateOTP()throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		int otpLength=0;
		
		StringBuilder pass=null;
		
		try {
				con = getConnection();
				
				PreparedStatement pstmt = con.prepareStatement("SELECT OTPLength from MST_Parameter");
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					
					otpLength=resultSet.getInt("OTPLength");
					System.out.println("otpLength" + otpLength);
					
				}
				
				resultSet.close();
				pstmt.close();
			
	
				String chars =  "0123456789"+ "0123456789"+"0123456789"
	                     + "0123456789"; //ABCDEFGHIJKLMNOPQRSTUVWXYZ

		        final int PW_LENGTH = otpLength;
		        Random rnd = new SecureRandom();
		        pass = new StringBuilder();
		        for (int i = 0; i < PW_LENGTH; i++)
		        pass.append(chars.charAt(rnd.nextInt(chars.length())));

			
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

		return  pass.toString();
	}

}
