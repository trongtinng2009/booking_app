package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout countrylayout,hotellayout,locationlayout;

    public AdminInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminInfoFragment newInstance(String param1, String param2) {
        AdminInfoFragment fragment = new AdminInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setClick();
    }

    private void initView(View view)
    {
        countrylayout = view.findViewById(R.id.fragadmininfo_country);
        hotellayout = view.findViewById(R.id.fragadmininfo_hotel);
        locationlayout = view.findViewById(R.id.fragadmininfo_location);
    }
    private void setClick()
    {
        FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
        countrylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.adminact_fragcontainer,new AdminCountryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        locationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.adminact_fragcontainer,new AdminLocationFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        hotellayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.adminact_fragcontainer,new AdminHotelFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}