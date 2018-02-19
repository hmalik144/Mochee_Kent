package com.example.h_mal.mochee;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.h_mal.mochee.Fragment2_Parts.Fragment_Two;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    TextView basketQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        basketQuantity = (TextView) findViewById(R.id.backet_quantity);

        RelativeLayout basket = (RelativeLayout) findViewById(R.id.basket);

        basket.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(basketQuantity.equals("0")){
                    Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                    startActivity(intent);
                }
            }
        });

        basketQuantity.setText(Basket_Counter.setBasketQuantity(this)+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        basketQuantity.setText(Basket_Counter.setBasketQuantity(this)+"");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment_home tab1 = new Fragment_home();
                    return tab1;
                case 1:
                    Fragment_Two tab2 = new Fragment_Two();
                    return tab2;
                case 2:
                    Fragment_Three tab3 = new Fragment_Three();
                    return tab3;
                case 3:
                    Fragment_Four tab4 = new Fragment_Four();
                    return tab4;
                case 4:
                    Fragment_Six tab6 = new Fragment_Six();
                    return tab6;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Shop";
                case 2:
                    return "Tailor Made";
                case 3:
                    return "Inspiration";
                case 4:
                    return "Contact";
            }
            return null;
        }


    }

}
