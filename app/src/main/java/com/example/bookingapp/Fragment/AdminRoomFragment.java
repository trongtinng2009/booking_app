package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Adapter.RoomAdapter;
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
 * Use the {@link AdminRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rcv;
    FirebaseFirestore db;
    TextView txttotal,txthotel;
    SearchView searchView;
    Button btnaddroom,btnchangeinfo;
    ArrayList<Room> rooms;
    RadioGroup rdg;
    RoomAdapter roomAdapter;
    ImageButton backbtn;
    Hotel hotel;

    public AdminRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminRoomFragment newInstance(String param1, String param2) {
        AdminRoomFragment fragment = new AdminRoomFragment();
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
        return inflater.inflate(R.layout.fragment_admin_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setClick();
        setRcv();
        setRdg();
        setSearchView();
    }
    private void initView(View view)
    {
        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(rooms,getContext(),R.layout.room_item);
        roomAdapter.setGuestAccess(false);
        hotel = (Hotel)getArguments().getSerializable("hotel");
        rcv = view.findViewById(R.id.fragadminroom_rcvroom);
        txttotal = view.findViewById(R.id.fragadminroom_roomresult);
        searchView = view.findViewById(R.id.fragadminroom_search);
        backbtn = view.findViewById(R.id.fragadminroom_backbtn);
        rdg = view.findViewById(R.id.fragadminchooseroom_rdg);
        RadioButton rdname = view.findViewById(R.id.fragadminchooseroom_rdname);
        rdname.setChecked(true);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager2.popBackStack();
            }
        });
        txthotel = view.findViewById(R.id.fragadminroom_hoteltxt);
        txthotel.setText(hotel.getName().toUpperCase());
        btnaddroom = view.findViewById(R.id.fragadminroom_addroombtn);
        btnchangeinfo = view.findViewById(R.id.fragadminroom_changeinfobtn);
    }
    private void setRdg()
    {
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fragadminchooseroom_rdname)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                    roomAdapter.setSearchoption(1);
                }
                else if(checkedId == R.id.fragadminchooseroom_rdprice)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    roomAdapter.setSearchoption(2);
                }
                else if(checkedId == R.id.fragadminchooseroom_rdcapicity)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    roomAdapter.setSearchoption(3);
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
    private void setClick()
    {
        FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
        btnaddroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("hotel",hotel);
                AdminAddRoomFragment adminAddRoomFragment = new AdminAddRoomFragment();
                adminAddRoomFragment.setArguments(b);
                transaction.replace(R.id.adminact_fragcontainer,adminAddRoomFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setRcv()
    {
        db.collection("Room").whereEqualTo("hotel.id",hotel.getId())
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    txttotal.setText("Total: " + task.getResult().size());
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