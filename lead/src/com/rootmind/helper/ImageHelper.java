package com.rootmind.helper;

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

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ImageWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ImageHelper extends Helper {
	
	public AbstractWrapper updateImage(UsersWrapper userProfileWrapper, ImageWrapper imageWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql=null;
		//int fileLength;
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
//	
		//File file=new File(imageWrapper.imageFile);
		//FileInputStream fis = new FileInputStream(file);
        //fileLength = (int)file.length();
        //System.out.println("Image File Length "+fileLength);

		try {
			
				if(imageWrapper.imageUploadStatus==true)
				{
					con = getConnection();
					
					ImageWrapper verifyImageWrapper = (ImageWrapper)verifyImage(imageWrapper);
					
					if(verifyImageWrapper.recordFound==false)
					{
		
						sql="INSERT INTO Images(RiderRefNo, RiderID, ImageID, ImageName, ImageFolder, Status, MakerID, MakerDateTime) "
								+ " Values(?,?,?,?,?,?,?,?)";
		
		
						System.out.println("sql " + sql);
						System.out.println("Image File Name "+imageWrapper.imageName);
	
						PreparedStatement pstmt = con.prepareStatement(sql);
		
						pstmt.setString(1,Utility.trim(imageWrapper.riderRefNo));
						pstmt.setString(2,Utility.trim(imageWrapper.riderID));
						pstmt.setString(3,Utility.trim(imageWrapper.imageID));
						pstmt.setString(4,Utility.trim(imageWrapper.imageName));
						pstmt.setString(5,Utility.trim(imageWrapper.imageFolder));
						pstmt.setString(6,Utility.trim(imageWrapper.status));
						pstmt.setString(7,Utility.trim(userProfileWrapper.userid));
						pstmt.setTimestamp(8,Utility.getCurrentTime()); //uploaddateTime
	
						pstmt.executeUpdate();
						pstmt.close();
		
						imageWrapper.recordFound=true;
					}
					else
					{

						sql="UPDATE Images set ImageName=?, ImageFolder=?, Status=?, ModifierID=?, ModifierDateTime=? " 
								+ " WHERE RiderRefNo=? AND RiderID=? AND ImageID=?";
		
		
						System.out.println("sql " + sql);
						System.out.println("Image File Name "+imageWrapper.imageName);
	
						PreparedStatement pstmt = con.prepareStatement(sql);
		
						pstmt.setString(1,Utility.trim(imageWrapper.imageName));
						pstmt.setString(2,Utility.trim(imageWrapper.imageFolder));
						pstmt.setString(3,Utility.trim(imageWrapper.status));
						pstmt.setString(4,Utility.trim(userProfileWrapper.userid));
						pstmt.setTimestamp(5,Utility.getCurrentTime()); //uploaddateTime
						pstmt.setString(6,Utility.trim(imageWrapper.riderRefNo));
						pstmt.setString(7,Utility.trim(imageWrapper.riderID));
						pstmt.setString(8,Utility.trim(imageWrapper.imageID));
	
						pstmt.executeUpdate();
						pstmt.close();
		
						imageWrapper.recordFound=true;

					}
				}
				
				
				dataArrayWrapper.imageWrapper=new ImageWrapper[1];
				dataArrayWrapper.imageWrapper[0]=imageWrapper;
				dataArrayWrapper.recordFound=true;
				
				
				System.out.println("Image uploaded successflly");
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
	
/*	public AbstractWrapper updateImageDetails(ImageDetailsWrapper imageWrapper)throws Exception {

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
		
	
		File file=new File(imageWrapper.imageFile);
		FileInputStream fis = new FileInputStream(file);
        int fileLength = (int)file.length();

		
		
		System.out.println("Update Image Details");
		
		
		try {
			con = getConnection();
			
			PreparedStatement pstmt = con.prepareStatement("UPDATE StudentImages SET RefNo=?, CIFNumber=?, ImageId=?, ImageFile=?, ModifiedUserId=?, ModifiedDateTime=? where RefNo=?");
			
			pstmt.setString(1,Utility.trim(imageWrapper.refNo));
			pstmt.setString(2,Utility.trim(imageWrapper.cifNumber));
			pstmt.setString(3,Utility.trim(imageWrapper.imageId));
			pstmt.setBinaryStream(4, fis, fileLength);
			pstmt.setString(5,Utility.trim(imageWrapper.modifiedUserId));
			pstmt.setTimestamp(6,new java.sql.Timestamp(System.currentTimeMillis())); //modifiedDateTime
			pstmt.setString(7,Utility.trim(imageWrapper.refNo));
			
			pstmt.executeUpdate();

			pstmt.close();
			
			imageWrapper.recordFound=true;
			
			dataArrayWrapper.imageWrapper=new ImageDetailsWrapper[1];
			dataArrayWrapper.imageWrapper[0]=imageWrapper;
			
			System.out.println("Image details updated successfully" );
			
			
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
	public AbstractWrapper fetchImage(ImageWrapper imageWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		Vector<Object> vector = new Vector<Object>();

		//byte[] fileBytes;
		boolean imageFoundStatus=imageWrapper.imageFoundStatus;
	
	
		try {
//				String imagesPath=fetchImagePath();	
//				PopoverHelper popoverHelper = new PopoverHelper();
				
				con = getConnection();
				String sql="SELECT RiderRefNo, RiderID, ImageID, ImageName, ImageFolder, Status "
						+ " FROM Images WHERE RiderRefNo=? AND RiderID=? ";
				
			
				if(!Utility.isEmpty(imageWrapper.imageID))
				{
					sql=sql + " AND ImageID=?";
				}
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				System.out.println("Image Details RefNo is" + imageWrapper.riderRefNo);
				
				pstmt.setString(1,Utility.trim(imageWrapper.riderRefNo));
				pstmt.setString(2, Utility.trim(imageWrapper.riderID));	
				
				if(!Utility.isEmpty(imageWrapper.imageID))
				{
					pstmt.setString(3, Utility.trim(imageWrapper.imageID));
					
				}
				
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					
					imageWrapper= new ImageWrapper();
					
					
					imageWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
					System.out.println("RefNo" + imageWrapper.riderRefNo);
					
	
					imageWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
					System.out.println("Riderid " + imageWrapper.riderID);
					
					imageWrapper.imageID=Utility.trim(resultSet.getString("ImageID"));
					
					imageWrapper.imageName=Utility.trim(resultSet.getString("ImageName"));
					imageWrapper.imageFolder=Utility.trim(resultSet.getString("ImageFolder"));
					imageWrapper.status=Utility.trim(resultSet.getString("Status"));

					imageWrapper.imageFoundStatus=imageFoundStatus;
					
					imageWrapper.recordFound=true;
					
					
			
					vector.addElement(imageWrapper);

			}
			
			if (vector.size()>0)
			{
				dataArrayWrapper.imageWrapper = new ImageWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.imageWrapper);
				dataArrayWrapper.recordFound=true;

				System.out.println("total trn. in fetch " + vector.size());

			}
 
			resultSet.close();
			pstmt.close();

			System.out.println("Fetch Image Details Successful");
			
			
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
	
	
	public AbstractWrapper verifyImage(ImageWrapper imageWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
		
//		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
//		Vector<Object> vector = new Vector<Object>();

		//byte[] fileBytes;
	
	
		boolean imageFoundStatus=imageWrapper.imageFoundStatus;
		
		try {
//				String imagesPath=fetchImagePath();	
//				PopoverHelper popoverHelper = new PopoverHelper();
				
				con = getConnection();
				String sql="SELECT RiderRefNo, RiderID, ImageID, ImageName, ImageFolder, Status "
						+ " FROM Images WHERE RiderRefNo=? AND RiderID=? AND ImageID=?";
				
				
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				System.out.println("Image Details RefNo is" + imageWrapper.riderRefNo);
				
				pstmt.setString(1,Utility.trim(imageWrapper.riderRefNo));
				pstmt.setString(2, Utility.trim(imageWrapper.riderID));	
				pstmt.setString(3, Utility.trim(imageWrapper.imageID));
				
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) 
				{
					
					imageWrapper.riderRefNo=Utility.trim(resultSet.getString("RiderRefNo"));
					System.out.println("RefNo" + imageWrapper.riderRefNo);
					
	
					imageWrapper.riderID=Utility.trim(resultSet.getString("RiderID"));
					System.out.println("Riderid " + imageWrapper.riderID);
					
					imageWrapper.imageID=Utility.trim(resultSet.getString("ImageID"));
					
					imageWrapper.imageName=Utility.trim(resultSet.getString("ImageName"));
					imageWrapper.imageFolder=Utility.trim(resultSet.getString("ImageFolder"));
					imageWrapper.status=Utility.trim(resultSet.getString("Status"));

					imageWrapper.imageFoundStatus=imageFoundStatus;
					
					imageWrapper.recordFound=true;
					
					

			}
			

			resultSet.close();
			pstmt.close();

			System.out.println("Fetch Image Details Successful");
			
			
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

		return imageWrapper;
	}	
	
	
//	public AbstractWrapper fetchImageFileNames(ImageWrapper imageWrapper)throws Exception {
//
//		Connection con = null;
//		ResultSet resultSet = null;
//		
//		
//		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//		
//		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
//		
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
//		
//		Vector<Object> vector = new Vector<Object>();
//
//		//byte[] fileBytes;
//
//		
//	
//		try {		
//				//PopoverHelper popoverHelper = new PopoverHelper();
//				
//				con = getConnection();
//				
//				String sql="SELECT RefNo, StudentID, ImageId, ImageFile, ImageFileName, ImageFileFolder, UploadUserId, UploadDateTime, "
//						+"ImageStatus,ModifiedUserId, ModifiedDateTime,a.DocID as DocID, b.Description as DocName  "  
//						+"FROM StudentImages a LEFT JOIN MST_DocChecklistMaster b ON a.DOCID=b.Code";
//				
//			
//				
//				if(imageWrapper.refNo!=null)
//				{
//					sql=sql + " WHERE RefNo=? and ImageStatus='ACTIVE'";
//				}
//				else
//				{
//					sql=sql + " WHERE StudentID=? and ImageStatus='ACTIVE'" ;
//				}
//				
//				PreparedStatement pstmt = con.prepareStatement(sql);
//				
//				System.out.println("Image Details RefNo is" + imageWrapper.refNo);
//				
//				if(imageWrapper.refNo!=null)
//				{
//					pstmt.setString(1,imageWrapper.refNo.trim());
//				}
//				else
//				{
//					pstmt.setString(1,imageWrapper.studentID.trim());
//				}
//					
//				
//				resultSet = pstmt.executeQuery();
//				
//				
//				while(resultSet.next()) 
//				{
//					
//					imageWrapper= new ImageWrapper();
//					
//					imageWrapper.refNo=Utility.trim(resultSet.getString("RefNo"));
//					System.out.println("RefNo" + imageWrapper.refNo);
//					
//	
//					imageWrapper.studentID=Utility.trim(resultSet.getString("StudentID"));
//					System.out.println("StudentID " + imageWrapper.studentID);
//					
//					imageWrapper.imageId=Utility.trim(resultSet.getString("ImageId"));
//					
//					//fileBytes = resultSet.getBytes("ImageFile");
//                    //OutputStream targetFile=  new FileOutputStream("D://RetriveImages//NewImageFromServer.JPG");
//
//                    //targetFile.write(fileBytes);
//                    //targetFile.close();
//
//					//imageWrapper.accountType=Utility.trim(resultSet.getString("AccountType"));
//					
//					imageWrapper.imageFileName=Utility.trim(resultSet.getString("ImageFileName"));
//					
//					imageWrapper.imageFileFolder=Utility.trim(resultSet.getString("ImageFileFolder"));
//					
//					imageWrapper.uploadUserId=Utility.trim(resultSet.getString("UploadUserId"));
//					
//					imageWrapper.uploadDateTime=Utility.trim(resultSet.getString("UploadDateTime"));
//					
//					imageWrapper.imageStatus=Utility.trim(resultSet.getString("ImageStatus"));
//					
//					imageWrapper.modifiedUserId=Utility.trim(resultSet.getString("ModifiedUserId"));
//					
//					imageWrapper.modifiedDateTime=Utility.trim(resultSet.getString("ModifiedDateTime"));	
//					imageWrapper.docID=Utility.trim(resultSet.getString("DocID"));	
//					
//					imageWrapper.docName=Utility.trim(resultSet.getString("DocName"));
//					
//					System.out.println("Doc Name " + imageWrapper.docName);
//					
//					//imageWrapper.docIDValue=popoverHelper.fetchPopoverDesc(imageWrapper.docID,"IMAGEDOC");
//					
//					//System.out.println("Fetch Image FileNames:docID "+imageWrapper.docID);
//					//System.out.println("Fetch Image FileNames:docIDValue "+imageWrapper.docIDValue);
//					
//					imageWrapper.imageFoundStatus=true;
//					imageWrapper.recordFound=true;
//			
//					vector.addElement(imageWrapper);
//
//			}
//			
//			if (vector.size()>0)
//			{
//				dataArrayWrapper.imageWrapper = new ImageWrapper[vector.size()];
//				vector.copyInto(dataArrayWrapper.imageWrapper);
//				dataArrayWrapper.recordFound=true;
//
//				System.out.println("total trn. in fetch " + vector.size());
//
//			}
//			else
//			{
//				dataArrayWrapper.imageWrapper = new ImageWrapper[1];
//				dataArrayWrapper.imageWrapper[0]= imageWrapper;
//				dataArrayWrapper.recordFound=true;
//				
//			}
// 
// 
//			resultSet.close();
//			pstmt.close();
//
//			System.out.println("Fetch Image FileNames Successful");
//			
//			
//		} catch (SQLException se) {
//			se.printStackTrace();
//			throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
//		} catch (NamingException ne) {
//			ne.printStackTrace();
//			throw new NamingException(ne.getMessage());
//		}
//		 catch (Exception ex) {
//			ex.printStackTrace();
//			throw new Exception(ex.getMessage());
//		}
//		finally
//		{
//			try
//			{
//				releaseConnection(resultSet, con);
//			} 
//			catch (SQLException se)
//			{
//				se.printStackTrace();
//				throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
//			}
//		}
//
//		return dataArrayWrapper;
//	}
//	
	
//	public AbstractWrapper updateImageStatus(ImageWrapper imageWrapper)throws Exception {
//
//		Connection con = null;
//		ResultSet resultSet = null;
//	
//
//		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//		//String sql=null;
//		
//		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
//	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
//		
//	
//		/*File file=new File(imageWrapper.imageFile);
//		FileInputStream fis = new FileInputStream(file);
//        int fileLength = (int)file.length();*/
//
//		
//		
//		System.out.println("Update Image Status");
//		
//		
//		try {
//				con = getConnection();
//				
//				PreparedStatement pstmt = con.prepareStatement("UPDATE StudentImages SET ImageStatus=? WHERE RefNo=? and ImageId=?");
//				
//				pstmt.setString(1,Utility.trim(imageWrapper.imageStatus));
//				pstmt.setString(2,Utility.trim(imageWrapper.refNo));
//				pstmt.setString(3,Utility.trim(imageWrapper.imageId));
//				
//				pstmt.executeUpdate();
//	
//				pstmt.close();
//				
//				imageWrapper.recordFound=true;
//				
//				dataArrayWrapper.imageWrapper=new ImageWrapper[1];
//				dataArrayWrapper.imageWrapper[0]=imageWrapper;
//				
//				System.out.println("Image Status updated successfully" );
//				
//			
//		} catch (SQLException se) {
//			se.printStackTrace();
//			throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
//		} catch (NamingException ne) {
//			ne.printStackTrace();
//			throw new NamingException(ne.getMessage());
//		}
//		 catch (Exception ex) {
//			ex.printStackTrace();
//			throw new Exception(ex.getMessage());
//		}
//		finally
//		{
//			try
//			{
//				releaseConnection(resultSet, con);
//			} 
//			catch (SQLException se)
//			{
//				se.printStackTrace();
//				throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
//			}
//		}
//
//		return dataArrayWrapper;
//	}
//	
	
	//---------------------- Start fetchImagePath-----------------
//	public String fetchImagePath()throws Exception {
//
//			Connection con = null;
//			ResultSet resultSet = null;
//			
//			
//			String imagePath=null;
//			
//			
//			try {
//					con=getConnection();
//					//-----country code--
//					
//					PreparedStatement pstmt = con.prepareStatement("SELECT ImagesPath from MST_Parameter");
//				
//					resultSet = pstmt.executeQuery();
//					if (resultSet.next()) 
//					{
//													
//						imagePath=resultSet.getString("ImagesPath");
//						
//						System.out.println("Images Path "+imagePath);
//						
//					}
//					
//					resultSet.close();
//					pstmt.close();
//					
//					//----------
//
//				
//			} catch (SQLException se) {
//				se.printStackTrace();
//				throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
//			} catch (NamingException ne) {
//				ne.printStackTrace();
//				throw new NamingException(ne.getMessage());
//			}
//			 catch (Exception ex) {
//				ex.printStackTrace();
//				throw new Exception(ex.getMessage());
//			}
//			finally
//			{
//				try
//				{
//					releaseConnection(resultSet, con);
//				} 
//				catch (SQLException se)
//				{
//					se.printStackTrace();
//					throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
//				}
//			}
//	
//			return imagePath;
//	}
//	//---------------------- End fetchImagePath-----------------
//	
//	public AbstractWrapper deleteImageDetails(ImageWrapper imageWrapper)throws Exception {
//
//		Connection con = null;
//		ResultSet resultSet = null;
//	
//
//		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//		//String sql=null;
//		
//		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
//	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
//		
//	
//		/*File file=new File(imageWrapper.imageFile);
//		FileInputStream fis = new FileInputStream(file);
//        int fileLength = (int)file.length();*/
//
//		
//		
//		System.out.println("Delete Image ");
//		
//		
//		try {
//				con = getConnection();
//				
//				PreparedStatement pstmt = con.prepareStatement("DELETE FROM BrideGroomImages WHERE RefNo=? and ImageId=?");
//				
//			
//				pstmt.setString(1,Utility.trim(imageWrapper.refNo));
//				pstmt.setString(2,Utility.trim(imageWrapper.imageId));
//				
//				pstmt.executeUpdate();
//	
//				pstmt.close();
//				
//				imageWrapper.imageDeleteStatus=true;
//				imageWrapper.recordFound=true;
//				
//				dataArrayWrapper.imageWrapper=new ImageWrapper[1];
//				dataArrayWrapper.imageWrapper[0]=imageWrapper;
//				
//				dataArrayWrapper.recordFound=true;
//				
//				System.out.println("Image deleted successfully" );
//				
//			
//		} catch (SQLException se) {
//			se.printStackTrace();
//			throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
//		} catch (NamingException ne) {
//			ne.printStackTrace();
//			throw new NamingException(ne.getMessage());
//		}
//		 catch (Exception ex) {
//			ex.printStackTrace();
//			throw new Exception(ex.getMessage());
//		}
//		finally
//		{
//			try
//			{
//				releaseConnection(resultSet, con);
//			} 
//			catch (SQLException se)
//			{
//				se.printStackTrace();
//				throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
//			}
//		}
//
//		return dataArrayWrapper;
//	}
//	

}
