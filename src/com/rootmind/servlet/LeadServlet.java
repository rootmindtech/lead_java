package com.rootmind.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.repackaged.com.google.common.base.StandardSystemProperty;
import com.google.cloud.FieldSelector.Helper;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rootmind.controller.FileUploadController;
import com.rootmind.controller.UsersController;
import com.rootmind.helper.DriverHelper;
import com.rootmind.helper.FareHelper;
import com.rootmind.helper.FavoriteHelper;
import com.rootmind.helper.FileUploadHelper;
import com.rootmind.helper.ImageHelper;
import com.rootmind.helper.OTPHelper;
import com.rootmind.helper.PopoverHelper;
import com.rootmind.helper.RideHelper;
import com.rootmind.helper.RiderHelper;
import com.rootmind.helper.ServiceHelper;
import com.rootmind.helper.UserAuditHelper;
import com.rootmind.helper.UserMenuHelper;
import com.rootmind.helper.UsersHelper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.DriverLocationWrapper;
import com.rootmind.wrapper.DriverWrapper;
import com.rootmind.wrapper.FareWrapper;
import com.rootmind.wrapper.FavoriteWrapper;
import com.rootmind.wrapper.FileUploadWrapper;
import com.rootmind.wrapper.ImageWrapper;
import com.rootmind.wrapper.OTPWrapper;
import com.rootmind.wrapper.PopoverWrapper;
import com.rootmind.wrapper.RideWrapper;
import com.rootmind.wrapper.RiderWrapper;
import com.rootmind.wrapper.ServiceWrapper;
import com.rootmind.wrapper.UserMenuWrapper;
import com.rootmind.wrapper.UsersWrapper;

/**
 * Servlet implementation class NowcabsServlet
 */
