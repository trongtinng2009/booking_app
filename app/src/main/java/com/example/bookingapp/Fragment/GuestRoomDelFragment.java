package com.example.bookingapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.Activity.BookingActivity;
import com.example.bookingapp.Adapter.ImgAdapter;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.FavoriteHotel;
import com.example.bookingapp.Model.FavoriteRoom;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestRoomDelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestRoomDelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText edtname,edtmaxal,edtmaxkid,edthotel,edtpriceal,edtpricekid,edtsize,
            edtnor;
    FirebaseFirestore db;
    ImageView img;
    RecyclerView rcv;
    Button btn;
    ImgAdapter imgAdapter;
    TextView txtroom;
    Room room;
    ImageButton backbtn,favbtn;
    FavoriteRoom favoriteRoom;
    boolean favRoom = false;

    public GuestRoomDelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestRoomDelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestRoomDelFragment newInstance(String param1, String param2) {
        GuestRoomDelFragment fragment = new GuestRoomDelFragment();
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
        return inflater.inflate(R.layout.fragment_guest_room_del, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db= FirebaseFirestore.getInstance();
        initView(view);
        setContentForView();
        setBtn();
        if(MainActivity.user != null)
        {
            setFavbtn();
            checkFavbtn();
        }
    }
    private void initView(View view)
    {
        room = (Room) getArguments().getSerializable("room");
        edtmaxal = view.findViewById(R.id.fragguestroomdel_maxal);
        edtmaxkid = view.findViewById(R.id.fragguestroomdel_maxkid);
        edtname = view.findViewById(R.id.fragguestroomdel_name);
        edthotel = view.findViewById(R.id.fragguestroomdel_holname);
        edtnor = view.findViewById(R.id.fragguestroomdel_quan);
        backbtn = view.findViewById(R.id.fragguestroomdel_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        favbtn = view.findViewById(R.id.fragguestroomdel_favbtn);
        edtpriceal = view.findViewById(R.id.fragguestroomdel_priceal);
        txtroom = view.findViewById(R.id.fragguestroomdel_countrytitle);
        edtpricekid = view.findViewById(R.id.fragguestroomdel_pricekid);
        btn = view.findViewById(R.id.fragguestroomdel_bookbtn);
        edtsize = view.findViewById(R.id.fragguestroomdel_size);
        img = view.findViewById(R.id.fragguestroomdel_img);
        rcv = view.findViewById(R.id.fragguestroomdel_rcv);
        imgAdapter = new ImgAdapter(getContext(),null,R.layout.pop_des_item);
        imgAdapter.setBitmapstring(room.getImgs());
    }
    private void setFavbtn()
    {
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(favRoom)
                {
                    db.collection("FavoriteRoom").document(favoriteRoom.getId()).delete();
                    favbtn.setColorFilter(getResources().getColor(R.color.black));
                    favRoom = false;
                }
                else
                {
                    favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                    DocumentReference ref = db.collection("FavoriteRoom").document();
                    FavoriteRoom favoriteRoom1 = new FavoriteRoom(ref.getId(),MainActivity.user,room);
                    favoriteRoom = favoriteRoom1;
                    ref.set(favoriteRoom1);
                    favRoom = true;
                }
            }
        });
    }
    private void checkFavbtn()
    {
        db.collection("FavoriteRoom").
                whereEqualTo("user.id",MainActivity.user.getId())
                .whereEqualTo("room.id",room.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() > 0)
                        {
                            favoriteRoom = task.getResult().getDocuments().get(0).toObject(FavoriteRoom.class);
                            favRoom = true;
                            favbtn.setColorFilter(getResources().getColor(R.color.high_pink));
                        }
                        else
                            favbtn.setColorFilter(getResources().getColor(R.color.black));
                    }
                });
    }
    private void setContentForView()
    {
        edtsize.setText(Double.toString(room.getSize()));
        edtpricekid.setText(Double.toString(room.getPricekid()));
        edtpriceal.setText(Double.toString(room.getPriceadult()));
        edtnor.setText(Integer.toString(room.getRemain_quantity()));
        edthotel.setText(room.getHotel().getName());
        edtname.setText(room.getName());
        Picasso.get().load(room.getImg()).into(img);
        txtroom.setText(room.getName().toUpperCase());
        edtmaxkid.setText(Integer.toString(room.getMaxkid()));
        edtmaxal.setText(Integer.toString(room.getMaxadult()));
        setRcv();
    }
    private void setRcv()
    {
        rcv.setAdapter(imgAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }
    private void setBtn()
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BookingActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("room",room);
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }
}