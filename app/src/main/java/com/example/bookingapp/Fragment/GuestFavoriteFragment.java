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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapp.Adapter.HotelAdapter;
import com.example.bookingapp.Adapter.RoomAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.FavoriteHotel;
import com.example.bookingapp.Model.FavoriteRoom;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestFavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestFavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    RecyclerView rcv;
    RadioGroup rdg;
    SearchView searchView;
    ImageButton backbtn;
    HotelAdapter hotelAdapter;
    RoomAdapter roomAdapter;
    TextView txttotal;
    ArrayList<Hotel> hotels;
    ArrayList<Room> rooms;
    public GuestFavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestFavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestFavoriteFragment newInstance(String param1, String param2) {
        GuestFavoriteFragment fragment = new GuestFavoriteFragment();
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
        return inflater.inflate(R.layout.fragment_guest_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setList();
        setRdg();
        setSearchView();
    }
    private void initView(View view)
    {
        hotels = new ArrayList<>();
        rooms = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotels,getContext(),R.layout.bestdeal_hotel_item);
        roomAdapter = new RoomAdapter(rooms,getContext(),R.layout.room_item);
        rcv = view.findViewById(R.id.fragguestfav_rcvroom);
        backbtn = view.findViewById(R.id.fragguestfav_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager3.popBackStack();
            }
        });
        rdg = view.findViewById(R.id.fragguestfav_rdg);
        txttotal = view.findViewById(R.id.fragguestfav_roomresult);
        searchView = view.findViewById(R.id.fragguestfav_search);
    }
    private void setRdg()
    {
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fragguestfav_rdhol)
                {
                    txttotal.setText(hotels.size() + " results");
                    rcv.setAdapter(hotelAdapter);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
                else if(checkedId == R.id.fragguestfav_rdroom)
                {
                    txttotal.setText(rooms.size() + " results");
                    rcv.setAdapter(roomAdapter);
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
                if(rdg.getCheckedRadioButtonId() == R.id.fragguestfav_rdhol)
                    hotelAdapter.getFilter().filter(query);
                else if(rdg.getCheckedRadioButtonId() == R.id.fragguestfav_rdroom)
                    roomAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(rdg.getCheckedRadioButtonId() == R.id.fragguestfav_rdhol)
                    hotelAdapter.getFilter().filter(newText);
                else if(rdg.getCheckedRadioButtonId() == R.id.fragguestfav_rdroom)
                    roomAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void setList()
    {
        db.collection("FavoriteHotel").whereEqualTo("user.id",MainActivity.user.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               Hotel hotel = queryDocumentSnapshot.toObject(FavoriteHotel.class).getHotel();
                               hotels.add(hotel);
                           }
                       }
                    }
                });
        db.collection("FavoriteRoom").whereEqualTo("user.id",MainActivity.user.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                Room room = queryDocumentSnapshot.toObject(FavoriteRoom.class).getRoom();
                                rooms.add(room);
                            }
                        }
                    }
                });
    }
}