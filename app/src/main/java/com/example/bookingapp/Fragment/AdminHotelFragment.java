package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Adapter.HotelAdapter;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHotelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHotelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnadd;
    TextView txttotal;
    FirebaseFirestore db;
    SearchView searchView;
    HotelAdapter hotelAdapter;
    ArrayList<Hotel> hotels;
    RecyclerView rcv;

    public AdminHotelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHotelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHotelFragment newInstance(String param1, String param2) {
        AdminHotelFragment fragment = new AdminHotelFragment();
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
        return inflater.inflate(R.layout.fragment_admin_hotel, container, false);
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
        hotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotels,getContext(),R.layout.bestdeal_hotel_item);
        hotelAdapter.setGuestAccess(false);
        rcv = view.findViewById(R.id.fragadminhol_rcv);
        searchView = view.findViewById(R.id.fragadminhol_search);
        txttotal = view.findViewById(R.id.fragadminhol_total);
        btnadd = view.findViewById(R.id.fragadminhol_addbtn);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction= AdminActivity.fragmentManager2.beginTransaction();
                transaction.replace(R.id.adminact_fragcontainer,new AdminAddHotelFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setRcv()
    {
        db.collection("Hotel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    txttotal.setText("Total: " + task.getResult().size());
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Hotel hotel = queryDocumentSnapshot.toObject(Hotel.class);
                        hotels.add(hotel);
                    }
                    rcv.setAdapter(hotelAdapter);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hotelAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hotelAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}