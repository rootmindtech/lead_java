package com.rootmind.wrapper;

import java.math.BigDecimal;

public class RiderWrapper extends AbstractWrapper {

	public String riderRefNo = null;
	public String riderID = null;
	public String firstName = null;
	public String lastName = null;
	public String mobileNo = null;
	public String email = null;
	public String gender = null;
	public String city = null;
	public String drivingLicence = null;
	public String aadhaarNo = null;
	public String passportNo = null;
	public String emiratesID = null;
	public String visaNo = null;

	public String registerDate = null;
	public String userid = null;

	public String rcRegNo = null;
	public String vehicleNo = null;
	public String vehicleType = null;
	public String panNo = null;

	public String address1 = null;
	public String address2 = null;
	public String address3 = null;
	public String cityID = null;
	public String pinCode = null;
	public String districtID = null;
	public String stateID = null;
	public String permAddress1 = null;
	public String permAddress2 = null;
	public String permAddress3 = null;
	public String permCityID = null;
	public String permPINCode = null;
	public String permDistrictID = null;
	public String permStateID = null;

	public String genderValue = null;
	public String cityValue = null;

	public boolean emobileFound = false; // email or mobile found
	public String password = null;
	public String image = null; // this is encoded image in Base64;
	public boolean avatarImageFound = false;

	public BigDecimal currentLat = null;
	public BigDecimal currentLng = null;
	public String currentLocation = null;
	public String fcmToken = null;
	public String vacantStatus = null;
	public String status = null;
	public double distance = 0;
	public double duration = 0;

	public String serviceCode = null;
	public String favorite = null;
	public float avgRating = 0.0f;
	public float yourRating = 0.0f;
	public String locale = null;
	public String imageID=null;

	public ImageWrapper[] imageWrappers=null;
	
	public String customToken=null; //firebase authentication token
	
	public boolean recordFound = false;
}
