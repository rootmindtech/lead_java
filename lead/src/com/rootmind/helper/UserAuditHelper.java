package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class UserAuditHelper extends Helper {
	
//	public  Connection getConnection1() throws SQLException, NamingException, Exception{
//		Connection con=null;
//		try{
//			Context initialContext = new InitialContext();
//			String applicationId="USERAUDIT"; 
//			
//			//initialContext = (Context)initialContext.lookup("java:comp/env");
//			
//			DataSource dataSource = (DataSource)initialContext.lookup("java:/" + applicationId + "DSN");
//			con = dataSource.getConnection();
//		}
//		catch(SQLException se)
//		{
//			se.printStackTrace();
//			throw new SQLException(se.getSQLState()+ " " + se.getMessage());
//		}
//		catch(NamingException ne)
//		{
//			ne.printStackTrace();
//			throw new SQLException(ne.getMessage());
//		}
//		
//		catch(Exception se)
//		{
//			se.printStackTrace();
//			throw new Exception(se.getMessage());
//		}
//				return con; 		
//	}
//	
	
	public void releaseConnection(ResultSet rs,Connection con)throws SQLException{
		if(rs!=null)
		{
			rs.close();
		}
		if(con!=null)
		{
			con.close();
		}

	}
	
	public AbstractWrapper updateUserAudit(String userid, String sessionid,String activity,String screenName,String message)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		UsersWrapper usersWrapper=	new UsersWrapper();

		
		System.out.println("Nowcabs user audit userid :"+ userid);

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
	
				/*UsersWrapper tmpUsersWrapper=	new UsersWrapper();
				tmpUsersWrapper=(UsersWrapper)getCIFNumber(userid);
				
				pstmt = con.prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=? where userid=?");
				
				pstmt.setTimestamp(1,Utility.getCurrentTime());
				pstmt.setInt(2,tmpUsersWrapper.noLoginRetry+1);
				pstmt.setString(3,userid.trim());
	
				pstmt.executeUpdate();
	
				pstmt.close();*/
				
				usersWrapper.recordFound=true;
				
				System.out.println("Nowcabs user audit table updated " );
			
			
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

	
	

}
