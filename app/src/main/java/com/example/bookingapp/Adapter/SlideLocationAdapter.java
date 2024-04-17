package com.example.bookingapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bookingapp.Fragment.GuestCountryDetailFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideLocationAdapter extends RecyclerView.Adapter<SlideLocationAdapter.LocationHolder>{
    ArrayList<Location> locations;
    Context context;
    int layoutid;
    ViewPager2 viewPager2;
    FirebaseFirestore db;

    public SlideLocationAdapter(ArrayList<Location> locations, Context context, int layoutid,ViewPager2 viewPager2) {
        this.locations = locations;
        this.context = context;
        this.layoutid = layoutid;
        this.viewPager2 = viewPager2;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view =  layoutInflater.inflate(layoutid,parent,false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        LocationHolder locationHolder = (LocationHolder) holder;
        Location location = locations.get(position);
        locationHolder.txtcountry.setText("In country : " + location.getCountry().getName());
        Picasso.get().load(location.getImg()).into(locationHolder.img);
        locationHolder.txtlocation.setText(location.getName());

        locationHolder.btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestCountryDetailFragment guestCountryDetailFragment = new GuestCountryDetailFragment();
                Bundle b = new Bundle();
                b.putSerializable("country",location.getCountry());
                guestCountryDetailFragment.setArguments(b);
                FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainact_fragcontainer,guestCountryDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder
    {
        TextView txtlocation,txtcountry;
        ImageView img;
        Button btnview;
        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            txtcountry = itemView.findViewById(R.id.slide_popular_place_txtcountry);
            txtlocation = itemView.findViewById(R.id.slide_popular_place_txtplace);
            img = itemView.findViewById(R.id.slide_popular_place_img);
            btnview = itemView.findViewById(R.id.slide_popular_place_btnview);
        }
    }
    private Runnable runnable= new Runnable() {
        @Override
        public void run() {
            locations.addAll(locations);
            notifyDataSetChanged();
        }
    };
}
