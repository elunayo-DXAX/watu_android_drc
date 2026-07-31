package com.watudrc.app;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.watudrc.app.font.FirmaTextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public enum TabIconMain {


 
    CUSTOM_TAB_ICONS(R.string.demo_title_custom_tab_icons, R.layout.tab_layout_main) {
        @Override
        public int[] tabs() {
            return new int[] {
                    R.string.demo_tab_1,
                    R.string.demo_tab_2,
                    R.string.demo_tab_3,
                    R.string.demo_tab_4,
                    R.string.demo_tab_5,

                  
            };
        }
        @SuppressLint("NewApi")
		@Override
        public void setup(final SmartTabLayout layout) {
            super.setup(layout);

            final LayoutInflater inflater = LayoutInflater.from(layout.getContext());
            final Resources res = layout.getContext().getResources();

            layout.setCustomTabView(new SmartTabLayout.TabProvider() {
                @Override
                public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                    
                	

                   RelativeLayout theView = (RelativeLayout) inflater.inflate(R.layout.tab_icon_main, container, false);
                   FirmaTextView iconText = (FirmaTextView) theView.findViewById(R.id.iconText);
                   ImageView icon = (ImageView) theView.findViewById(R.id.icon);

                 
                   switch (position) {
                        case 0:
                            iconText.setText(R.string.tab_1);
                            icon.setImageResource(R.drawable.ics_bot_home);
                            break;
                            
                        case 1:
                            iconText.setText(R.string.tab_2);
                            icon.setImageResource(R.drawable.ics_bot_transactions);
                            break;

                       case 2:
                           iconText.setText(R.string.tab_3);
                           icon.setImageResource(R.drawable.ics_bot_payments);
                           break;


                       case 3:
                           iconText.setText(R.string.notifications);
                           icon.setImageResource(R.drawable.ics_bot_notifications);
                           break;

                       case 4:
                           iconText.setText(R.string.support);
                           icon.setImageResource(R.drawable.ics_bot_support);
                           break;



                       default:
                            throw new IllegalStateException("Invalid position: " + position);
                    }
                    return theView;
                }
            });
        }
    };

    public final int titleResId;
    public final int layoutResId;

    TabIconMain(int titleResId, int layoutResId) {
        this.titleResId = titleResId;
        this.layoutResId = layoutResId;
    }

  

    public void setup(final SmartTabLayout layout) {
        //Do nothing.
    }

    public int[] tabs() {
        return tab4();
    }

    public static int[] tab4() {
        return new int[]{
                R.string.demo_tab_1,
                R.string.demo_tab_2,
                R.string.demo_tab_3,
//                R.string.demo_tab_4,

        };
    }

    public static TabIconMain getTab() {
        return TabIconMain.valueOf("CUSTOM_TAB_ICONS");
    }

}
