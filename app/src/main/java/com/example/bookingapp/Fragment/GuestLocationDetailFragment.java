package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookingapp.Adapter.HotelAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestLocationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestLocationDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Hotel> hotels;
    private HotelAdapter hotelAdapter;
    private TextView total,locname;
    private FirebaseFirestore db;
    private RecyclerView rcv;
    private ImageButton backbtn;
    private SearchView searchView;
    Location location;

    public GuestLocationDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestLocationDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestLocationDetailFragment newInstance(String param1, String param2) {
        GuestLocationDetailFragment fragment = new GuestLocationDetailFragment();
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
        return inflater.inflate(R.layout.fragment_guest_location_detail, container, false);
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
        location = (Location) getArguments().getSerializable("location");
        rcv = view.findViewById(R.id.fragguestlocdel_rcv);
        total = view.findViewById(R.id.fragguestlocdel_resulttxt);
        backbtn = view.findViewById(R.id.fragguestlocdel_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        locname = view.findViewById(R.id.fragguestlocdel_loc);
        locname.setText(location.getName().toUpperCase());
        searchView = view.findViewById(R.id.fragguestlocdel_search);
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
    private void setRcv()
    {
        db.collection("Hotel").whereEqualTo("location.id",location.getId())
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    total.setText(task.getResult().size() + " results");
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
}