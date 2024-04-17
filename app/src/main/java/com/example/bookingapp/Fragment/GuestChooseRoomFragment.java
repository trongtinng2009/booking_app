package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapp.Adapter.RoomAdapter;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestChooseRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestChooseRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcv;
    private TextView allroomtxt,hoteltxt;
    private ArrayList<Room> rooms;
    private FirebaseFirestore db;
    RoomAdapter roomAdapter;
    SearchView searchView;
    RadioGroup rdg;
    private Hotel hotel;

    public GuestChooseRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestChooseRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestChooseRoomFragment newInstance(String param1, String param2) {
        GuestChooseRoomFragment fragment = new GuestChooseRoomFragment();
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
        return inflater.inflate(R.layout.fragment_guest_choose_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        db = FirebaseFirestore.getInstance();
        setRcv();
        setContentForView();
        setRdg();
        setSearchView();
    }
    private void initView(View view)
    {
        rcv = view.findViewById(R.id.fragguestchooseroom_rcvroom);
        allroomtxt = view.findViewById(R.id.fragguestchooseroom_roomresult);
        hoteltxt = view.findViewById(R.id.fragguestchooseroom_hoteltxt);
        rdg = view.findViewById(R.id.fragguestchooseroom_rdg);
        searchView = view.findViewById(R.id.fragguestchooseroom_search);
        RadioButton rdname = view.findViewById(R.id.fragguestchooseroom_rdname);
        rdname.setChecked(true);
        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(rooms,getContext(),R.layout.room_item);
        Bundle b = getArguments();
        hotel = (Hotel) b.getSerializable("hotel");
    }
    private void setRdg()
    {
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fragguestchooseroom_rdname)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                    roomAdapter.setSearchoption(1);
                }
                else if(checkedId == R.id.fragguestchooseroom_rdprice)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    roomAdapter.setSearchoption(2);
                }
                else if(checkedId == R.id.fragguestchooseroom_rdcapacity)
                {
                    roomAdapter.setSearchoption(3);
                    searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });
    }
    private void setSearchView()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                roomAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                roomAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void setContentForView()
    {
        hoteltxt.setText(hotel.getName());
    }
    private void setRcv()
    {
        db.collection("Room").
        whereEqualTo("hotel.id",hotel.getId())
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    allroomtxt.setText(task.getResult().size() + " results");
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Room room = queryDocumentSnapshot.toObject(Room.class);
                        rooms.add(room);
                    }
                    rcv.setAdapter(roomAdapter);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }
}