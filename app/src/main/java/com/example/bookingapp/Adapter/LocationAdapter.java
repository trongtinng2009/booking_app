package com.example.bookingapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Fragment.GuestLocationDetailFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable
{
    Context context;
    ArrayList<Location> locations,locationsold;
    int layoutid;
    boolean guestAccess = true;

    public LocationAdapter(Context context, ArrayList<Location> locations, int layoutid) {
        this.context = context;
        this.locations = locations;
        this.layoutid = layoutid;
        locationsold = locations;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    locations = locationsold;
                }
                else
                {
                    ArrayList<Location> locationstemp = new ArrayList<>();
                    for(Location location : locationsold)
                    {
                            if(location.getName().toLowerCase().contains(search.toLowerCase()))
                                locationstemp.add(location);
                    }
                    locations = locationstemp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = locations;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                locations = (ArrayList<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Location location = locations.get(position);
        LocationHolder locationHolder = (LocationHolder) holder;
        locationHolder.name.setText(location.getName() + " - " + location.getCountry().getName());
        locationHolder.setClick(location);
        Picasso.get().load(location.getImg()).into(locationHolder.img);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
    public class LocationHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView img;
        public LocationHolder(@NonNull View itemView)
        {
            super(itemView);
            img = itemView.findViewById(R.id.loc_img);
            name = itemView.findViewById(R.id.loc_txt);
        }
        private void setClick(Location location)
        {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(guestAccess) {
                        GuestLocationDetailFragment guestLocationDetailFragment = new GuestLocationDetailFragment();
                        Bundle b = new Bundle();
                        b.putSerializable("location", location);
                        guestLocationDetailFragment.setArguments(b);
                        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                        transaction.replace(R.id.mainact_fragcontainer, guestLocationDetailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                }
            });
        }
    }
}
