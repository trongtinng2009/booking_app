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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.Activity.AdminActivity;
import com.example.bookingapp.Adapter.ImgAdapter;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminRoomDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRoomDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText edtid,edtname,edthotel,edtmaxal,edtmaxkid,edtpriceal,edtpricekid,edtsize,
            edtnor;
    ImageView img;
    FirebaseFirestore db;
    Button btnadd,btnimgs;
    ImageButton backbtn;
    RecyclerView oldimgsrcv,newimgsrcv;
    ArrayList<String> imgstring;
    ArrayList<Bitmap> bitmaps;
    ProgressDialog progressDialog;
    StorageReference sr;
    Room room;
    FirebaseStorage fs;
    ImgAdapter imgAdapter,newImgAdapter;
    boolean pickImgTitle = true;

    public AdminRoomDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminRoomDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminRoomDetailFragment newInstance(String param1, String param2) {
        AdminRoomDetailFragment fragment = new AdminRoomDetailFragment();
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
        return inflater.inflate(R.layout.fragment_admin_room_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        fs = FirebaseStorage.getInstance();
        initView(view);
        setContentForView(room);
        setImgTitle();
        setImgsAdd();
        setBtnadd();
        setRcv();
    }
    private void initView(View view)
    {
        room = (Room) getArguments().getSerializable("room");
        imgstring = room.getImgs();
        bitmaps = new ArrayList<>();
        imgAdapter = new ImgAdapter(getContext(),null,R.layout.pop_des_item);
        imgAdapter.setBitmapstring(imgstring);
        imgAdapter.setCanRemove(true);
        newImgAdapter = new ImgAdapter(getContext(),bitmaps,R.layout.pop_des_item);
        edtid = view.findViewById(R.id.fragadminroomdel_id);
        edthotel = view.findViewById(R.id.fragadminroomdel_holname);
        edtmaxal = view.findViewById(R.id.fragadminroomdel_maxal);
        edtname = view.findViewById(R.id.fragadminroomdel_name);
        edtnor = view.findViewById(R.id.fragadminroomdel_quan);
        edtmaxkid = view.findViewById(R.id.fragadminroomdel_maxkid);
        edtpriceal = view.findViewById(R.id.fragadminroomdel_priceal);
        edtpricekid = view.findViewById(R.id.fragadminroomdel_pricekid);
        backbtn = view.findViewById(R.id.fragadminroomdel_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.fragmentManager2.popBackStack();
            }
        });
        edtsize = view.findViewById(R.id.fragadminroomdel_size);
        img = view.findViewById(R.id.fragadminroomdel_img);
        oldimgsrcv = view.findViewById(R.id.fragadminroomdel_rcv);
        newimgsrcv = view.findViewById(R.id.fragadminroomdel_newimgrcv);
        btnadd = view.findViewById(R.id.fragadminroomdel_addbtn);
        btnimgs = view.findViewById(R.id.fragadminroomdel_addimgbtn);
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
        title.setText("EDITTED");
        maintxt.setText("You've editted this room successfully");
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void setConfirmPopUp(String id, String name, int maxal,int maxkid,
                                 double priceal, double pricekid,double size,int nor)
    {
        Dialog dialog = Utils.getPopup(getContext(),R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),
                maintxt = dialog.findViewById(R.id.alert_popup_maintxt);
        Button ig = dialog.findViewById(R.id.alert_popup_igbtn),
                acc = dialog.findViewById(R.id.alert_popup_accbtn);
        title.setText("CONFIRM TO EDIT ROOM");
        maintxt.setText("Are you sure to edit this room ?");
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
                AddInfo(id,name,maxal,maxkid,
                        priceal,pricekid,size,nor);
            }
        });
        dialog.show();
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
        btnimgs.setOnClickListener(new View.OnClickListener() {
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
                                bitmaps.add(selectedImgBit);
                                newImgAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
    private void setBtnadd()
    {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfoValid();
            }
        });

    }
    private void checkInfoValid()
    {
        boolean valid = true;
        String name = edtname.getText().toString().trim(),
                maxalstr = edtmaxal.getText().toString().trim(),
                maxkidstr = edtmaxkid.getText().toString().trim(),
                pricealstr = edtpriceal.getText().toString().trim(),
                pricekidstr = edtpricekid.getText().toString().trim(),
                sizestr = edtsize.getText().toString().trim(),
                norstr = edtnor.getText().toString().trim();
        if(name.isEmpty() || name.length() == 0) {
            edtname.setError("Must have value");valid = false;
        }
        if(maxalstr.isEmpty() || maxkidstr.length() == 0)
        {
            edtmaxal.setError("Must have value");valid = false;
        }
        else
        {
            int maxal = Integer.parseInt(edtmaxal.getText().toString().trim());
            if(maxal <= 0){
                edtmaxal.setError("Must greater than 0");valid = false;}
        }
        if(maxkidstr.isEmpty() || maxkidstr.length() == 0)
        {
            edtmaxkid.setError("Must have value");valid = false;
        }
        else
        {
            int maxkid = Integer.parseInt(edtmaxkid.getText().toString().trim());
            if(maxkid <=0){edtmaxkid.setError("Must greater than 0");valid = false;}
        }
        if(pricealstr.isEmpty() || pricealstr.length() == 0){valid = false; edtpriceal.setError("Must have value");}
        else
        {
            double priceal = Double.parseDouble(pricealstr);
            if(priceal <= 0){edtpriceal.setError("Must greater than 0");valid = false;}
        }
        if(pricekidstr.isEmpty() || pricekidstr.length() == 0){valid = false;edtpricekid.setError("Must have value");}
        else
        {
            double pricekid = Double.parseDouble(pricekidstr);
            if(pricekid <= 0){edtpricekid.setError("Must greater than 0");valid = false;}
        }
        if(sizestr.isEmpty() || sizestr.length() == 0){valid = false;edtsize.setError("Must have value");}
        else
        {
            double size = Double.parseDouble(sizestr);
            if(size <= 0){edtsize.setError("Must greater than 0");valid = false;}
        }
        if(norstr.isEmpty() || norstr.length() == 0){valid = false;edtnor.setError("Must have value");}
        else
        {
            int nor = Integer.parseInt(norstr);
            if(nor <= 0){edtnor.setError("Must greater than 0");valid = false;}
        }
        if(valid)
        {
            setConfirmPopUp(edtid.getText().toString().trim(),name,
                    Integer.parseInt(maxalstr),Integer.parseInt(maxkidstr),
                    Double.parseDouble(pricealstr),Double.parseDouble(pricekidstr)
                    ,Double.parseDouble(sizestr),Integer.parseInt(norstr));
        }
    }
    private void AddInfo(String id, String name, int maxal,int maxkid,
                         double priceal, double pricekid,double size,int nor)
    {
        for(Bitmap bitmap1 : bitmaps)
        {
            UpImgs(bitmap1);
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] hinhAnh = outputStream.toByteArray();
        String path = "bookingapp/rooms/" + UUID.randomUUID() + ".png";
        sr = fs.getReference(path);
        sr.putBytes(hinhAnh).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sr.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        db.collection("Room").document(id).
                                update("name",name,"maxadult",maxal
                                        ,"maxkid",maxkid,"priceadult",priceal,
                                        "pricekid",pricekid,"quantity",nor,"size",
                                        size,"img",task.getResult().toString(),
                                        "imgs",imgstring);
                        db.collection("Room").document(id)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    imgstring.clear();
                                    bitmaps.clear();
                                    Room room1 = task.getResult().toObject(Room.class);
                                    imgstring = room1.getImgs();
                                    imgAdapter.setBitmapstring(imgstring);
                                    imgAdapter.notifyDataSetChanged();
                                    newImgAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    Picasso.get().load(room1.getImg()).into(img);
                                    setContentForView(room1);
                                    setSuccessPopup();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    private void UpImgs(Bitmap bitmap)
    {
        String path = "bookingapp/rooms/" + UUID.randomUUID() + ".png";
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
                        imgstring.add(task.getResult().toString());
                    }
                });
            }
        });
    }
    private void setContentForView(Room room)
    {
        edtsize.setText(Double.toString(room.getSize()));
        edtpricekid.setText(Double.toString(room.getPricekid()));
        edtpriceal.setText(Double.toString(room.getPriceadult()));
        edtmaxkid.setText(Integer.toString(room.getMaxkid()));
        edtnor.setText(Integer.toString(room.getQuantity()));
        edtname.setText(room.getName());
        edtid.setText(room.getId());
        edthotel.setText(room.getHotel().getId());
        edtmaxal.setText(Integer.toString(room.getMaxadult()));
        Picasso.get().load(room.getImg()).into(img);
    }
    private void setRcv()
    {
        oldimgsrcv.setAdapter(imgAdapter);
        oldimgsrcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        newimgsrcv.setAdapter(newImgAdapter);
        newimgsrcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }
}