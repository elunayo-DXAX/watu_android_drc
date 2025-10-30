package com.watutz.app.adapters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.datamanager.ResponcePlaceResult;
import com.watutz.app.datamanager.TotalDataManager;
import com.watutz.app.datamanager.YPYNetUtils;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.prefs.StoreAndGetFromPrefs;


public class GooglePlaceAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable{
	
	    ViewHolder viewHolder;
        private ResponcePlaceResult mItems = new ResponcePlaceResult();

        private static Context mContext;
        static StoreAndGetFromPrefs storageObject;
        LayoutInflater vi;
                      
        static Activity parentActivity;
        TotalDataManager mTotalMng;



    public GooglePlaceAdapter(Activity activity, ResponcePlaceResult list) {
            parentActivity = activity;
            mContext = parentActivity.getApplicationContext();

            mItems = list;
            mTotalMng= new TotalDataManager(parentActivity);
            storageObject = new StoreAndGetFromPrefs(mContext);
        }

        @Override
        public int getCount() {
            return mItems.getListPlaceObjects().size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.getListPlaceObjects().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if(convertView==null){
                
                // inflate the layout
            	vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	
                v = vi.inflate(R.layout.list_item_places, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                
               viewHolder.placeName = (FirmaTextView) v.findViewById(R.id.placeName);
               viewHolder.placeBack = (RelativeLayout) v.findViewById(R.id.placeBack);
               viewHolder.placeImage = (ImageView) v.findViewById(R.id.placeImage);

               
              
                // store the holder with the view.
                v.setTag(viewHolder);
                 
            }
            
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            
            String placeDesc= mItems.getListPlaceObjects().get(position).getName();
            String plImageIconUrl= mItems.getListPlaceObjects().get(position).getIcon();
            
            
            viewHolder.placeName.setText(placeDesc);

			
                     
            if(mItems.getListPlaceObjects().get(position).isSelected())
            {
            

            	viewHolder.placeBack.setBackgroundColor(parentActivity.getResources().getColor(R.color.theme_orange));
            	viewHolder.placeName.setTextColor(parentActivity.getResources().getColor(R.color.white));

            }
            else
            {
             viewHolder.placeBack.setBackgroundColor(parentActivity.getResources().getColor(R.color.white));
         	 viewHolder.placeName.setTextColor(parentActivity.getResources().getColor(R.color.theme_dark));

            }
            
            
            return v;
        }
        
        
        
        
        static class ViewHolder {
        	  FirmaTextView placeName;
        	  ImageView placeImage;
        	  RelativeLayout placeBack;
        	 
        	  int position;
        }
        
        
        @Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.

						final String mCurrentLocationLatLng= storageObject.retrieveCurrentLocationFromPrefs();
						mItems = autocomplete(constraint.toString(),mCurrentLocationLatLng, mTotalMng.getCurrentLocation().getLongitude(),mTotalMng.getCurrentLocation().getLatitude());
						if(mItems !=null) {
                            // Assign the data to the FilterResults
                            filterResults.values = mItems;
                            filterResults.count = mItems.getListPlaceObjects().size();
                        }
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
		
        
        public static ResponcePlaceResult autocomplete(String input, String mCurrentLocationLatLng, double mCurrentLocationLong, double mCurrentLocationLat) {
			ResponcePlaceResult resultList = null;

			try {
				resultList = YPYNetUtils.getListPlacesBaseOnText(parentActivity, mCurrentLocationLong, mCurrentLocationLat, URLEncoder.encode(input, "utf8"), 50);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d("ZeResults","ZeResults"+resultList);
			return resultList;
		}
       
    }