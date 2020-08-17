package com.rootmind.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;




import com.rootmind.helper.FileUploadHelper;




import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.FileUploadWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class FileUploadController  {
	
	
		public AbstractWrapper dataFileProcess(UsersWrapper usersProfileWrapper,FileUploadWrapper fileUploadWrapper)throws Exception {
			
			
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			formatter.applyPattern("###,###,###,##0.00");
			formatter.setDecimalFormatSymbols(symbols);
			
			//Date currentDate=new Date();
			//String strCurrentDate=dmyFormat.format(currentDate);
	       
			//Vector<Object> vector = new Vector<Object>();
			//ErrorWrapper errorWrapper=null;
			
			try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(fileUploadWrapper.destinationPath));
					FileUploadHelper fileUploadHelper=new FileUploadHelper();
					String sCurrentLine=null;
					String firstLine=null;
					String [] templateArray =null;
					
					firstLine=bufferedReader.readLine();
					String [] firstlineArray =firstLine.split("\\" + fileUploadWrapper.fileSeperator);
					System.out.println("firstlineArray "+Arrays.toString(firstlineArray));
					
					if(fileUploadWrapper.fileTemplateID.trim().equals("MARKSUPD01"))
					{
						templateArray =new String[]{"RefNo", "StudentID", "GradeID", "SectionID", "AcademicYearID", "TermID", "SubjectID", "TargetMarks", "SecuredMarks", "Percentage", "RankID", "GroupTerm", "Grade"};
					}
					else if(fileUploadWrapper.fileTemplateID.trim().equals("ATENDUPD02")){
						
						templateArray=new String[]{"RefNo", "AcademicYearID", "StudentID", "GradeID", "SectionID", "CalendarDate", "MorningStatus", "EveningStatus"};
					}
					
					
					
					if( Arrays.equals(firstlineArray, templateArray))
					{	
						
						while ((sCurrentLine = bufferedReader.readLine()) != null) {
							
							System.out.println("sCurrentLine "+sCurrentLine);
						
							//System.out.println(Arrays.asList(Pattern.compile("\\|").split(sCurrentLine)));
							
							String [] lineData = sCurrentLine.split("\\" + fileUploadWrapper.fileSeperator);
							
							System.out.println("Controller Before insert "+fileUploadWrapper.fileTemplateID.trim());
							
							if(fileUploadWrapper.fileTemplateID.trim().equals("MARKSUPD01"))
							{
								fileUploadHelper.insertStudentAcademicsTemp(usersProfileWrapper,lineData);
							}
							if(fileUploadWrapper.fileTemplateID.trim().equals("ATENDUPD02"))
							{
								System.out.println("Controller dataFileProcess Attendance  " +fileUploadWrapper.fileTemplateID.trim());
								
								fileUploadHelper.insertStudentAttendanceTemp(usersProfileWrapper,lineData);
							}
							
							System.out.println("Controller After insert");
							
						}
						bufferedReader.close();
						
						fileUploadWrapper.recordFound=true;
						
					}
					else{
						
						
						fileUploadWrapper.invalidFileFormat=true;
						
						
					}
					
					dataArrayWrapper.fileUploadWrapper=new FileUploadWrapper[1];
					dataArrayWrapper.fileUploadWrapper[0]=fileUploadWrapper;
					dataArrayWrapper.recordFound=true;
					
					
				
			} 
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				/*try
				{
					
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}*/
			}
	
			return dataArrayWrapper;
		}
		
		public AbstractWrapper dataFileUploadView(UsersWrapper usersProfileWrapper,FileUploadWrapper fileUploadWrapper)throws Exception {
		
		
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			formatter.applyPattern("###,###,###,##0.00");
			formatter.setDecimalFormatSymbols(symbols);
			
			//Date currentDate=new Date();
			//String strCurrentDate=dmyFormat.format(currentDate);
	       
			//Vector<Object> vector = new Vector<Object>();
			//ErrorWrapper errorWrapper=null;
			
			try {
				
						FileUploadHelper fileUploadHelper=new FileUploadHelper();
				        
			        	System.out.println("fetch dataFileUploadView Temp   " +fileUploadWrapper.fileTemplateID.trim());
						
						if(fileUploadWrapper.fileTemplateID.trim().equals("MARKSUPD01"))
						{
							
			        		dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.fetchStudentAcademicsTemp();
		
						}
						if(fileUploadWrapper.fileTemplateID.trim().equals("ATENDUPD02"))
						{
							
			        		dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.fetchStudentAttendanceTemp();
		
						}
						

					dataArrayWrapper.fileUploadWrapper=new FileUploadWrapper[1];
					dataArrayWrapper.fileUploadWrapper[0]=fileUploadWrapper;
					dataArrayWrapper.recordFound=true;
					
					
				
			} 
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				/*try
				{
					
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}*/
			}
	
			return dataArrayWrapper;
		}
		//-----data file upload delete--------
		
		public AbstractWrapper dataFileUploadDelete(UsersWrapper usersProfileWrapper,FileUploadWrapper fileUploadWrapper)throws Exception {
			
			
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		
			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			formatter.applyPattern("###,###,###,##0.00");
			formatter.setDecimalFormatSymbols(symbols);
			
			//Date currentDate=new Date();
			//String strCurrentDate=dmyFormat.format(currentDate);
	       
			//Vector<Object> vector = new Vector<Object>();
			//ErrorWrapper errorWrapper=null;
			
			try {
				
						FileUploadHelper fileUploadHelper=new FileUploadHelper();
				        
			        	System.out.println("fetch dataFileUploadDelete Temp   " +fileUploadWrapper.fileTemplateID.trim());
						
						if(fileUploadWrapper.fileTemplateID.trim().equals("MARKSUPD01"))
						{
							
			        		dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.deleteStudentAcademicsTemp();
		
						}
						if(fileUploadWrapper.fileTemplateID.trim().equals("ATENDUPD02"))
						{
							
			        		dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.deleteStudentAttendanceTemp();
		
						}
						

					dataArrayWrapper.fileUploadWrapper=new FileUploadWrapper[1];
					dataArrayWrapper.fileUploadWrapper[0]=fileUploadWrapper;
					dataArrayWrapper.recordFound=true;
					
					
				
			} 
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				/*try
				{
					
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}*/
			}
	
			return dataArrayWrapper;
		}
		
		//-----end data file upload delete----
		
