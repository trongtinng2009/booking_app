package com.example.bookingapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookingapp.Activity.LoginActivity;
import com.example.bookingapp.Activity.SignUpActivity;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button signupbtn,loginbtn,logoutbtn;
    private TextView txtname,favbtn;
    private LinearLayout layouthaveacc,layoutnothaveacc;

    public GuestProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestProfileFragment newInstance(String param1, String param2) {
        GuestProfileFragment fragment = new GuestProfileFragment();
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
        return inflater.inflate(R.layout.fragment_guest_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if(MainActivity.user == null)
        {
            layoutnothaveacc.setVisibility(View.VISIBLE);
            layouthaveacc.setVisibility(View.GONE);
        }
        else
        {
            layoutnothaveacc.setVisibility(View.GONE);
            layouthaveacc.setVisibility(View.VISIBLE);
        }
    }
    private void initView(View view)
    {
        favbtn = view.findViewById(R.id.fragguestprofile_favbtn);
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager3.beginTransaction();
                transaction.replace(R.id.mainact_fragcontainer,new GuestFavoriteFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        layouthaveacc = view.findViewById(R.id.guestprofile_haveacc);
        layoutnothaveacc = view.findViewById(R.id.guestprofile_nothaveacc);
        signupbtn = view.findViewById(R.id.fragguestprofile_signupbtn);
        loginbtn = view.findViewById(R.id.fragguestprofile_loginbtn);
        logoutbtn = view.findViewById(R.id.guestprofile_logoutbtn);
        logoutbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                MainActivity.user = null;
                layoutnothaveacc.setVisibility(View.VISIBLE);
                layouthaveacc.setVisibility(View.GONE);
            }
        });
        txtname = view.findViewById(R.id.guestprofile_guestname);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), SignUpActivity.class));
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.user == null)
        {
            layoutnothaveacc.setVisibility(View.VISIBLE);
            layouthaveacc.setVisibility(View.GONE);
        }
        else
        {
            layoutnothaveacc.setVisibility(View.GONE);
            layouthaveacc.setVisibility(View.VISIBLE);
            txtname.setText(MainActivity.user.getFullname());
        }
    }
}