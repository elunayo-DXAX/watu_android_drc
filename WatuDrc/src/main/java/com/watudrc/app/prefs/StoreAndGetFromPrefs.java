
package com.watudrc.app.prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.ContractObject;
import com.watudrc.app.objects.LoanCompleteIconObject;
import com.watudrc.app.objects.LoanListItemObject;
import com.watudrc.app.objects.NotificationObject;
import com.watudrc.app.objects.Payment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.watudrc.app.TinyDB;
import com.watudrc.app.objects.FriendRequestObject;
import com.watudrc.app.objects.GroupMemberObject;
import com.watudrc.app.objects.ProvidersItem;
import com.watudrc.app.objects.ServiceDownObject;
import com.watudrc.app.objects.UpgradeDevicesItem;
import com.watudrc.app.objects.User;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONObject;


public class StoreAndGetFromPrefs
{
	private  Context mContext;
	TinyDB tinyDb;
	
	public StoreAndGetFromPrefs (Context context)
	{
		this.mContext = context;
		tinyDb = new TinyDB(this.mContext);
	}
	

    //set first time starting app 
    public void setFistTimeUsingApp()
    {
      tinyDb.putBoolean("IsFirstTime",true);
        	   	
    }



	//Check if  Is first time starting app
    public boolean isFirstTimeUsingApp()
    {
    	return tinyDb.getBoolean("IsFirstTime");
    }
        
    
    //Stop first time starting app 
    public void stopFirstTimeUsingApp()
    {
        tinyDb.putBoolean("IsFirstTime",false);
    }
 
   //Check if User Is Registered
    public boolean checkIfUserIsRegistered()
    {
    	 return tinyDb.getBoolean("IsRegistered");	    	   	
    }
 

    public void setUserRegistered(boolean value)
    {
    	tinyDb.putBoolean("IsRegistered", value);
    	    	   	
    }

	//Check if Agreed to Terms

	public void setIsAgreedToTerms(boolean value)
	{
		tinyDb.putBoolean("IsAgreedToTerms",value);

	}

	public boolean isAgreedToTerms()
	{
		return tinyDb.getBoolean("IsAgreedToTerms");
	}



	public void setIsPhoneLocked(boolean value)
	{
		tinyDb.putBoolean("IsPhoneLocked",value);

	}

	public boolean isPhoneLocked()
	{
		return tinyDb.getBoolean("IsPhoneLocked");
	}



	//Check if User Register Later
    public boolean isUserRegisterLater()
    {
        return  tinyDb.getBoolean("IsRegisterLater");
    	    	   	
    }
 
    public void setUserRegisterLater(boolean value)
    {
    	tinyDb.putBoolean("IsRegisterLater", value);
    	    	   	
    }
    
    public void setRegisterFirst(boolean value)
    {
		tinyDb.putBoolean("IsRegisterFirst", value);
    }
    public boolean isRegisterFirstSet()
    {
      return tinyDb.getBoolean("IsRegisterFirst");
    }

	public void setRegisterSecond(boolean value)
	{
		tinyDb.putBoolean("IsRegisterSecond", value);
	}
	public boolean isRegisterSecondSet()
	{
		return tinyDb.getBoolean("IsRegisterSecond");
	}

	public void setRegisterThird(boolean value)
	{
		tinyDb.putBoolean("IsRegisterThird", value);
	}
	public boolean isRegisterThirdSet()
	{
		return tinyDb.getBoolean("IsRegisterThird");
	}

	public void setRegisterFinal(boolean value)
	{
		tinyDb.putBoolean("IsRegisterFinal", value);
	}
	public boolean isRegisterFinalSet()
	{
		return tinyDb.getBoolean("IsRegisterFinal");
	}


	public void setIsSimuUser(boolean value)
	{
		tinyDb.putBoolean("IsSimuUser", value);
	}
	public boolean isSimuUser()
	{
		return tinyDb.getBoolean("IsSimuUser");
	}


	public void setIsBodaUser(boolean value)
	{
		tinyDb.putBoolean("IsBodaUser", value);
	}
	public boolean isBodaUser()
	{
		return tinyDb.getBoolean("IsBodaUser");
	}



	public  void saveLanguageToPrefs(String lang)
    {
    	tinyDb.putString("Language", lang);
    	
    }

    public String retrieveLanguageFromPrefs()
    {
    	tinyDb = new TinyDB(mContext);
    	
    	String lang = tinyDb.getString("Language");
    	
    	if(lang.equals(""))
    	{
    		saveLanguageToPrefs("en");
    		lang="en";
    	}
    	
    	return lang;
    	
    }
  
    
    public  void saveUserPasswordToPrefs(String password)
    {
    	tinyDb.putString("UserPassword", password);
    	
    }
    public String retrieveUserPasswordFromPrefs()
    {
    	return tinyDb.getString("UserPassword");
    	
    }

	public  void saveUserDayToPrefs(String day)
	{
		tinyDb.putString("UserDay", day);

	}
	public String retrieveUserDayFromPrefs()
	{
		return tinyDb.getString("UserDay");

	}

	public  void saveUserMonthToPrefs(String month)
	{
		tinyDb.putString("UserMonth", month);

	}
	public String retrieveUserMonthFromPrefs()
	{
		return tinyDb.getString("UserMonth");

	}

	public  void saveUserYearToPrefs(String year)
	{
		tinyDb.putString("UserYear", year);

	}
	public String retrieveUserYearFromPrefs()
	{
		return tinyDb.getString("UserYear");

	}
	public  void saveUserGenderToPrefs(String gender)
	{
		tinyDb.putString("UserGender", gender);

	}
	public String retrieveUserGenderFromPrefs()
	{
		return tinyDb.getString("UserGender");

	}

	public  void saveUserDOBToPrefs(String gender)
	{
		tinyDb.putString("UserDOB", gender);

	}
	public String retrieveUserDOBFromPrefs()
	{
		return tinyDb.getString("UserDOB");

	}


	public  void saveUserBusinessTypeToPrefs(String uBusinessType)
	{
		tinyDb.putString("UserBusinessType", uBusinessType);

	}
	public String retrieveUserBusinessTypeFromPrefs()
	{
		return tinyDb.getString("UserBusinessType");

	}

	public  void saveUserBusinessCompanyTypeToPrefs(String uBusinessCompanyType)
	{
		tinyDb.putString("UserBusinessCompanyType", uBusinessCompanyType);

	}
	public String retrieveUserBusinessCompanyTypeFromPrefs()
	{
		return tinyDb.getString("UserBusinessCompanyType");

	}

	public  void saveUserBusinessCompanyRegisteredToPrefs(String uBusinessCompanyRegistered)
	{
		tinyDb.putString("UserBusinessCompanyRegistered", uBusinessCompanyRegistered);

	}
	public String retrieveUserBusinessCompanyRegisteredFromPrefs()
	{
		return tinyDb.getString("UserBusinessCompanyRegistered");

	}

	public void setPayIcon(boolean value)
	{
		tinyDb.putBoolean("PayIcon",value);

	}

	public boolean showPayIcon()
	{
		return tinyDb.getBoolean("PayIcon");
	}

	public void setPayTooltip(boolean value)
	{
		tinyDb.putBoolean("PayTooltip",value);

	}

	public boolean showPayTooltip()
	{
		return tinyDb.getBoolean("PayTooltip");
	}




