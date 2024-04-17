package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookingapp.Adapter.RoomBookingAdapter;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.Model.RoomBooking;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminBookingConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBookingConfirmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcv;
    private ArrayList<RoomBooking> roomBookings;
    private FirebaseFirestore db;
    private Button btnwaiting,btncheckout,btnpaid,btncanceled;
    private TextView txtresult;
    private int btnselect;

    public AdminBookingConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBookingConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminBookingConfirmFragment newInstance(String param1, String param2) {
        AdminBookingConfirmFragment fragment = new AdminBookingConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_admin_booking_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        btnselect = RoomBooking.RoomBookingState.BOOKING.value;
        setRcv(RoomBooking.RoomBookingState.BOOKING.value);
        setButtonSelect();
    }
    private void initView(View view)
    {
        rcv = view.findViewById(R.id.adminbookcf_rcv);
        btnwaiting = view.findViewById(R.id.adminbookcf_btnwaiting);
        btncheckout = view.findViewById(R.id.adminbookcf_btnaccepted);
        btnpaid = view.findViewById(R.id.adminbookcf_btnpaid);
        btncanceled = view.findViewById(R.id.adminbookcf_btncanceled);
        txtresult = view.findViewById(R.id.adminbookcf_resulttxt);
        roomBookings = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }
    private void setRcv(int status)
    {
        db.collection("RoomBooking").
                whereEqualTo("status", status)
                .orderBy("bookingday", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            txtresult.setText(Integer.toString(task.getResult().size()) + " results");
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                RoomBooking roomBooking = queryDocumentSnapshot.toObject(RoomBooking.class);
                                roomBookings.add(roomBooking);
                            }
                            rcv.setAdapter(new RoomBookingAdapter(roomBookings,getContext(),R.layout.booking_item));
                            rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    }
                });
    }
    private void setButtonSelect()
    {
        btnwaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnselect != RoomBooking.RoomBookingState.BOOKING.value)
                {
                    btnselect = RoomBooking.RoomBookingState.BOOKING.value;
                    roomBookings.clear();
                    setRcv(btnselect);
                }
            }
        });
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnselect != RoomBooking.RoomBookingState.ENDBOOKING.value)
                {
                    btnselect = RoomBooking.RoomBookingState.ENDBOOKING.value;
                    roomBookings.clear();
                    setRcv(btnselect);
                }
            }
        });
        btnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnselect != RoomBooking.RoomBookingState.PAID.value)
                {
                    btnselect = RoomBooking.RoomBookingState.PAID.value;
                    roomBookings.clear();
                    setRcv(btnselect);
                }
            }
        });
        btncanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnselect != RoomBooking.RoomBookingState.CANCELED.value)
                {
                    btnselect = RoomBooking.RoomBookingState.CANCELED.value;
                    roomBookings.clear();
                    setRcv(btnselect);
                }
            }
        });
    }
}