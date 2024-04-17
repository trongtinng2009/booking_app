package com.example.bookingapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Fragment.AdminBookingConfirmDelFragment;
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

import java.util.ArrayList;

public class RoomBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RoomBooking> roomBookings;
    FirebaseFirestore db;
    Context context;
    int layoutid;
    boolean guestAccess = false;

    public boolean isGuestAccess() {
        return guestAccess;
    }

    public void setGuestAccess(boolean guestAccess) {
        this.guestAccess = guestAccess;
    }

    public RoomBookingAdapter(ArrayList<RoomBooking> roomBookings, Context context, int layoutid) {
        this.roomBookings = roomBookings;
        this.context = context;
        this.layoutid = layoutid;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomBookingHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RoomBookingHolder roomBookingHolder = (RoomBookingHolder) holder;
        int pos = position;
        RoomBooking roomBooking = roomBookings.get(position);
        roomBookingHolder.txttotal.setText("Total price: " +Double.toString(roomBooking.getTotalprice())+"$");
        roomBookingHolder.txtbookingdate.setText("Booking date: " +Utils.dateToString(roomBooking.getBookingday().toDate()));
        if(roomBooking.getUserguest() != null)
        {
          roomBookingHolder.txtusername.setText("Guest: " +roomBooking.getUserguest().getFullname());
        }
        else {
            roomBookingHolder.txtusername.setText("Guest: " + roomBooking.getUseraccount().getFullname());
        }
        Picasso.get().load(roomBooking.getRoom().getImg()).into(roomBookingHolder.img);
        roomBookingHolder.txthotelname.setText(roomBooking.getRoom().getHotel().getName());
        roomBookingHolder.setOnClickBtn(roomBooking,roomBooking.getRoom().getHotel(),roomBooking.getRoom(),pos);
    }

    @Override
    public int getItemCount() {
        return roomBookings.size();
    }

    public class RoomBookingHolder extends RecyclerView.ViewHolder
    {
        TextView txthotelname,txtusername,txtbookingdate,txttotal;
        Button btn;
        ImageView img;
        public RoomBookingHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bookingitem_img);
            txtbookingdate = itemView.findViewById(R.id.bookingitem_bookdate);
            txthotelname = itemView.findViewById(R.id.bookingitem_hotelname);
            txttotal = itemView.findViewById(R.id.bookingitem_total);
            txtusername = itemView.findViewById(R.id.bookingitem_username);
            btn = itemView.findViewById(R.id.bookingitem_accbtn);
        }
        public void setOnClickBtn(RoomBooking roomBooking,Hotel hotel,Room room,int pos)
        {
            if(roomBooking.getStatus() == RoomBooking.RoomBookingState.BOOKING.value)
            {
                btn.setText("View detail");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putParcelable("roombooking",roomBooking);
                        b.putSerializable("hotel",hotel);
                        b.putSerializable("room",room);
                        if(guestAccess) {
                            b.putBoolean("guest",true);
                            FragmentTransaction transaction = MainActivity.fragmentManager2.beginTransaction();
                            AdminBookingConfirmDelFragment adminBookingConfirmDelFragment = new AdminBookingConfirmDelFragment();
                            adminBookingConfirmDelFragment.setArguments(b);
                            transaction.replace(R.id.mainact_fragcontainer, adminBookingConfirmDelFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else
                        {
                            b.putBoolean("guest",false);
                            FragmentTransaction transaction = AdminActivity.fragmentManager.beginTransaction();
                            AdminBookingConfirmDelFragment adminBookingConfirmDelFragment = new AdminBookingConfirmDelFragment();
                            adminBookingConfirmDelFragment.setArguments(b);
                            transaction.replace(R.id.adminact_fragcontainer, adminBookingConfirmDelFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                });
            }
            else if(roomBooking.getStatus() == RoomBooking.RoomBookingState.PAID.value)
            {
                btn.setText("Checkout");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          showAlertPopUp(roomBooking,pos);
                    }
                });
            }
            else if(roomBooking.getStatus() == RoomBooking.RoomBookingState.ENDBOOKING.value)
            {
                btn.setVisibility(View.GONE);
            }
            else if(roomBooking.getStatus() == RoomBooking.RoomBookingState.CANCELED.value)
            {
                btn.setVisibility(View.GONE);
            }
        }
        private void showAlertPopUp(RoomBooking roomBooking,int pos)
        {
            Dialog dialog = Utils.getPopup(context,R.layout.alert_popup);
            TextView txttitle = dialog.findViewById(R.id.alert_popup_title),
                    txtmain = dialog.findViewById(R.id.alert_popup_maintxt);
            txttitle.setText("CONFIRM CHECKOUT");
            txtmain.setText("Did this guest checkout ?");
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
                public void onClick(View v)
                {
                    dialog.cancel();
                    db.document("RoomBooking/"+ roomBooking.getId())
                            .update("status", RoomBooking.RoomBookingState.ENDBOOKING.value);
                    db.collection("Room").document(roomBooking.getRoom().getId())
                                    .update("remain_quantity", FieldValue.increment(roomBooking.getTotalroom()));
                    roomBookings.remove(pos);
                    notifyDataSetChanged();
                }
            });
            dialog.show();
        }
    }
}
