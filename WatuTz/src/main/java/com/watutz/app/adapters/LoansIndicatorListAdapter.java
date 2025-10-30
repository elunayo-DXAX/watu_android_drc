package com.watutz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.watutz.app.GeneralCommands;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.objects.AssetObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import java.util.ArrayList;


public class LoansIndicatorListAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable{

	    ViewHolder viewHolder;
        private ArrayList<AssetObject> mItems = new ArrayList<AssetObject>();
        private ArrayList<AssetObject> mOriginalItems= new ArrayList<AssetObject>();

        private Context mContext;
        static StoreAndGetFromPrefs storageObject;
        LayoutInflater vi;

        static Activity parentActivity;




        public LoansIndicatorListAdapter(Activity activity, ArrayList<AssetObject> list) {
            parentActivity = activity;
            mContext = parentActivity.getApplicationContext();

            mItems = list;
            mOriginalItems = list;

            storageObject = new StoreAndGetFromPrefs(mContext);


        }
      

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
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
            	
                v = vi.inflate(R.layout.list_item_indicator, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
				viewHolder.icon = (ImageView) v.findViewById(R.id.icon);



				// store the holder with the view.
                v.setTag(viewHolder);
                 
            }
            
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }



			boolean isSelected= mItems.get(position).isSelected();
			if(isSelected)
			{
				viewHolder.icon.setImageResource(R.xml.rounded_circle_theme_small);
			}
			else {
				viewHolder.icon.setImageResource(R.xml.rounded_circle_grey_small);

			}


			return v;
        }

	public ArrayList<AssetObject> getItems()
	{
		return mItems;

	}


	static class ViewHolder {

			ImageView icon;


        	  int position;
        }
        
        
        @Override
    	public Filter getFilter() {
    		// TODO Auto-generated method stub
    		Filter filter = new Filter() {

    	        @SuppressWarnings("unchecked")
    	        @Override
    	        protected void publishResults(CharSequence constraint,FilterResults results) {

    	            mItems = (ArrayList<AssetObject>) results.values; // has the filtered values
    	            notifyDataSetChanged();  // notifies the data with new filtered values
    	        }

    	        @Override
    	        protected FilterResults performFiltering(CharSequence constraint) {
    	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
    	            ArrayList<AssetObject> FilteredArrList = new ArrayList<AssetObject>();

    	            if (mOriginalItems == null) {
    	                mOriginalItems = new ArrayList<AssetObject>(mItems); // saves the assetObjectal data in mAssetObjectalValues
    	            }

    	            /********
    	             * 
    	             *  If constraint(CharSequence that is received) is null returns the mAssetObjectalValues(AssetObjectal) values
    	             *  else does the Filtering and returns FilteredArrList(Filtered)  
    	             *
    	             ********/
    	            if (constraint == null || constraint.length() == 0) {

    	                // set the AssetObjectal result to return  
    	                results.count = mOriginalItems.size();
    	                results.values = mOriginalItems;
    	            } else {
    	                constraint = constraint.toString().toLowerCase();
    	                for (int i = 0; i < mOriginalItems.size(); i++) {
    	                    String data =""+ mOriginalItems.get(i).getLoanProductName();
    	                    if (data.toLowerCase().contains(constraint.toString())) {
    	                        
    	                    	AssetObject item = mOriginalItems.get(i);
    	                    	FilteredArrList.add(item);
    	                    }
    	                }
    	                // set the Filtered result to return
    	                results.count = FilteredArrList.size();
    	                results.values = FilteredArrList;
    	            }
    	            return results;
    	        }
    	    };
    	    return filter;
    	}

	public void deselectAllItems(ArrayList<AssetObject> mItems)
	{
		for(int i=0;i<mItems.size();i++)
		{
			AssetObject assetObject= (AssetObject) mItems.get(i);
			assetObject.setSelected(false);
		}

		notifyDataSetChanged();
	}


    }