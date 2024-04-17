package com.example.bookingapp.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import android.provider.MediaStore;
import android.util.Base64;
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
import com.example.bookingapp.Adapter.CountryAutoAdapter;
import com.example.bookingapp.Model.Country;
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
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAddLocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddLocFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtname,edtid;
    private AutoCompleteTextView countryid;
    private ImageButton backbtn;
    private ImageView img;
    private ArrayList<Country> countries;
    private Button btnadd;
    private FirebaseFirestore db;
    private FirebaseStorage fs;
    private StorageReference sr;
    ProgressDialog progressDialog;

    public AdminAddLocFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAddLocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddLocFragment newInstance(String param1, String param2) {
        AdminAddLocFragment fragment = new AdminAddLocFragment();
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
        return inflater.inflate(R.layout.fragment_admin_add_loc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        fs = FirebaseStorage.getInstance();
        initView(view);
        setCountry();
        setAddbtn();
        setChangeImg();
    }
    private void initView(View view)
    {
        btnadd = view.findViewById(R.id.fragadminaddloc_addbtn);
        edtid = view.findViewById(R.id.fragadminaddloc_id);
        edtname = view.findViewById(R.id.fragadminaddloc_name);
        countryid = view.findViewById(R.id.fragadminaddloc_counname);
        img = view.findViewById(R.id.fragadminaddloc_img);
        backbtn = view.findViewById(R.id.fragadminaddloc_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager2.popBackStack();
            }
        });
    }
    private void setCountry()
    {
        countries = new ArrayList<>();
        db.collection("Country").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        Country country = queryDocumentSnapshot.toObject(Country.class);
                        countries.add(country);
                    }
                    countryid.setAdapter(new CountryAutoAdapter(getContext(),R.layout.country_auto_item,countries));
                }
            }
        });
    }
    private void setAddbtn()
    {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtid.getText().toString().trim();
                if(id.isEmpty() || id.length() == 0)
                {
                    edtid.setError("Must have value");
                }
                else
                {
                    db.collection("Location").whereEqualTo("id",id)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                  if(task.getResult().size() > 0)
                                      edtid.setError("Existing ID");
                                  else
                                  {
                                      String country = countryid.getText().toString().trim();
                                      db.collection("Country").whereEqualTo("id",country)
                                              .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                      if(task.getResult().size() == 1)
                                                      {
                                                          Country country1 = task.getResult().getDocuments().get(0).toObject(Country.class);
                                                          setConfirmPopUp(id,country1);
                                                      }
                                                      else
                                                      {
                                                          countryid.setError("Not available country");
                                                      }
                                                  }
                                              });

                                  }
                                }
                            });
                }
            }
        });
    }
    private void setInfoAdd(String id,Country country)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] hinhAnh = outputStream.toByteArray();
        String path = "bookingapp/locations/" + UUID.randomUUID() + ".png";
        sr = fs.getReference(path);
        sr.putBytes(hinhAnh).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sr.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Location location = new Location(id,
                                edtname.getText().toString().trim(),task.getResult().toString(),
                                country,0);
                        db.collection("Location").document(location.getId()).
                                set(location);
                        progressDialog.dismiss();
                        setSuccessPopup();
                    }
                });
            }
        });
    }
    private void showProgressDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    private void setConfirmPopUp(String id,Country country)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        Button ig = dialog.findViewById(R.id.alert_popup_igbtn),
                acc = dialog.findViewById(R.id.alert_popup_accbtn);
        title.setText("CONFIRM LOCATION");
        maintxt.setText("Are you sure to add this location ?");
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
                setInfoAdd(id,country);
            }
        });
        dialog.show();
    }
    private void setSuccessPopup()
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.success_popup);
        TextView maintxt = dialog.findViewById(R.id.reg_libcard_popup_txt),
                title = dialog.findViewById(R.id.reg_libcard_popup_title);
        Button acc = dialog.findViewById(R.id.successpopup_btn);
        title.setText("ADDED");
        maintxt.setText("You've added this location successfully");
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void setChangeImg()
    {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            img.setImageBitmap(selectedImgBit);
                        }
                    }
                }
            });
}