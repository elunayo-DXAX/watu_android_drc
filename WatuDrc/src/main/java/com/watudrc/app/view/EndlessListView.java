package com.watudrc.app.view;


import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.watudrc.app.adapters.TransactionsListAdapter;
import com.watudrc.app.objects.Payment;

public class EndlessListView extends ListView implements OnScrollListener {

   private View footer;
   private boolean isLoading;
   private EndlessListener listener;
   private TransactionsListAdapter adapter;

   Handler mHandler = new Handler();

   public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.setOnScrollListener(this);

   }

   public EndlessListView(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.setOnScrollListener(this);
   }

   public EndlessListView(Context context) {
      super(context);
      this.setOnScrollListener(this);
   }




   @Override
   public void onScrollStateChanged(AbsListView view, int scrollState) {}

   @Override
   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

   }

   public void setLoadingView(int resId) {
      LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      footer = (View) inflater.inflate(resId, null);
      this.addFooterView(footer);

   }


   public void setAdapter(TransactionsListAdapter adapter) {
      super.setAdapter(adapter);
      this.adapter = adapter;
      this.removeFooterView(footer);
   }


   public void addNewData(ArrayList<Payment> data) {

      this.removeFooterView(footer);
      adapter.addItems(data);
      adapter.notifyDataSetChanged();
      isLoading = false;
   }

   public void setListener(EndlessListener listener) {
      this.listener = listener;
   }

   public interface EndlessListener {

      public void onObjectReady(String title);
      public void loadMoreData() ;
   }



}
