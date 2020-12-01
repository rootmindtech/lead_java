package com.rootmind.wrapper;

import java.math.BigDecimal;

public class UsersWrapper extends AbstractWrapper {
	
	
	public String userid=null;
	public String password=null;
	public String status=null;
	public String mobileNo=null;
	public String email=null;
	public String lastLoginDate=null;
	public String deviceToken=null;
	public BigDecimal trnLimit=null;
	public BigDecimal trnDayLimit=null;	
	public int noLoginRetry=0;
	public int maxRetry=0;
	public String sessionid=null;
	public String pwdChgDate=null;
	public int otp=0;
	public String businessUnit=null;
	public int sessionExpiryTime=0;
	public String userGroup=null;
	public String oldPassword=null;
	public String ipAddress=null;
	public boolean validUser=false;
	public boolean passwordChanged=false;
	public boolean oldPasswordRepeat=false;
	public boolean invalidOldPassword=false;
	public String refNo=null;
	public String riderRefNo=null;
	public String riderID=null;
	public String driverRefNo=null;
	public String driverID=null;
	public String admin=null;
	public boolean sessionTimeOut=false;
	
	public UserMenuWrapper[] userMenuWrapper=null;
	
	public boolean recordFound=false;
			
}
