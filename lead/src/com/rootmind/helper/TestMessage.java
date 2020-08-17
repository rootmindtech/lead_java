package com.rootmind.helper;

import java.text.SimpleDateFormat;

public class TestMessage {

	
	
	public void Test(){
		
		int studentId=25125;
		Double d =null;
		
		String percentDist=Double.toString((double)(Integer.parseInt("80")*((double)10/(double)100)));
		
		
		//System.out.println(" parseInt " + Integer.parseInt("80"));
		//System.out.println(" div " + ((double)10/(double)100));
		
		
		System.out.println(" doub " + Double.toString(Math.round(79.6)));
		
		d= new Double(Math.round(79.6));
		int i = d.intValue();
		
		String value=Integer.toString( (d.intValue()));
		
		System.out.println(" i " + i);
		System.out.println(" value " + value);
		
		
		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy");
		
		int paddingSize=6-String.valueOf(studentId).length();
		
		System.out.println("paddingSize =" + paddingSize);
		
		
		//System.out.println("Savings Account  " + studentProfileWrapper.accountType);
		
		//System.out.println("Savings Account " + studentProfileWrapper.accountType.substring(0,2));
		String schoolCode="SCH";
		
		String finalStudentID=schoolCode+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" +5 +"d",studentId);
		
		System.out.println("finalStudentID =" + finalStudentID);
		
	}
	
	public static void main(String args[])
	{
		
		new TestMessage().Test();
	}
	
}
