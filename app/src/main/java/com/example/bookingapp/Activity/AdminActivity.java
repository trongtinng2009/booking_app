package com.example.bookingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.bookingapp.Fragment.AdminBookingFragment;
import com.example.bookingapp.Fragment.AdminInfoFragment;
import com.example.bookingapp.Fragment.AdminProfileFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminActivity extends AppCompatActivity {
    public static User user;
    BottomNavigationView bottomnav;
    AdminBookingFragment adminBookingFragment;
    AdminInfoFragment adminInfoFragment;
    AdminProfileFragment adminProfileFragment;
    public static FragmentManager fragmentManager,fragmentManager2,fragmentManager3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        fragmentManager = getSupportFragmentManager();
        fragmentManager2 = getSupportFragmentManager();
        fragmentManager3 = getSupportFragmentManager();
        initView();
        setBottomnav();
        bottomnav.setSelectedItemId(R.id.adminnav_booking);
        setFragment(fragmentManager,adminBookingFragment);
    }
    private void setFragment(FragmentManager fragmentManager, Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.adminact_fragcontainer,fragment);
        transaction.commit();
    }
    private void initView()
    {
        adminBookingFragment = new AdminBookingFragment();
        adminInfoFragment = new AdminInfoFragment();
        adminProfileFragment = new AdminProfileFragment();
        bottomnav = findViewById(R.id.adminact_bottomnav);
        user = getIntent().getParcelableExtra("user");
    }
    private void setBottomnav()
    {
        bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.adminnav_booking)
                {
                    setFragment(fragmentManager,adminBookingFragment);
                }
                else if(item.getItemId() == R.id.adminnav_cart)
                {
                    setFragment(fragmentManager2,adminInfoFragment);
                }
                else if(item.getItemId() == R.id.adminnav_profile)
                {
                    setFragment(fragmentManager3,adminProfileFragment);
                }
                return true;
            }
        });
    }
}