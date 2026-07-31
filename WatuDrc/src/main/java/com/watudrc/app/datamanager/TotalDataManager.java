package com.watudrc.app.datamanager;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;



public class TotalDataManager implements IWhereMyLocationConstants {

	public static final String TAG = TotalDataManager.class.getSimpleName();

	private static TotalDataManager totalDataManager;
	private static Location currentLocation;
	StoreAndGetFromPrefs storageObject;
	public static Context mContext;
	


	public TotalDataManager(Context theContext) 
	{
      mContext = theContext;
      storageObject= new StoreAndGetFromPrefs(mContext);
	}

	public Location getCurrentLocation() {
		// TODO Auto-generated method stub
		
		if(storageObject.retrieveCurrentLocationFromPrefs()!="")
		{
			Location loc= getActualLocation(storageObject.retrieveCurrentLocationFromPrefs());
			currentLocation= loc;
		}
				
		return currentLocation;
	}

	public void setCurrentLocation(Location mLastLocation) {
		// TODO Auto-generated method stub
		
		if(mLastLocation != null)
		{
			currentLocation = mLastLocation;
			String currentLoc= currentLocation.getLatitude()+","+currentLocation.getLongitude();
			storageObject.saveCurrentLocationToPrefs(currentLoc);
		}
		
	}
	
	public String getCurrentLocationName() {
		// TODO Auto-generated method stub
				
		return storageObject.retrieveCurrentLocationNameFromPrefs();
	}

	public void setCurrentLocationName(String name)
	{
		// TODO Auto-generated method stub
		
		storageObject.saveCurrentLocationNameToPrefs(name);
		
	}
	public Location getActualLocation(String loc)
	 {
		 
		 Location theLoc = new Location ("POINT");
		 
		
		 List<String> location = Arrays.asList(loc.split("\\s*,\\s*"));
		 
		 theLoc.setLatitude(Double.valueOf(location.get(0)));
		 
		 theLoc.setLongitude(Double.valueOf(location.get(1)));	
		 
		 
		 return theLoc;
			 
		 
	 }

	

}
