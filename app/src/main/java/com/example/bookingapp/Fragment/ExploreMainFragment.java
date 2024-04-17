
package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.bookingapp.Adapter.CountryAdapter;
import com.example.bookingapp.Adapter.HotelAdapter;
import com.example.bookingapp.Adapter.SlideLocationAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager2 banner;
    private RecyclerView rcvPopDes,rcvBestDeals;
    private FirebaseFirestore db;
    private CollectionReference countrycoll,
    locationcoll,hotelcoll;
    private ArrayList<Location> locations;
    private ArrayList<Country> countries;
    private ArrayList<Hotel> hotels;
    private Handler handler = new Handler();
    TextView allhoteltxt;
    AutoCompleteTextView search;

    public ExploreMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreMainFragment newInstance(String param1, String param2) {
        ExploreMainFragment fragment = new ExploreMainFragment();
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
        return inflater.inflate(R.layout.fragment_explore_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setDataCountry();
        setDataLocation();
        setDataHotel();
    }
    private void initView(View view)
    {
        db = FirebaseFirestore.getInstance();
        countrycoll = db.collection("Country");
        hotelcoll = db.collection("Hotel");
        locationcoll = db.collection("Location");
        banner = view.findViewById(R.id.fragexploremain_banner);
        allhoteltxt = view.findViewById(R.id.fragexploremain_viewallhotel);
        allhoteltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                transaction.replace(R.id.mainact_fragcontainer,new GuestAllHotelFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        rcvPopDes = view.findViewById(R.id.fragexploremain_rcvpopdes);
        search = view.findViewById(R.id.fragexploremain_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                transaction.replace(R.id.mainact_fragcontainer,new GuestSearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        locations = new ArrayList<>();
        countries = new ArrayList<>();
        hotels = new ArrayList<>();
        rcvBestDeals = view.findViewById(R.id.fragexploremain_rcvbestdeals);
    }
    private void setDataCountry()
    {
        countrycoll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful())
               {
                   for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                   {
                       Country country = queryDocumentSnapshot.toObject(Country.class);
                       countries.add(country);
                   }
                   rcvPopDes.setAdapter(new CountryAdapter(countries,getContext(),R.layout.pop_des_item));
                   rcvPopDes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
               }
            }
        });
    }
    private void setDataHotel()
    {
        hotelcoll.limit(5).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Hotel hotel = queryDocumentSnapshot.toObject(Hotel.class);
                        hotels.add(hotel);
                    }
                    rcvBestDeals.setAdapter(new HotelAdapter(hotels,getContext(),R.layout.bestdeal_hotel_item));
                    rcvBestDeals.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    rcvBestDeals.setNestedScrollingEnabled(false);
                }
            }
        });
    }
    private void setDataLocation()
    {
        locationcoll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult())
                    {
                        Location location = queryDocumentSnapshot.toObject(Location.class);
                        locations.add(location);
                    }
                    banner.setAdapter(new SlideLocationAdapter(locations,getContext(),
                            R.layout.slide_popular_places,banner));
                    banner.setClipToPadding(false);
                    banner.setClipChildren(false);
                    banner.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
                    banner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1- Math.abs(position);
                            page.setScaleY(0.85f + r*0.15f);
                        }
                    });

                    banner.setPageTransformer(compositePageTransformer);
                    banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable,2000);
                        }
                    });
                }
            }
        });
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            banner.setCurrentItem(banner.getCurrentItem() + 1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable,3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}