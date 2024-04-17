package com.example.bookingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Activity.BookingActivity;
import com.example.bookingapp.Activity.BookingNextStepActivity;
import com.example.bookingapp.Fragment.AdminRoomDetailFragment;
import com.example.bookingapp.Fragment.GuestRoomDelFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Room> rooms,oldrooms;
    Context context;
    int layoutid,searchoption = 1;
    boolean guestAccess = true;

    public int getSearchoption() {
        return searchoption;
    }

    public void setSearchoption(int searchoption) {
        this.searchoption = searchoption;
    }

    public boolean isGuestAccess() {
        return guestAccess;
    }

    public void setGuestAccess(boolean guestAccess) {
        this.guestAccess = guestAccess;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    rooms = oldrooms;
                }
                else
                {
                    ArrayList<Room> roomtemp = new ArrayList<>();
                    for(Room room : oldrooms)
                    {
                        if(searchoption == 1)
                        {
                            if (room.getName().toLowerCase().contains(search.toLowerCase()))
                                roomtemp.add(room);
                        }
                        else if(searchoption == 2)
                        {
                            try {
                                double price = Double.parseDouble(search);
                                if (room.getPriceadult() <= price)
                                    roomtemp.add(room);
                            }
                            catch (Exception e)
                            {

                            }
                        }
                        else if(searchoption == 3)
                        {
                            int total = Integer.parseInt(search);
                            if(room.getMaxadult() + room.getMaxkid() <= total)
                                roomtemp.add(room);
                        }
                    }
                    rooms = roomtemp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = rooms;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                rooms = (ArrayList<Room>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public RoomAdapter(ArrayList<Room> rooms, Context context, int layoutid) {
        this.rooms = rooms;
        this.context = context;
        this.layoutid = layoutid;
        oldrooms = rooms;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomHolder(LayoutInflater.from(context).inflate(R.layout.room_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Room room = rooms.get(position);
        RoomHolder roomHolder = (RoomHolder) holder;
        roomHolder.txtroomname.setText(room.getName());
        roomHolder.txtsize.setText(room.getSize() + "m2");
        roomHolder.txtremain.setText("Only " + room.getRemain_quantity() +" rooms left");
        roomHolder.txtmax.setText("Maximum: "+ room.getMaxadult() + " adults, " + room.getMaxkid() + " kids");
        roomHolder.txtpricekid.setText("Price per kid: " + room.getPricekid() + "$");
        roomHolder.txtpriceadl.setText("Price per adult: " + room.getPriceadult() + "$");
        Picasso.get().load(room.getImg()).into(roomHolder.img);
        roomHolder.setOnClickBook(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView txtroomname,txtsize,txtmax,txtpriceadl,txtpricekid
                ,txtremain;
        Button btnbook;
        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.room_item_img);
            txtmax = itemView.findViewById(R.id.room_item_roommax);
            txtpriceadl = itemView.findViewById(R.id.room_item_priceadult);
            txtpricekid = itemView.findViewById(R.id.room_item_pricekid);
            txtremain = itemView.findViewById(R.id.room_item_roomremainquantity);
            txtroomname = itemView.findViewById(R.id.room_item_roomname);
            txtsize = itemView.findViewById(R.id.room_item_roomsize);
            btnbook = itemView.findViewById(R.id.room_item_roombook);
        }
        public void setOnClickBook(Room room)
        {
            btnbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!guestAccess)
                    {
                        AdminRoomDetailFragment adminRoomDetailFragment = new AdminRoomDetailFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("room",room);
                        adminRoomDetailFragment.setArguments(b);
                        FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
                        transaction.replace(R.id.adminact_fragcontainer,adminRoomDetailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        GuestRoomDelFragment guestRoomDelFragment = new GuestRoomDelFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("room",room);
                        guestRoomDelFragment.setArguments(b);
                        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                        transaction.replace(R.id.mainact_fragcontainer,guestRoomDelFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
    }
}
