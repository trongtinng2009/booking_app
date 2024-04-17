package com.example.bookingapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Fragment.AdminRoomFragment;
import com.example.bookingapp.Fragment.GuestDetailHotelFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable
{
    ArrayList<Hotel> hotels,hotelsold;
    FirebaseFirestore db;
    Context context;
    int layoutid,searchoption = 1;
    boolean guestAccess = true;

    public HotelAdapter(ArrayList<Hotel> hotels, Context context, int layoutid) {
        this.hotels = hotels;
        this.context = context;
        this.layoutid = layoutid;
        hotelsold = hotels;
        db = FirebaseFirestore.getInstance();
    }

    public boolean isGuestAccess() {
        return guestAccess;
    }

    public void setGuestAccess(boolean guestAccess) {
        this.guestAccess = guestAccess;
    }

    public int getSearchoption() {
        return searchoption;
    }

    public void setSearchoption(int searchoption) {
        this.searchoption = searchoption;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HotelHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HotelHolder hotelHolder = (HotelHolder) holder;
        Hotel hotel = hotels.get(position);
        Picasso.get().load(hotel.getImgtitle()).into(hotelHolder.img);
        hotelHolder.txthotelname.setText(hotel.getName());
        hotelHolder.ratingBar.setRating((float) hotel.getOverallrating());
        hotelHolder.txtdis.setText(hotel.getDistance() + "km to downtown");
        hotelHolder.txtdesloc.setText(hotel.getLocation().getName() + ", " + hotel.getLocation().getCountry().getName());
        hotelHolder.setOnClick(hotel.getLocation().getCountry(), hotel.getLocation(),hotel);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    hotels = hotelsold;
                }
                else
                {
                    ArrayList<Hotel> hoteltemp = new ArrayList<>();
                    for(Hotel hotel : hotelsold)
                    {
                        if(searchoption == 1)
                        {
                            if (hotel.getName().toLowerCase().contains(search.toLowerCase()))
                                hoteltemp.add(hotel);
                        }
                        else if(searchoption == 2)
                        {
                            try
                            {
                                Double price = Double.parseDouble(search);
                                if(hotel.getPrice() <= price)
                                    hoteltemp.add(hotel);
                            }
                            catch (Exception e)
                            {

                            }

                        }
                        else if(searchoption == 3)
                        {
                            if(hotel.getLocation().getName().toLowerCase().contains(search.toLowerCase()))
                                hoteltemp.add(hotel);
                        }
                        else if(searchoption == 4)
                        {
                            if(hotel.getAddress().toLowerCase().contains(search.toLowerCase()))
                                hoteltemp.add(hotel);
                        }
                    }
                    hotels = hoteltemp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hotels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hotels = (ArrayList<Hotel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public class HotelHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout layout;
        ImageView img;
        TextView txthotelname,txtdis,txtdesloc;
        RatingBar ratingBar;
        public HotelHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.bestdeal_hotel_rating);
            txtdis = itemView.findViewById(R.id.bestdeal_hotel_distxt);
            img = itemView.findViewById(R.id.bestdeal_hotel_img);
            txtdesloc = itemView.findViewById(R.id.bestdeal_hotel_desloc);
            txthotelname = itemView.findViewById(R.id.bestdeal_hotel_hotelname);
            layout = itemView.findViewById(R.id.bestdeal_hotel_detail_layout);
        }
        public void setOnClick(Country country,Location location,Hotel hotel)
        {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(guestAccess) {
                        GuestDetailHotelFragment guestDetailHotelFragment = new GuestDetailHotelFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("hotel", hotel);
                        b.putSerializable("country", country);
                        b.putSerializable("location", location);
                        guestDetailHotelFragment.setArguments(b);
                        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                        transaction.replace(R.id.mainact_fragcontainer, guestDetailHotelFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        AdminRoomFragment adminRoomFragment = new AdminRoomFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("hotel", hotel);
                        b.putSerializable("country", country);
                        b.putSerializable("location", location);
                        adminRoomFragment.setArguments(b);
                        FragmentTransaction transaction = AdminActivity.fragmentManager2.beginTransaction();
                        transaction.replace(R.id.adminact_fragcontainer, adminRoomFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
    }
}