	public void setKnoxFeatureStatus(boolean value)
	{
		tinyDb.putBoolean("KnoxFeatureStatus",value);

	}

	public boolean isKnoxFeatureStatusActive()
	{
		return tinyDb.getBoolean("KnoxFeatureStatus");
	}


	public void setKnoxOTPEnabled(boolean value)
	{
		tinyDb.putBoolean("IsKnoxOTPEnabled",value);

	}

	public boolean isKnoxOTPEnabled()
	{
		if(isKnoxFeatureStatusActive())
		{
			return tinyDb.getBoolean("IsKnoxOTPEnabled");

		}
		return false;
	}

	public void setKnoxOTPDelay(int value)
	{
		tinyDb.putInt("KnoxOTPDelay",value);

	}

	public Integer retrieveKnoxOTPDelay()
	{
		return tinyDb.getInt("KnoxOTPDelay");
	}


	public void setTemporaryUnlockTooltip(boolean value)
	{
		tinyDb.putBoolean("TemporaryUnlockTooltip",value);

	}

	public boolean showTemporaryUnlockTooltip()
	{
		return tinyDb.getBoolean("TemporaryUnlockTooltip");
	}



	public  void saveTokenToPrefs(String token)
    {
    	tinyDb.putString("Token", token);
    	
    }

    public String retrieveTokenFromPrefs()
    {
    	return tinyDb.getString("Token");
    	
    }

	public  void saveHashToPrefs(String hash)
	{
		tinyDb.putString("Hash", hash);

	}

	public String retrieveHashFromPrefs()
	{
		return tinyDb.getString("Hash");

	}


	public  void saveApiSecretToPrefs(String secret)
	{
		tinyDb.putString("ApiSecret", secret);

	}

	public String retrieveApiSecretFromPrefs()
	{
		return tinyDb.getString("ApiSecret");

	}

	public  void saveDeviceIDToPrefs(String deviceID)
	{
		tinyDb.putString("DeviceID", deviceID);

	}

	public String retrieveDeviceIDFromPrefs()
	{
		return tinyDb.getString("DeviceID");

	}

	public  void saveTokenCookieStringToPrefs(String token)
	{
		tinyDb.putString("TokenCookieString", token);

	}

	public String retrieveTokenCookieStringFromPrefs()
	{
		return tinyDb.getString("TokenCookieString");

	}

	public  void saveUserDueDateToPrefs(String date)
	{
		tinyDb.putString("UserDueDate", date);

	}
	public String retrieveUserDueDateFromPrefs()
	{
		return tinyDb.getString("UserDueDate");

	}

	public  void saveTokenClientToPrefs(String token)
	{
		tinyDb.putString("TokenClient", token);

	}

	public String retrieveTokenClientFromPrefs()
	{
		return tinyDb.getString("TokenClient");

	}

	public  void saveTokenUidToPrefs(String token)
	{
		tinyDb.putString("TokenUid", token);

	}

	public String retrieveTokenUidFromPrefs()
	{
		return tinyDb.getString("TokenUid");

	}

	public  void saveTokenClientPortalSessionToPrefs(String token)
	{
		tinyDb.putString("TokenClientSession", token);

	}

	public String retrieveTokenClientPortalSessionFromPrefs()
	{
		return tinyDb.getString("TokenClientSession");

	}
    
   //Save User Object
    
    public  void saveUserNameToPrefs(String name)
    {
		if(StringUtils.isStringEmpty(name)) return;
		tinyDb.putString("UserName", name);
    	
    }

    public String retrieveUserNameFromPrefs()
    {
    	return tinyDb.getString("UserName");
    	
    }

	public  void saveUserCountryToPrefs(String country)
	{
		tinyDb.putString("UserCountry", country);

	}

	public String retrieveUserCountryFromPrefs()
	{
		return tinyDb.getString("UserCountry");

	}

	public  void saveUserCityToPrefs(String city)
	{
		tinyDb.putString("UserCity", city);

	}

	public String retrieveUserCityFromPrefs()
	{
		return tinyDb.getString("UserCity");

	}


	public  void saveUserAddressToPrefs(String add)
	{
		tinyDb.putString("UserAddress", add);

	}

	public String retrieveUserAddressFromPrefs()
	{
		return tinyDb.getString("UserAddress");

	}


	public  void saveUserCountryCodeToPrefs(String countryCode)
	{
		tinyDb.putString("UserCountryCode", countryCode);

	}

	public String retrieveUserCountryCodeFromPrefs()
	{
		String country = tinyDb.getString("UserCountryCode");
		return country.equals("")? "ng": country;

	}

	public String retrieveAmountCodeFromPrefs()
	{
		if(retrieveUserCountryCodeFromPrefs().equalsIgnoreCase("ug"))
		{
			return "UGX ";
		}

		else if(retrieveUserCountryCodeFromPrefs().equalsIgnoreCase("rw"))
		{
			return "RWF ";
		}

		return "USD. ";
	}

	public  void saveUserEmployedToPrefs(String employed)
	{
		tinyDb.putString("UserEmployed", employed);

	}

	public String retrieveUserEmployedFromPrefs()
	{
		return tinyDb.getString("UserEmployed");

	}

	public  void saveUserPrivateToPrefs(String uPrivate)
	{
		tinyDb.putString("UserPrivate", uPrivate);

	}

	public String retrieveUserPrivateFromPrefs()
	{
		return tinyDb.getString("UserPrivate");

	}

	public  void saveUserCompanyToPrefs(String uCompany)
	{
		tinyDb.putString("UserCompany", uCompany);

	}

	public String retrieveUserCompanyFromPrefs()
	{
		return tinyDb.getString("UserCompany");

	}

	public  void saveOtherNumberCountToPrefs(int count)
	{
		tinyDb.putInt("OtherNumberCount", count);

	}
	public  void increaseOtherNumberCountToPrefs()
	{
		int newCount= retrieveOtherNumberCountFromPrefs()+1;
		tinyDb.putInt("OtherNumberCount", newCount);

	}
	public int retrieveOtherNumberCountFromPrefs()
	{
		return tinyDb.getInt("OtherNumberCount");

	}

	public  void saveTempUnlockConfigToPrefs(int count)
	{
		tinyDb.putInt("TempUnlockConfig", count);

	}

	public int retrieveTempUnlockConfigFromPrefs()
	{
		int config=tinyDb.getInt("TempUnlockConfig");
		return config;

	}


	public  void savePayOtherConfigToPrefs(int count)
	{
		tinyDb.putInt("PayOtherConfig", count);

	}

	public int retrievePayOtherConfigFromPrefs()
	{
		int config=tinyDb.getInt("PayOtherConfig");
		return config;

	}


	public  void saveTemporaryUnlockCountToPrefs(int count)
	{
		tinyDb.putInt("TemporaryUnlockCount", count);

	}
	public  void increaseTemporaryUnlockCountToPrefs()
	{
		int newCount= retrieveTemporaryUnlockCountFromPrefs()+1;
		tinyDb.putInt("TemporaryUnlockCount", newCount);

	}
	public int retrieveTemporaryUnlockCountFromPrefs()
	{
		return tinyDb.getInt("TemporaryUnlockCount");

	}

	public  void savePaymentTimestampToPrefsToPrefs(long timeStamp)
	{
		tinyDb.putLong("PaymentTimeStamp", timeStamp);

	}
	public long retrievePaymentTimeStampFromPrefs()
	{
		return tinyDb.getLong("PaymentTimeStamp");

	}


