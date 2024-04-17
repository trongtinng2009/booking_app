package com.example.bookingapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Fragment.GuestCountryDetailFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable
{
    ArrayList<Country> countries,countriesold;
    Context context;
    int layoutid;

    public CountryAdapter(ArrayList<Country> countries, Context context, int layoutid) {
        this.countries = countries;
        this.context = context;
        countriesold = countries;
        this.layoutid = layoutid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutid,parent,false);
        if(viewType == 1)
            return new CountryHolder(view);
        else if(viewType == 2)
            return new CountryHolder2(view);
        else
            return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(layoutid == R.layout.pop_des_item)
            return 1;
        else if(layoutid == R.layout.location_item)
            return 2;
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    countries = countriesold;
                }
                else
                {
                    ArrayList<Country> countrytemp = new ArrayList<>();
                    for(Country country : countriesold)
                    {
                        if(country.getName().toLowerCase().contains(search.toLowerCase()))
                            countrytemp.add(country);
                    }
                    countries = countrytemp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = countries;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countries = (ArrayList<Country>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Country country = countries.get(position);
        if(getItemViewType(position) == 1) {
            CountryHolder countryHolder = (CountryHolder) holder;
            Picasso.get().load(country.getImg()).into(countryHolder.img);
            countryHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuestCountryDetailFragment guestCountryDetailFragment = new GuestCountryDetailFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("country", country);
                    guestCountryDetailFragment.setArguments(b);
                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                    transaction.replace(R.id.mainact_fragcontainer, guestCountryDetailFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            countryHolder.txt.setText(country.getName());
        }
        else if(getItemViewType(position) == 2)
        {
            CountryHolder2 countryHolder2 = (CountryHolder2) holder;
            Picasso.get().load(country.getImg()).into(countryHolder2.img);
            countryHolder2.txt.setText(country.getName());
        }
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class CountryHolder extends RecyclerView.ViewHolder
    {
        FrameLayout frameLayout;
        TextView txt;
        ImageView img;
        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.pop_des_txt);
            img = itemView.findViewById(R.id.pop_des_img);
            frameLayout = itemView.findViewById(R.id.pop_des_layout);
        }
    }
    public class CountryHolder2 extends RecyclerView.ViewHolder
    {
        TextView txt;
        ImageView img;
        public CountryHolder2(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.loc_txt);
            img = itemView.findViewById(R.id.loc_img);
        }
    }
}
