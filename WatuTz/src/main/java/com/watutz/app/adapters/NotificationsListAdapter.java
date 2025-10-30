package com.watutz.app.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.objects.NotificationObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class NotificationsListAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable{

	    ViewHolder viewHolder;
        private ArrayList<NotificationObject> mItems = new ArrayList<NotificationObject>();
        private ArrayList<NotificationObject> mOriginalItems= new ArrayList<NotificationObject>();

        private Context mContext;
        static StoreAndGetFromPrefs storageObject;
        LayoutInflater vi;

        static Activity parentActivity;




        public NotificationsListAdapter(Activity activity, ArrayList<NotificationObject> list) {
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
			if (position >= 0 && position < mItems.size()) {
				return mItems.get(position);
			} else {
				return null; // or handle the case appropriately
			}
		}
        @Override
        public long getItemId(int position) {
            return position;
        }

	    public void addItems(ArrayList<NotificationObject> data) {
		mItems.addAll(data);
	}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if(convertView==null){
                
                // inflate the layout
            	vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	
                v = vi.inflate(R.layout.list_item_notification, null);
                 
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                
                viewHolder.title = (FirmaTextView) v.findViewById(R.id.title);
				viewHolder.message = (FirmaTextView) v.findViewById(R.id.message);
				viewHolder.indicator = (ImageView) v.findViewById(R.id.indicator);
				viewHolder.date = (FirmaTextView) v.findViewById(R.id.date);
				viewHolder.icon = (ImageView) v.findViewById(R.id.icon);


              
                // store the holder with the view.
                v.setTag(viewHolder);
                 
            }
            
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }

			NotificationObject notification= mItems.get(position);
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM, yyyy"); // You can customize the format
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss"); // You can customize the format


			String type = notification.getType() != null ? notification.getType() : "";
			String notificationId = notification.getId() != null ? notification.getId() : "";
			String title = notification.getTitle() != null ? notification.getTitle() : "";
			String body = notification.getBody() != null ? notification.getBody() : "";
			String date = notification.getDate() != null ? notification.getDate() : "";
			String time = notification.getTime() != null ? notification.getTime() : "";


			if(notification.isRead())
			{
				viewHolder.indicator.setBackgroundResource(R.xml.rounded_circle_white_small);
			}
			else
			{
				viewHolder.indicator.setBackgroundResource(R.xml.rounded_circle_green_small);

			}



			viewHolder.title.setText(title);
			viewHolder.message.setText(body);
			viewHolder.date.setText(date+" | "+time);

			if(type.contains("payment"))
			{
				viewHolder.icon.setImageResource(R.drawable.ics_notification_payment);
			}
			else if(type.contains("reminder"))
			{
				viewHolder.icon.setImageResource(R.drawable.ics_notification_reminder);
			}
			else
			{
				viewHolder.icon.setImageResource(R.drawable.ics_notification_general);
			}


            return v;
        }
        
        
        
        
        static class ViewHolder {
        	 
        	  FirmaTextView title,message,date;
			  ImageView icon, indicator;
        	  int position;
        }
        
        
        @Override
    	public Filter getFilter() {
    		// TODO Auto-generated method stub
    		Filter filter = new Filter() {

    	        @SuppressWarnings("unchecked")
    	        @Override
    	        protected void publishResults(CharSequence constraint,FilterResults results) {

    	            mItems = (ArrayList<NotificationObject>) results.values; // has the filtered values
    	            notifyDataSetChanged();  // notifies the data with new filtered values
    	        }

    	        @Override
    	        protected FilterResults performFiltering(CharSequence constraint) {
    	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
    	            ArrayList<NotificationObject> FilteredArrList = new ArrayList<NotificationObject>();

    	            if (mOriginalItems == null) {
    	                mOriginalItems = new ArrayList<NotificationObject>(mItems); // saves the contractObjectal data in mTransactionListItemalValues
    	            }

    	            /********
    	             * 
    	             *  If constraint(CharSequence that is received) is null returns the mTransactionListItemalValues(TransactionListItemal) values
    	             *  else does the Filtering and returns FilteredArrList(Filtered)  
    	             *
    	             ********/
    	            if (constraint == null || constraint.length() == 0) {

    	                // set the TransactionListItemal result to return  
    	                results.count = mOriginalItems.size();
    	                results.values = mOriginalItems;
    	            } else {
    	                constraint = constraint.toString().toLowerCase();
    	                for (int i = 0; i < mOriginalItems.size(); i++) {
    	                    String data =""+ mOriginalItems.get(i).getTitle();
    	                    if (data.toLowerCase().contains(constraint.toString())) {
    	                        
    	                    	NotificationObject item = mOriginalItems.get(i);
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