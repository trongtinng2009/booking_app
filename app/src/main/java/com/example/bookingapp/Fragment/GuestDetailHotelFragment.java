package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bookingapp.Adapter.HotelAdapter;
import com.example.bookingapp.Adapter.ImgAdapter;
import com.example.bookingapp.Adapter.ReviewAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.FavoriteHotel;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.Model.Review;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestDetailHotelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestDetailHotelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    ReviewAdapter reviewAdapter;
    private TextView txtbannername,txtbannerlocation,txtsale,txthotline,
    txthotelname,txtlocation,txtprice,txtallrev,txtrevbanner,txtoverrate;
    private Button btnbook;
    private ImageView hotelimg;
    ImageButton backbtn,favbtn;
    private ImgAdapter imgAdapter;
    private RecyclerView rcvimgs,rcvrevs;
    ArrayList<Review> reviews;
    private Country country;
    private Hotel hotel;
    ProgressBar star1,star2,star3,star4,star5;
    RatingBar ratingBarbanner,ratingoverral;
    private Location location;
    FavoriteHotel favoriteHotel;
    boolean favHotel = false;

    public GuestDetailHotelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestDetailHotelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestDetailHotelFragment newInstance(String param1, String param2) {
        GuestDetailHotelFragment fragment = new GuestDetailHotelFragment();
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
        return inflater.inflate(R.layout.fragment_guest_detail_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setDataForView();
        setBookbtn();
        setRcvrevs();
        setAllRev();
        if(MainActivity.user != null)
        {
            setFavbtn();
            checkFavbtn();
        }
    }
    private void initView(View view)
    {
        country = (Country)getArguments().getSerializable("country");
        location = (Location)getArguments().getSerializable("location");
        hotel = (Hotel)getArguments().getSerializable("hotel");
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews,getContext(),R.layout.review_item);
        imgAdapter = new ImgAdapter(getContext(),null,R.layout.pop_des_item);
        txtbannerlocation = view.findViewById(R.id.fragguestholdel_bannerlocation);
        txtbannername  = view.findViewById(R.id.fragguestholdel_bannername);
        txtrevbanner = view.findViewById(R.id.fragguestholdel_revbannertxt);
        txthotelname = view.findViewById(R.id.fragguestholdel_hotelname);
        hotelimg = view.findViewById(R.id.fragguestholdel_hotelimg);
        txtlocation = view.findViewById(R.id.fragguestholdel_hotellocation);
        ratingoverral = view.findViewById(R.id.fragguestholdel_ratingoveral);
        rcvimgs = view.findViewById(R.id.fragguestholdel_rcvimgs);
        ratingBarbanner = view.findViewById(R.id.fragguestholdel_ratingbanner);
        btnbook = view.findViewById(R.id.fragguestholdel_btnbook);
        txtallrev = view.findViewById(R.id.fragguestholdel_allrev);
        backbtn = view.findViewById(R.id.fragguestholdel_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        star1 = view.findViewById(R.id.fragguestholdel_star1);
        star2 = view.findViewById(R.id.fragguestholdel_star2);
        star3 = view.findViewById(R.id.fragguestholdel_star3);
        star4 = view.findViewById(R.id.fragguestholdel_star4);
        star5 = view.findViewById(R.id.fragguestholdel_star5);
        txtoverrate = view.findViewById(R.id.fragguestholdel_txtoverrate);
        txtsale = view.findViewById(R.id.fragguestholdel_sale);
        txtprice = view.findViewById(R.id.fragguestholdel_price);
        rcvrevs = view.findViewById(R.id.fragguestholdel_rcvreviews);
        txthotline = view.findViewById(R.id.fragguestholdel_hotelhotline);
        favbtn = view.findViewById(R.id.fragguestholdel_favbtn);
    }
    private void setAllRev()
    {
        txtallrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("hotel",hotel);
                GuestReviewFragment guestReviewFragment = new GuestReviewFragment();
                guestReviewFragment.setArguments(b);
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                transaction.replace(R.id.mainact_fragcontainer,guestReviewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    private void setFavbtn()
    {
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(favHotel)
                {
                    db.collection("FavoriteHotel").document(favoriteHotel.getId()).delete();
                    favbtn.setColorFilter(getResources().getColor(R.color.jade));
                    favHotel = false;
                }
                else
                {
                    favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                    DocumentReference ref = db.collection("FavoriteHotel").document();
                    FavoriteHotel favoriteHotel1 = new FavoriteHotel(MainActivity.user,hotel, ref.getId());
                    favoriteHotel = favoriteHotel1;
                    ref.set(favoriteHotel1);
                    favHotel = true;
                }
            }
        });
    }
    private void checkFavbtn()
    {
        db.collection("FavoriteHotel").
                whereEqualTo("user.id",MainActivity.user.getId())
                .whereEqualTo("hotel.id",hotel.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() > 0)
                        {
                            favoriteHotel = task.getResult().getDocuments().get(0).toObject(FavoriteHotel.class);
                            favHotel = true;
                            favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                        }
                        else
                            favbtn.setColorFilter(getResources().getColor(R.color.jade));
                    }
                });
    }
    private void setDataForView()
    {
        txtbannerlocation.setText(hotel.getAddress() + " - " +
                hotel.getDistance() + "km to downtown");
        txthotelname.setText(hotel.getName().toUpperCase());
        txtbannername.setText(hotel.getName().toUpperCase());
        ratingBarbanner.setRating((float) hotel.getOverallrating());
        ratingoverral.setRating((float) hotel.getOverallrating());
        txtoverrate.setText(Double.toString(hotel.getOverallrating()));
        txthotline.setText("Hotline: " +hotel.getHotline());
        txtlocation.setText(location.getName() + "," + country.getName() + " - " +
                hotel.getDistance() + "km to downtown");
        txtprice.setText("Price from: " + hotel.getPrice() + "$");
        int max = 0;
        for(int i : hotel.getStars())
        {
            max += i;
        }
        star1.setMax(max);
        star2.setMax(max);
        star3.setMax(max);
        star4.setMax(max);
        star5.setMax(max);
        star1.setProgress(hotel.getStars().get(0));
        star2.setProgress(hotel.getStars().get(1));
        star3.setProgress(hotel.getStars().get(2));
        star4.setProgress(hotel.getStars().get(3));
        star5.setProgress(hotel.getStars().get(4));
        if(hotel.getSaleoff() != 0)
            txtsale.setText(hotel.getSaleoff() + "% sale off");
        else
            txtsale.setVisibility(View.GONE);
        Picasso.get().load(hotel.getImgtitle()).into(hotelimg);
        imgAdapter.setBitmapstring(hotel.getImgs());
        rcvimgs.setAdapter(imgAdapter);
        rcvimgs.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }
    private void setBookbtn()
    {
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               GuestChooseRoomFragment guestChooseRoomFragment = new GuestChooseRoomFragment();
               Bundle b = new Bundle();
               b.putSerializable("hotel",hotel);
               b.putSerializable("country",country);
               b.putSerializable("location",location);
               guestChooseRoomFragment.setArguments(b);
               FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.mainact_fragcontainer,guestChooseRoomFragment);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();
            }
        });
    }
    private void setRcvrevs()
    {
        db.collection("Review").whereEqualTo("hotel.id",hotel.getId())
                .orderBy("postdate", Query.Direction.DESCENDING).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    txtrevbanner.setText(task.getResult().size() + " reviews");
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        if(reviews.size() == 3)
                            break;
                        Review review = queryDocumentSnapshot.toObject(Review.class);
                        reviews.add(review);
                    }
                    rcvrevs.setAdapter(reviewAdapter);
                    rcvrevs.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }
}