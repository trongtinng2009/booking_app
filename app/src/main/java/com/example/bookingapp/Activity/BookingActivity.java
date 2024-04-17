package com.example.bookingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.Hotel;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;

import java.util.regex.Pattern;

public class BookingActivity extends AppCompatActivity {
    EditText edtname,edtphone,edtmail,edtadultquan,edtkidquan,edtroomquan;
    Button btnbook;
    private Room room;
    ImageButton backbtn;
    private User user;
    private TextView logintxt,loginreminder;
    boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initView();
        valid = true;
        setBtnbook();
    }
    private void initView()
    {
        edtadultquan = findViewById(R.id.bookingact_edtadult);
        edtkidquan = findViewById(R.id.bookingact_edtkid);
        edtmail = findViewById(R.id.bookingact_edtemail);
        edtname = findViewById(R.id.bookingact_edtname);
        edtroomquan = findViewById(R.id.bookingact_roomquan);
        edtphone = findViewById(R.id.bookingact_edtphone);
        btnbook = findViewById(R.id.bookingact_nextbtn);
        backbtn = findViewById(R.id.bookingact_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginreminder = findViewById(R.id.bookingact_loginreminder);
        logintxt = findViewById(R.id.bookingact_logintxt);
        room = (Room) getIntent().getExtras().getSerializable("room");
        checkUser();
    }
    private void checkUser()
    {
        if(MainActivity.user != null)
        {
            user = MainActivity.user;
            logintxt.setVisibility(View.GONE);
            loginreminder.setVisibility(View.GONE);
            edtphone.setText(user.getPhone());
            edtphone.setEnabled(false);
            edtname.setText(user.getFullname());
            edtname.setEnabled(false);
            edtmail.setText(user.getEmail());
            edtmail.setEnabled(false);
        }
        else {
            edtphone.setText("");
            edtphone.setEnabled(true);
            edtmail.setText("");
            edtmail.setEnabled(true);
            edtname.setText("");
            edtname.setEnabled(true);
            logintxt.setVisibility(View.VISIBLE);
            loginreminder.setVisibility(View.VISIBLE);
        }
    }
    private void setBtnbook()
    {
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edtmail.getText().toString().trim();
                if(mail.isEmpty() || mail.length() == 0)
                {
                    edtmail.setError("Must have value");
                    valid = false;
                }
                else
                {
                    Pattern emailregex =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    if(!emailregex.matcher(mail).matches())
                    {
                        edtmail.setError("Not an email");
                        valid = false;
                    }
                }
                String phone = edtphone.getText().toString().trim();
                if(phone.isEmpty() || phone.length() == 0)
                {
                    edtphone.setError("Must have value");
                    valid =false;
                }
                else
                {
                    if(phone.length() < 7)
                    {
                        valid = false;
                        edtphone.setError("Must have more than 7 characters");
                    }
                }
                String name = edtname.getText().toString().trim();
                if(name.isEmpty() || name.length() == 0)
                {
                    valid = false;
                    edtname.setError("Must have value");
                }
                else
                {
                    if(name.length() < 5)
                    {
                        valid = false;
                        edtname.setError("Must have more than 5 characters");
                    }
                }
                String roomquan = edtroomquan.getText().toString().trim();
                if(roomquan.isEmpty() || roomquan.length() ==0)
                {
                    valid = false;
                    edtroomquan.setError("Must have value");
                }
                else
                {
                    int roomtotal = Integer.parseInt(roomquan);
                    if(roomtotal > room.getRemain_quantity())
                    {
                        valid = false;
                        edtroomquan.setError("Can't greater than remain rooms");
                    }
                    String adultquan = edtadultquan.getText().toString().trim();
                    if(adultquan.isEmpty() || adultquan.length() == 0)
                    {
                        valid = false;
                        edtadultquan.setError("Must have value");
                    }
                    String kidquan = edtkidquan.getText().toString().trim();
                    if(kidquan.isEmpty() || kidquan.length() == 0)
                    {
                        valid = false;
                        edtkidquan.setError("Must have value");
                    }
                    if(valid)
                    {
                        Intent i = new Intent(BookingActivity.this, BookingNextStepActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("room",room);
                        b.putInt("adultquan",Integer.parseInt(adultquan));
                        b.putInt("kidquan",Integer.parseInt(kidquan));
                        b.putInt("roomquan",roomtotal);
                        i.putExtras(b);
                        if(MainActivity.user == null)
                        {
                            User user = new User(null,phone,name,mail,"Guest",null,null);
                            i.putExtra("user",user);
                        }
                        startActivity(i);
                    }
                }
                valid = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }
}