	public  void saveUserUpcomingAmountToPrefs(int amount)
	{
		tinyDb.putInt("UserUpcomingAmount", amount);

	}

	public Integer retrieveUserUpcomingAmountFromPrefs()
	{
		return tinyDb.getInt("UserUpcomingAmount");

	}


	public  void saveUserOutStandingAmountToPrefs(int amount)
	{
		tinyDb.putInt("UserOutStandingAmount", amount);

	}

	public Integer retrieveUserOutStandingAmountFromPrefs()
	{
		return tinyDb.getInt("UserOutStandingAmount");

	}



	public  void saveUserVerifyTokenToPrefs(String uVerifyToken)
	{
		tinyDb.putString("UserVerifyToken", uVerifyToken);

	}

	public String retrieveUserVerifyTokenFromPrefs()
	{
		return tinyDb.getString("UserVerifyToken");

	}



	public  void saveUserPaymentMethodToPrefs(String uPaymentMethod)
	{
		tinyDb.putString("UserPaymentMethod", uPaymentMethod);

	}

	public String retrieveUserPaymentMethodFromPrefs()
	{
		return tinyDb.getString("UserPaymentMethod");

	}




    public  void saveUserEmailToPrefs(String email)
    {
    	tinyDb.putString("UserEmail", email);
    	
    }

    public String retrieveUserEmailFromPrefs()
    {
    	return tinyDb.getString("UserEmail");
    	
    }
    
    public  void saveUserPhoneNumberToPrefs(String value)
    {
    	tinyDb.putString("UserPhoneNumber", value);
    	
    }

    public String retrieveUserPhoneNumberFromPrefs()
    {
    	return tinyDb.getString("UserPhoneNumber");
    	
    }

	public  void saveUserIDNumberToPrefs(String value)
	{
		tinyDb.putString("UserIDNumber", value);

	}

	public String retrieveUserIDNumberFromPrefs()
	{
		return tinyDb.getString("UserIDNumber");

	}



	public  void saveUserFirstNameToPrefs(String value)
	{
		if(StringUtils.isStringEmpty(value)) return;
		tinyDb.putString("UserFirstName", value);

	}

	public String retrieveUserFirstNameFromPrefs()
	{
		String name= tinyDb.getString("UserFirstName");

		return name;

	}


	public  void saveTempLoanNumberToPrefs(String value)
	{
		tinyDb.putString("TemporaryLoanNumber", value);

	}

	public String retrieveUserTempLoanNumberFromPrefs()
	{
		return tinyDb.getString("TemporaryLoanNumber");

	}


	public  void saveTempActiveTillToPrefs(long value)
	{
		tinyDb.putLong("TemporaryActiveTill", value);

	}

	public long retrieveUserTempActiveTillFromPrefs()
	{
		return tinyDb.getLong("TemporaryActiveTill");

	}

	public  void saveUserLastNameToPrefs(String value)
	{
		tinyDb.putString("UserLastName", value);

	}

	public String retrieveUserLastNameFromPrefs()
	{
		return tinyDb.getString("UserLastName");

	}



	public  void saveUserFCMTokenPrefs(String token)
	{
		tinyDb.putString("UserFT", token);

	}

	public String retrieveUserFCMTokenFromPrefs()
	{
		return tinyDb.getString("UserFT");

	}

	public void saveUserObjectToPrefs(User user) {
    	
	    Gson gson = new Gson();
		String objectsString = gson.toJson(user);
		tinyDb.putString("UserObject", objectsString);
   	
    }
    
    
	
    //Retrieve User Object
    public User retrieveUserObjectFromPrefs()
	{
    	  User requestObject= new User();
 		
    	  try
    	  {
	    	    Gson gson = new Gson();
	
				String objectsString = tinyDb.getString("UserObject");
				Type type = new TypeToken <User>(){}.getType();
				
						
				@SuppressWarnings("unchecked")
				User theObject= (User) gson.fromJson(objectsString, type);
				
		    			
				if(theObject!= null)
				{
					requestObject= theObject;
				}
    	  }
    	  catch(Exception e)
    	  {
    		  e.printStackTrace();
    	  }

	    	return requestObject;
	}




    public void logOutUser() {

        // setPermissionsWizardComplete(false);
//        setUserRegistered(false);
//        saveUserPasswordToPrefs("");

//        saveUserKeyToPrefs("");
//    	saveTokenToPrefs("");
//        saveUserObjectToPrefs(new User());
//
//        saveUserOutStandingAmountToPrefs(0);
//        saveUserUpcomingAmountToPrefs(0);
//    	savePaymentObjectsToPrefs(new ArrayList<Payment>());

    }
	 

    

    
  //------------------------Chat stuff--------------------------------
	
