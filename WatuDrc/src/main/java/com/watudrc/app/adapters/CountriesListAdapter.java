package com.watudrc.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.objects.Country;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.CircleImage;

import java.util.ArrayList;


public class CountriesListAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable{

	    ViewHolder viewHolder;
        private ArrayList<Country> mItems = new ArrayList<Country>();
        private ArrayList<Country> mOriginalItems= new ArrayList<Country>();

        private Context mContext;
        static StoreAndGetFromPrefs storageObject;
        LayoutInflater vi;

        static Activity parentActivity;




        public CountriesListAdapter(Activity activity, ArrayList<Country> list) {
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
            	
                v = vi.inflate(R.layout.list_item_country, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                
                viewHolder.countryName = (FirmaTextView) v.findViewById(R.id.countryName);
				viewHolder.countryImage= (CircleImage) v.findViewById(R.id.countryImage);

              
                // store the holder with the view.
                v.setTag(viewHolder);
                 
            }
            
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            
            String cName= mItems.get(position).getName();
			int cRes= mItems.get(position).getRes();


            
            viewHolder.countryName.setText(cName);
			viewHolder.countryImage.setImageResource(cRes);

			//RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(cRes,null);
			//dr.setCornerRadius(5);
			//imageView.setImageDrawable(dr);



			CheckBox countryChecked= v.findViewById(R.id.countryChecked);
            countryChecked.setChecked(mItems.get(position).isSelected());

            countryChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					if(checked)
					{
						deselectAllItems(mItems);
						mItems.get(position).setSelected(true);
					}
				}
			});

                         
            
            return v;
        }
        
        
        
        
        static class ViewHolder {
        	 
        	  FirmaTextView countryName;
			  CircleImage countryImage;
        	  int position;
        }
        
        
        @Override
    	public Filter getFilter() {
    		// TODO Auto-generated method stub
    		Filter filter = new Filter() {

    	        @SuppressWarnings("unchecked")
    	        @Override
    	        protected void publishResults(CharSequence constraint,FilterResults results) {

    	            mItems = (ArrayList<Country>) results.values; // has the filtered values
    	            notifyDataSetChanged();  // notifies the data with new filtered values
    	        }

    	        @Override
    	        protected FilterResults performFiltering(CharSequence constraint) {
    	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
    	            ArrayList<Country> FilteredArrList = new ArrayList<Country>();

    	            if (mOriginalItems == null) {
    	                mOriginalItems = new ArrayList<Country>(mItems); // saves the countryal data in mCountryalValues
    	            }

    	            /********
    	             * 
    	             *  If constraint(CharSequence that is received) is null returns the mCountryalValues(Countryal) values
    	             *  else does the Filtering and returns FilteredArrList(Filtered)  
    	             *
    	             ********/
    	            if (constraint == null || constraint.length() == 0) {

    	                // set the Countryal result to return  
    	                results.count = mOriginalItems.size();
    	                results.values = mOriginalItems;
    	            } else {
    	                constraint = constraint.toString().toLowerCase();
    	                for (int i = 0; i < mOriginalItems.size(); i++) {
    	                    String data = mOriginalItems.get(i).getName();
    	                    if (data.toLowerCase().contains(constraint.toString())) {
    	                        
    	                    	Country item = mOriginalItems.get(i);
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

	public void deselectAllItems(ArrayList<Country> mItems)
	{
		for(int i=0;i<mItems.size();i++)
		{
			Country country= (Country) mItems.get(i);
			country.setSelected(false);
		}

		notifyDataSetChanged();
	}
       
    }