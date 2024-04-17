package com.example.bookingapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.Model.RoomBooking;
import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminBookingConfirmDelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBookingConfirmDelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RoomBooking roomBooking;
    private Hotel hotel;
    private Room room;
    private FirebaseFirestore db;
    private ImageView img;
    private TextView txthotel,txtroom,txtcheckinout,
    txtroomdel,txtname,txtphone,txtmail,txtcard,
    txtbookingdate,txtprice;
    private Button accbtn,cancelbtn;

    public AdminBookingConfirmDelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBookingConfirmDelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminBookingConfirmDelFragment newInstance(String param1, String param2) {
        AdminBookingConfirmDelFragment fragment = new AdminBookingConfirmDelFragment();
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
        return inflater.inflate(R.layout.fragment_admin_booking_confirm_del, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        db= FirebaseFirestore.getInstance();
        setDataForView();
        setVerify();
    }
    private void initView(View view)
    {
        roomBooking = getArguments().getParcelable("roombooking");
        room = (Room)getArguments().getSerializable("room");
        hotel = (Hotel) getArguments().getSerializable("hotel");
        img = view.findViewById(R.id.adminbookcfdel_img);
        Picasso.get().load(room.getImg()).into(img);
        cancelbtn = view.findViewById(R.id.adminbookcfdel_cancelbtn);
        accbtn = view.findViewById(R.id.adminbookcfdel_confirmbtn);
        txtbookingdate = view.findViewById(R.id.adminbookcfdel_bookingdate);
        txtcard = view.findViewById(R.id.adminbookcfdel_card);
        txtcheckinout = view.findViewById(R.id.adminbookcfdel_checkinout);
        txthotel = view.findViewById(R.id.adminbookcfdel_hotelname);
        txtmail = view.findViewById(R.id.adminbookcfdel_usermail);
        txtname = view.findViewById(R.id.adminbookcfdel_username);
        txtphone = view.findViewById(R.id.adminbookcfdel_userphone);
        txtprice = view.findViewById(R.id.adminbookcfdel_total);
        txtroom = view.findViewById(R.id.adminbookcfdel_roomname);
        txtroomdel = view.findViewById(R.id.adminbookcfdel_roomdetail);
        if(getArguments().getBoolean("guest"))
            accbtn.setVisibility(View.GONE);
    }
    private void setDataForView()
    {
        img.setImageResource(getContext().getResources().getIdentifier(room.getImg(),
                "drawable",getContext().getPackageName()));
        txtbookingdate.setText(Utils.dateToString(roomBooking.getBookingday().toDate()));
        txtroomdel.setText(roomBooking.getTotalroom() + " rooms, "+ roomBooking.getTotaladult() + " adults, "
        + roomBooking.getTotalkid() + " kids");
        txtroom.setText(room.getName());
        txtprice.setText("Prices: " +Double.toString(roomBooking.getTotalprice())+"$");
        txthotel.setText(hotel.getName());
        if(roomBooking.getUserguest() == null)
        {
            txtmail.setText(roomBooking.getUseraccount().getEmail());
            txtname.setText(roomBooking.getUseraccount().getFullname());
            txtphone.setText(roomBooking.getUseraccount().getPhone());
        }
        else
        {
            txtmail.setText(roomBooking.getUserguest().getEmail());
            txtname.setText(roomBooking.getUserguest().getFullname());
            txtphone.setText(roomBooking.getUserguest().getPhone());
        }
        txtcheckinout.setText(Utils.dateToString(roomBooking.getCheckin().toDate())
        + " - " + Utils.dateToString(roomBooking.getCheckout().toDate()));
        txtcard.setText(roomBooking.getCardnumber());
    }
    private void setVerify()
    {
        accbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertPopUp();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertPopUpCancel();
            }
        });
    }
    private void showAlertPopUp()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView txttitle = dialog.findViewById(R.id.alert_popup_title),
                txtmain = dialog.findViewById(R.id.alert_popup_maintxt);
            txttitle.setText("CONFIRM PAYMENT");
            txtmain.setText("Did this guest paid ?");
        Button ig = dialog.findViewById(R.id.alert_popup_igbtn),
                acc = dialog.findViewById(R.id.alert_popup_accbtn);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                db.document("RoomBooking/"+roomBooking.getId())
                                .update("status", RoomBooking.RoomBookingState.PAID.value
                                ,"accdate", Timestamp.now());
                AdminActivity.fragmentManager.popBackStack();
            }
        });
        dialog.show();
    }
    private void showAlertPopUpCancel()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView txttitle = dialog.findViewById(R.id.alert_popup_title),
                txtmain = dialog.findViewById(R.id.alert_popup_maintxt);
            txttitle.setText("CONFIRM CANCEL");
            txtmain.setText("Are you sure to cancel ?");
        Button ig = dialog.findViewById(R.id.alert_popup_igbtn),
                acc = dialog.findViewById(R.id.alert_popup_accbtn);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                db.document("RoomBooking/"+roomBooking.getId())
                        .update("status", RoomBooking.RoomBookingState.CANCELED.value
                                ,"accdate", Timestamp.now());
                db.collection("Room").document(roomBooking.getRoom().getId())
                        .update("remain_quantity", FieldValue.increment(roomBooking.getTotalroom()));
                if(getArguments().getBoolean("guest"))
                    MainActivity.fragmentManager2.popBackStack();
                else
                    AdminActivity.fragmentManager.popBackStack();
            }
        });
        dialog.show();
    }
}