  		public  void saveUserKeyToPrefs(String key)
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	tinyDb.putString("UserKey", key);
  	    	
  	    }

  	    public String retrieveUserKeyFromPrefs()
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	
  	    	String key = tinyDb.getString("UserKey");
  	    	
  	    	return key;
  	    	
  	    }
  		//Join Group
  		 
  		 public void joinGroup(String groupId)
  		 {
  		 	ArrayList<String> oldGroups = retrieveMyJoinedGroupsFromPrefs();
  		 	
  		 	oldGroups.add(groupId);
  		 	    	
  		  	   	
  		  	tinyDb.putList("MyJoinedGroups", oldGroups);
  		 	    	
  		 }

  		 public ArrayList<String> retrieveMyJoinedGroupsFromPrefs()
  		 {
  		 	ArrayList<String> myJoinedGroups= new ArrayList<String>();
  		 	tinyDb = new TinyDB(mContext);
  		 		
  		 	myJoinedGroups = tinyDb.getList("MyJoinedGroups");
  		 	
  		 	return myJoinedGroups;
  		 }







  		  public boolean isJoinedGroup(String groupId) {
  		 		// TODO Auto-generated method stub
  		 		ArrayList<String> oldGroups= retrieveMyJoinedGroupsFromPrefs();
  		 		
  		 		if (oldGroups != null && oldGroups.size() > 0 && !StringUtils.isStringEmpty(groupId))
  		 		{
  		 				for (int i=0;i<oldGroups.size();i++) 
  		 				{
  		 					if (!StringUtils.isStringEmpty(groupId) && oldGroups.get(i).equals(groupId)) {
  		 						return true;
  		 					}
  		 				}
  		 		}
  		 			return false;
  		 		}

  		  public void removeFromJoinedGroups(String groupId) {
  		 		// TODO Auto-generated method stub
  		 		ArrayList<String> oldGroups = new ArrayList<String>();
  		 		tinyDb = new TinyDB(mContext);
  		 			
  		 		oldGroups = tinyDb.getList("MyJoinedGroups");  
  		 		
  		 		oldGroups.remove(groupId);
  		 		
  		 		
  		 		tinyDb.putList("MyJoinedGroups", oldGroups);
  		 		
  		 	}
  		  
  		  //Number of vybes
  		 
  		public  void saveuserNumberOfChatsToPrefs(String numberOfVybes)
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	tinyDb.putString("userNumberOfChats", numberOfVybes);
  	    	
  	    }

  	    public String retrieveuserNumberOfChatsFromPrefs()
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	
  	    	String numberOfVybes = tinyDb.getString("userNumberOfChats");
  	    	
  	    	return numberOfVybes;
  	    	
  	    }
  	    
  	    public  void saveUserNumberOfGroupVybesToPrefs(String numberOfGroupVybes)
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	tinyDb.putString("UserNumberOfGroupVybes", numberOfGroupVybes);
  	    	
  	    }
  	    
  	    
  	    

  	    public String retrieveUserNumberOfGroupVybesFromPrefs()
  	    {
  	    	tinyDb = new TinyDB(mContext);
  	    	
  	    	String numberOfGroupVybes = tinyDb.getString("UserNumberOfGroupVybes");
  	    	
  	    	return numberOfGroupVybes;
  	    	
  	    }
  	    
  	  //Save Group Members
  	    
  	    public void saveGroupMemberObjectsInPreferences(ArrayList<GroupMemberObject> theMemberObjects) {
  	    	
  	    	
  	   	    
  	    	tinyDb = new TinyDB(mContext);
  	    	
  	    	Gson gson = new Gson();
    		String objectsString = gson.toJson(theMemberObjects);
    			
    			tinyDb = new TinyDB(mContext);
    			tinyDb.putString("GroupMemberObjects", objectsString);
     	   	
  	    }
  	    
  	    
  	   //Add Group Members
  	    
  	   public void addGroupMemberObjectsToPreferences(GroupMemberObject theMemberObject) {
  	   	
  		ArrayList<GroupMemberObject> memberObjects=   retrieveGroupMemberObjectsFromPreferences();
  	   	
  	    boolean isMemberAlreadyAdded= false;
  	   	
  	   	for(int i=0;i<memberObjects.size();i++)
  	   	{
  	   	    
  	   		GroupMemberObject member= new GroupMemberObject();
  	   		
  	   		member.setGroupMemberGroupId(memberObjects.get(i).getGroupMemberGroupId());
  	   		member.setGroupMemberKey(memberObjects.get(i).getGroupMemberKey());

  	   		if(member.getGroupMemberGroupId().equals(theMemberObject.getGroupMemberGroupId()) && member.getGroupMemberKey().equals(theMemberObject.getGroupMemberKey()))
  	   		{
  	   			//Member Already Added
  	   			
  	   			isMemberAlreadyAdded= true;
  	   			
  	   			break;
  	   		}
  	   		
  	   	

  	   	}
  	   	
  	   	if(!isMemberAlreadyAdded)
  	   	{
  	   		memberObjects.add(theMemberObject);	   	
  	   	}
  	   	
  	   	saveGroupMemberObjectsInPreferences(memberObjects);
  	   	
  	   	    	   	
  	   }
  	    
  	    
  	   	
  	   //Retrieve Group Member Objects
  	    
  	   public ArrayList<GroupMemberObject> retrieveGroupMemberObjectsFromPreferences()
  	   {
  	   		
  		    Gson gson = new Gson();
  			tinyDb = new TinyDB(mContext);

  			String objectsString = tinyDb.getString("GroupMemberObjects");
  			Type type = new TypeToken <ArrayList<GroupMemberObject>>(){}.getType();
  			
  					
  			@SuppressWarnings("unchecked")
  			ArrayList<GroupMemberObject> theObjects= (ArrayList<GroupMemberObject>) gson.fromJson(objectsString, type);
  			
  	    	ArrayList<GroupMemberObject> memberObjects= new ArrayList<GroupMemberObject>();
  	    			
  			if(theObjects!= null)
  			{
  				memberObjects= theObjects;
  			}

  	    	return memberObjects;
  	   }
  	   
  	   //Save Friend Request Objects
  	    
  	    public void saveFriendRequestObjectsInPreferences(ArrayList<FriendRequestObject> theRequestObjects) {
  	    	
  	    	
  	   	    
  	    	tinyDb = new TinyDB(mContext);
  	    	
  	    	Gson gson = new Gson();
   			String objectsString = gson.toJson(theRequestObjects);
   			
   			tinyDb = new TinyDB(mContext);
   			tinyDb.putString("FriendRequestObjects", objectsString);
    	   	
  	    }
  		
  		//Add New Friend Request Object

  		public void addFriendRequestObjectInPreferences(FriendRequestObject requestObject) {
  			
  			
  			ArrayList<FriendRequestObject> requestObjects=   retrieveFriendRequestObjectsFromPreferences();
  		   	
  		    boolean isRequestAlreadyAdded= false;
  		   	
  		   	for(int i=0;i<requestObjects.size();i++)
  		   	{
  		   	    
  		   		FriendRequestObject member= new FriendRequestObject();
  		   		
  		   		member.setUserEmail(requestObjects.get(i).getUserEmail());
  		   		member.setUserKey(requestObjects.get(i).getUserKey());

  		   		if(member.getUserEmail().equals(requestObject.getUserEmail()) && member.getUserKey().equals(requestObject.getUserKey()))
  		   		{
  		   			//Request Already Added
  		   			
  		   			isRequestAlreadyAdded= true;
  		   			
  		   			break;
  		   		}
  		   		
  		   	

  		   	}
  		   	
  		   	if(!isRequestAlreadyAdded)
  		   	{
  		   		requestObjects.add(requestObject);	   	
  		   	}
  		   	
  		   	saveFriendRequestObjectsInPreferences(requestObjects);
  		   	
  		   	    	   	
  		   }

  	      //Retrieve Friend Requests Objects
  	  public ArrayList<FriendRequestObject> retrieveFriendRequestObjectsFromPreferences()
  		{
  						
  	    	    Gson gson = new Gson();
  				tinyDb = new TinyDB(mContext);

  				String objectsString = tinyDb.getString("FriendRequestObjects");
  				Type type = new TypeToken <ArrayList<FriendRequestObject>>(){}.getType();
  				
  						
  				@SuppressWarnings("unchecked")
  				ArrayList<FriendRequestObject> theObjects= (ArrayList<FriendRequestObject>) gson.fromJson(objectsString, type);
  				
  		    	ArrayList<FriendRequestObject> requestObjects= new ArrayList<FriendRequestObject>();
  		    			
  				if(theObjects!= null)
  				{
  					requestObjects= theObjects;
  				}

  		    	return requestObjects;
  		}
  				
  				
  				
  				
  				//Remove Friend Request Object
  		      public void removeFriendRequestObject(String userKeyToRemove)
  				{
  				
  					tinyDb = new TinyDB(mContext);
  					
  					int indexToRemove = 0;
  					
  					boolean removeIndex= false;
  					
  					ArrayList<FriendRequestObject> theRequestObjects = retrieveFriendRequestObjectsFromPreferences();
  					
  					
  					for(int i=0;i<theRequestObjects.size();i++)
  					{
  						if(theRequestObjects.get(i).getUserKey().equals(userKeyToRemove))
  						{
  							indexToRemove= i;
  							removeIndex = true;
  							break;
  						}
  					}
  					
  					if(removeIndex)
  					{
  											
  						theRequestObjects.remove(indexToRemove);
  					}
  					
  				 	saveFriendRequestObjectsInPreferences(theRequestObjects);
  					
  				}
  	
  		
  	     //Save Users
  	     public void saveUsersInPreferences(ArrayList<User> userObjects) {
  			
  			    	Gson gson = new Gson();
  		 			String objectsString = gson.toJson(userObjects);
  		 			
  		 			tinyDb = new TinyDB(mContext);
  		 			tinyDb.putString("Users", objectsString);
  		  	     	   	
  				}
  	     
  	   public void blockUser(String userKey)
	    {
	    	ArrayList<String> blockedUsers = retrieveBlockedUsersFromPrefs();
	    	
	    	blockedUsers.add(userKey);
	    	    	
	     	   	
	     	tinyDb.putList("BlockedUsers", blockedUsers);
	    	    	
	    }
		
		public ArrayList<String> retrieveBlockedUsersFromPrefs()
		{
	    	ArrayList<String> blockedUsers= new ArrayList<String>();
			tinyDb = new TinyDB(mContext);
				
			blockedUsers = tinyDb.getList("BlockedUsers");
			
			return blockedUsers;
		}
		
		 public boolean isBlockedUser(String userKey) {
				// TODO Auto-generated method stub
				ArrayList<String> blockedUsers= retrieveBlockedUsersFromPrefs();
				
				if (blockedUsers != null && blockedUsers.size() > 0 && !StringUtils.isStringEmpty(userKey))
				{
						for (int i=0;i<blockedUsers.size();i++) 
						{
							if (!StringUtils.isStringEmpty(userKey) && blockedUsers.get(i).equals(userKey)) {
								return true;
							}
						}
				}
					return false;
				}
		
		 public void unBlockUser(String userKey) {
				// TODO Auto-generated method stub
				ArrayList<String> blockedUsers = new ArrayList<String>();
				tinyDb = new TinyDB(mContext);
					
				blockedUsers = tinyDb.getList("BlockedUsers");  
				blockedUsers.remove(userKey);
				
				tinyDb.putList("BlockedUsers", blockedUsers);
				
			}
		 
		 
		//Mute Group
		 
		 public void muteGroup(String groupId)
		 {
		 	ArrayList<String> oldGroups = retrieveMyMuteGroupsFromPrefs();
		 	
		 	oldGroups.add(groupId);
		 	    	
		  	   	
		  	tinyDb.putList("MyMuteGroups", oldGroups);
		 	    	
		 }

		 public ArrayList<String> retrieveMyMuteGroupsFromPrefs()
		 {
		 	ArrayList<String> myMuteGroups= new ArrayList<String>();
		 	tinyDb = new TinyDB(mContext);
		 		
		 	myMuteGroups = tinyDb.getList("MyMuteGroups");
		 	
		 	return myMuteGroups;
		 }


		  public boolean isMuteGroup(String groupId) {
		 		// TODO Auto-generated method stub
		 		ArrayList<String> oldGroups= retrieveMyMuteGroupsFromPrefs();
		 		
		 		if (oldGroups != null && oldGroups.size() > 0 && !StringUtils.isStringEmpty(groupId))
		 		{
		 				for (int i=0;i<oldGroups.size();i++) 
		 				{
		 					if (!StringUtils.isStringEmpty(groupId) && oldGroups.get(i).equals(groupId)) {
		 						return true;
		 					}
		 				}
		 		}
		 			return false;
		 		}

		  public void unMuteGroup(String groupId) {
		 		// TODO Auto-generated method stub
		 		ArrayList<String> oldGroups = new ArrayList<String>();
		 		tinyDb = new TinyDB(mContext);
		 			
		 		oldGroups = tinyDb.getList("MyMuteGroups");  
		 		
		 		oldGroups.remove(groupId);
		 		
		 		
		 		tinyDb.putList("MyMuteGroups", oldGroups);
		 		
		 	}
		  
		   public void saveNumberOfUnreadMessagesToPrefs(int number) {
				// TODO Auto-generated method stub
				
			    	tinyDb = new TinyDB(mContext);
			    	tinyDb.putInt("TotalUnread", number);
			   
			}
			
			public int retrieveNumberOfUnreadMessagesFromPrefs()
		    {
		    	tinyDb = new TinyDB(mContext);
		    	
		    	int unread= tinyDb.getInt("TotalUnread") + retrieveFriendRequestObjectsFromPreferences().size();
		    	return unread;
		    	
		    }
			
			 public void saveNumberOfUnreadGroupMessagesToPrefs(int number) {
					// TODO Auto-generated method stub
					
				    	tinyDb = new TinyDB(mContext);
				    	tinyDb.putInt("TotalGroupUnread", number);
				   
				}
				
				public int retrieveNumberOfUnreadGroupMessagesFromPrefs()
			    {
			    	tinyDb = new TinyDB(mContext);
			    	return tinyDb.getInt("TotalGroupUnread");
			    }
				
