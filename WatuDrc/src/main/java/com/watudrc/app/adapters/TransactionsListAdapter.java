package com.watudrc.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.objects.Payment;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TransactionsListAdapter extends BaseAdapter implements IWhereMyLocationConstants, Filterable {

	ViewHolder viewHolder;
	private ArrayList<Payment> mItems = new ArrayList<Payment>();
	private ArrayList<Payment> mOriginalItems = new ArrayList<Payment>();

	private final Context mContext;
	static StoreAndGetFromPrefs storageObject;
	LayoutInflater vi;

	static Activity parentActivity;


	public TransactionsListAdapter(Activity activity, ArrayList<Payment> list) {
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

	public void addItems(ArrayList<Payment> data) {
		mItems.addAll(data);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (convertView == null) {

			// inflate the layout
			vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = vi.inflate(R.layout.list_item_transactions, null);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();

			viewHolder.transactionType = v.findViewById(R.id.transactionType);
			viewHolder.transactionCurrency = v.findViewById(R.id.transactionCurrency);
			viewHolder.transactionAmount = v.findViewById(R.id.transactionAmount);
			viewHolder.transactionId = v.findViewById(R.id.transactionId);
			viewHolder.transactionFrom = v.findViewById(R.id.transactionFrom);
			viewHolder.transactionTime = v.findViewById(R.id.transactionTime);
			viewHolder.transactionDate = v.findViewById(R.id.transactionDate);


			// store the holder with the view.
			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Payment transaction = mItems.get(position);
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM, yyyy"); // You can customize the format
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss"); // You can customize the format


		String type = transaction.getPaymentInstName();
		String transactionId = transaction.getTrxId();
		String currency = transaction.getCurrencyCode().equals("SLE") ? "SLE." : transaction.getCurrencyCode();
		String amount = "" + transaction.getAmount();
		String category = transaction.getPaymentTypeName();


		String from = transaction.getPhoneNumber();
		from = from == null ? "" : from;
		String time = sdfTime.format(transaction.getPaymentDate());
		String date = sdfDate.format(transaction.getPaymentDate());
		if (from == "") {
			viewHolder.transactionFrom.setVisibility(View.GONE);
		} else {
			viewHolder.transactionFrom.setVisibility(View.VISIBLE);

		}


		viewHolder.transactionType.setText(mContext.getResources().getString(R.string.payment));
		viewHolder.transactionCurrency.setText(currency);
		viewHolder.transactionAmount.setText(GeneralCommands.fixCommas(amount));
		viewHolder.transactionId.setText(transactionId);
		viewHolder.transactionFrom.setText(mContext.getResources().getString(R.string.loanfrom, from));
		viewHolder.transactionTime.setText(mContext.getResources().getString(R.string.at, time));
		viewHolder.transactionDate.setText(mContext.getResources().getString(R.string.on, date));


		return v;
	}


	static class ViewHolder {

		FirmaTextView transactionType, transactionCurrency, transactionAmount, transactionFrom, transactionTime, transactionDate, transactionId;
		int position;
	}


	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				mItems = (ArrayList<Payment>) results.values; // has the filtered values
				notifyDataSetChanged();  // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
				ArrayList<Payment> FilteredArrList = new ArrayList<Payment>();

				if (mOriginalItems == null) {
					mOriginalItems = new ArrayList<Payment>(mItems); // saves the contractObjectal data in mTransactionListItemalValues
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
						String data = mOriginalItems.get(i).getPaymentTypeName();
						if (data.toLowerCase().contains(constraint.toString())) {

							Payment item = mOriginalItems.get(i);
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