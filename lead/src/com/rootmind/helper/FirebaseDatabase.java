package com.rootmind.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

import javax.naming.NamingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.DriverLocationWrapper;
import com.rootmind.wrapper.DriverWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class FirebaseDatabase extends Helper{
	
	
	public AbstractWrapper getDriverLocations(UsersWrapper  usersProfileWrapper, DriverLocationWrapper driverLocationWrapper)throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql=null;
		Vector<Object> vector = new Vector<Object>();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt=null;
		
//		String gcmKey=null;
//		String gcmActivate=null;
//		String gcmURL=null;
		String frdbURL=null;
		
		String result = "";
		
		try {
				con = getConnection();
				
				//-----GCMKey GCMActivate code--
				
				sql="SELECT FRDBURL from MST_Parameter";
				
				pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
						
					frdbURL=Utility.trim(resultSet.getString("FRDBURL"));

				}
				
				resultSet.close();
				pstmt.close();
				
				//----------
				
				//*----------------get list of drivers available ----*//
					Gson gson = new GsonBuilder().create();
		  
					String locationURL=frdbURL + "/cabs/driver/location.json?print=pretty";
					String request        = locationURL;						
					URL    url            = new URL( request );
					HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
					conn.setDoOutput( true );
					conn.setInstanceFollowRedirects( false );
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json"); 
					conn.setRequestProperty("charset", "UTF-8");
					conn.setUseCaches(false);
			       
			        String line;
			        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
			        while ((line = reader.readLine()) != null) {
			            result += line;
			        }
			       
			        //writer.close();
			        reader.close();
			        System.out.println("result after GET driver location "+result);
					
			        DriverLocationWrapper subDriverLocationWrapper=null;
			        
			        
			        //parse driver locaiton json data received from firebase
	        		HashMap<String, DriverLocationWrapper> driverLocationJson = gson.fromJson(result, new TypeToken<HashMap<String, DriverLocationWrapper>>() {}.getType());
			    		        		
	        		for (Map.Entry<String, DriverLocationWrapper> entry : driverLocationJson.entrySet()) {
	        			
	        			 subDriverLocationWrapper= new DriverLocationWrapper();
	        			 subDriverLocationWrapper = entry.getValue();
	        			 subDriverLocationWrapper.driverRefNo=subDriverLocationWrapper.driverID; //this is written temporarily since refno is not null

	        			 //add additional info
	        			 subDriverLocationWrapper.riderRefNo=driverLocationWrapper.riderRefNo;
	        			 subDriverLocationWrapper.riderID=driverLocationWrapper.riderID;
	        			 subDriverLocationWrapper.riderLat=driverLocationWrapper.riderLat;
	        			 subDriverLocationWrapper.riderLng=driverLocationWrapper.riderLng;
	        			 
	        			 System.out.println("driverID :" +subDriverLocationWrapper.driverID);
	        			 vector.addElement(subDriverLocationWrapper);
	        		}		
			        
					dataArrayWrapper.driverLocationWrapper=new DriverLocationWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.driverLocationWrapper);

					//insert into driver location and call stored procedure to get nearest driver locations
					DriverHelper driverHelper = new DriverHelper();
					dataArrayWrapper = (DataArrayWrapper)driverHelper.insertDriverLocation(usersProfileWrapper, dataArrayWrapper.driverLocationWrapper);

					
					
					//update rider with nearest driver locations
					JsonObject mainJsonObj = new JsonObject();
					JsonElement elementObj = null;
					elementObj = gson.toJsonTree(dataArrayWrapper.driverLocationWrapper);
					mainJsonObj.add("cablocation", elementObj);
					
					System.out.println("elementObj is = " + mainJsonObj.toString());
					String jsonFormattedString  = mainJsonObj.toString();
					String  urlParameters = jsonFormattedString.replaceAll("\\\\", "");
					
					System.out.println("urlParameters is = " + urlParameters);
		  
					String riderURL=frdbURL + "/cabs/rider/location/"+driverLocationWrapper.riderID+".json?print=pretty";
					byte[] postData       = urlParameters.getBytes("UTF-8");
					int    postDataLength = postData.length;
					request        = riderURL;						
					url            = new URL( request );
					HttpURLConnection connpatch= (HttpURLConnection) url.openConnection();           
					connpatch.setDoOutput( true );
					connpatch.setInstanceFollowRedirects( false );
					connpatch.setRequestProperty("X-HTTP-Method-Override", "PATCH");
					connpatch.setRequestMethod("PUT");
					connpatch.setRequestProperty("Content-Type", "application/json"); 
					connpatch.setRequestProperty("charset", "UTF-8");
					connpatch.setRequestProperty("Content-Length", Integer.toString(postDataLength));
					connpatch.setUseCaches(false);
					// POST
					DataOutputStream writer = new DataOutputStream(connpatch.getOutputStream());
			        System.out.println("before write "+postData.toString());
			        writer.write(postData);
			        System.out.println("after write ");
			        writer.flush();

			        String linepatch;
			        BufferedReader readerpatch = new BufferedReader(new InputStreamReader(connpatch.getInputStream()));
	
			        while ((linepatch = readerpatch.readLine()) != null) {
			            result += linepatch;
			        }
			       
			        writer.close();
			        readerpatch.close();
			        System.out.println("result after driver location patch put "+result);
			
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