//-------------------------------End Chat Stuff------------------------------------//
				
				//Location Stuff
				public  void saveCurrentLocationToPrefs(String loc)
			    {
			    	tinyDb = new TinyDB(mContext);
			    	tinyDb.putString("CurrentLocation", loc);
			    	
			    }
			    
			    public String retrieveCurrentLocationFromPrefs()
			    {
			    	tinyDb = new TinyDB(mContext);
			    	
			    	String loc = tinyDb.getString("CurrentLocation");
			    	
			    	return loc;
			    	
			    }
			    
			    public  void saveCurrentLocationNameToPrefs(String locName)
			    {
			    	tinyDb = new TinyDB(mContext);
			    	tinyDb.putString("CurrentLocationName", locName);
			    	
			    }
			    
			    public String retrieveCurrentLocationNameFromPrefs()
			    {
			    	tinyDb = new TinyDB(mContext);
			    	
			    	String locName = tinyDb.getString("CurrentLocationName");
			    	
			    	return locName;
			    	
			    }


	//-------------------------Start App Stuff------

	public void savePaymentObjectsToPrefs(ArrayList<Payment> paymentObjects)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(paymentObjects);
		tinyDb.putString("PaymentObjects", objectsString);

	}

	public void addPaymentObjectToPrefs(Payment paymentObject)
	{
		ArrayList<Payment> oldPayments= retrievePaymentObjectsFromPrefs();

		if(!isPaymentAdded(paymentObject.getTrxId()))
		{
			oldPayments.add(paymentObject);
		}
		savePaymentObjectsToPrefs(oldPayments);

	}


	//Retrieve Payment Objects
	public ArrayList<Payment> retrievePaymentObjectsFromPrefs()
	{
		ArrayList<Payment> reqObjects= new ArrayList<Payment>();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("PaymentObjects");
			Type type = new TypeToken <ArrayList<Payment>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<Payment> theObjects= (ArrayList<Payment>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				reqObjects= theObjects;
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		Collections.sort(reqObjects, new Comparator<Payment>() {
			@Override
			public int compare(Payment o1, Payment o2) {
				// Sorting in ascending order, modify as needed
				return Long.compare(o1.getPaymentDate(), o2.getPaymentDate());
			}
		});
//
		Collections.reverse(reqObjects);


		return reqObjects;
	}

	//Retrieve Payment
	public Payment retrievePaymentFromPrefs(String paymentId)
	{
		Payment reqObject= new Payment();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("PaymentObjects");
			Type type = new TypeToken <ArrayList<Payment>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<Payment> theObjects= (ArrayList<Payment>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				for(int i =0;i< theObjects.size();i++)
				{
					String pId = ""+theObjects.get(i).getTrxId();
					if(pId.equals(paymentId))
					{
						reqObject= theObjects.get(i);
						break;
					}
				}

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

	public boolean isPaymentAdded(String payment_id)
	{
		boolean isPaymentAlreadyAdded= false;
		ArrayList<Payment> requestObjects=   retrievePaymentObjectsFromPrefs();


		for(int i=0;i<requestObjects.size();i++)
		{

			Payment pObject= requestObjects.get(i) ;

			if(pObject.getTrxId().equals(payment_id))
			{
				//Payment Already Added
				isPaymentAlreadyAdded= true;
				break;
			}

		}

		return isPaymentAlreadyAdded;

	}

	//Aseset Objects
	public void saveAssetObjectObjectsToPrefs(ArrayList<AssetObject> assetObjectObjects)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(assetObjectObjects);
		tinyDb.putString("AssetObjects", objectsString);

	}

	public void addAssetObjectObjectToPrefs(AssetObject assetObjectObject)
	{
		ArrayList<AssetObject> oldAssetObjects= retrieveAssetObjectObjectsFromPrefs();

		if(!isAssetObjectAdded(assetObjectObject.getLoanNumber()))
		{
			oldAssetObjects.add(assetObjectObject);
		}
		saveAssetObjectObjectsToPrefs(oldAssetObjects);

	}


	//Retrieve AssetObject Objects
	public ArrayList<AssetObject> retrieveAssetObjectObjectsFromPrefs()
	{
		ArrayList<AssetObject> reqObjects= new ArrayList<AssetObject>();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("AssetObjects");
			Type type = new TypeToken <ArrayList<AssetObject>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<AssetObject> theObjects= (ArrayList<AssetObject>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				reqObjects= theObjects;
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		Collections.reverse(reqObjects);

		return reqObjects;
	}

	//Retrieve AssetObject
	public AssetObject retrieveAssetObjectFromPrefs(String assetObjectId)
	{
		AssetObject reqObject= new AssetObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("AssetObjects");
			Type type = new TypeToken <ArrayList<AssetObject>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<AssetObject> theObjects= (ArrayList<AssetObject>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				for(int i =0;i< theObjects.size();i++)
				{
					String pId = ""+theObjects.get(i).getLoanNumber();
					if(pId.equals(assetObjectId))
					{
						reqObject= theObjects.get(i);
						break;
					}
				}

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

	public boolean isAssetObjectAdded(String assetObject_id)
	{
		boolean isAssetObjectAlreadyAdded= false;
		ArrayList<AssetObject> requestObjects=   retrieveAssetObjectObjectsFromPrefs();


		for(int i=0;i<requestObjects.size();i++)
		{

			AssetObject pObject= requestObjects.get(i) ;

			if(pObject.getLoanNumber().equals(assetObject_id))
			{
				//AssetObject Already Added
				isAssetObjectAlreadyAdded= true;
				break;
			}

		}

		return isAssetObjectAlreadyAdded;

	}



	public void saveCurrentAssetObjectToPrefs(AssetObject asset)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(asset);
		tinyDb.putString("CurrentAssetObject", objectsString);

	}





	//Retrieve CurrentAsset
	public AssetObject retrieveCurrentAssetObjectFromPrefs()
	{
		AssetObject reqObject= new AssetObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("CurrentAssetObject");
			Type type = new TypeToken <AssetObject>(){}.getType();

			@SuppressWarnings("unchecked")
			AssetObject theObject= (AssetObject) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;


			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}


	public void saveCurrentContractObjectToPrefs(ContractObject contract)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(contract);
		tinyDb.putString("CurrentContractObject", objectsString);

	}




	//Retrieve CurrentContract
	public ContractObject retrieveCurrentContractObjectFromPrefs()
	{
		ContractObject reqObject= new ContractObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("CurrentContractObject");
			Type type = new TypeToken <ContractObject>(){}.getType();

			@SuppressWarnings("unchecked")
			ContractObject theObject= (ContractObject) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;


			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

//LoanListItemObject

	public void saveLoanListItemObjectObjectsToPrefs(ArrayList<LoanListItemObject> loanListItemObjectObjects)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(loanListItemObjectObjects);
		tinyDb.putString("LoanListItemObjectObjects", objectsString);

	}

	public void addLoanListItemObjectToPrefs(LoanListItemObject loanListItemObjectObject)
	{
		ArrayList<LoanListItemObject> oldLoanListItemObjects= retrieveLoanListItemObjectObjectsFromPrefs();

		if(!isLoanListItemObjectAdded(loanListItemObjectObject.getLoanNumber()))
		{
			oldLoanListItemObjects.add(loanListItemObjectObject);
		}
		saveLoanListItemObjectObjectsToPrefs(oldLoanListItemObjects);

	}


	//Retrieve LoanListItemObject Objects
	public ArrayList<LoanListItemObject> retrieveLoanListItemObjectObjectsFromPrefs()
	{
		ArrayList<LoanListItemObject> reqObjects= new ArrayList<LoanListItemObject>();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("LoanListItemObjectObjects");
			Type type = new TypeToken <ArrayList<LoanListItemObject>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<LoanListItemObject> theObjects= (ArrayList<LoanListItemObject>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				reqObjects= theObjects;
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		Collections.reverse(reqObjects);

		return reqObjects;
	}

	//Retrieve LoanListItemObject
	public LoanListItemObject retrieveLoanListItemObjectFromPrefs(String loanListItemObjectId)
	{
		LoanListItemObject reqObject= new LoanListItemObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("LoanListItemObjectObjects");
			Type type = new TypeToken <ArrayList<LoanListItemObject>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<LoanListItemObject> theObjects= (ArrayList<LoanListItemObject>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				for(int i =0;i< theObjects.size();i++)
				{
					String pId = ""+theObjects.get(i).getLoanNumber();
					if(pId.equals(loanListItemObjectId))
					{
						reqObject= theObjects.get(i);
						break;
					}
				}

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

	public boolean isLoanListItemObjectAdded(String loanListItemObject_id)
	{
		boolean isLoanListItemObjectAlreadyAdded= false;
		ArrayList<LoanListItemObject> requestObjects=   retrieveLoanListItemObjectObjectsFromPrefs();


		for(int i=0;i<requestObjects.size();i++)
		{

			LoanListItemObject pObject= requestObjects.get(i) ;

			if(pObject.getLoanNumber().equals(loanListItemObject_id))
			{
				//LoanListItemObject Already Added
				isLoanListItemObjectAlreadyAdded= true;
				break;
			}

		}

		return isLoanListItemObjectAlreadyAdded;

	}


	public void saveCurrentUserObjectToPrefs(User user)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(user);
		tinyDb.putString("CurrentUserObject", objectsString);

	}



	//Notification Object
	public void saveNotificationObjectObjectsToPrefs(ArrayList<NotificationObject> notificationObjectObjects)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(notificationObjectObjects);
		tinyDb.putString("NotificationObjectObjects", objectsString);

	}

	public void addNotificationObjectToPrefs(NotificationObject notificationObjectObject)
	{
		ArrayList<NotificationObject> oldNotificationObjects= retrieveNotificationObjectObjectsFromPrefs();

		if(!isNotificationObjectAdded(notificationObjectObject.getId()))
		{
			oldNotificationObjects.add(notificationObjectObject);
		}
		saveNotificationObjectObjectsToPrefs(oldNotificationObjects);

	}

	// Category of a push notification tapped on an activity (Login/Splash) that has
	// no viewPager of its own — consumed by MainActivity once it starts, to pick the tab.
	public void savePendingNotificationCategoryToPrefs(String category)
	{
		tinyDb.putString("PendingNotificationCategory", category);
	}

	public String retrievePendingNotificationCategoryFromPrefs()
	{
		tinyDb = new TinyDB(mContext);
		return tinyDb.getString("PendingNotificationCategory");
	}

	public void clearPendingNotificationCategoryFromPrefs()
	{
		tinyDb.putString("PendingNotificationCategory", "");
	}


	//Retrieve NotificationObject Objects
	public ArrayList<NotificationObject> retrieveNotificationObjectObjectsFromPrefs()
	{
		ArrayList<NotificationObject> reqObjects= new ArrayList<NotificationObject>();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("NotificationObjectObjects");
			Type type = new TypeToken <ArrayList<NotificationObject>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<NotificationObject> theObjects= (ArrayList<NotificationObject>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				reqObjects= theObjects;
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		Collections.reverse(reqObjects);

		return reqObjects;
	}

	public boolean isNotificationObjectAdded(String notificationObject_id)
	{
		boolean isNotificationObjectAlreadyAdded= false;
		ArrayList<NotificationObject> requestObjects=   retrieveNotificationObjectObjectsFromPrefs();


		for(int i=0;i<requestObjects.size();i++)
		{

			NotificationObject pObject= requestObjects.get(i) ;

			if(pObject.getId().equals(notificationObject_id))
			{
				//NotificationObject Already Added
				isNotificationObjectAlreadyAdded= true;
				break;
			}

		}

		return isNotificationObjectAlreadyAdded;

	}



	//Retrieve CurrentUser
	public User retrieveCurrentUserObjectFromPrefs()
	{
		User reqObject= new User();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("CurrentUserObject");
			Type type = new TypeToken <User>(){}.getType();

			@SuppressWarnings("unchecked")
			User theObject= (User) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;


			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}





	public void saveCurrentLoanCompleteIconObjectToPrefs(LoanCompleteIconObject adCard)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(adCard);
		tinyDb.putString("CurrentLoanCompleteIconObject", objectsString);

	}

	public LoanCompleteIconObject retrieveCurrentLoanCompleteIconObjectFromPrefs()
	{
		LoanCompleteIconObject reqObject= new LoanCompleteIconObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("CurrentLoanCompleteIconObject");
			Type type = new TypeToken <LoanCompleteIconObject>(){}.getType();

			@SuppressWarnings("unchecked")
			LoanCompleteIconObject theObject= (LoanCompleteIconObject) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;


			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

	//PaymentProvider Object
	public void savePaymentProvidersToPrefs(ArrayList<ProvidersItem> providersItemObjects)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(providersItemObjects);
		tinyDb.putString("ProvidersItemObjects", objectsString);

	}




	//Retrieve ProvidersItem Objects
	public ArrayList<ProvidersItem> retrieveProvidersFromPrefs()
	{
		ArrayList<ProvidersItem> reqObjects= new ArrayList<ProvidersItem>();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("ProvidersItemObjects");
			Type type = new TypeToken <ArrayList<ProvidersItem>>(){}.getType();

			@SuppressWarnings("unchecked")
			ArrayList<ProvidersItem> theObjects= (ArrayList<ProvidersItem>) gson.fromJson(objectsString, type);

			if(theObjects!= null)
			{
				reqObjects= theObjects;
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		Collections.reverse(reqObjects);

		return reqObjects;
	}




	public  void saveOTPKnoxEnabledToPrefs(boolean value)
	{
		tinyDb.putBoolean("OTPKnoxEnabled", value);

	}


	public boolean isOTPKnoxEnabledFromPrefs()
	{
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("OTPKnoxEnabled");

	}


	public  void saveOTPSMSEnabledToPrefs(boolean value)
	{
		tinyDb.putBoolean("OTPSMSEnabled", value);

	}


	public boolean isOTPSMSEnabledFromPrefs()
	{
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("OTPSMSEnabled");

	}

	public  void saveOTPWhatsAppEnabledToPrefs(boolean value)
	{
		tinyDb.putBoolean("OTPWhatsAppEnabled", value);

	}


	public boolean isOTPWhatsAppEnabledFromPrefs()
	{
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("OTPWhatsAppEnabled");

	}

	public void saveUserChannelsToPrefs(String[] channels) {
		Gson gson = new Gson();
		String objectsString = gson.toJson(channels);
		tinyDb.putString("UserChannels", objectsString);

	}

	public String[] retrieveUserChannelsFromPrefs() {
		Gson gson = new Gson();
		String[] channels = new String[0];
		String objectsString = tinyDb.getString("UserChannels");
		Type type = new TypeToken <String[]>(){}.getType();

		@SuppressWarnings("unchecked")
		String[] theObjects= gson.fromJson(objectsString, type);

		if(theObjects!= null)
		{
			channels = theObjects;
		}

		return channels;

	}

    public void saveUSerFingeprintEnabled(boolean value)
	{
		tinyDb.putBoolean("UserFingerprintEnabled", value);
    }

	public boolean isUserFingerprintEnabled()
	{
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("UserFingerprintEnabled");
	}

	public  void saveSupportNumberToPrefs(String number)
	{
		tinyDb = new TinyDB(mContext);
		tinyDb.putString("SupportNumber", number);

	}

	public String retrieveSupportNumberFromPrefs()
	{
		tinyDb = new TinyDB(mContext);

		String number = tinyDb.getString("SupportNumber");
		if(StringUtils.isStringEmpty(number)) number="08142967729";
		return number;

	}


	public  void saveWhatsAppNumberToPrefs(String number)
	{
		tinyDb = new TinyDB(mContext);
		tinyDb.putString("WhatsappNumber", number);

	}

	public String retrieveWhatsAppNumberFromPrefs()
	{
		tinyDb = new TinyDB(mContext);

		String number = tinyDb.getString("WhatsappNumber");
		if(StringUtils.isStringEmpty(number)) number="255777716615";
		return number;

	}

	//Save Current Upgarde Object To Prefs
	public void saveCurrentUpgradeDevicesItemToPrefs(UpgradeDevicesItem asset)
	{
		Gson gson = new Gson();
		String objectsString = gson.toJson(asset);
		tinyDb.putString("CurrentUpgradeDevicesItem", objectsString);

	}



	//Retrieve CurrentAsset
	public UpgradeDevicesItem retrieveCurrentUpgradeDevicesItemFromPrefs()
	{
		UpgradeDevicesItem reqObject= new UpgradeDevicesItem();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("CurrentUpgradeDevicesItem");
			Type type = new TypeToken <UpgradeDevicesItem>(){}.getType();

			@SuppressWarnings("unchecked")
			UpgradeDevicesItem theObject= (UpgradeDevicesItem) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;


			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}

	public void saveUpgradeFeatureEnabledToPrefs(boolean upgrade_feature_enabled) {
		tinyDb.putBoolean("upgrade_feature_enabled", upgrade_feature_enabled);
	}

	public boolean isUpgradeFeatureEnabled() {
		return tinyDb.getBoolean("upgrade_feature_enabled");
	}


	public boolean showWhatsAppOnError()
	{
		return tinyDb.getBoolean("ShowWhatsAppOnError");
	}

	public void saveShowWhatsAppOnError(boolean value)
	{
		tinyDb.putBoolean("ShowWhatsAppOnError", value);
	}

	public void saveShowCallSupportOptionsOnError(boolean value) {
		tinyDb.putBoolean("ShowCallSupportOptionsOnErrorPage", value);
	}

	public boolean showCallSupportOptionsOnError() {
		return tinyDb.getBoolean("ShowCallSupportOptionsOnErrorPage");
	}

	public boolean isDefaultNotificationAdded() {
		return tinyDb.getBoolean("DefaultNotificationAdded");
	}

	public void setDefaultWelcomeNotificationAdded(boolean value) {
		tinyDb.putBoolean("DefaultNotificationAdded", value);
	}

	public void saveSupportPageConfigToPrefs(JSONObject supportObj) {
		Gson gson = new Gson();
		String objectsString = gson.toJson(supportObj);
		tinyDb.putString("SupportPageConfigs", objectsString);
	}

	public JSONObject retrieveSupportPageConfigFromPrefs() {
		Gson gson = new Gson();
		String objectsString = tinyDb.getString("SupportPageConfigs");
		Type type = new TypeToken <JSONObject>(){}.getType();

		JSONObject theObject = null;
		try {
			theObject = gson.fromJson(objectsString, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return theObject;
	}

	public void saveLastPaymentDate(String s)
	{
        tinyDb.putString("LastPaymentDate", s);

	}

	public String retrieveLastPaymentDate() {
		tinyDb = new TinyDB(mContext);
		String lastPaymentDate = tinyDb.getString("LastPaymentDate");
		if (StringUtils.isStringEmpty(lastPaymentDate)) lastPaymentDate = "";
		return lastPaymentDate;
	}



	public void setRatingEnabled(boolean value) {

		tinyDb.putBoolean("HasRatingEnabled", value);
	}

	public boolean isRatingEnabled() {
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("HasRatingEnabled");
	}


	public  void saveMinimumRatingDaysToPrefs(int count)
	{
		tinyDb.putInt("MIN_DAYS_SINCE_INSTALL", count);

	}
	public int retrieveRatingMinDaysFromPrefs()
	{
		tinyDb = new TinyDB(mContext);

		int count = tinyDb.getInt("MIN_DAYS_SINCE_INSTALL");
		if (count <= 0) count = 30;
		return count;

	}

	//Save ServiceDownObject

	// Save ServiceDownObject (existing object)
	public void saveServiceDownObjectToPrefs(ServiceDownObject service) {
		if (service == null) return;
		Gson gson = new Gson();
		String objectsString = gson.toJson(service);
		tinyDb.putString("ServiceDownObject", objectsString);
	}

	// Save from raw JSON string (safe parsing + save)
	public void saveServiceDownObjectToPrefs(String jsonString) {
		if (jsonString == null || jsonString.trim().isEmpty()) return;
		try {
			Gson gson = new Gson();
			ServiceDownObject service = gson.fromJson(jsonString, ServiceDownObject.class);
			if (service != null) {
				saveServiceDownObjectToPrefs(service);
			} else {
				Log.w("Storage", "Parsed ServiceDownObject was null");
			}
		} catch (Exception e) {
			Log.e("Storage", "Failed to parse ServiceDownObject JSON", e);
		}
	}

	// Save from a JSONObject (convenience)
	public void saveServiceDownObjectToPrefs(JSONObject jsonObject) {
		if (jsonObject == null) return;
		saveServiceDownObjectToPrefs(jsonObject.toString());
	}



	//Retrieve ServiceDownObject
	public ServiceDownObject retrieveServiceDownObjectFromPrefs()
	{
		ServiceDownObject reqObject= new ServiceDownObject();

		try
		{
			Gson gson = new Gson();

			String objectsString = tinyDb.getString("ServiceDownObject");
			Type type = new TypeToken <ServiceDownObject>(){}.getType();

			@SuppressWarnings("unchecked")
			ServiceDownObject theObject= (ServiceDownObject) gson.fromJson(objectsString, type);

			if(theObject!= null)
			{
				reqObject= theObject;

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return reqObject;
	}


	//End Service Down


	//Data COllector
	public void saveAndroidIDObjectToPrefs(String androidId) {
		tinyDb.putString("AndroidID", androidId);
	}

	public String retrieveAndroidIDObjectFromPrefs() {

		String aID = tinyDb.getString("AndroidID");

		if (StringUtils.isStringEmpty(aID)) {
			aID = UUID.randomUUID().toString();
			saveAndroidIDObjectToPrefs(aID);
		}

		return aID;
	}


	public String saveLastAppEventToPrefs() {
		return tinyDb.getString("LastAppEvent");
	}
	public String retrieveLastAppEventFromPrefs() {
		return tinyDb.getString("LastAppEvent");
	}

	public void saveTimeLastOnline(long currentTimeMillis) {

		tinyDb.putLong("TimeLastOnline", currentTimeMillis);
	}

	public long retrieveTimeLastOnline() {
		return tinyDb.getLong("TimeLastOnline");
	}

	public void saveLogDataUsageEveryLaunchToPrefs(boolean should_log_data_usage_every_launch) {
		tinyDb.putBoolean("LogDataUsageEveryLaunch", should_log_data_usage_every_launch);
	}

	public boolean shouldLogDataUsageEveryLaunchFromPrefs()
	{
		return tinyDb.getBoolean("LogDataUsageEveryLaunch");
	}


	public void saveLastLogSendDate(long currentTimeMillis) {

		tinyDb.putLong("LastLogSendDate", currentTimeMillis);
	}

	public long retrieveLastLogSendDate() {
		return tinyDb.getLong("LastLogSendDate");
	}

	public boolean isUpgradeTestTwoEnabled() {
		return tinyDb.getBoolean("UpgradeTestTwoEnabled");
	}


	public void setEligibleForUpgrade(boolean value) {
		tinyDb.putBoolean("CanBeEligibleForUpgrade", value);

	}

	public boolean canBeEligibleForUpgrade() {
		tinyDb = new TinyDB(mContext);
		return tinyDb.getBoolean("CanBeEligibleForUpgrade");
	}

	public void setUpgradeOptionsAvailable(boolean value) {
		tinyDb.putBoolean("UpgradeOptionsAvailable", value);
	}

    //End Data COllector


	public  void saveUserPagaNumberToPrefs(String gender)
	{
		tinyDb.putString("UserPagaNumber", gender);

	}
	public String retrieveUserPagaNumberFromPrefs()
	{
		return tinyDb.getString("UserPagaNumber");

	}

    public boolean checkPermissionsWizardComplete()
    {
        return tinyDb.getBoolean("IsPermissionWizardComplete");
    }

    public void setPermissionsWizardComplete(boolean complete)
    {
        tinyDb.putBoolean("IsPermissionWizardComplete", complete);

    }

    public void saveForceAuthenticateEveryLaunch(boolean value) {
        tinyDb.putBoolean("ForceAuthenticateEveryLaunch", value);
    }

    public boolean shouldForceAuthenticateEveryLaunch() {
        return tinyDb.getBoolean("ForceAuthenticateEveryLaunch");
    }


    public  void saveCurrentLocationLatToPrefs(double version) {
        tinyDb = new TinyDB(mContext);
        tinyDb.putString("Latitude", String.valueOf(version));

    }

    public String retrieveCurrentLocationLatFromPrefs()
    {
        tinyDb = new TinyDB(mContext);

        String latitude = tinyDb.getString("Latitude");
        return latitude;

    }

    public void saveCurrentLocationLongToPrefs(double longitude) {
        tinyDb = new TinyDB(mContext);
        tinyDb.putString("Longitude", String.valueOf(longitude));

    }

    public String retrieveCurrentLocationLongFromPrefs() {
        tinyDb = new TinyDB(mContext);

        String longitude = tinyDb.getString("Longitude");
        return longitude;

    }

}