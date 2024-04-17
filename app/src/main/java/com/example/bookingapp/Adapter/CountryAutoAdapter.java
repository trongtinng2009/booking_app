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
import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CountryAutoAdapter extends ArrayAdapter<Country> {
    ArrayList<Country> countries;

    public CountryAutoAdapter(@NonNull Context context, int resource, @NonNull List<Country> objects) {
        super(context, resource, objects);
        this.countries = new ArrayList<>(objects);
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
        Country country = getItem(position);
        txtid.setText("Country ID: " + country.getId());
        txtname.setText(country.getName());
        Picasso.get().load(country.getImg()).into(img);
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Country> countrySuggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    countrySuggest.addAll(countries);
                }
                else
                {
                    String filter = constraint.toString().toLowerCase().trim();
                    for(Country country : countries)
                    {
                        if(country.getName().toLowerCase().contains(filter))
                        {
                            countrySuggest.add(country);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = countrySuggest;
                filterResults.count = countrySuggest.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Country>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Country)resultValue).getId();
            }
        };
    }
}