@WebServlet("/Lead")
public class LeadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LeadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		String methodAction = request.getParameter("methodAction");

		if (methodAction == null)

		{

			PrintWriter errorOut = response.getWriter();

			response.setContentType("text/html");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			response.setHeader("Access-Control-Max-Age", "86400");

			System.out.println("Request received at GET Lead but methodAction null");

			String msg = "<html><body><h1>Invalid request received at Lead</h1></body></html>";

			errorOut.println(msg);
			errorOut.flush();
			errorOut.close();

			return;

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);

		try {
			String userProfile = request.getParameter("userProfile");
			System.out.println("param userProfile :" + userProfile);

			String message = request.getParameter("message");
			System.out.println("message :" + message);

			String methodAction = request.getParameter("methodAction");
			// String customerCode = request.getParameter("customerCode");

			System.out.println("method action in the servlet  " + methodAction);
			// System.out.println("customer code in the servlet " + customerCode);

			// String tableName = request.getParameter("tableName");

			RiderWrapper riderWrapper = null;
			DriverWrapper driverWrapper = null;
			FavoriteWrapper favoriteWrapper=null;
			RideWrapper rideWrapper = null;
			FareWrapper fareWrapper = null;
			OTPWrapper otpWrapper = null;
			DriverLocationWrapper[] driverLocationWrapperArray = null;
			DriverLocationWrapper driverLocationWrapper = null;

			ServiceWrapper serviceWrapper=null;
			ServiceWrapper [] serviceWrapperArray=null;

			ImageWrapper imageDetailsWrapper = null;

			ImageWrapper imageWrapper = null;

			
			UserMenuWrapper userMenuWrapper = null;

			PopoverWrapper popoverWrapper = null;
			PopoverWrapper[] popoverWrapperArray = null;
			UsersController usersController = null;
			// PasswordWrapper passwordWrapper=null;
			UsersWrapper usersWrapper = null;

			FileUploadWrapper fileUploadWrapper = null;

			String errorCode = null;
			String errorDescription = null;

			ServletContext application = getServletConfig().getServletContext();

			System.out.println("min version  " + application.getMinorVersion());

			System.out.println("max version  " + application.getMajorVersion());

			System.out.println("content type" + request.getContentType());

			System.out.println("content length  " + request.getContentLength());

			UsersWrapper usersProfileWrapper = new UsersWrapper();
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			UsersHelper usersHelper = new UsersHelper();
			UserAuditHelper userAuditHelper = new UserAuditHelper();

			System.out.println("methodaction  " + methodAction);

			System.out.println("before gson");
			Gson gson = new Gson();
			JsonObject mainJsonObj = new JsonObject();
			JsonElement elementObj = null;

			HttpSession session = null;
			dataArrayWrapper.usersWrapper = new UsersWrapper[1];

			// String localUserid=null;
			String localSessionid = null;

			session = request.getSession();
			// session.setAttribute("userid",usersWrapper.userid);
			// setting session to expiry in 30 mins
			localSessionid = (String) session.getId();
			System.out.println("local sessionid :" + localSessionid);
			boolean validSession = false;

			// ----------Customer section---//
			String appName = "Lead";

			// ---------

			if (methodAction == null)

			{

				PrintWriter errorOut = response.getWriter();

				response.setContentType("text/html");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "POST");
				response.setHeader("Access-Control-Allow-Headers", "Content-Type");
				response.setHeader("Access-Control-Max-Age", "86400");

				System.out.println("Request received at POST " + appName + " but methodAction null");

				String msg = "<html><body><h1>Invalid request received at " + appName + " </h1></body></html>";

				errorOut.println(msg);
				errorOut.flush();
				errorOut.close();

				return;

			}

			if (userProfile != null) {

				usersProfileWrapper = gson.fromJson(userProfile, UsersWrapper.class);
				usersProfileWrapper.ipAddress = request.getRemoteAddr();
				usersHelper.updateUserDetails(usersProfileWrapper.userid, usersProfileWrapper.noLoginRetry,
						localSessionid, usersProfileWrapper.deviceToken);

				userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction, appName,
						message);

			}

			if (methodAction.equals("validateUser")) {
				usersWrapper = gson.fromJson(message, UsersWrapper.class);

				usersWrapper.ipAddress = request.getRemoteAddr();

				usersHelper.updateSessionDetails(usersWrapper, usersProfileWrapper.noLoginRetry, localSessionid);

				System.out.println("in validateUser");

				// usersWrapper =
				// (UsersWrapper)usersHelper.validateCredentials(usersWrapper.userid,usersWrapper.password,"",true);
				usersController = new UsersController();
				usersWrapper = (UsersWrapper) usersController.validate(usersWrapper);

				// session.setMaxInactiveInterval(usersWrapper.sessionExpiryTime);
				usersWrapper.sessionid = localSessionid;
				dataArrayWrapper.usersWrapper[0] = usersWrapper;

				if (usersWrapper.validUser == true && usersWrapper.userGroup.equals("RIDER")) {
					RiderHelper riderHelper = new RiderHelper();
					riderWrapper = new RiderWrapper();
					riderWrapper.riderRefNo = usersWrapper.riderRefNo;
					riderWrapper.riderID = usersWrapper.riderID;
					riderWrapper.status = "ACTIVE";
					riderWrapper = (RiderWrapper) riderHelper.fetchRider(riderWrapper);
					dataArrayWrapper.riderWrapper = new RiderWrapper[1];
					dataArrayWrapper.riderWrapper[0] = riderWrapper;
				}
				if (usersWrapper.validUser == true && usersWrapper.userGroup.equals("DRIVER")) {
					/*
					 * RiderHelper riderHelper=new RiderHelper(); riderWrapper = new RiderWrapper();
					 * riderWrapper.riderRefNo=usersWrapper.riderRefNo;
					 * riderWrapper.riderID=usersWrapper.riderID; riderWrapper =
					 * (RiderWrapper)riderHelper.fetchRider(riderWrapper);
					 * dataArrayWrapper.riderWrapper= new RiderWrapper[1];
					 * dataArrayWrapper.riderWrapper[0]=riderWrapper;
					 */
				}
				validSession = true;

				/*
				 * if(usersWrapper.recordFound==true && usersWrapper.validUser==true) {
				 * 
				 * dataArrayWrapper.recordFound=true;
				 * 
				 * } else { System.out.println("usersWrapper.validUser :" +
				 * usersWrapper.validUser ); dataArrayWrapper.recordFound=true; }
				 */

				dataArrayWrapper.validSession = validSession;
				dataArrayWrapper.recordFound = true;

				elementObj = gson.toJsonTree(dataArrayWrapper);

				mainJsonObj.add(methodAction, elementObj);

				if (dataArrayWrapper.recordFound == true) {
					mainJsonObj.addProperty("success", true);
				} else {
					mainJsonObj.addProperty("success", false);
				}

			} else if (methodAction.equals("insertRider") || methodAction.equals("insertOTP")
					|| methodAction.equals("validateOTP") || methodAction.equals("verifyRider")
					|| methodAction.equals("updateRider") || methodAction.equals("fetchRider")
					|| methodAction.equals("insertDriver") ||  methodAction.equals("fetchRiderSearch")  
					|| methodAction.equals("fetchDriver") || methodAction.equals("verifyDriver")
					|| methodAction.equals("updateVacantStatus") || methodAction.equals("updateRiderLocation")
					|| methodAction.equals("fetchDriverLocation") 
					|| methodAction.equals("riderAutoLogin") || methodAction.equals("insertRating")
					|| methodAction.equals("driverAutoLogin") 
					|| methodAction.equals("updateDriver") 
					|| methodAction.equals("insertFavorite")
					|| methodAction.equals("fetchServiceLocation") || methodAction.equals("fetchService")
					|| methodAction.equals("insertService") || methodAction.equals("updateImage")
					
					) {
				

				validSession = true;

				userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction, appName,
						message);

			} else {

				UsersWrapper userSessionWrapper = (UsersWrapper) usersHelper.fetchSessionDetails(usersProfileWrapper);

				usersProfileWrapper.sessionid = usersProfileWrapper.sessionid.replaceAll(" ", "+");
				System.out.println("usersProfileWrapper.sessionid " + usersProfileWrapper.sessionid);
				System.out.println("userSessionWrapper.sessionid " + userSessionWrapper.sessionid);

				if (usersProfileWrapper.sessionid == null || userSessionWrapper.sessionid == null
						|| !usersProfileWrapper.sessionid.equals(userSessionWrapper.sessionid)) {
					validSession = false;

					System.out.println("Invalid session");

					dataArrayWrapper.validSession = validSession;
					dataArrayWrapper.recordFound = true;

					elementObj = gson.toJsonTree(dataArrayWrapper);

					mainJsonObj.add(methodAction, elementObj);

					if (dataArrayWrapper.recordFound == true) {
						mainJsonObj.addProperty("success", true);
					} else {
						mainJsonObj.addProperty("success", false);
					}

				} else {
					validSession = true;
				}

			}

			if (message != null && validSession == true) {

				if (methodAction.equals("fetchMultiPopoverData")) {

					popoverWrapperArray = gson.fromJson(message, PopoverWrapper[].class);

				}

				if (methodAction.equals("fetchMasterData")) {

					popoverWrapper = gson.fromJson(message, PopoverWrapper.class);

				}
				if (methodAction.equals("fetchTableNames")) {

					popoverWrapper = gson.fromJson(message, PopoverWrapper.class);

				}
				if (methodAction.equals("updateMasterData")) {

					popoverWrapper = gson.fromJson(message, PopoverWrapper.class);

				}

				if (methodAction.equals("uploadImage"))

				{
					imageDetailsWrapper = gson.fromJson(message, ImageWrapper.class);

				}
				if (methodAction.equals("fetchImage"))

				{
					imageWrapper = gson.fromJson(message, ImageWrapper.class);

				}

				if (methodAction.equals("fetchImageFileNames"))

				{
					imageDetailsWrapper = gson.fromJson(message, ImageWrapper.class);

				}
				
  			   if(methodAction.equals("updateImage"))
				  
				{ 
  				   imageWrapper= gson.fromJson(message,ImageWrapper.class);
				  
				 }
				 
				if (methodAction.equals("updateImageStatus"))

				{
					imageDetailsWrapper = gson.fromJson(message, ImageWrapper.class);

				}

				if (methodAction.equals("updateUserMenu")) {

					userMenuWrapper = gson.fromJson(message, UserMenuWrapper.class);

				}
				if (methodAction.equals("fetchUserMenu")) {

					userMenuWrapper = gson.fromJson(message, UserMenuWrapper.class);

				}

				if (methodAction.equals("updateLoginProfile")) {

					usersWrapper = gson.fromJson(message, UsersWrapper.class);

				}

				if (methodAction.equals("fetchLoginProfile")) {

					usersWrapper = gson.fromJson(message, UsersWrapper.class);

				}

				if (methodAction.equals("changePassword")) {

					usersWrapper = gson.fromJson(message, UsersWrapper.class);

				}

				// -------Nowcabs
				if (methodAction.equals("verifyRider")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}

				if (methodAction.equals("insertRider")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("updateRider")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("fetchRider")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("fetchRiderSearch")) {

					driverWrapper = gson.fromJson(message, DriverWrapper.class);
				}
				if (methodAction.equals("insertDriver")) {

					driverWrapper = gson.fromJson(message, DriverWrapper.class);
				}
				if (methodAction.equals("updateDriver")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("fetchDriver")) {

					driverWrapper = gson.fromJson(message, DriverWrapper.class);
				}
				if (methodAction.equals("fetchDriverSearch")) {

					driverWrapper = gson.fromJson(message, DriverWrapper.class);
				}
				if (methodAction.equals("insertRide")) {

					rideWrapper = gson.fromJson(message, RideWrapper.class);
				}
				if (methodAction.equals("fetchRide")) {

					rideWrapper = gson.fromJson(message, RideWrapper.class);
				}
				if (methodAction.equals("updateRide")) {

					rideWrapper = gson.fromJson(message, RideWrapper.class);
				}
				if (methodAction.equals("fetchRideSearch")) {

					rideWrapper = gson.fromJson(message, RideWrapper.class);
				}
				if (methodAction.equals("insertFare")) {

					fareWrapper = gson.fromJson(message, FareWrapper.class);
				}
				if (methodAction.equals("updateFare")) {

					fareWrapper = gson.fromJson(message, FareWrapper.class);
				}
				if (methodAction.equals("fetchFare")) {

					fareWrapper = gson.fromJson(message, FareWrapper.class);
				}
				if (methodAction.equals("insertOTP")) {

					otpWrapper = gson.fromJson(message, OTPWrapper.class);

				}
				if (methodAction.equals("validateOTP")) {

					otpWrapper = gson.fromJson(message, OTPWrapper.class);

				}
				if (methodAction.equals("getDriverLocations")) {

					driverLocationWrapper = gson.fromJson(message, DriverLocationWrapper.class);

				}
				if (methodAction.equals("updateVacantStatus")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("updateRiderLocation")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("fetchDriverLocation")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("riderAutoLogin")) {

					riderWrapper = gson.fromJson(message, RiderWrapper.class);
				}
				if (methodAction.equals("driverAutoLogin")) {

					driverWrapper = gson.fromJson(message, DriverWrapper.class);
				}
				if (methodAction.equals("insertFavorite")) {

					favoriteWrapper = gson.fromJson(message, FavoriteWrapper.class);
				}
				if (methodAction.equals("insertRating")) {

					favoriteWrapper = gson.fromJson(message, FavoriteWrapper.class);
				}
				if (methodAction.equals("fetchService")) {

					serviceWrapper = gson.fromJson(message, ServiceWrapper.class);
				}
				if (methodAction.equals("insertService")) {

					serviceWrapperArray = gson.fromJson(message, ServiceWrapper[].class);
				}
				if (methodAction.equals("fetchServiceLocation")) {

					serviceWrapper = gson.fromJson(message, ServiceWrapper.class);
				}
			
				
				

			} // --if message not null condition close

			// ------end of JSON parsing

			PrintWriter out = response.getWriter();

			System.out.println("methodaction  " + methodAction);

			System.out.println("before gson after printwriter :");

			// if validSession then start process
			if (validSession == true) {

				if (methodAction.equals("fetchMultiPopoverData")) {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetch MultiPopoverController  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchMultiPopoverData(popoverWrapperArray);

				}

				if (methodAction.equals("fetchTableNames")) {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetchTableNames PopoverData  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchTableNames(popoverWrapper);

				}
				if (methodAction.equals("fetchMasterData")) {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetchMasterData   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchMasterData(popoverWrapper);

				}
				if (methodAction.equals("updateMasterData")) {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("update PopoverData  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.updateMasterData(popoverWrapper);

				}

				if (methodAction.equals("uploadImageDetails")) {

					/*
					 * String password = "password"; String passwordEnc =
					 * AESEncryption.encrypt(password);
					 */

					// Create path components to save the file
					String path = request.getParameter("destination");
					String originalFilePath = path;

					final Part filePart = request.getPart("file");

					final String fileName = getSubmittedFileName(filePart);

					OutputStream outputStream = null;
					InputStream filecontent = null;
					OutputStream outputStreamThumbnail = null;
					InputStream filecontentThumbnail = null;

					// final PrintWriter writer = response.getWriter();

					try {

						System.out.println("path " + path);
						System.out.println("filePart " + filePart);
						System.out.println("fileName " + fileName);

						//ImageHelper imageHelper = new ImageHelper();
						//path = imageDetailsHelper.fetchImagePath() + path;

						File folderPath = new File(path);

						if (!folderPath.exists()) {
							System.out.println("creating directory: " + path);
							boolean result = false;

							try {
								folderPath.mkdirs();
								result = true;
							} catch (SecurityException se) {
								// handle it
								se.printStackTrace();
							}
							if (result) {
								System.out.println("DIR created");
							}
						}

						outputStream = new FileOutputStream(new File(path + File.separator + fileName));

						filecontent = filePart.getInputStream();

						int read = 0;
						final byte[] bytes = new byte[16384];

						while ((read = filecontent.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);

							// System.out.println("writing file " + bytes);
						}

						// -------create thumbnail from profile image
						if (imageDetailsWrapper.docID.trim().equals("DOC001")) {

							outputStreamThumbnail = new FileOutputStream(
									new File(path + File.separator + "thumbnail_" + fileName));
							filecontentThumbnail = filePart.getInputStream();

							int readThumbnail = 0;
							final byte[] bytesThumbnail = new byte[16384];

							while ((readThumbnail = filecontentThumbnail.read(bytesThumbnail)) != -1) {
								outputStreamThumbnail.write(bytesThumbnail, 0, readThumbnail);
								// System.out.println("writing file " + bytes);
							}

							Image image = javax.imageio.ImageIO
									.read(new File(path + File.separator + "thumbnail_" + fileName));

							int thumbWidth = 70;
							int thumbHeight = 70;
							// int quality=80;
							double thumbRatio = (double) thumbWidth / (double) thumbHeight;
							int imageWidth = image.getWidth(null);
							int imageHeight = image.getHeight(null);
							double imageRatio = (double) imageWidth / (double) imageHeight;
							if (thumbRatio < imageRatio) {
								thumbHeight = (int) (thumbWidth / imageRatio);
							} else {
								thumbWidth = (int) (thumbHeight * imageRatio);
							}

							if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
								thumbWidth = imageWidth;
								thumbHeight = imageHeight;
							} else if (imageWidth < thumbWidth)
								thumbWidth = imageWidth;
							else if (imageHeight < thumbHeight)
								thumbHeight = imageHeight;

							BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
									BufferedImage.TYPE_INT_RGB);
							Graphics2D graphics2D = thumbImage.createGraphics();
							graphics2D.setBackground(Color.WHITE);
							graphics2D.setPaint(Color.WHITE);
							graphics2D.fillRect(0, 0, thumbWidth, thumbHeight);
							graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
									RenderingHints.VALUE_INTERPOLATION_BILINEAR);
							graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

							javax.imageio.ImageIO.write(thumbImage, "JPG",
									new File(path + File.separator + "thumbnail_" + fileName));
						}
						// -------

						imageDetailsWrapper.imageName = fileName;
						imageDetailsWrapper.imageFolder = originalFilePath;

						imageDetailsWrapper.imageUploadStatus = true;
						// writer.println("New file " + fileName + " created at " + path);
						// LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
						// new Object[]{fileName, path});

						System.out.println("bytes " + bytes.length);

					} catch (FileNotFoundException fne) {

						System.out.println("File Not Found ");

						fne.printStackTrace();

						// writer.println("You either did not specify a file to upload or are "
						// + "trying to upload a file to a protected or nonexistent "
						// + "location.");
						// writer.println("<br/> ERROR: " + fne.getMessage());

						// LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
						// new Object[]{fne.getMessage()});
					} finally {
						if (outputStream != null) {
							outputStream.close();
						}
						if (filecontent != null) {
							filecontent.close();
						}
						if (outputStreamThumbnail != null) {
							outputStreamThumbnail.close();
						}
						if (filecontentThumbnail != null) {
							filecontentThumbnail.close();
						}
						// if (writer != null) {
						// writer.close();
						// }
					}

					ImageHelper imageHelper = new ImageHelper();

					System.out.println("Upload Image Details Helper " + methodAction);

					// imageDetailsWrapper.imageFile="C://Users//RootMind//Pictures//images.JPG";

					dataArrayWrapper = (DataArrayWrapper) imageHelper.updateImage(usersProfileWrapper,
							imageDetailsWrapper);

				}
				if (methodAction.equals("fetchImageDetails")) {

					try {
						// BufferedImage img = ImageIO.read(new File("C://testfile.jpg"+ ""));

						ImageHelper imageDetailsHelper = new ImageHelper();

						System.out.println("Fetch Image Details Helper " + methodAction);

						dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.fetchImage(imageDetailsWrapper);

						if (dataArrayWrapper.recordFound == true && dataArrayWrapper.imageWrapper.length > 0) {

							imageDetailsWrapper = dataArrayWrapper.imageWrapper[0];

							System.out.println("file path to fetch " + imageDetailsWrapper.imageFolder
									+ File.separator + imageDetailsWrapper.imageName);
							System.out.println(new File(imageDetailsWrapper.imageFolder + File.separator
									+ imageDetailsWrapper.imageName).getCanonicalPath());

							BufferedImage img = ImageIO.read(new File(imageDetailsWrapper.imageFolder
									+ File.separator + imageDetailsWrapper.imageName));

							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(img, "jpg", baos);
							baos.flush();
							byte[] imageInByte = baos.toByteArray();
							// String encodedString = Base64.getEncoder().encodeToString(imageInByte);
							String encodedString = new String(Base64.encodeBase64(imageInByte));
							mainJsonObj.addProperty("image", encodedString);
							baos.close();
							System.out.println("Image added to JSON");
							imageDetailsWrapper.imageFoundStatus = true;

							dataArrayWrapper.imageWrapper[0] = imageDetailsWrapper;

							System.out.println("Fetch Image success in servlet ");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (methodAction.equals("updateImage")) {

					ImageHelper imageHelper = new ImageHelper();

					System.out.println("Image Details Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageHelper.updateImage(usersProfileWrapper,imageWrapper);

				}
				if (methodAction.equals("fetchImage")) {

					ImageHelper imageHelper = new ImageHelper();

					System.out.println("Image filenames  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageHelper.fetchImage(imageWrapper);

				}
//				if (methodAction.equals("updateImageStatus")) {
//
//					ImageHelper imageDetailsHelper = new ImageHelper();
//
//					System.out.println("Image Details Helper " + methodAction);
//
//					dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.updateImageStatus(imageDetailsWrapper);
//
//				}

				if (methodAction.equals("updateUserMenu")) {

					UserMenuHelper userMenuHelper = new UserMenuHelper();

					System.out.println("update Grade Subjects   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) userMenuHelper.updateUserMenu(usersProfileWrapper,
							userMenuWrapper);

				}

				if (methodAction.equals("fetchUserMenu")) {

					UserMenuHelper userMenuHelper = new UserMenuHelper();

					System.out.println("fetch Grade Subjects   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) userMenuHelper.fetchUserMenu(userMenuWrapper);

				}

				if (methodAction.equals("updateLoginProfile")) {

					UsersHelper usersHelperLoginProfile = new UsersHelper();

					System.out.println("update Login profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) usersHelperLoginProfile
							.updateLoginProfile(usersProfileWrapper, usersWrapper, "");

				}

				if (methodAction.equals("fetchLoginProfile")) {

					UsersHelper usersHelperFetchLogin = new UsersHelper();

					System.out.println("fetch Login profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) usersHelperFetchLogin.fetchLoginProfile(usersWrapper);

				}

				if (methodAction.equals("changePassword")) {

					UsersHelper usersHelperfetch = new UsersHelper();

					System.out.println("fetchUsersStaff  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) usersHelperfetch.changePassword(usersWrapper);

				}

				// --------data file upload-----------

				if (methodAction.equals("dataFileUpload")) {

					/*
					 * String password = "password"; String passwordEnc =
					 * AESEncryption.encrypt(password);
					 */

					// Create path components to save the file
					String path = request.getParameter("destination");
					String originalFilePath = path;

					final Part filePart = request.getPart("file");

					final String fileName = getSubmittedFileName(filePart);

					OutputStream outputStream = null;
					InputStream filecontent = null;

					// final PrintWriter writer = response.getWriter();

					try {

						System.out.println("path " + path);
						System.out.println("filePart " + filePart);
						System.out.println("fileName " + fileName);

						FileUploadHelper fileUploadHelper = new FileUploadHelper();
						fileUploadWrapper = fileUploadHelper.fetchDataFileTemplate(fileUploadWrapper);
						path = fileUploadWrapper.destinationPath + path;

						File folderPath = new File(path);

						// file creation date time
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						System.out.println("file creation date time : " + sdf.format(folderPath.lastModified()));

						if (!folderPath.exists()) {
							System.out.println("creating directory: " + path);
							boolean result = false;

							try {
								folderPath.mkdirs();
								result = true;
							} catch (SecurityException se) {
								// handle it
								se.printStackTrace();
							}
							if (result) {
								System.out.println("DIR created");
							}
						}

						outputStream = new FileOutputStream(new File(path + File.separator + fileName));

						filecontent = filePart.getInputStream();

						int read = 0;
						final byte[] bytes = new byte[16384];

						while ((read = filecontent.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);

							// System.out.println("writing file " + bytes);
						}

						fileUploadWrapper.dataFileName = fileName;
						fileUploadWrapper.dataFileFolder = originalFilePath;

						fileUploadWrapper.dataFileDateTime = sdf.format(folderPath.lastModified());
						fileUploadWrapper.destinationPath = path + File.separator + fileName;
						fileUploadWrapper.fileUploadStatus = true;
						// writer.println("New file " + fileName + " created at " + path);
						// LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
						// new Object[]{fileName, path});

						System.out.println("bytes " + bytes.length);

					} catch (FileNotFoundException fne) {

						System.out.println("File Not Found ");

						fne.printStackTrace();

						// writer.println("You either did not specify a file to upload or are "
						// + "trying to upload a file to a protected or nonexistent "
						// + "location.");
						// writer.println("<br/> ERROR: " + fne.getMessage());

						// LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
						// new Object[]{fne.getMessage()});
					} finally {
						if (outputStream != null) {
							outputStream.close();
						}
						if (filecontent != null) {
							filecontent.close();
						}

						// if (writer != null) {
						// writer.close();
						// }
					}

					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("File Upload Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileProcess(usersProfileWrapper,
							fileUploadWrapper);

				}
				// --------end data file upload-------

				if (methodAction.equals("dataFileUploadView")) {

					// StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper();
					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("File Upload Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileUploadView(usersProfileWrapper,
							fileUploadWrapper);

				}
				if (methodAction.equals("dataFileUploadDelete")) {

					// StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper();
					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("dataFileUploadDelete " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileUploadDelete(usersProfileWrapper,
							fileUploadWrapper);

				}

				// ---------Nowcabs----

				if (methodAction.equals("verifyRider")) {
					System.out.println("inside verify Rider");

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.verifyRider(usersProfileWrapper, riderWrapper);

				}

				if (methodAction.equals("insertRider")) {
					System.out.println("inside insert rider");

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.insertRider(usersProfileWrapper, riderWrapper);

				}
				if (methodAction.equals("updateRider")) {

					System.out.println("inside update Rider");

					RiderHelper riderHelper = new RiderHelper();

					riderWrapper = (RiderWrapper) riderHelper.updateRider(usersProfileWrapper, riderWrapper);
					dataArrayWrapper.riderWrapper = new RiderWrapper[1];
					dataArrayWrapper.riderWrapper[0] = riderWrapper;
					dataArrayWrapper.recordFound = true;

				}
				if (methodAction.equals("fetchRider")) {

					RiderHelper riderHelper = new RiderHelper();

					System.out.println("before riderWrapper.vehicleNo" + riderWrapper.vehicleNo);

					riderWrapper = (RiderWrapper) riderHelper.fetchRider(riderWrapper);
					System.out.println("riderWrapper.vehicleNo" + riderWrapper.vehicleNo);
					dataArrayWrapper.riderWrapper = new RiderWrapper[1];
					dataArrayWrapper.riderWrapper[0] = riderWrapper;
					dataArrayWrapper.recordFound = true;

				}
				if (methodAction.equals("fetchRiderSearch")) {

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.fetchRiderSearch(usersProfileWrapper,
							driverWrapper);

				}
				if (methodAction.equals("insertDriver")) {

					DriverHelper driverHelper = new DriverHelper();

					dataArrayWrapper = (DataArrayWrapper) driverHelper.insertDriver(usersProfileWrapper, driverWrapper);

				}
				if (methodAction.equals("updateDriver")) {

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.updateDriver(usersProfileWrapper, riderWrapper);

				}
				if (methodAction.equals("fetchDriver")) {

					DriverHelper driverHelper = new DriverHelper();

					driverWrapper = (DriverWrapper) driverHelper.fetchDriver(driverWrapper);

					dataArrayWrapper.driverWrapper = new DriverWrapper[1];
					dataArrayWrapper.driverWrapper[0] = driverWrapper;
					dataArrayWrapper.recordFound = true;

				}
				if (methodAction.equals("fetchDriverSearch")) {

					DriverHelper driverHelper = new DriverHelper();

					dataArrayWrapper = (DataArrayWrapper) driverHelper.fetchDriverSearch(usersProfileWrapper,
							driverWrapper);

				}
				if (methodAction.equals("insertRide")) {

					RideHelper rideHelper = new RideHelper();

					dataArrayWrapper = (DataArrayWrapper) rideHelper.insertRide(usersProfileWrapper, rideWrapper);

				}
				if (methodAction.equals("updateRide")) {

					RideHelper rideHelper = new RideHelper();

					dataArrayWrapper = (DataArrayWrapper) rideHelper.updateRide(usersProfileWrapper, rideWrapper);

				}
				if (methodAction.equals("fetchRide")) {

					RideHelper rideHelper = new RideHelper();

					dataArrayWrapper = (DataArrayWrapper) rideHelper.fetchRide(usersProfileWrapper, rideWrapper);

				}
				if (methodAction.equals("fetchRideSearch")) {

					RideHelper rideHelper = new RideHelper();

					dataArrayWrapper = (DataArrayWrapper) rideHelper.fetchRideSearch(usersProfileWrapper, rideWrapper);

				}

				if (methodAction.equals("insertFare")) {

					FareHelper fareHelper = new FareHelper();

					dataArrayWrapper = (DataArrayWrapper) fareHelper.insertFare(usersProfileWrapper, fareWrapper);

				}
				if (methodAction.equals("fetchFare")) {

					FareHelper fareHelper = new FareHelper();

					dataArrayWrapper = (DataArrayWrapper) fareHelper.fetchFare(usersProfileWrapper, fareWrapper);

				}
				if (methodAction.equals("updateFare")) {

					FareHelper fareHelper = new FareHelper();

					dataArrayWrapper = (DataArrayWrapper) fareHelper.updateFare(usersProfileWrapper, fareWrapper);

				}
				if (methodAction.equals("insertOTP")) {

					OTPHelper otpHelper = new OTPHelper();

					System.out.println("insert otp  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) otpHelper.insertOTP(otpWrapper);

				}
				if (methodAction.equals("validateOTP")) {

					OTPHelper otpHelper = new OTPHelper();

					System.out.println("validate otp  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) otpHelper.validateOTP(otpWrapper);

					usersProfileWrapper.noLoginRetry = 1;
					usersProfileWrapper.ipAddress = request.getRemoteAddr();

					if (otpWrapper.userGroup.equals("RIDER")) {

						// *--------get user details----*//
						System.out.println("servlet validate otp  get rider details");
						usersProfileWrapper.userid = otpWrapper.riderID;
						// usersProfileWrapper.noLoginRetry=1;
						// usersProfileWrapper.ipAddress=request.getRemoteAddr();
						usersHelper.updateUserDetails(usersProfileWrapper.userid, usersProfileWrapper.noLoginRetry,
								localSessionid, usersProfileWrapper.deviceToken);
						userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction,
								appName, message);

						// ---------To get user details -----//
						usersWrapper = new UsersWrapper();
						usersWrapper.userid = otpWrapper.riderID;
						usersWrapper.riderRefNo = otpWrapper.riderRefNo;
						usersWrapper.riderID = otpWrapper.riderID;
						usersWrapper.userGroup = "RIDER";
						usersWrapper = (UsersWrapper) usersHelper.validateCredentials(usersWrapper);
						usersWrapper.validUser = true;
						usersWrapper.sessionid = localSessionid;

						dataArrayWrapper.usersWrapper = new UsersWrapper[1];
						dataArrayWrapper.usersWrapper[0] = usersWrapper;

						// ----------To get Rider details ----------//

						RiderHelper riderHelper = new RiderHelper();
						riderWrapper = new RiderWrapper();
						riderWrapper.riderRefNo = otpWrapper.riderRefNo;
						riderWrapper.riderID = otpWrapper.riderID;
						riderWrapper.status = "ACTIVE";
						riderWrapper = (RiderWrapper) riderHelper.fetchRider(riderWrapper);

						dataArrayWrapper.riderWrapper = new RiderWrapper[1];
						dataArrayWrapper.riderWrapper[0] = riderWrapper;

						dataArrayWrapper.validSession = validSession;
						dataArrayWrapper.recordFound = true;

						/*--------end of user details*---------*/
					}
					// *--------get user details----*//
					if (otpWrapper.userGroup.equals("DRIVER")) {

						System.out.println("servlet validate otp  get Driver details");
						usersProfileWrapper.userid = otpWrapper.driverID;
						// usersProfileWrapper.noLoginRetry=1;
						// usersProfileWrapper.ipAddress=request.getRemoteAddr();
						usersHelper.updateUserDetails(usersProfileWrapper.userid, usersProfileWrapper.noLoginRetry,
								localSessionid, usersProfileWrapper.deviceToken);
						userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction,
								appName, message);

						// ---------To get user details -----//
						usersWrapper = new UsersWrapper();
						usersWrapper.userid = otpWrapper.driverID;
						usersWrapper.driverRefNo = otpWrapper.driverRefNo;
						usersWrapper.driverID = otpWrapper.driverID;
						usersWrapper.userGroup = "DRIVER";
						usersWrapper = (UsersWrapper) usersHelper.validateCredentials(usersWrapper);
						usersWrapper.validUser = true;
						usersWrapper.sessionid = localSessionid;

						dataArrayWrapper.usersWrapper = new UsersWrapper[1];
						dataArrayWrapper.usersWrapper[0] = usersWrapper;

						// ----------To get Driver details ----------//

						DriverHelper driverHelper = new DriverHelper();
						driverWrapper = new DriverWrapper();
						driverWrapper.driverRefNo = otpWrapper.driverRefNo;
						driverWrapper.driverID = otpWrapper.driverID;
						driverWrapper.status = "ACTIVE";
						driverWrapper = (DriverWrapper) driverHelper.fetchDriver(driverWrapper);

						dataArrayWrapper.driverWrapper = new DriverWrapper[1];
						dataArrayWrapper.driverWrapper[0] = driverWrapper;

						dataArrayWrapper.validSession = validSession;
						dataArrayWrapper.recordFound = true;

					}
					/*--------end of user details*---------*/

				}

				if (methodAction.equals("getDriverLocations")) {

					// DriverHelper driverHelper = new DriverHelper();

					System.out.println("getDriverLocations  " + methodAction);

					// dataArrayWrapper =
					// (DataArrayWrapper)driverHelper.insertDriverLocation(usersProfileWrapper,
					// driverLocationWrapperArray);

					System.out.println("firebase started  ");

					// FirebaseDatabase firebaseDatabase = new FirebaseDatabase();
					// dataArrayWrapper = (DataArrayWrapper)
					// firebaseDatabase.getDriverLocations(usersProfileWrapper,
					// driverLocationWrapper);

					System.out.println("firebase completed  ");

				}
				if (methodAction.equals("updateVacantStatus")) {

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.updateVacantStatus(usersProfileWrapper, riderWrapper);

				}
				if (methodAction.equals("updateRiderLocation")) {

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.updateRiderLocation(usersProfileWrapper, riderWrapper);

				}
//				if (methodAction.equals("fetchDriverLocation")) {
//
//					RiderHelper riderHelper = new RiderHelper();
//
//					dataArrayWrapper = (DataArrayWrapper) riderHelper.fetchDriverLocation(usersProfileWrapper, riderWrapper);
//
//				}
				if (methodAction.equals("riderAutoLogin")) {

					RiderHelper riderHelper = new RiderHelper();

					dataArrayWrapper = (DataArrayWrapper) riderHelper.verifyRider(usersProfileWrapper, riderWrapper);
					
					if(dataArrayWrapper.recordFound==true && dataArrayWrapper.riderWrapper!=null && dataArrayWrapper.riderWrapper.length>0)
					{
						RiderWrapper verifyRiderWrapper = dataArrayWrapper.riderWrapper[0];
						if(verifyRiderWrapper.recordFound==true)
						{
								riderWrapper.riderRefNo=verifyRiderWrapper.riderRefNo;
								riderWrapper.riderID = verifyRiderWrapper.riderID;
								riderHelper.updateLoginRider(usersProfileWrapper, riderWrapper);
						}
					}

				}
				if (methodAction.equals("driverAutoLogin")) {

					DriverHelper driverHelper = new DriverHelper();

					System.out.println("before locale1 " + driverWrapper.locale);
					
					dataArrayWrapper = (DataArrayWrapper) driverHelper.verifyDriver(usersProfileWrapper, driverWrapper);
					
					if (dataArrayWrapper.recordFound == true && dataArrayWrapper.driverWrapper != null
							&& dataArrayWrapper.driverWrapper.length > 0) {
						DriverWrapper verifyDriverWrapper = dataArrayWrapper.driverWrapper[0];
						if (verifyDriverWrapper.recordFound == true) 
						{
								driverWrapper.driverRefNo = verifyDriverWrapper.driverRefNo;
								driverWrapper.driverID = verifyDriverWrapper.driverID;
								System.out.println("before locale2 " + driverWrapper.locale);
								driverHelper.updateLoginDriver(usersProfileWrapper, driverWrapper);
						}
					}
				}
				if (methodAction.equals("insertFavorite")) {

					FavoriteHelper favoriteHelper = new FavoriteHelper();

					dataArrayWrapper = (DataArrayWrapper) favoriteHelper.insertFavorite(usersProfileWrapper, favoriteWrapper);
					
				}				
				if (methodAction.equals("insertRating")) {

					FavoriteHelper favoriteHelper = new FavoriteHelper();

					dataArrayWrapper = (DataArrayWrapper) favoriteHelper.insertRating(usersProfileWrapper, favoriteWrapper);
					
				}				
				if (methodAction.equals("fetchService")) {

					ServiceHelper serviceHelper = new ServiceHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceHelper.fetchService(serviceWrapper);
					
				}				
				if (methodAction.equals("insertService")) {

					ServiceHelper serviceHelper = new ServiceHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceHelper.insertService(usersProfileWrapper, serviceWrapperArray);
					
				}				
				if (methodAction.equals("fetchServiceLocation")) {

					ServiceHelper serviceHelper = new ServiceHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceHelper.fetchServiceLocation(usersProfileWrapper, serviceWrapper);

				}
				
				dataArrayWrapper.validSession = validSession;
				elementObj = gson.toJsonTree(dataArrayWrapper);

				mainJsonObj.add(methodAction, elementObj);

				if (dataArrayWrapper.recordFound == true) {
					mainJsonObj.addProperty("success", true);
				} else {
					mainJsonObj.addProperty("success", false);
				}

			} // ----end of validSession condition

			mainJsonObj.addProperty("errorCode", errorCode);
			mainJsonObj.addProperty("errorDescription", errorDescription);
			mainJsonObj.addProperty("userid", usersProfileWrapper.userid);
			// mainJsonObj.addProperty("password",paramPassword);
			// mainJsonObj.addProperty("sessionid",paramSessionid);
			// mainJsonObj.addProperty("cifNumber",usersWrapper.cifNumber);

			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			response.setHeader("Access-Control-Max-Age", "86400");

			System.out.println("user id is: " + usersProfileWrapper.userid);

			out.println(mainJsonObj.toString());
			out.flush();
			out.close();

			if (methodAction.equals("fetchImageDetails")) {
				mainJsonObj.remove("image");
				System.out.println("image removed from user audit");

				userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction, appName,
						mainJsonObj.toString());
			} else {
				userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid, methodAction, appName,
						mainJsonObj.toString());
			}
			System.out.println("out close");

		} catch (JsonParseException jse) {
			jse.printStackTrace();
			System.out.println("parse exc " + jse.getMessage());

		}

		catch (Exception jse) {
			jse.printStackTrace();
			System.out.print("main exc " + jse.getMessage());
		}
	}

	private static String getSubmittedFileName(Part part) {

		System.out.println("part header " + part.getHeader("content-disposition"));

		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {

				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");

				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE
																													// fix.
			}
		}
		return null;
	}

}
