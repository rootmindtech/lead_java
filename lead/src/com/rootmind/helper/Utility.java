package com.rootmind.helper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class Utility {

	
	public final static java.sql.Date getDate(String strDate)
	{
		
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MMM-dd");
		
			SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			java.sql.Date returnDate=null;
			
			try {
				
				if(strDate!=null)
				{
					//System.out.println("In Utility getdate" +strDate);
					
					//strDate=strDate.replace("\"", "");
					
					strDate=strDate.replaceAll("/","-");
					
					
					//System.out.println("In Utility getdate after replace" +strDate);
					
					returnDate=new java.sql.Date(dmyFormat.parse(strDate.trim()).getTime());
					
					
					System.out.println(" Utility Returning getdate " +returnDate);
					
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnDate;
		
	}
	
	public final static String setDateAMPM(String strDate)
	{
		
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    SimpleDateFormat dmy = new SimpleDateFormat("dd-MMM-yyyy KK:mm a");
		
		String returnDate=null;
		Date date;
			
			try {
				
				if(strDate!=null)
				{
					//System.out.println("In Utility setDateAMPM" +strDate);
					
					date = ymd.parse(strDate);
					strDate = dmy.format(date);
					
					//System.out.println("In Utility setDateAMPM after format" +strDate);
					
					returnDate=strDate;
					
					System.out.println(" Utility Returning setDateAMPM " +returnDate);
					
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnDate;
		
	}
	
	public final static String setDateMMM(String strDate)
	{
		
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dmy = new SimpleDateFormat("dd-MMM-yyyy");
		
		String returnDate=null;
		Date date;
			
			try {
				
				if(strDate!=null)
				{
					//System.out.println("In Utility setDateMMM" +strDate);
					
					date = ymd.parse(strDate);
					strDate = dmy.format(date);
					
					//System.out.println("In Utility setDateMMM after format" +strDate);
					
					returnDate=strDate;
					
					System.out.println(" Utility Returning setDateMMM " +returnDate);
					
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnDate;
		
	}
	public final static String setDate(String strDate)
	{
		
		
			SimpleDateFormat ymd = new SimpleDateFormat("yyyy-mm-dd");
		    SimpleDateFormat dmy = new SimpleDateFormat("dd-mm-yyyy");
			
			String returnDate=null;
			Date date;
			try {
				
				if(strDate!=null)
				{
					//System.out.println("In Utility setDate" +strDate);
					
					date = ymd.parse(strDate);
					strDate = dmy.format(date);
					
					//System.out.println("In Utility setDate after format" +strDate);
					
					returnDate=strDate.replaceAll("-","/");
					
					System.out.println("Utility Returning setDate " +returnDate);
					
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnDate;
		
	}
	
	
	
	public final static int dateDifference(String strDate)
	{
		
	
		int age=0;
		
			try {
				
				if(strDate!=null)
				{
					
					Calendar calDOB = Calendar.getInstance();
					calDOB.setTime(Utility.getDate(strDate));
					//System.out.println("calDOB year "+calDOB.get(Calendar.YEAR));
					//System.out.println("calDOB month "+calDOB.get(Calendar.MONTH));
					//System.out.println("calDOB day "+calDOB.get(Calendar.DAY_OF_MONTH));

					//System.out.println("System Today date "+ new java.sql.Timestamp(System.currentTimeMillis()));
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(new java.sql.Timestamp(System.currentTimeMillis()));
					
					
					//System.out.println("year "+cal.get(Calendar.YEAR));
					//System.out.println("month "+cal.get(Calendar.MONTH));
					//System.out.println("day "+cal.get(Calendar.DAY_OF_MONTH));

					Calendar start = Calendar.getInstance();
					Calendar end = Calendar.getInstance();
					start.set(calDOB.get(Calendar.YEAR),calDOB.get(Calendar.MONTH), calDOB.get(Calendar.DAY_OF_MONTH));
					end.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					Date startDate = start.getTime();
					Date endDate = end.getTime();
					
					long startTime = startDate.getTime();
					long endTime = endDate.getTime();
					long diffTime = endTime - startTime;
					long diffDays = diffTime / (1000 * 60 * 60 * 24);
					//System.out.println("diffTime is "+diffTime);
					//System.out.println("diffDays is "+diffDays);
					age = Math.round(diffDays /365);
					
					System.out.println("Age is "+age);
					//DateFormat dateFormat = DateFormat.getDateInstance();
				/*	//System.out.println("The difference between "+
					  dateFormat.format(startDate)+" and "+
					  dateFormat.format(endDate)+" is "+
					  diffDays+" days.");*/
					
					
				}
			
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return age;
		
	}
	public final static int timeDifference(String strDate)
	{
		
	
		int timeDiff=0;
		
			try {
				
				if(strDate!=null)
				{
					
					Calendar calDOB = Calendar.getInstance();
					calDOB.setTime(Utility.getDate(strDate));
					//System.out.println("calDOB year "+calDOB.get(Calendar.YEAR));
					//System.out.println("calDOB month "+calDOB.get(Calendar.MONTH));
					//System.out.println("calDOB day "+calDOB.get(Calendar.DAY_OF_MONTH));

					//System.out.println("System Today date "+ new java.sql.Timestamp(System.currentTimeMillis()));
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(new java.sql.Timestamp(System.currentTimeMillis()));
					
					
					//System.out.println("year "+cal.get(Calendar.YEAR));
					//System.out.println("month "+cal.get(Calendar.MONTH));
					//System.out.println("day "+cal.get(Calendar.DAY_OF_MONTH));

					Calendar start = Calendar.getInstance();
					Calendar end = Calendar.getInstance();
					start.set(calDOB.get(Calendar.YEAR),calDOB.get(Calendar.MONTH), calDOB.get(Calendar.DAY_OF_MONTH));
					end.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					Date startDate = start.getTime();
					Date endDate = end.getTime();
					
					long startTime = startDate.getTime();
					long endTime = endDate.getTime();
					long diffTime = endTime - startTime;
					
					//long diffDays = diffTime / (1000 * 60 * 60 * 24);
					//System.out.println("diffTime is "+diffTime);
					//System.out.println("diffDays is "+diffDays);
					//age = Math.round(diffDays /365);
					
					timeDiff=(int)diffTime;
					System.out.println("Time difference is "+timeDiff);
				
					
				}
			
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return timeDiff;
		
	}
	public final static java.sql.Timestamp getCurrentTime()
	{
		
		return new java.sql.Timestamp(System.currentTimeMillis() + (90*60*1000)); //add one and half hour for India time
		
	}

	public final static String trim(String strInput)
	{
		
		return (strInput==null?null:strInput.trim());
		
	}
	
	
	public final static BigDecimal trim(BigDecimal input)
	{
		/*DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);*/
		
		return (input==null?BigDecimal.ZERO:input);
		
	}
	public final static String replace(String input)
	{
		/*DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);*/
		
		return (input.replace("\"", ""));
		
	}
	
	public final static boolean isEmpty(String input) {
		
		if(input==null)
		{
			return true;
		}
		else if(input.trim().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    System.out.println("lat1 " + lat1);
	    System.out.println("lon1 " + lon1);
	    System.out.println("lat2 " + lat2);
	    System.out.println("lon2 " + lon2);
	    
	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    System.out.println("distance in function " + Math.sqrt(distance));

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);
	    
	    System.out.println("distance in function1 " + Math.sqrt(distance));

	    return Math.sqrt(distance);
	}

}
