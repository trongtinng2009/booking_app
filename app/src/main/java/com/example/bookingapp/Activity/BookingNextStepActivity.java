package com.example.bookingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.Model.RoomBooking;
import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.internal.Util;

public class BookingNextStepActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView txtlogin,txttitle,txtadult,txtkid,txtroom,txttotal,txthotelname;
    EditText edtname,edtmail,edtphone,edtcard;
    Button btncheckin,btncheckout;
    User user;
    Button btnbook;
    ImageView imgroom,imghotel;
    ImageButton backbtn;
    Room room;
    boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_next_step);
        initView();
        db = FirebaseFirestore.getInstance();
        checkUser();
        setDataForView();
        valid = true;
        setChooseDay();
        setBtnbook();
    }
    private void initView()
    {
        txtlogin = findViewById(R.id.booknextstep_logintxt);
        txttitle = findViewById(R.id.booknextstep_title);
        txtadult = findViewById(R.id.booknextstep_adulttotalandprice);
        txtkid = findViewById(R.id.booknextstep_kidtotalandprice);
        txttotal = findViewById(R.id.booknextstep_totalprice);
        txtroom = findViewById(R.id.booknextstep_roomtotal);
        edtname = findViewById(R.id.booknextstep_nameedt);
        edtcard = findViewById(R.id.booknextstep_cardedt);
        btncheckin = findViewById(R.id.booknextstep_checkinedt);
        btncheckout = findViewById(R.id.booknextstep_checkoutedt);
        edtphone = findViewById(R.id.booknextstep_phoneedt);
        imgroom = findViewById(R.id.booknextstep_imgroom);
        backbtn = findViewById(R.id.bookingnextact_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imghotel = findViewById(R.id.bookingnextact_imghotel);
        txthotelname = findViewById(R.id.bookingnextact_txthotelname);
        btnbook = findViewById(R.id.bookingnextact_bookbtn);
        edtmail = findViewById(R.id.booknextstep_mailedt);
        room = (Room) getIntent().getExtras().getSerializable("room");
    }
    private void setDataForView()
    {
        int kidquan = getIntent().getExtras().getInt("kidquan");
        int adultquan = getIntent().getExtras().getInt("adultquan");
        txtroom.setText(getIntent().getExtras().getInt("roomquan") + " x " + room.getName());
        txtkid.setText( kidquan+ " kids: " + room.calKidPrice(kidquan)+"$");
        txtadult.setText(adultquan + " adults: " + room.calAdultPrice(adultquan)+"$");
        txttotal.setText("Total: " + room.calPrice(adultquan,kidquan)+"$");
        edtname.setText(user.getFullname());
        edtname.setEnabled(false);
        edtmail.setText(user.getEmail());
        edtmail.setEnabled(false);
        edtphone.setText(user.getPhone());
        edtphone.setEnabled(false);
        imgroom.setImageResource(getResources().getIdentifier(room.getImg(),"drawable",getPackageName()));
        Picasso.get().load(room.getHotel().getImgtitle()).into(imghotel);
        txthotelname.setText(room.getHotel().getName());
    }
    private void setChooseDay()
    {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        btncheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingNextStepActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(year,month,dayOfMonth);
                        Date date = calendar1.getTime();
                        btncheckin.setText(Utils.dateToString(date));
                    }
                },nam,thang,ngay);
                datePickerDialog.show();
            }
        });
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingNextStepActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(year,month,dayOfMonth);
                        Date date = calendar1.getTime();
                        btncheckout.setText(Utils.dateToString(date));
                    }
                },nam,thang,ngay);
                datePickerDialog.show();
            }
        });
    }
    private void setBtnbook()
    {
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String card = edtcard.getText().toString().trim();
               if(card.isEmpty() || card.length() == 0)
               {
                   valid = false;
                   edtcard.setError("Must have value");
               }
               String checkin = btncheckin.getText().toString().trim();
                String checkout = btncheckout.getText().toString().trim();
                if(checkin.isEmpty() || checkout.isEmpty())
                {
                    if (checkin.isEmpty() || checkin.length() == 0) {
                        valid = false;
                        btncheckin.setError("Must have value");
                    }
                    if (checkout.isEmpty() || checkout.length() == 0) {
                        valid = false;
                        btncheckout.setError("Must have value");
                    }
                }
                else
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date checkindate = null;
                    try {
                        checkindate = simpleDateFormat.parse(checkin);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Date checkoutdate = null;
                    try {
                        checkoutdate = simpleDateFormat.parse(checkout);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if(checkindate.compareTo(checkoutdate) > 0)
                    {
                        valid = false;
                        btncheckin.setError("Can't greater than checkout date");
                        btncheckout.setError("Can't less than checkin date");
                    }
                    if(valid)
                    {
                        showAlertPopUp(card,checkindate,checkoutdate);
                    }
                }
                valid = true;
            }
        });
    }
    private void showSuccesPopup()
    {
        Dialog dialog = Utils.getPopup(this,R.layout.success_popup);
        TextView txttitle = dialog.findViewById(R.id.reg_libcard_popup_title),txtmain
                = dialog.findViewById(R.id.reg_libcard_popup_txt);
        Button ok = dialog.findViewById(R.id.successpopup_btn);
        ImageButton ig = dialog.findViewById(R.id.successpopup_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txttitle.setText("BOOKING SUCCESS");
        txtmain.setText("You've reserved this room \n " +
                "Staff'll call you in a few minutes !");
        dialog.show();
    }
    private void showAlertPopUp(String card,Date checkin,Date checkout)
    {
        Dialog dialog = Utils.getPopup(this,R.layout.alert_popup);
        TextView title = dialog.findViewById(R.id.alert_popup_title),maintxt
                = dialog.findViewById(R.id.alert_popup_maintxt);
        title.setText("BOOKING CONFIRM");
        maintxt.setText("Are you sure to book this room ?");
        Button acc = dialog.findViewById(R.id.alert_popup_accbtn),ig = dialog.findViewById(R.id.alert_popup_igbtn);
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
                int totalkid = getIntent().getExtras().getInt("kidquan");
                int totaladult = getIntent().getExtras().getInt("adultquan");
                int totalroom = getIntent().getExtras().getInt("roomquan");
                User userref = null;
                if(MainActivity.user != null)
                {
                    userref = MainActivity.user;
                    user = null;
                }
                RoomBooking roomBooking = new RoomBooking(null,userref,null
                ,room,card,
                        user, new Timestamp(checkin),new Timestamp(checkout)
                ,Timestamp.now(),null, RoomBooking.RoomBookingState.BOOKING.value,
                        totalkid,totaladult,totalroom,room.calPrice(totaladult,totalkid));
                DocumentReference documentReference = db.collection("RoomBooking")
                                .document();
                roomBooking.setId(documentReference.getId());
                documentReference.set(roomBooking);
                db.collection("Room").document(room.getId())
                                .update("remain_quantity",room.getRemain_quantity() -totalroom);
                showSuccesPopup();
            }
        });
        dialog.show();
    }
    private void checkUser()
    {
        if(MainActivity.user != null)
        {
            txtlogin.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = 0;
            params.weight = 8.0f;
            txttitle.setLayoutParams(params);
            user = MainActivity.user;
        }
        else
        {
            txtlogin.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = 0;
            params.weight = 6.0f;
            txttitle.setLayoutParams(params);
            user = getIntent().getParcelableExtra("user");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }
}