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
import android.widget.RelativeLayout;

import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.objects.Contact;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import java.util.ArrayList;


public class ContactsListAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable{

	    ViewHolder viewHolder;
        private ArrayList<Contact> mItems = new ArrayList<Contact>();
        private ArrayList<Contact> mOriginalItems= new ArrayList<Contact>();

        private Context mContext;
        static StoreAndGetFromPrefs storageObject;
        LayoutInflater vi;

        static Activity parentActivity;
        String shareMessage="";




        public ContactsListAdapter(Activity activity, ArrayList<Contact> list, String shareMsg) {
            parentActivity = activity;
            mContext = parentActivity.getApplicationContext();

            mItems = list;
            mOriginalItems = list;

            storageObject = new StoreAndGetFromPrefs(mContext);
            shareMessage= shareMsg;
           


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
            	
                v = vi.inflate(R.layout.list_item_contact, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                
                viewHolder.contactName = (FirmaTextView) v.findViewById(R.id.contactName);
				viewHolder.contactPhone= (FirmaTextView) v.findViewById(R.id.contactPhone);

              
                // store the holder with the view.
                v.setTag(viewHolder);
                 
            }
            
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            
            String cName= mItems.get(position).getName();
			final String cPhone= mItems.get(position).getPhone();


            
            viewHolder.contactName.setText(cName);
			viewHolder.contactPhone.setText(cPhone);

			RelativeLayout btnShareToContact=  v.findViewById(R.id.btnShareToContact);

			btnShareToContact.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address"  , cPhone);
                    smsIntent.putExtra("sms_body"  , shareMessage);
                    parentActivity.startActivity(smsIntent);
				}
			});

                         
            
            return v;
        }
        
        
        
        
        static class ViewHolder {
        	 
        	  FirmaTextView contactName, contactPhone;
        	  int position;
        }
        
        
        @Override
    	public Filter getFilter() {
    		// TODO Auto-generated method stub
    		Filter filter = new Filter() {

    	        @SuppressWarnings("unchecked")
    	        @Override
    	        protected void publishResults(CharSequence constraint,FilterResults results) {

    	            mItems = (ArrayList<Contact>) results.values; // has the filtered values
    	            notifyDataSetChanged();  // notifies the data with new filtered values
    	        }

    	        @Override
    	        protected FilterResults performFiltering(CharSequence constraint) {
    	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
    	            ArrayList<Contact> FilteredArrList = new ArrayList<Contact>();

    	            if (mOriginalItems == null) {
    	                mOriginalItems = new ArrayList<Contact>(mItems); // saves the contactal data in mContactalValues
    	            }

    	            /********
    	             * 
    	             *  If constraint(CharSequence that is received) is null returns the mContactalValues(Contactal) values
    	             *  else does the Filtering and returns FilteredArrList(Filtered)  
    	             *
    	             ********/
    	            if (constraint == null || constraint.length() == 0) {

    	                // set the Contactal result to return  
    	                results.count = mOriginalItems.size();
    	                results.values = mOriginalItems;
    	            } else {
    	                constraint = constraint.toString().toLowerCase();
    	                for (int i = 0; i < mOriginalItems.size(); i++) {
    	                    String data = mOriginalItems.get(i).getName();
    	                    if (data.toLowerCase().contains(constraint.toString())) {
    	                        
    	                    	Contact item = mOriginalItems.get(i);
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
        
        
       
    }