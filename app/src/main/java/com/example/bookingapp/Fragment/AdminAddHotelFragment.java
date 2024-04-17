package com.example.bookingapp.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Adapter.ImgAdapter;
import com.example.bookingapp.Adapter.LocationAutoAdapter;
import com.example.bookingapp.Model.Country;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Location;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddHotelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddHotelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtid,edtname,edtaddress,edthotline,edtsum,edtdis,edtprice;
    private AutoCompleteTextView autoloc;
    private RecyclerView rcvimgs;
    private ProgressDialog progressDialog;
    private ImageView img;
    private Button btnadd,btnaddimgs;
    ImageButton backbtn;
    private ArrayList<Location> locations;
    private ArrayList<String> imgsstring;
    private FirebaseFirestore db;
    private FirebaseStorage fs;
    private StorageReference sr;
    private ArrayList<Bitmap> imgs;
    private ImgAdapter imgAdapter;
    private boolean pickImgTitle = true;

    public AdminAddHotelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAddHotelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddHotelFragment newInstance(String param1, String param2) {
        AdminAddHotelFragment fragment = new AdminAddHotelFragment();
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
        return inflater.inflate(R.layout.fragment_admin_add_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        fs = FirebaseStorage.getInstance();
        initView(view);
        setLocation();
        setAddBtn();
        setImgTitle();
        setImgsAdd();
    }

    private void initView(View view)
    {
        imgs = new ArrayList<>();
        locations = new ArrayList<>();
        imgsstring = new ArrayList<>();
        imgAdapter = new ImgAdapter(getContext(),imgs,R.layout.pop_des_item);
        edtid = view.findViewById(R.id.fragadminaddhol_id);
        edtaddress = view.findViewById(R.id.fragadminaddhol_address);
        edtdis = view.findViewById(R.id.fragadminaddhol_distance);
        edtname = view.findViewById(R.id.fragadminaddhol_name);
        edtsum = view.findViewById(R.id.fragadminaddhol_summary);
        edthotline = view.findViewById(R.id.fragadminaddhol_hotline);
        img = view.findViewById(R.id.fragadminaddhol_img);
        autoloc = view.findViewById(R.id.fragadminaddhol_locname);
        backbtn = view.findViewById(R.id.fragadminaddhol_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager2.popBackStack();
            }
        });
        btnadd = view.findViewById(R.id.fragadminaddhol_addbtn);
        rcvimgs = view.findViewById(R.id.fragadminaddhol_rcv);
        edtprice = view.findViewById(R.id.fragadminaddhol_price);
        rcvimgs.setAdapter(imgAdapter);
        rcvimgs.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        btnaddimgs = view.findViewById(R.id.fragadminaddhol_addimgbtn);
    }
    private void setAddBtn()
    {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtid.getText().toString().trim();
                if(id.isEmpty() || id.length() == 0)
                    edtid.setError("Must have value");
                else
                {
                    db.collection("Hotel").whereEqualTo("id",id)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.getResult().size() > 0)
                                edtid.setError("Existing ID");
                            else
                            {
                                String locid = autoloc.getText().toString().trim();
                                if(locid.isEmpty() || locid.length() == 0)
                                    autoloc.setError("Must have value");
                                else
                                {
                                    db.collection("Location").whereEqualTo("id",locid)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.getResult().size() == 0)
                                                autoloc.setError("Not available ID");
                                            else
                                            {
                                                Location location = task.getResult().getDocuments().get(0).toObject(Location.class);
                                                setInfoAdd(id,location);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    private void setInfoAdd(String id,Location location)
    {
        String hotelname = edtname.getText().toString().trim()
                ,hotline = edthotline.getText().toString().trim()
                ,address = edtaddress.getText().toString().trim()
                ,summary = edtsum.getText().toString().trim()
                ,dis = edtdis.getText().toString().trim()
                ,price = edtprice.getText().toString().trim();
        boolean valid = true;
        if(hotelname.isEmpty() || hotelname.length() == 0)
        {
            edtname.setError("Must have value");
            valid = false;
        }
        if(hotline.isEmpty() || hotline.length() == 0)
        {
            edthotline.setError("Must have value");
            valid = false;
        }
        if(address.isEmpty() || address.length() == 0)
        {
            edtaddress.setError("Must have value");
            valid = false;
        }
        if(price.isEmpty() || price.length() == 0)
        {
            edtprice.setError("Must have value");
            valid = false;
        }
        else if(Double.parseDouble(price) < 1)
        {
            edtprice.setError("Must greater than 0");
            valid = false;
        }
        if(valid)
        {
            setConfirmPopUp(id,location,address,hotline,hotelname,
                    summary,price,dis);
        }
    }
    private void AddInfo(String id,Location location,String address,String hotline,String hotelname,
                         String summary,String price,String dis)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        for(Bitmap bitmap1 : imgs)
        {
            UpImgs(bitmap1);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] hinhAnh = outputStream.toByteArray();
        String path = "bookingapp/hotels/" + UUID.randomUUID() + ".png";
        sr = fs.getReference(path);
        sr.putBytes(hinhAnh).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sr.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Hotel hotel = new Hotel(imgsstring,location,address,hotline,id,
                                task.getResult().toString(),hotelname,summary,Double.parseDouble(price));
                        if(dis.length() > 0)
                            hotel.setDistance(Double.parseDouble(dis));
                        db.collection("Hotel").document(hotel.getId())
                                .set(hotel);
                        progressDialog.dismiss();
                        imgs.clear();
                        imgsstring.clear();
                        imgAdapter.notifyDataSetChanged();
                        setSuccessPopup();
                    }
                });
            }
        });
    }
    private void UpImgs(Bitmap bitmap)
    {
        String path = "bookingapp/hotels/" + UUID.randomUUID() + ".png";
        StorageReference storageReference = fs.getReference(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] hinhAnh = outputStream.toByteArray();
        storageReference.putBytes(hinhAnh).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        imgsstring.add(task.getResult().toString());
                    }
                });
            }
        });
    }
    private void setImgTitle()
    {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImgTitle = true;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startPickImg.launch(i);

            }
        });
    }
    private void setImgsAdd()
    {
        btnaddimgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImgTitle = false;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startPickImg.launch(i);
            }
        });
    }
    ActivityResultLauncher<Intent> startPickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data != null && data.getData() != null)
                        {
                            Uri selectedImg = data.getData();
                            Bitmap selectedImgBit = null;
                            try {
                                selectedImgBit = MediaStore.Images.Media.getBitmap(
                                        getContext().getContentResolver(),selectedImg
                                );
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            if(pickImgTitle)
                                img.setImageBitmap(selectedImgBit);
                            else
                            {
                                imgs.add(selectedImgBit);
                                imgAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
    private void setConfirmPopUp(String id,Location location,String address,String hotline,String hotelname,
                                 String summary,String price,String dis)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        Button ig = dialog.findViewById(R.id.alert_popup_igbtn),
                acc = dialog.findViewById(R.id.alert_popup_accbtn);
        title.setText("CONFIRM HOTEL");
        maintxt.setText("Are you sure to add this hotel ?");
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                showProgressDialog();
                AddInfo(id,location,address,hotline,hotelname,
                        summary,price,dis);
            }
        });
        dialog.show();
    }
    private void showProgressDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    private void setSuccessPopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.success_popup);
        TextView maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt),
                title = dialog.findViewById(R.id.reg_libcard_popup_title);
        Button acc = dialog.findViewById(R.id.successpopup_btn);
        title.setText("ADDED");
        maintxt.setText("You've added this hotel successfully");
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void setLocation()
    {
        db.collection("Location").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                           {
                               Location location = queryDocumentSnapshot.toObject(Location.class);
                               locations.add(location);
                           }
                           autoloc.setAdapter(new LocationAutoAdapter(getContext(),R.layout.country_auto_item,locations));
                       }
                    }
                });
    }
}