//		public AbstractWrapper updateStudentAttendanceUpload(UsersWrapper usersProfileWrapper,StudentAttendanceWrapper[] studentAttendanceWrapperArray)throws Exception {
//			
//			
//			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//			
//			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
//		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
//			
//			//Date currentDate=new Date();
//			//String strCurrentDate=dmyFormat.format(currentDate);
//	       
//			//Vector<Object> vector = new Vector<Object>();
//			//ErrorWrapper errorWrapper=null;
//			
//			try {
//				
//					StudentAttendanceHelper studentAttendanceHelper=new StudentAttendanceHelper(); 
//					FileUploadHelper fileUploadHelper=new FileUploadHelper();
//			        
//	        		dataArrayWrapper = (DataArrayWrapper)studentAttendanceHelper.updateStudentAttendance(usersProfileWrapper,studentAttendanceWrapperArray);
//	        		
//	        		dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.insertStudentAttendanceTempHistory();
//	        		
//					
//					
//					
//				
//			} 
//			 catch (Exception ex) {
//				ex.printStackTrace();
//				throw new Exception(ex.getMessage());
//			}
//			finally
//			{
//				/*try
//				{
//					
//				} 
//				catch (Exception ex)
//				{
//					ex.printStackTrace();
//					throw new Exception(ex.getMessage());
//				}*/
//			}
//
//			return dataArrayWrapper;
//		}
//		
//		public AbstractWrapper updateStudentMarksUpload(UsersWrapper usersProfileWrapper,StudentAcademicsWrapper[] studentAcademicsWrapperArray)throws Exception {
//			
//			
//			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//			
//			
//			
//			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
//		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
//			
//			//Date currentDate=new Date();
//			//String strCurrentDate=dmyFormat.format(currentDate);
//	       
//			//Vector<Object> vector = new Vector<Object>();
//			//ErrorWrapper errorWrapper=null;
//			
//			try {
//				
//						
//	        		StudentMarksController studentMarksController=new StudentMarksController();
//	        		FileUploadHelper fileUploadHelper=new FileUploadHelper();
//		        	dataArrayWrapper = (DataArrayWrapper)studentMarksController.validate(usersProfileWrapper,studentAcademicsWrapperArray);
//		        	dataArrayWrapper = (DataArrayWrapper)fileUploadHelper.insertStudentAcademicsTempHistory();
//		        	
//					
//					
//				
//			} 
//			 catch (Exception ex) {
//				ex.printStackTrace();
//				throw new Exception(ex.getMessage());
//			}
//			finally
//			{
//				/*try
//				{
//					
//				} 
//				catch (Exception ex)
//				{
//					ex.printStackTrace();
//					throw new Exception(ex.getMessage());
//				}*/
//			}
//
//			return dataArrayWrapper;
//		}

}
