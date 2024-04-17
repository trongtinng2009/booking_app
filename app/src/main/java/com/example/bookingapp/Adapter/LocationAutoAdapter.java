package com.example.bookingapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationAutoAdapter extends ArrayAdapter<Location>
{
    ArrayList<Location> locations;

    public LocationAutoAdapter(@NonNull Context context, int resource, @NonNull List<Location> objects) {
        super(context, resource, objects);
        locations = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_auto_item,parent,false);
        }
        TextView txtname = convertView.findViewById(R.id.coun_itemauto_name),
                txtid = convertView.findViewById(R.id.coun_itemauto_id);
        ImageView img = convertView.findViewById(R.id.coun_itemauto_img);
        Location location = getItem(position);
        txtid.setText("Location ID: " + location.getId());
        txtname.setText(location.getName() + " - " + location.getCountry().getName());
        Picasso.get().load(location.getImg()).into(img);
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Location> locationSuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    locationSuggest.addAll(locations);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(Location location : locations)
                    {
                        if(location.getName().toLowerCase().contains(filter))
                        {
                            locationSuggest.add(location);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = locationSuggest;
                filterResults.count = locationSuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Location>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Location)resultValue).getId();
            }
        };
    }
}
