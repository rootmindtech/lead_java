package com.rootmind.controller;

//import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

//import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

//import org.bouncycastle.jce.provider.symmetric.AES.KeyGen;









import com.rootmind.helper.Helper;
import com.rootmind.helper.Utility;

import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import java.util.Base64;


import org.apache.commons.codec.binary.Base64;


public class AES128Crypto extends Helper {
	
	

	private static final String ALGORITHM = "AES/ECB/PKCS7Padding";
	
	
	
	//private static final byte[] keyValue = new byte[]{'A', 'b', 'c', 'D', 'e', 'f', 'G', 'h', 'I', 'L', 'm', 'n', 'o', 'P', 'Q', 'r'};
	
	//private static final String aesEncryptKey="AbcDefGhILmnoPQr";
	
	private static final String aesEncryptKey="Sri534260$#%&@^$";
	
	//DO NOT USE
	//private static final String ALGORITHM = "AES/CBC/PKCS7Padding";			//"AES/CBC/PKCS7Padding";
	//private static final byte[] ivValue = new byte[]{'A', 'b', 'c', 'D', 'e', 'f', 'G', 'h', 'I', 'L', 'm', 'n', 'o', 'P', 'Q', 'r'};
	//private static String ivValue="AbcDefGhILmnoPQr";
	//-------
	
	public String encrypt(String valueToEnc) throws Exception {
		
		/*Connection con = null;
		ResultSet resultSet = null;
		String sql=null;
		PreparedStatement pstmt=null;
		*/
		String encryptedValue=null;
		//String aesEncryptKey=null;
		
		try{
			
				/*con = getConnection();
			
				//----
				sql="SELECT AESEncryptKey from MST_Parameter";
				
				pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
												
					aesEncryptKey=Utility.trim(resultSet.getString("AESEncryptKey"));
					
					System.out.println("aesEncryptKey "+aesEncryptKey);
					
				}
				
				resultSet.close();
				pstmt.close();*/
				
				//------
			
			//use ECB,Don't use iv. This works for ios 20/04/2016
			
			//IvParameterSpec iv = new IvParameterSpec(ivValue.getBytes("UTF-8")); //--do not use
			
			
			
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Key key = generateKey(aesEncryptKey);
			Cipher c = Cipher.getInstance(ALGORITHM, "BC");
			//c.init(Cipher.ENCRYPT_MODE, key,iv); //--do not use
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encValue = c.doFinal(valueToEnc.getBytes("UTF8"));
			encryptedValue = DatatypeConverter.printBase64Binary(encValue);
			System.out.println("Encrypted " + encryptedValue);
			
			String decryptedValue=decrypt(encryptedValue);
			System.out.println("decryptedValue "+decryptedValue);
			
			
			encryptedValue=md5Manual(encryptedValue);
		
			System.out.println("Encrypted+md5+salt "+encryptedValue);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return encryptedValue;
	}
	
	
	
	
	public String decrypt(String encryptedValue) throws Exception {
		
		
	
		//String aesEncryptKey=null;
		String decryptedValue=null;
		
		try{
			
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				Key key = generateKey(aesEncryptKey);
				Cipher c = Cipher.getInstance(ALGORITHM, "BC");
				c.init(Cipher.DECRYPT_MODE, key);
				//Base64.Decoder decoder = Base64.getDecoder();
				//byte[] decodedValue = decoder.decode(encryptedValue);
				//byte[] decValue = c.doFinal(decodedValue);
				//decryptedValue = new String(decValue);
				System.out.println("Value to decrypt "+encryptedValue);
				byte[]   bytesDecoded = Base64.decodeBase64(encryptedValue.getBytes());
				byte [] docValue = c.doFinal(bytesDecoded);
				decryptedValue= new String(docValue);
				
				
				System.out.println("Decoded "+decryptedValue);
				
	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return decryptedValue;
	}

	private  Key generateKey(String aesKey) throws Exception{
		
		//System.out.println(keyValue[0]);
		byte[] keyValue = aesKey.getBytes();
		
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}
	
    public  String md5DB(String input) throws Exception {
        
    	Connection con = null;
    	ResultSet resultSet = null;
    	String sql=null;
    	PreparedStatement pstmt=null;
        String md5 = null;
    
        
        String salt=null;
       
         
        try {
        	    System.out.println("Before md5 is  " +input);
        	
        		if(input!=null)
        		{
        			con = getConnection();  
		        	//----
					sql="SELECT AESSaltKey from MST_Parameter";
					
					pstmt = con.prepareStatement(sql);
				
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) 
					{
													
						
						salt=Utility.trim(resultSet.getString("AESSaltKey"));
						
						System.out.println("salt is  "+salt);
					}
					
					resultSet.close();
					pstmt.close();
					//-----
					
		        	//String salt = "Rootmind534260$PalakolValue#WithSpecialCharacters12@$@4&#%^$*";
		        	String inputSalt = input+salt;
		            MessageDigest md = MessageDigest.getInstance("MD5");
		            byte[] messageDigest = md.digest(inputSalt.getBytes());
		            BigInteger number = new BigInteger(1, messageDigest);
		            md5 = number.toString(16);
		            
		            System.out.println("after md5db " + md5);
        		}
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
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
        
        return md5;
    }
	
 public  String md5Manual(String input) throws Exception {
        
    
        String md5 = null;
    
        
        String salt=null;
       
         
        try {
        	
        		if(input!=null)
        		{
        			 
					
        			salt = "Rootmind534260$PalakolValue#WithSpecialCharacters12@$@4&#%^$*";
		        	String inputSalt = input+salt;
		            MessageDigest md = MessageDigest.getInstance("MD5");
		            byte[] messageDigest = md.digest(inputSalt.getBytes());
		            BigInteger number = new BigInteger(1, messageDigest);
		            md5 = number.toString(16);
        		}
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
        }
        return md5;
    }
 
 
  public String encryptPasswordDB(String valueToEnc) throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;
		String sql=null;
		PreparedStatement pstmt=null;
		
		String encryptedValue=null;
		String aesEncryptKeyDB=null;
		
		try{
			
				con = getConnection();
			
				//----
				sql="SELECT AESEncryptKey from MST_Parameter";
				
				pstmt = con.prepareStatement(sql);
			
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
				{
												
					aesEncryptKeyDB=Utility.trim(resultSet.getString("AESEncryptKey"));
					
					System.out.println("aesEncryptKeyDB "+aesEncryptKeyDB);
					
				}
				
				resultSet.close();
				pstmt.close();
				
				//------

			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Key key = generateKey(aesEncryptKeyDB);
			Cipher c = Cipher.getInstance(ALGORITHM, "BC");
			
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encValue = c.doFinal(valueToEnc.getBytes("UTF8"));
			encryptedValue = DatatypeConverter.printBase64Binary(encValue);
			System.out.println("Encrypted " + encryptedValue);
			
			String decryptedValue=decrypt(encryptedValue);
			System.out.println("decryptedValue "+decryptedValue);
			
			
			encryptedValue=md5DB(encryptedValue);
		
			System.out.println("Encrypted+md5+salt "+encryptedValue);
			
		}
		catch(Exception e){
			e.printStackTrace();
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
		return encryptedValue;
	}
  
  
	public static void main(String[] args) throws Exception
	{
		if(args[0]==null || args[0].equals(""))
		{
			System.out.println("Pass first parameter as password.");
			return;
		}
		new AES128Crypto().encrypt(args[0]);
		
		
		if(args[1]!=null && !args[1].equals(""))
		{
			new AES128Crypto().decrypt(args[1]);
		}
				
		
	}

}
