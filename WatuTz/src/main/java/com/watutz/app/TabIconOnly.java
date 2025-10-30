package com.watutz.app;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public enum TabIconOnly {


    CUSTOM_TAB_ICONS(R.string.demo_title_custom_tab_icons, R.layout.tab_layout_text) {
        @Override
        public int[] tabs() {
            return new int[] {
                    R.string.loantype,
                    R.string.loantype,
                    R.string.loantype,


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



                   RelativeLayout theView = (RelativeLayout) inflater.inflate(R.layout.tab_icon_only, container, false);
                   ImageView icon = (ImageView) theView.findViewById(R.id.icon);


                   switch (position) {
                        case 0:
                            break;

                        case 1:
                            break;

                       case 2:
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

    TabIconOnly(int titleResId, int layoutResId) {
        this.titleResId = titleResId;
        this.layoutResId = layoutResId;
    }

  

    public void setup(final SmartTabLayout layout) {
        //Do nothing.
    }

    public int[] tabs() {
        return tab2();
    }

    public static int[] tab2() {
        return new int[]{
                R.string.loantype,
                R.string.loantype,
                R.string.loantype,

        };
    }

   public  static TabIconOnly getTab() {
        return TabIconOnly.valueOf("CUSTOM_TAB_ICONS");
    }

}
