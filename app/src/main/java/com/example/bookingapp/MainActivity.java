package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.bookingapp.Fragment.ExploreMainFragment;
import com.example.bookingapp.Fragment.GuestCartFragment;
import com.example.bookingapp.Fragment.GuestProfileFragment;
import com.example.bookingapp.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomnav;
    ExploreMainFragment exploreMainFragment;
    GuestCartFragment guestCartFragment;
    public static GuestProfileFragment guestProfileFragment;
    public static User user;
    public static FragmentManager fragmentManager,fragmentManager2,fragmentManager3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setBottomNav();
        fragmentManager = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();
        fragmentManager3 = getSupportFragmentManager();
        bottomnav.setSelectedItemId(R.id.bottomnav_search);
        setFragment(fragmentManager,exploreMainFragment);
    }
    private void setFragment(FragmentManager fragmentManager,Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainact_fragcontainer,fragment);
        transaction.commit();
    }
    private void initView()
    {
        bottomnav = findViewById(R.id.mainact_bottomnav);
        guestCartFragment = new GuestCartFragment();
        exploreMainFragment = new ExploreMainFragment();
        guestProfileFragment = new GuestProfileFragment();
    }
    private void setBottomNav()
    {
        bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bottomnav_search)
                {
                    setFragment(fragmentManager,exploreMainFragment);
                }
                else if(item.getItemId() == R.id.bottomnav_cart)
                {
                    if(user != null)
                        setFragment(fragmentManager2,guestCartFragment);
                }
                else if(item.getItemId() == R.id.bottomnav_profile)
                {
                    setFragment(fragmentManager3,guestProfileFragment);
                }
                return true;
            }
        });
    }
}