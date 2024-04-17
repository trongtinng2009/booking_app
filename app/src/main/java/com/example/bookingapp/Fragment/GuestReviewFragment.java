package com.example.bookingapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bookingapp.Adapter.ReviewAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Review;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText edtcom;
    ImageButton sendbtn,backbtn;
    RecyclerView rcv;
    FirebaseFirestore db;
    Hotel hotel;
    private LinearLayout reviewlayout;
    TextView txttotal;
    RatingBar ratingBar;
    ArrayList<Review> reviews;
    ReviewAdapter reviewAdapter;

    public GuestReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestReviewFragment newInstance(String param1, String param2) {
        GuestReviewFragment fragment = new GuestReviewFragment();
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
        return inflater.inflate(R.layout.fragment_guest_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setRcv();
        setSendbtn();

        if(MainActivity.user == null)
            reviewlayout.setVisibility(View.GONE);
    }
    private void initView(View view)
    {
        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviews,getContext(),R.layout.review_item);
        hotel = (Hotel)getArguments().getSerializable("hotel");
        sendbtn = view.findViewById(R.id.fragguestrev_postbtn);
        edtcom = view.findViewById(R.id.fragguestrev_edtrev);
        txttotal = view.findViewById(R.id.fragguestrev_resulttxt);
        reviewlayout = view.findViewById(R.id.fragguestrev_reviewlayout);
        ratingBar = view.findViewById(R.id.fragguestrev_ratingbtn);
        rcv = view.findViewById(R.id.fragguestrev_rcv);
        backbtn = view.findViewById(R.id.fragguestrev_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
    }
    private void setRcv()
    {
        db.collection("Review").whereEqualTo("hotel.id",hotel.getId()).
                orderBy("postdate", Query.Direction.DESCENDING).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    txttotal.setText(task.getResult().size() + " reviews");
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Review review = queryDocumentSnapshot.toObject(Review.class);
                        reviews.add(review);
                    }
                    rcv.setAdapter(reviewAdapter);
                    rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }
            }
        });
    }

    private void setSendbtn()
    {
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtcom.getText().toString().trim();
                int rating_point = (int) ratingBar.getRating();
                if((content.isEmpty() || content.length() == 0) || rating_point == 0)
                    edtcom.setError("Please rating first then add comment");
                else
                {
                    DocumentReference ref = db.collection("Review").document();
                    Review review = new Review(ref.getId(),content, Timestamp.now(),
                            rating_point,MainActivity.user,hotel);
                    int i = review.getHotel().getStars().get(rating_point - 1) + 1;
                    review.getHotel().getStars().set(rating_point - 1,i);
                    float t = review.calcRatingPoint();
                    review.getHotel().setOverallrating(t);
                    db.document("Hotel/" + review.getHotel().getId()).
                            update("stars", review.getHotel().getStars(),
                                    "overallrating",t);
                    ref.set(review);
                    reviews.add(0,review);
                    txttotal.setText(reviews.size() + " reviews");
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}