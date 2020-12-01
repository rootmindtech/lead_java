package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
//import java.util.Arrays;
//import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.PopoverWrapper;

public class PopoverHelper extends Helper {
	
	public AbstractWrapper fetchPopoverData(PopoverWrapper popoverWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		String sql=null;
		String filter=null;
		String tableName=null;
		DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
		Vector<Object> vector = new Vector<Object>();

	
		try {
				con = getConnection();
				
				filter=popoverWrapper.filter;
				tableName=popoverWrapper.tableName;
				
				System.out.println("fetchPopoverData in try Table name is " + popoverWrapper.tableName);
				
				
				//sql="SELECT Code, Description FROM [" + popoverWrapper.tableName + "]";
				sql="SELECT Code, Description FROM " + popoverWrapper.tableName + "";
				
				if(filter!=null && !filter.isEmpty())
				{
					//sql="SELECT Code, Description,Filter FROM [" + popoverWrapper.tableName + "] WHERE Filter=?";
					
					sql="SELECT Code, Description,Filter FROM " + popoverWrapper.tableName + " WHERE Filter=?";
				}
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				if(filter!=null && !filter.isEmpty())
				{
					pstmt.setString(1, filter.trim());
				}
				//pstmt.setString(1,tableName);
						
				
				//System.out.println("SQL statement is " + pstmt);
				
				
			
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					
					popoverWrapper=new PopoverWrapper();
					
					popoverWrapper.code=Utility.trim(resultSet.getString("Code"));
					
					//System.out.println("Popover Code " + popoverWrapper.code);
	
					popoverWrapper.desc=Utility.trim(resultSet.getString("Description"));
					//System.out.println("Popover  Desc" + popoverWrapper.desc);
					
					if(filter!=null && !filter.isEmpty())
					{
						popoverWrapper.filter=Utility.trim(resultSet.getString("Filter"));
					}
					
					popoverWrapper.tableName=tableName;
					popoverWrapper.recordFound=true;
				
					
					
					vector.addElement(popoverWrapper);
					
	
				}
				
				if (vector.size()>0)
				{
					
					
					dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.popoverWrapper);
					dataArrayWrapper.recordFound=true;
	
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				resultSet.close();
				pstmt.close();
				
				System.out.println("fetchPopover Data completed " );

			
			
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
	
	
	
	//--------------updatePopoverWrapper data-------------
	
	public AbstractWrapper updateMasterData(PopoverWrapper popoverWrapper)throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		//String sql=null;
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		PreparedStatement pstmt=null;
		
		String sql=null;
		
		
		try {
				con = getConnection();
				
				//pstmt = con.prepareStatement("SELECT Code FROM [" + popoverWrapper.tableName + "] WHERE Code=?");
				
				pstmt = con.prepareStatement("SELECT Code FROM " + popoverWrapper.tableName + " WHERE Code=?");
				
				pstmt.setString(1,popoverWrapper.code.trim());
				
				resultSet = pstmt.executeQuery();
				if (!resultSet.next()) 
				{
					resultSet.close();
					pstmt.close();

					System.out.println("Popover insert table "+popoverWrapper.tableName);
					//sql="INSERT INTO [" + popoverWrapper.tableName + "] (Code,Description) VALUES(?,?)";
					
					sql="INSERT INTO " + popoverWrapper.tableName + " (Code,Description) VALUES(?,?)";
					
					if(popoverWrapper.filter!=null && !popoverWrapper.filter.equals(""))	
					{
						
						//sql="INSERT INTO [" + popoverWrapper.tableName + "](Code,Description,Filter) VALUES(?,?,?)";
						sql="INSERT INTO " + popoverWrapper.tableName + " (Code,Description,Filter) VALUES(?,?,?)";
					}
					
					pstmt = con.prepareStatement(sql);

					pstmt.setString(1,Utility.trim(popoverWrapper.code));
					pstmt.setString(2,Utility.trim(popoverWrapper.desc));
					
					if(popoverWrapper.filter!=null && !popoverWrapper.filter.equals(""))	
					{
						
						pstmt.setString(3,Utility.trim(popoverWrapper.filter)); 
					}
			

					pstmt.executeUpdate();
					pstmt.close();	
					
					
					
					popoverWrapper.recordFound=true;
					
					dataArrayWrapper.popoverWrapper=new PopoverWrapper[1];
					dataArrayWrapper.popoverWrapper[0]=popoverWrapper;

					dataArrayWrapper.recordFound=true;
					
					
					
					System.out.println("Successfully " +popoverWrapper.tableName+" inserted");
					
					
				}
				else
				{
				
						resultSet.close();
						pstmt.close();
						
						System.out.println("Popover update table "+popoverWrapper.tableName);
						//sql="UPDATE [" + popoverWrapper.tableName + "] SET Description=? WHERE Code=?";
						
						sql="UPDATE " + popoverWrapper.tableName + " SET Description=? WHERE Code=?";
						
						if(popoverWrapper.filter!=null && !popoverWrapper.filter.equals(""))	
						{
							
							//sql="UPDATE [" + popoverWrapper.tableName + "] SET Description=?, Filter=? WHERE Code=?";
							sql="UPDATE " + popoverWrapper.tableName + " SET Description=?, Filter=? WHERE Code=?";
						}
						
						pstmt = con.prepareStatement(sql);

						pstmt.setString(1,Utility.trim(popoverWrapper.desc));
						
						if(popoverWrapper.filter!=null && !popoverWrapper.filter.equals(""))	
						{
							pstmt.setString(2,Utility.trim(popoverWrapper.filter));
							pstmt.setString(3,Utility.trim(popoverWrapper.code)); 
						}
						else
						{
							pstmt.setString(2,Utility.trim(popoverWrapper.code));
						}
		
						
						
						pstmt.executeUpdate();
						pstmt.close();
						
						
						popoverWrapper.recordFound=true;
						
						
						
						dataArrayWrapper.popoverWrapper=new PopoverWrapper[1];
						dataArrayWrapper.popoverWrapper[0]=popoverWrapper;
		

						dataArrayWrapper.recordFound=true;
						
						
						System.out.println("Successfully " +popoverWrapper.tableName+" Updated");
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
	
	//--------------end updatePopoverWrapper data---------
	
	
	
	
	
		//--------------fetchMultiPopoverData-------
		public AbstractWrapper fetchMultiPopoverData(PopoverWrapper[] popoverWrapper)throws Exception {

			Connection con = null;
			ResultSet resultSet = null;
			
			String sql=null;
			String filter=null;
			String tableName=null;
			DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
			
			Vector<Object> vector = new Vector<Object>();
			PopoverWrapper recordPopoverWrapper=null;
			
		
		
			try {
					con = getConnection();
					PreparedStatement pstmt=null;
					for(int i=0;i<=popoverWrapper.length-1;i++){
						
						//System.out.println("popoverWrapper.length " + popoverWrapper.length);
					
								filter="";
								tableName="";
							
								filter=popoverWrapper[i].filter;
								tableName=popoverWrapper[i].tableName;
								
								System.out.println("fetchMultiPopoverData in try Table name is " + popoverWrapper[i].tableName);
								
								
								//sql="SELECT Code, Description FROM [" + popoverWrapper[i].tableName + "]";
								
								sql="SELECT Code, Description FROM " + popoverWrapper[i].tableName + "";
								
								//System.out.println("fetchMultiPopoverData SQL is " + sql);
								
								if(filter!=null && !filter.equals(""))
								{
									//sql="SELECT Code, Description,Filter FROM [" + popoverWrapper[i].tableName + "] WHERE Filter=?";
									 sql="SELECT Code, Description,Filter FROM " + popoverWrapper[i].tableName + " WHERE Filter=?";
									 
									 
								}
								
								pstmt = con.prepareStatement(sql);
								
								if(filter!=null && !filter.equals(""))
								{
									pstmt.setString(1, filter.trim());
								}
								//pstmt.setString(1,tableName);
										
								
								//System.out.println("SQL statement is " + pstmt);
								
								
							
								resultSet = pstmt.executeQuery();
								while (resultSet.next()) 
								{
									
									recordPopoverWrapper=new PopoverWrapper();
									
									recordPopoverWrapper.code=Utility.trim(resultSet.getString("Code"));
									
									//System.out.println("Popover Code " + recordPopoverWrapper.code);
				
									recordPopoverWrapper.desc=Utility.trim(resultSet.getString("Description"));
									//System.out.println("Popover  Desc" + recordPopoverWrapper.desc);
									
									if(filter!=null && !filter.equals(""))
									{
										recordPopoverWrapper.filter=Utility.trim(resultSet.getString("Filter"));
									}
									
									recordPopoverWrapper.tableName=tableName;
									recordPopoverWrapper.recordFound=true;
								
									
									vector.addElement(recordPopoverWrapper);
									
				
								}
	
								resultSet.close();
								pstmt.close();
					}
					if (vector.size()>0)
					{
						
						
						dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
						vector.copyInto(dataArrayWrapper.popoverWrapper);
						dataArrayWrapper.recordFound=true;
	
						System.out.println("total master data in fetch " + vector.size());
	
					}
					
					
					System.out.println("fetch MultiPopover Data completed " );

				
				
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
		
	
	
	
	
	public String fetchPopoverDesc(String code,String tableName)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		String desc=null;
		
	
		try {
				con = getConnection();
				
				System.out.println("fetchPopoverDesc in try Table name is" + tableName);
				
				
				
				//PreparedStatement pstmt = con.prepareStatement("SELECT Code, Description FROM [" + tableName + "] WHERE Code=?");
				
				PreparedStatement pstmt = con.prepareStatement("SELECT Code, Description FROM " + tableName + " WHERE Code=?");
				//pstmt.setString(1,tableName);
						
				pstmt.setString(1,Utility.trim(code));
				
				//System.out.println("SQL statement is " + pstmt);
				
				
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
					
					
	
					desc=Utility.trim(resultSet.getString("Description"));
					//System.out.println("Desc" + desc);

					System.out.println("fetchPopover Desc completed " );
					
					
					
	
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

		return desc;
	}

	
	
	public AbstractWrapper fetchTableNames(PopoverWrapper popoverWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		String sql=null;
		
		DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
		Vector<Object> vector = new Vector<Object>();

	
		try {
				con = getConnection();
				
				sql="SELECT Code, Description, Filter FROM MST_TableNames";
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
			
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					
					popoverWrapper=new PopoverWrapper();
					
					popoverWrapper.code=Utility.trim(resultSet.getString("Code"));
					
					//System.out.println("Popover Code " + popoverWrapper.code);
	
					popoverWrapper.desc=Utility.trim(resultSet.getString("Description"));
					//System.out.println("Popover  Desc" + popoverWrapper.desc);
					
					popoverWrapper.tableFilter=Utility.trim(resultSet.getString("Filter"));
				
					popoverWrapper.recordFound=true;
				
					vector.addElement(popoverWrapper);
					
	
				}
				
				if (vector.size()>0)
				{
					
					
					dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.popoverWrapper);
					dataArrayWrapper.recordFound=true;
	
					System.out.println("total trn. in fetchTableNames " + vector.size());
	
				}
				resultSet.close();
				pstmt.close();
				
				System.out.println("Popover info  fetchTableNames completed " );

			
			
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
	
	
	public AbstractWrapper fetchMasterData(PopoverWrapper popoverWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		String sql=null;
		String filter=null;
		String tableName=null;
		DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
		
		Vector<Object> vector = new Vector<Object>();

	
		try {
				con = getConnection();
				
				filter=popoverWrapper.filter;
				tableName=popoverWrapper.tableName;
				
				System.out.println("fetchMasterData in try Table name is " + popoverWrapper.tableName);
				
				
				//sql="SELECT Code, Description FROM [" + popoverWrapper.tableName + "]";
				
				sql="SELECT Code, Description FROM " + popoverWrapper.tableName + "";
				
				//System.out.println("SQL statement is " + sql);
				
				if(filter!=null && filter.equals("Y"))
				{
					//sql="SELECT Code, Description,Filter FROM [" + popoverWrapper.tableName + "] ";
					sql="SELECT Code, Description,Filter FROM " + popoverWrapper.tableName + " ";
				}
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				
				//pstmt.setString(1,tableName);
						
				
				//System.out.println("SQL statement is " + pstmt);
				
				
			
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					
					popoverWrapper=new PopoverWrapper();
					
					popoverWrapper.code=Utility.trim(resultSet.getString("Code"));
					
					//System.out.println("Popover Code " + popoverWrapper.code);
	
					popoverWrapper.desc=Utility.trim(resultSet.getString("Description"));
					//System.out.println("Popover  Desc" + popoverWrapper.desc);
					
					if(filter!=null && filter.equals("Y"))
					{
						popoverWrapper.filter=Utility.trim(resultSet.getString("Filter"));
					}
					
					popoverWrapper.tableName=tableName;
					popoverWrapper.recordFound=true;
				
					
					
					vector.addElement(popoverWrapper);
					
	
				}
				
				if (vector.size()>0)
				{
					
					
					dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.popoverWrapper);
					dataArrayWrapper.recordFound=true;
	
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				resultSet.close();
				pstmt.close();
				
				System.out.println("Popover info  fetchMasterData completed " );

			
			
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
	
	

}
