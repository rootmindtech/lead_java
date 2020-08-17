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

import com.rootmind.controller.AES128Crypto;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.PasswordWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class UsersHelper extends Helper{
	
	
	
	public AbstractWrapper fetchRiderID(UsersWrapper usersWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	//UsersWrapper usersWrapper=	new UsersWrapper();

	//SimpleDateFormat dmyFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy, hh:mm:ss");

	String sql=null;
	System.out.println("fetchRiderID mobileNo/email :"+ usersWrapper.userid);
	

	try {
			con = getConnection();
			
			/*if(usersWrapper.mobileNo !=null && !usersWrapper.mobileNo.trim().equals("")){
				sql="SELECT  RideRefNo, RiderID, Userid from Rider where MobilleNo=?";
				//userwrapper.mobileNo
			}
			else if(usersWrapper.email !=null && usersWrapper.email.trim().equals("") ){
				
				sql="SELECT  RideRefNo, RiderID, Userid from Rider where Email=?";
			}*/
			
			sql="SELECT  RiderRefNo, RiderID, Userid from Rider where MobileNo=? OR Email=?"; 
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			/*if(usersWrapper.mobileNo !=null && !usersWrapper.mobileNo.trim().equals("")){
				pstmt.setString(1,usersWrapper.mobileNo.trim());
			}
			else if(usersWrapper.email !=null && usersWrapper.email.trim().equals("") ){
				
				pstmt.setString(1,usersWrapper.email.trim());
			}*/
			// because user uses email/mobileNo as userid ,from that we are getting usual userid
			pstmt.setString(1,usersWrapper.userid.trim()); 
			pstmt.setString(2,usersWrapper.userid.trim());
	
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) 
			{
				
				
				usersWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
				usersWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
				usersWrapper.userid=Utility.trim(resultSet.getString("Userid"));
				
				usersWrapper.recordFound=true;
				
				System.out.println("fetch RiderID Successful");
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

	return usersWrapper;
}

	
	public AbstractWrapper fetchDriverID(UsersWrapper usersWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		//UsersWrapper usersWrapper=	new UsersWrapper();

		//SimpleDateFormat dmyFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy, hh:mm:ss");

		String sql=null;
		System.out.println("fetchDriverID mobileNo/email :"+ usersWrapper.userid);
		

		try {
				con = getConnection();
				
				
				sql="SELECT  DriverRefNo, DriverID, Userid from Driver where MobilleNo=? OR Email=?"; 
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				// because user uses email/mobileNo as userid ,from that we are getting usual userid
				pstmt.setString(1,usersWrapper.userid.trim()); 
				pstmt.setString(2,usersWrapper.userid.trim());
		
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					
					
					usersWrapper.driverRefNo=Utility.trim(resultSet.getString("DriverRefNo"));
					usersWrapper.driverID=Utility.trim(resultSet.getString("DriverID"));
					usersWrapper.userid=Utility.trim(resultSet.getString("Userid"));
					
					usersWrapper.recordFound=true;
					
					System.out.println("fetch DriverID Successful");
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

		return usersWrapper;
	}

	


/*public AbstractWrapper updateUserAudit(String userid, String sessionid,String activity,String screenName,String message)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	UsersWrapper usersWrapper=	new UsersWrapper();

	
	System.out.println("userid :"+ userid);

	try {
		con = getConnection();
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO UserAudit(userid,sessionid,Activity,"
				+ "Screenname,Datestamp,Message) values (?,?,?,?,?,?) ");
		
		pstmt.setString(1,(userid==null?null:userid.trim()));
		pstmt.setString(2,(sessionid==null?null:sessionid.trim()));
		pstmt.setString(3,(activity==null?null:activity.trim()));
		pstmt.setString(4,screenName.trim());
		pstmt.setTimestamp(5,Utility.getCurrentTime());
		pstmt.setString(6,(message==null?null:message.trim()));
		pstmt.executeUpdate();

		pstmt.close();

		UsersWrapper tmpUsersWrapper=	new UsersWrapper();
		tmpUsersWrapper=(UsersWrapper)getCIFNumber(userid);
		
		pstmt = con.prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=? where userid=?");
		
		pstmt.setTimestamp(1,Utility.getCurrentTime());
		pstmt.setInt(2,tmpUsersWrapper.noLoginRetry+1);
		pstmt.setString(3,userid.trim());

		pstmt.executeUpdate();

		pstmt.close();
		
		usersWrapper.recordFound=true;
		
		System.out.println("user audit table updated " );
		
		
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

	return usersWrapper;
}*/

public void updateUserDetails(String userid,int noLoginRetry,String sessionid, String deviceToken)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	//UsersWrapper usersWrapper=	new UsersWrapper();

	
	System.out.println("userid :"+ userid);
	String sql=null;
	PreparedStatement pstmt = null;
	String userGroup=null;

	try {
		con = getConnection();
		
		//----- code--
		
		/*sql="SELECT UserGroup from Users WHERE Userid=?";
		
	
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1,userid.trim());
	
		resultSet = pstmt.executeQuery();
		if (resultSet.next()) 
		{
				
			userGroup=Utility.trim(resultSet.getString("UserGroup"));
			
		}
		
		resultSet.close();
		pstmt.close();*/
		
		//System.out.println("user group is "+userGroup.trim() );
		
		System.out.println("updateUserDetails deviceToken "+deviceToken );
		
		//----------do not update Device id for Staff and admin, update only for Students when not null
		sql="UPDATE Users set Lastlogindate=?, NoLoginRetry=?,DeviceToken=?  where userid=?";
		
		pstmt = con.prepareStatement(sql);
		
		pstmt.setTimestamp(1,Utility.getCurrentTime());
		pstmt.setInt(2,noLoginRetry+1);
		pstmt.setString(3,(deviceToken==null?"":deviceToken.trim()));
		pstmt.setString(4,(userid==null?"":userid.trim()));
		

		pstmt.executeUpdate();

		pstmt.close();
		//usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
		//usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
		//usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());

		

		//usersWrapper.recordFound=true;
		
		System.out.println("user table device updated " );
		
		
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

	//return usersWrapper;
}

public AbstractWrapper validateCredentials(UsersWrapper usersWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	//UsersWrapper usersWrapper=	new UsersWrapper();
	usersWrapper.validUser=false;
	usersWrapper.recordFound=false;
	SimpleDateFormat dmyFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy, hh:mm:ss");
	
	PreparedStatement pstmt=null;
	//Vector<Object> vector = new Vector<Object>();
	//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	String sql=null;
	System.out.println("userid :"+ usersWrapper.userid);
	//UserStudentMapWrapper userStudentMapWrapper=null;
	//UserStudentMapWrapper[] userStudentMapWrapperArray=null;
	
	
	try {
		
			con = getConnection();
			
		
			
			sql="SELECT userid, password, status, MobileNo, Email, RiderRefNo, RiderID, DriverRefNo, DriverID,  "
					+ " Lastlogindate,Devicetoken,NoLoginRetry,MaxRetry,sessionid,PwdChgDate,OTP,BusinessUnit, "
					+ " SessionExpirytime,UserGroup,Admin from Users  where userid=? and Status=?";
			pstmt = con.prepareStatement(sql);
			
			
			pstmt.setString(1,usersWrapper.userid.trim());
			pstmt.setString(2,"ACTIVE");
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) 
			{
				 usersWrapper=new UsersWrapper();
				
				usersWrapper.userid=(resultSet.getString("userid")==null?"":resultSet.getString("userid").trim());
				System.out.println("userid " + usersWrapper.userid);
				
				usersWrapper.password=(resultSet.getString("password")==null?"":resultSet.getString("password").trim());
				System.out.println("password " + usersWrapper.password);
	
				usersWrapper.status=(resultSet.getString("status")==null?"":resultSet.getString("status").trim());
				
				usersWrapper.mobileNo=(resultSet.getString("MobileNo")==null?"":resultSet.getString("MobileNo").trim());
				System.out.println("MobileNo " + usersWrapper.mobileNo);
				
				usersWrapper.email=(resultSet.getString("Email")==null?"":resultSet.getString("Email").trim());
				System.out.println("Email " + usersWrapper.email);
				
				usersWrapper.riderRefNo=(resultSet.getString("RiderRefNo")==null?"":resultSet.getString("RiderRefNo").trim());
				usersWrapper.riderID=(resultSet.getString("RiderID")==null?"":resultSet.getString("RiderID").trim());
				usersWrapper.driverRefNo=(resultSet.getString("DriverRefNo")==null?"":resultSet.getString("DriverRefNo").trim());
				usersWrapper.driverID=(resultSet.getString("DriverID")==null?"":resultSet.getString("DriverID").trim());
				
				System.out.println("last login date " + resultSet.getDate("Lastlogindate"));
	
				usersWrapper.lastLoginDate= Utility.setDateAMPM(resultSet.getString("Lastlogindate"));//(resultSet.getDate("Lastlogindate")==null?"":dmyFormat.format(resultSet.getTimestamp("Lastlogindate")));
				
				usersWrapper.deviceToken=(resultSet.getString("Devicetoken")==null?"":resultSet.getString("Devicetoken").trim());
				System.out.println("Devicetoken " + usersWrapper.deviceToken);
				
				usersWrapper.noLoginRetry=resultSet.getInt("NoLoginRetry");
				
				usersWrapper.maxRetry=resultSet.getInt("MaxRetry");
				
				usersWrapper.sessionid=(resultSet.getString("sessionid")==null?"":resultSet.getString("sessionid").trim());
				
				usersWrapper.pwdChgDate= (resultSet.getDate("PwdChgDate")==null?"":dmyFormat.format(resultSet.getTimestamp("PwdChgDate")));
				
				usersWrapper.otp=resultSet.getInt("OTP");
				
				usersWrapper.businessUnit=(resultSet.getString("BusinessUnit")==null?"":resultSet.getString("BusinessUnit").trim());
				
				usersWrapper.sessionExpiryTime=resultSet.getInt("SessionExpirytime");
				
				usersWrapper.userGroup=(resultSet.getString("UserGroup")==null?"":resultSet.getString("UserGroup").trim());
				usersWrapper.admin=(resultSet.getString("Admin")==null?"":resultSet.getString("Admin").trim());
				
				usersWrapper.recordFound=true;
				
				
				System.out.println("usersWrapper lastLoginDate " + usersWrapper.lastLoginDate);
				

	
			}
	
			resultSet.close();
			pstmt.close();
			
			
			//-----setting user values for mobile app--
			
		/*	if(usersWrapper.userGroup !=null && usersWrapper.userGroup.trim().equals("RIDER"))
			{
				System.out.println("setting user values for mobile app ");
				
				
				pstmt = con.prepareStatement("SELECT RiderRefNo, RiderID, FirstName, LastName, MobileNo, Email, "
						+ " Gender, City, RegisterDate, Status from Rider WHERE RiderRefNo=? AND RiderID=?");
				
				System.out.println("riderRefNo  "+usersWrapper.riderRefNo);
				System.out.println("riderID  "+usersWrapper.riderID);
				pstmt.setString(1,Utility.trim(usersWrapper.riderRefNo));
				pstmt.setString(2,Utility.trim(usersWrapper.riderID));
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					System.out.println("inside if condition  ");
					usersWrapper.riderWrapper=new RiderWrapper[1];
					usersWrapper.riderWrapper[0].riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
					usersWrapper.riderWrapper[0].riderID=Utility.trim(resultSet.getString("RiderID"));
					usersWrapper.riderWrapper[0].firstName=Utility.trim(resultSet.getString("FirstName"));
					usersWrapper.riderWrapper[0].lastName=Utility.trim(resultSet.getString("LastName"));
					usersWrapper.riderWrapper[0].mobileNo=Utility.trim(resultSet.getString("MobileNo"));
					usersWrapper.riderWrapper[0].email=Utility.trim(resultSet.getString("Email"));
					usersWrapper.riderWrapper[0].gender=Utility.trim(resultSet.getString("Gender"));
					usersWrapper.riderWrapper[0].city=Utility.trim(resultSet.getString("City"));
					usersWrapper.riderWrapper[0].registerDate=Utility.setDate(resultSet.getString("RegisterDate"));
					usersWrapper.riderWrapper[0].status=Utility.trim(resultSet.getString("Status"));
					usersWrapper.riderWrapper[0].recordFound=true;
					
					
					System.out.println("validate credentials fetch user details successful ");
					
					
					//usersWrapper.riderWrapper[0].rid=popoverHelper.fetchPopoverDesc(usersWrapper.riderWrapper[0].academicYearID, "MST_AcademicYear");
			
					
				}
				
				resultSet.close();
				pstmt.close();
				System.out.println("setting user values end ");
			
				if(usersWrapper.riderID !=null && !usersWrapper.riderID.equals(""))
				{
					
					//-----StudentID for user student map--
					
					sql="SELECT RiderID from UserStudentMap WHERE Userid=?";
					
					pstmt = con.prepareStatement(sql);
					
					pstmt.setString(1,Utility.trim(usersWrapper.userid));
					
					resultSet = pstmt.executeQuery();
					while(resultSet.next()) 
					{
						userStudentMapWrapper =new UserStudentMapWrapper();
						
						userStudentMapWrapper.studentID=Utility.trim(resultSet.getString("StudentID"));
						
						System.out.println("StudentID for userstudentmap " + userStudentMapWrapper.studentID);
						userStudentMapWrapper.recordFound=true;
			
						vector.addElement(userStudentMapWrapper);
					}
					if (vector.size()>0)
					{
						userStudentMapWrapperArray= new UserStudentMapWrapper[vector.size()];
						vector.copyInto(userStudentMapWrapperArray);
						
						System.out.println("total trn. in fetch " + vector.size());
			
					}
					
					resultSet.close();
					pstmt.close();
					
					//----------
					if(userStudentMapWrapperArray !=null && userStudentMapWrapperArray.length>0)
					{
						
						usersWrapper.studentProfileWrapper=new StudentProfileWrapper[userStudentMapWrapperArray.length];
						PopoverHelper popoverHelper=new PopoverHelper();
						
						for(int i=0;i<=userStudentMapWrapperArray.length-1;i++)
						{
							System.out.println("Student for loop userstudentmap " + userStudentMapWrapperArray[i].studentID);
							
							pstmt = con.prepareStatement("SELECT RefNo,StudentID,AcademicYearID,SchoolID,GradeID,SectionID,StudentName,"
									+ " Surname from StudentProfile WHERE StudentID=?");
		
							pstmt.setString(1,Utility.trim(userStudentMapWrapperArray[i].studentID));
							resultSet = pstmt.executeQuery();
							if (resultSet.next()) 
							{
								
								
		
								usersWrapper.studentProfileWrapper[i]=new StudentProfileWrapper();
								usersWrapper.studentProfileWrapper[i].refNo=Utility.trim(resultSet.getString("RefNo"));
								usersWrapper.studentProfileWrapper[i].studentID=Utility.trim(resultSet.getString("StudentID"));
								usersWrapper.studentProfileWrapper[i].academicYearID=Utility.trim(resultSet.getString("AcademicYearID"));
								usersWrapper.studentProfileWrapper[i].schoolID=Utility.trim(resultSet.getString("SchoolID"));
								usersWrapper.studentProfileWrapper[i].gradeID=Utility.trim(resultSet.getString("GradeID"));
								usersWrapper.studentProfileWrapper[i].sectionID=Utility.trim(resultSet.getString("SectionID"));
								usersWrapper.studentProfileWrapper[i].studentName=Utility.trim(resultSet.getString("StudentName"));
								usersWrapper.studentProfileWrapper[i].surname=Utility.trim(resultSet.getString("Surname"));
								
								usersWrapper.studentProfileWrapper[i].academicYearIDValue=popoverHelper.fetchPopoverDesc(usersWrapper.studentProfileWrapper[i].academicYearID, "MST_AcademicYear");
								usersWrapper.studentProfileWrapper[i].schoolIDValue=popoverHelper.fetchPopoverDesc(usersWrapper.studentProfileWrapper[i].schoolID, "MST_School");
								usersWrapper.studentProfileWrapper[i].gradeIDValue=popoverHelper.fetchPopoverDesc(usersWrapper.studentProfileWrapper[i].gradeID, "MST_Grade");
								usersWrapper.studentProfileWrapper[i].sectionIDValue=popoverHelper.fetchPopoverDesc(usersWrapper.studentProfileWrapper[i].sectionID, "MST_Section");
				
						
							
								
								
							}
							
							resultSet.close();
							pstmt.close();
						}
					}
				}
				
			}*/
			//----------
			
			

			//------------User Menu ----------------
			/*
			if(usersWrapper.recordFound==true)
			{
				
				   System.out.println("validate credentials User Menu ");
				
					UserMenuWrapper userMenuWrapper=null;  //new UserMenuWrapper();
					pstmt = con.prepareStatement("SELECT Userid,MenuID FROM MST_UserMenu WHERE userid=?");
					pstmt.setString(1,usersWrapper.userid.trim());
			
					resultSet = pstmt.executeQuery();
					
					vector.clear();
					
					while (resultSet.next()) 
					{
						
						userMenuWrapper = new UserMenuWrapper();
						
				
						userMenuWrapper.userid = Utility.trim(resultSet.getString("Userid"));
						System.out.println("userid" + userMenuWrapper.userid);
				
						userMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));
						System.out.println("menuID" + userMenuWrapper.menuID);
						
						userMenuWrapper.recordFound = true;
						
						vector.addElement(userMenuWrapper);
						
						
						System.out.println("Fetch UserMenu Details Successful" );
				
					}
			
					if (vector.size()>0)
					{
						
						usersWrapper.userMenuWrapper = new UserMenuWrapper[vector.size()];
						vector.copyInto(usersWrapper.userMenuWrapper);
						
						System.out.println("total trn. in fetch " + vector.size());
				
					}
					else // ----IF USER NOT FOUND-------
					{
						usersWrapper.userMenuWrapper = new UserMenuWrapper[1];
						usersWrapper.userMenuWrapper[0]= userMenuWrapper;
						
					}
					//dataArrayWrapper.recordFound=true;
					resultSet.close();
					pstmt.close();

			}
			//-----------User Menu end------
*/			
			
			
			
			
			
			
			/*dataArrayWrapper.usersWrapper=new UsersWrapper[1];
			dataArrayWrapper.usersWrapper[0]=usersWrapper;*/
			
			
		
			
			
			
			
		/*	//--user not found validation ---
			if(usersWrapper.recordFound==false)
			{
				System.out.println("usersWrapper.recordFound :"+ usersWrapper.recordFound);
				usersWrapper.validUser=false;
				//dataArrayWrapper.usersWrapper[0]=usersWrapper;
				//dataArrayWrapper.recordFound=true;
				return usersWrapper;
			}
			else if (usersWrapper.recordFound==true)
			{
				//--inactive user validation
				if(!usersWrapper.status.trim().equals("ACTIVE"))
				{
					System.out.println("usersWrapper.status :"+ usersWrapper.status);
					usersWrapper.validUser=false;
					//dataArrayWrapper.usersWrapper[0]=usersWrapper;
					//dataArrayWrapper.recordFound=true;
					return usersWrapper;
				}
				//--invalid session id validation
				else if(newSession==false && !sessionid.trim().equals(usersWrapper.sessionid))
				{
					System.out.println("sessionid :"+ sessionid);
					System.out.println("usersWrapper.sessionid :"+ usersWrapper.sessionid);
					usersWrapper.validUser=false;
					//dataArrayWrapper.usersWrapper[0]=usersWrapper;
					//dataArrayWrapper.recordFound=true;
					return usersWrapper;
				}
				else
				{
					//System.out.println("update usersWrapper.noLoginRetry :"+ usersWrapper.noLoginRetry);
					
					if(newSession==true)
					{
						pstmt = con.prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=?,sessionid=?,DeviceToken=?  where userid=?");
						
						pstmt.setTimestamp(1,Utility.getCurrentTime());
						pstmt.setInt(2,usersWrapper.noLoginRetry+1);
						pstmt.setString(3,(sessionid==null?"":sessionid.trim()));
						pstmt.setString(4,(deviceToken==null?"":deviceToken.trim()));
						pstmt.setString(5,(userid==null?"":userid.trim()));
	
						pstmt.executeUpdate();
	
						pstmt.close();
						usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
						usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
						usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());
					}
					
					usersWrapper.validUser=true;
					//dataArrayWrapper.usersWrapper[0]=usersWrapper;
					//dataArrayWrapper.recordFound=true;
					System.out.println("validate credentials end");
				}
				
			}*/
			
			
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

	
	
	return usersWrapper;
	

}
public AbstractWrapper fetchPasswordPolicy(PasswordWrapper passwordWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	
	DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	
	//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
	DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
	symbols.setGroupingSeparator(',');
	formatter.applyPattern("###,###,###,##0.00");
	formatter.setDecimalFormatSymbols(symbols);
	

	try {
		
			
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT MinLength, MaxLength, CapitalLetter, SpecialCharacter, OldPasswordRepeat FROM RMT_PasswordPolicy");
		
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) 
			{
				
				passwordWrapper = new PasswordWrapper();
				
				passwordWrapper.minLength=Utility.trim(resultSet.getString("MinLength"));
				System.out.println("minLength " + passwordWrapper.minLength);
				passwordWrapper.maxLength=Utility.trim(resultSet.getString("MaxLength"));
				passwordWrapper.capitalLetter=Utility.trim(resultSet.getString("CapitalLetter"));
				passwordWrapper.specialCharacter=Utility.trim(resultSet.getString("SpecialCharacter"));
				passwordWrapper.oldPasswordRepeat=Utility.trim(resultSet.getString("OldPasswordRepeat"));		
				
				passwordWrapper.recordFound=true;
			    passwordWrapper.policyAvailable=true;
			  
				System.out.println("PasswordWrapper");

			}
			
				dataArrayWrapper.passwordWrapper = new PasswordWrapper[1];				
				dataArrayWrapper.passwordWrapper[0]=passwordWrapper;
				dataArrayWrapper.recordFound=true;

				System.out.println("total trn. in fetchPasswordPolicy ");

			
			
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

/*public AbstractWrapper changePassword(UsersWrapper usersWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

	
	System.out.println("userid :"+ usersWrapper.userid);
    String oldPasswordRepeat=null;
	try {
		
		con = getConnection();
		PreparedStatement pstmt=null;
		
		//-----check password valid or not
		pstmt = con.prepareStatement("SELECT Password FROM Users WHERE USERID=?");
		pstmt.setString(1, usersWrapper.userid);
		resultSet = pstmt.executeQuery();
		if(resultSet.next()){
			
			
		  if(!usersWrapper.oldPassword.trim().equals(Utility.trim(resultSet.getString("Password"))))
		  {
			  usersWrapper.invalidOldPassword=true;
			  usersWrapper.recordFound=true;
		  }
		   
		   
		}
		resultSet.close();
		pstmt.close();
		//-------
		
		if(usersWrapper.invalidOldPassword==false)
		{
			
		
		
				//------------repeat password verification
				pstmt = con.prepareStatement("SELECT OldPasswordRepeat FROM RMT_PasswordPolicy");
				
				resultSet = pstmt.executeQuery();
				if(resultSet.next()){
					
				   oldPasswordRepeat=Utility.trim(resultSet.getString("OldPasswordRepeat"));	
				   
				   System.out.println("oldPasswordRepeat "+oldPasswordRepeat );
				}
				
				resultSet.close();
				pstmt.close();
				
				
				if(oldPasswordRepeat !=null && !oldPasswordRepeat.trim().equals(""))
				{
					pstmt = con.prepareStatement("SELECT OldPassword FROM RMT_PasswordHistory Where UserId=?");
					
					pstmt.setString(1,Utility.trim(usersWrapper.userid));
					
					resultSet = pstmt.executeQuery();
					
					ArrayList<String> oldPassword = new ArrayList<String>();
					while(resultSet.next()){
						
						oldPassword.add(Utility.trim(resultSet.getString("OldPassword")));	
						
						 System.out.println("OldPassword "+Utility.trim(resultSet.getString("OldPassword")));
					}
					
					resultSet.close();
					pstmt.close();
					
					for(int i=0;i<= Integer.parseInt(oldPasswordRepeat)-1;i++){
						
						System.out.println("PASSWORD "+usersWrapper.password.trim());
						System.out.println("OldPassword "+oldPassword.get(i).trim());
						
						if(usersWrapper.password.trim().equals(oldPassword.get(i).trim())){
							
							usersWrapper.oldPasswordRepeat=true;
							usersWrapper.recordFound=true;
						}
					}
				}
				
				if(usersWrapper.oldPasswordRepeat==false){
					
					pstmt = con.prepareStatement("UPDATE Users set Password=? where userid=?");
					pstmt.setString(1,Utility.trim(usersWrapper.password));
					pstmt.setString(2,Utility.trim(usersWrapper.userid));
		
					pstmt.executeUpdate();
		
					pstmt.close();
				
					
					System.out.println("Change password details updated " );
					
					
					pstmt = con.prepareStatement("INSERT INTO RMT_PasswordHistory (UserId,OldPassword,PasswordChngDT,IPAddress) values (?,?,?,?)");
					
					pstmt.setString(1,Utility.trim(usersWrapper.userid));
					pstmt.setString(2,Utility.trim(usersWrapper.oldPassword));
					pstmt.setTimestamp(3,Utility.getCurrentTime());
					pstmt.setString(4,Utility.trim(usersWrapper.ipAddress));
				
					pstmt.executeUpdate();
		
					pstmt.close();
					
					System.out.println("PasswordHistory Details Inserted" );
					
					usersWrapper.recordFound=true;
					usersWrapper.passwordChanged=true;
					
				}
				
		}		
		dataArrayWrapper.usersWrapper=new UsersWrapper[1];
		dataArrayWrapper.usersWrapper[0]=usersWrapper;
		
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
*/

public AbstractWrapper changePassword(UsersWrapper usersWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

	
	System.out.println("userid :"+ usersWrapper.userid);
    
	try {
		
					con = getConnection();
					PreparedStatement pstmt=null;
					
					AES128Crypto aes128Crypto=new AES128Crypto();
		
		
				 	//------
					pstmt = con.prepareStatement("UPDATE Users set Password=?,PWDChgDate=? where userid=?");
					pstmt.setString(1,aes128Crypto.md5DB(usersWrapper.password));
					pstmt.setTimestamp(2,Utility.getCurrentTime());
					pstmt.setString(3,Utility.trim(usersWrapper.userid));
		
					pstmt.executeUpdate();
					pstmt.close();
					System.out.println("Change password details updated " );
					//------
					
					//-------
					pstmt = con.prepareStatement("INSERT INTO PasswordHistory(UserId,OldPassword,PasswordChngDT,IPAddress,ModifierID) values (?,?,?,?,?)");
					
					pstmt.setString(1,Utility.trim(usersWrapper.userid));
					pstmt.setString(2,Utility.trim(usersWrapper.oldPassword));
					pstmt.setTimestamp(3,Utility.getCurrentTime());
					pstmt.setString(4,Utility.trim(usersWrapper.ipAddress));
					pstmt.setString(5,Utility.trim(usersWrapper.userid));
					pstmt.executeUpdate();
		
					pstmt.close();
					
					System.out.println("PasswordHistory Details Inserted" );
					
					usersWrapper.recordFound=true;
					usersWrapper.passwordChanged=true;
					
					//------
					
		
				
					dataArrayWrapper.usersWrapper=new UsersWrapper[1];
					dataArrayWrapper.usersWrapper[0]=usersWrapper;
		
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


public AbstractWrapper fetchLoginProfile(UsersWrapper usersWrapper)throws Exception {

	Connection con = null;
	ResultSet resultSet = null;
	
	DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	
	//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
	DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
	symbols.setGroupingSeparator(',');
	formatter.applyPattern("###,###,###,##0.00");
	formatter.setDecimalFormatSymbols(symbols);
	

	try {
		
			
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT Userid,Password,Status FROM Users WHERE Userid=?");
		
			pstmt.setString(1,Utility.trim(usersWrapper.userid));
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) 
			{
				
	
				usersWrapper.userid=Utility.trim(resultSet.getString("Userid"));
				System.out.println("minLength " + usersWrapper.userid);
				usersWrapper.password=Utility.trim(resultSet.getString("Password"));
				usersWrapper.status=Utility.trim(resultSet.getString("Status"));	
				
				usersWrapper.recordFound=true;
			 

			}
			
				dataArrayWrapper.usersWrapper = new UsersWrapper[1];				
				dataArrayWrapper.usersWrapper[0]=usersWrapper;
				dataArrayWrapper.recordFound=true;

				System.out.println("total trn. in fetchUserid ");

			
			
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


/*//-----------------Start insertUserid---------------------

	public AbstractWrapper insertUserid(UsersWrapper usersWrapper)throws Exception {
			
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
					

					sql=" INSERT INTO StudentProfile(Userid,Password, StudentID,) Values(?,?,?)";
					
					System.out.println("sql " + sql);
					
					pstmt = con.prepareStatement(sql);

					pstmt.setString(1,Utility.trim(usersWrapper.userid));
					pstmt.setString(2,Utility.trim(usersWrapper.password));  
					pstmt.setString(3,Utility.trim(usersWrapper.studentID)); 

					System.out.println("insert  Userid "+usersWrapper.userid);
					
					pstmt.executeUpdate();
					pstmt.close();
					
					usersWrapper.recordFound=true;
				
					dataArrayWrapper.usersWrapper=new UsersWrapper[1];
					dataArrayWrapper.usersWrapper[0]=usersWrapper;
					
					dataArrayWrapper.recordFound=true;
					
					System.out.println("insertUserid is Successfully ");
					
				
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
		
		//-----------------End insertUserid---------------------
*/	
	
	public AbstractWrapper insertLoginProfile(UsersWrapper usersProfileWrapper,UsersWrapper usersWrapper,String userGroup)throws Exception {
	
		Connection con = null;
		ResultSet resultSet = null;
		
	
		DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
		PreparedStatement pstmt=null;
		
		//String defaultPassword=null;
		String sql=null;
		
		
		
		
	
		try {
				
				
					con = getConnection();
	
	
					if(userGroup.equals("RIDER"))
					{
						sql="INSERT INTO Users(Userid, Password, Status, MobileNo, Email, UserGroup, "
						 		+ " RiderRefNo, RiderID, MakerID,MakerDateTime) Values(?,?,?,?,?,?,?,?,?,?)";
					}
					else if(userGroup.equals("DRIVER"))
					{
						sql="INSERT INTO Users(Userid, Password, Status, MobileNo, Email, UserGroup, "
						 		+ " DriverRefNo, DriverID, MakerID,MakerDateTime) Values(?,?,?,?,?,?,?,?,?,?)";
					}
					 pstmt = con.prepareStatement(sql); 
					 
					 
				     System.out.println("Userid is "+Utility.trim(usersWrapper.userid) );
				     
					 pstmt.setString(1,Utility.trim(usersWrapper.userid));
					 pstmt.setString(2,usersWrapper.password);
					 pstmt.setString(3,"RIP"); //Registration In Progress
					 pstmt.setString(4,Utility.trim(usersWrapper.mobileNo));
					 pstmt.setString(5,Utility.trim(usersWrapper.email));
					 pstmt.setString(6,userGroup); //--ex:"STUDENT","STAFF"
					 
					 if(userGroup.equals("RIDER"))
					 {
						 pstmt.setString(7,Utility.trim(usersWrapper.riderRefNo));
						 pstmt.setString(8,Utility.trim(usersWrapper.riderID));
					 }
					 else if(userGroup.equals("DRIVER"))
					 {
						 pstmt.setString(7,Utility.trim(usersWrapper.driverRefNo));
						 pstmt.setString(8,Utility.trim(usersWrapper.driverID));
					 }
					 pstmt.setString(9,Utility.trim(usersWrapper.userid));			
					 pstmt.setTimestamp(10,Utility.getCurrentTime());
					 
					 
					 pstmt.executeUpdate();
					 pstmt.close();
					 
					 usersWrapper.recordFound=true;
					 
					
					dataArrayWrapper.usersWrapper=new UsersWrapper[1];
					dataArrayWrapper.usersWrapper[0]=usersWrapper;
					dataArrayWrapper.recordFound=true;
				
					System.out.println("userprofile inserted " + usersWrapper.userid);
			

			
			
			
		}
		
		 catch (SQLException se) {
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

	public AbstractWrapper updateLoginProfile(UsersWrapper usersProfileWrapper,UsersWrapper usersWrapper,String userGroup)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
	
		DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
		PreparedStatement pstmt=null;
		
		//String defaultPassword=null;
		String sql=null;
		
		
		
		
	
		try {
				
				
				con = getConnection();
				

				
				 if(userGroup.equals("RIDER"))
				 {
			
					 sql="UPDATE Users SET MobileNo=?, Email=?, ModifierID=?, ModifierDateTime=?  where RiderRefNo=? and RiderID=? "
				 		+ " and Userid=? ";
				 }
				 else if(userGroup.equals("DRIVER"))
				 {
			
					 sql="UPDATE Users SET MobileNo=?, Email=?, ModifierID=?, ModifierDateTime=?  where DriverRefNo=? and DriverID=? "
				 		+ " and Userid=? ";
				 }
				 pstmt = con.prepareStatement(sql);
				
								
			 	pstmt.setString(1,Utility.trim(usersWrapper.mobileNo));
			 	pstmt.setString(2,Utility.trim(usersWrapper.email));
				pstmt.setString(3,Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(4,Utility.getCurrentTime());
				
				if(userGroup.equals("RIDER"))
				{
					pstmt.setString(5,Utility.trim(usersWrapper.riderRefNo));
					pstmt.setString(6,Utility.trim(usersWrapper.riderID));
				}
				else if(userGroup.equals("DRIVER"))
				{
					pstmt.setString(5,Utility.trim(usersWrapper.driverRefNo));
					pstmt.setString(6,Utility.trim(usersWrapper.driverID));
				}
				pstmt.setString(7,Utility.trim(usersWrapper.userid));			
				
				pstmt.executeUpdate();
				pstmt.close();

				
			
				usersWrapper.recordFound=true;
							
				dataArrayWrapper.usersWrapper=new UsersWrapper[1];
				dataArrayWrapper.usersWrapper[0]=usersWrapper;
				dataArrayWrapper.recordFound=true;
				
							
				System.out.println("Login Profile updated " );
				
			
			
			
		}
		
		 catch (SQLException se) {
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
	
	
	public AbstractWrapper fetchUsersStaff()throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		UsersWrapper usersWrapper=null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		Vector<Object> vector = new Vector<Object>();

		try {
			
				
				con = getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT Userid,Status FROM Users WHERE UserGroup='STAFF'");
			
				
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					usersWrapper= new UsersWrapper();
					
					usersWrapper.userid=Utility.trim(resultSet.getString("Userid"));
					System.out.println("minLength " + usersWrapper.userid);
					
					usersWrapper.status=Utility.trim(resultSet.getString("Status"));	
					
					usersWrapper.recordFound=true;
					vector.addElement(usersWrapper);

				}
				
				if (vector.size()>0)
				{
					dataArrayWrapper.usersWrapper = new UsersWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.usersWrapper);
					dataArrayWrapper.recordFound=true;
		
					System.out.println("total trn. in fetchStaff " + vector.size());
		
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
	
	public AbstractWrapper fetchSessionDetails(UsersWrapper usersWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		//UsersWrapper usersWrapper=null;
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt=null;
		int sessionTimeOut=0;

		try {
	
				con = getConnection();
				
				//--------
				
				
				pstmt = con.prepareStatement("SELECT SessionTimeOut from MST_Parameter");
			
				resultSet = pstmt.executeQuery();
				if(resultSet.next()) 
				{
												
					sessionTimeOut=resultSet.getInt("SessionTimeOut");
					
				}
				
				resultSet.close();
				pstmt.close();
				//----------
				
				if(usersWrapper.userGroup.equals("RIDER")){
					
					usersWrapper=(UsersWrapper)fetchRiderID(usersWrapper);
				}
				//--to get Driver userid using mobile/email
				if(usersWrapper.userGroup.equals("DRIVER")){
					
					usersWrapper=(UsersWrapper)fetchDriverID(usersWrapper);
				}
				
				pstmt = con.prepareStatement("SELECT Userid,Lastlogindate,Sessionid FROM Users WHERE Userid=?");
			
				pstmt.setString(1,usersWrapper.userid);
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					usersWrapper= new UsersWrapper();
					
					usersWrapper.userid=Utility.trim(resultSet.getString("Userid"));
					System.out.println("minLength " + usersWrapper.userid);
					
					usersWrapper.lastLoginDate=resultSet.getString("Lastlogindate");	
					
					usersWrapper.sessionid=Utility.trim(resultSet.getString("Sessionid"));
					
					usersWrapper.recordFound=true;
					
					/*System.out.println("Lastlogindate  "+usersWrapper.lastLoginDate);
					System.out.println("Time diff "+Utility.timeDifference(usersWrapper.lastLoginDate));
					System.out.println("SessionTimeOut "+sessionTimeOut);
					
					if(Utility.timeDifference(usersWrapper.lastLoginDate)>sessionTimeOut)
					{
						usersWrapper.sessionTimeOut=true;
						System.out.println("Session Timed Out ");
					}*/
					

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

		return usersWrapper;
	}
	
	public void updateSessionDetails(UsersWrapper usersWrapper,int noLoginRetry,String sessionid)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		//UsersWrapper usersWrapper=	new UsersWrapper();

		
		System.out.println("userid :"+ usersWrapper.userid);
		String sql=null;
		PreparedStatement pstmt = null;
		//String userGroup=null;

		try {
				con = getConnection();
				
				
				//--to get Rider userid using mobile/email
				if(usersWrapper.userGroup.equals("RIDER")){
					
					usersWrapper=(UsersWrapper)fetchRiderID(usersWrapper);
				}
				//--to get Driver userid using mobile/email
				if(usersWrapper.userGroup.equals("DRIVER")){
					
					usersWrapper=(UsersWrapper)fetchDriverID(usersWrapper);
				}
				//----- code--
				
				/*sql="SELECT UserGroup from Users WHERE Userid=?";
				
			
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1,userid.trim());
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
						
					userGroup=Utility.trim(resultSet.getString("UserGroup"));
					
				}
				
				resultSet.close();
				pstmt.close();*/
				
				//System.out.println("user group is "+userGroup.trim() );
				
				
				
				//----------do not update Device id for Staff and admin, update only for Students when not null
				//if(userGroup != null && userGroup.trim().equals("RIDER") && deviceToken !=null && !deviceToken.trim().equals(""))
				//{
					sql="UPDATE Users set  NoLoginRetry=?,sessionid=?,DeviceToken=?  where userid=?";
				//}
				//else
				//{
				//	sql="UPDATE Users set  NoLoginRetry=?,sessionid=?  where userid=?";
				//}
				
				pstmt = con.prepareStatement(sql);
				
				
				pstmt.setInt(1,noLoginRetry+1);
				pstmt.setString(2,(sessionid==null?"":sessionid.trim()));
				
				
				//if(userGroup != null && userGroup.trim().equals("RIDER") && deviceToken !=null && !deviceToken.trim().equals(""))
				//{
					pstmt.setString(3,Utility.trim(usersWrapper.deviceToken));
					pstmt.setString(4,Utility.trim(usersWrapper.userid));
				//}
				//else
				//{
				//	pstmt.setString(3,(userid==null?"":userid.trim()));
				//}
	
				pstmt.executeUpdate();
	
				pstmt.close();
				//usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
				//usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
				//usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());
	
				
	
				//usersWrapper.recordFound=true;
				
				System.out.println("user table session updated " );
			
			
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

		//return usersWrapper;
	}

	
}
