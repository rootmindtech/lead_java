package com.rootmind.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.rootmind.helper.ImageHelper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ImageWrapper;


public class ImageController {

	
	
	public DataArrayWrapper fetchAvatarImageDetailsTraveller(DataArrayWrapper dataArrayWrapper) throws Exception
	
	{
		try {
			

					if(dataArrayWrapper.riderWrapper!=null)
					{
				
						Vector<Object> vector = new Vector<Object>();
			 			ImageHelper imageDetailsHelper=new ImageHelper();
			 			ImageWrapper imageDetailsWrapper=null;
			 			
			 			for(int i=0;i<=dataArrayWrapper.riderWrapper.length-1;i++)
			 			{
			
			 				imageDetailsWrapper = new ImageWrapper();
			 				imageDetailsWrapper.riderRefNo=dataArrayWrapper.riderWrapper[i].riderRefNo;
			 				imageDetailsWrapper.docID="DOC002";
			 				
		    	 			imageDetailsWrapper = (ImageWrapper)imageDetailsHelper.fetchImage(imageDetailsWrapper);
		    	 			
		    	 			if(imageDetailsWrapper!=null && imageDetailsWrapper.recordFound==true)
		    	 			{
		    	 			
		    	 				
		    	 				try{
		    	 				
					    	 			System.out.println("file path to fetch " + imageDetailsWrapper.imageFolder + File.separator + imageDetailsWrapper.imageName );
					    	 			System.out.println(new File(imageDetailsWrapper.imageFolder + File.separator + imageDetailsWrapper.imageName).getCanonicalPath());
					    	 			
					    	 			BufferedImage img = ImageIO.read(new File(imageDetailsWrapper.imageFolder + File.separator + imageDetailsWrapper.imageName));
					    	 			
					    	 			
					    	 			
					    	 			ByteArrayOutputStream baos = new ByteArrayOutputStream();
						    	 		ImageIO.write( img, "jpg", baos );
						    	 		baos.flush();
						    	 		byte[] imageInByte = baos.toByteArray();
						    	 		//String encodedString = Base64.getEncoder().encodeToString(imageInByte);
						    	 		String encodedString = new String(Base64.encodeBase64(imageInByte));
						    	 		//mainJsonObj.addProperty("image", encodedString);
						    	 		dataArrayWrapper.riderWrapper[i].image=encodedString;
						    	 		dataArrayWrapper.riderWrapper[i].avatarImageFound=true;
						    	 		baos.close();
						    	 		System.out.println("Image added to JSON");
						    	 		imageDetailsWrapper.imageFoundStatus=true;
		
						    	 		imageDetailsWrapper.recordFound=true;
		    	 				}
		    	 				catch(IOException ex)
		    	 				{
		    	 					System.out.println("Error occured during image file reading");
		    	 					ex.printStackTrace();
		    	 				}
				    	 		
				    	 		vector.addElement(imageDetailsWrapper);
				    	 		
				    	 		System.out.println("Fetch Image success in avatar ");
		    	 			}
			 			}
			 			
			 			if (vector.size()>0)
						{
							dataArrayWrapper.imageWrapper = new ImageWrapper[vector.size()];
							vector.copyInto(dataArrayWrapper.imageWrapper);
							dataArrayWrapper.recordFound=true;

							System.out.println("total trn. in fetch " + vector.size());

						}
				 			
				 	}
				} catch (IOException ex) {
			 		ex.printStackTrace();
			 		throw new Exception(ex.getMessage());
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
	
}
