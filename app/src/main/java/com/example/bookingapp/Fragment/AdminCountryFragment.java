package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Adapter.CountryAdapter;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCountryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCountryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button addbtn;
    private RecyclerView rcv;
    private ArrayList<Country> countries;
    private SearchView searchView;
    ImageButton backbtn;
    private CountryAdapter countryAdapter;
    private FirebaseFirestore db;
    TextView txttotal;

    public AdminCountryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminCountryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminCountryFragment newInstance(String param1, String param2) {
        AdminCountryFragment fragment = new AdminCountryFragment();
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
        return inflater.inflate(R.layout.fragment_admin_country, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setRcv();
        setSearchView();
    }
    private void initView(View view)
    {
        addbtn = view.findViewById(R.id.fragadmincoun_addbtn);
        rcv = view.findViewById(R.id.fragadmincoun_rcv);
        txttotal = view.findViewById(R.id.fragadmincoun_total);
        backbtn = view.findViewById(R.id.fragadminaddcoun_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AdminActivity.fragmentManager2.popBackStack();
            }
        });
        searchView = view.findViewById(R.id.fragadmincoun_search);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
                transaction.replace(R.id.adminact_fragcontainer,new AdminAddCountryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                countryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                countryAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void setRcv()
    {
        countries = new ArrayList<>();
        countryAdapter = new CountryAdapter(countries,getContext(),R.layout.location_item);
        db.collection("Country").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful())
               {
                   txttotal.setText("Total: " + task.getResult().size());
                   for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                   {
                       Country country = queryDocumentSnapshot.toObject(Country.class);
                       countries.add(country);
                   }
                   rcv.setAdapter(countryAdapter);
                   rcv.setLayoutManager(new GridLayoutManager(getContext(),2));
               }
            }
        });
    }
}