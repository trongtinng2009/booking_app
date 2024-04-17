package com.example.bookingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bookingapp.Fragment.GuestProfileFragment;
import com.example.bookingapp.MainActivity;
import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    ImageButton backbtn;
    FirebaseFirestore db;
    Button loginbtn;
    EditText edtphone,edtpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        db = FirebaseFirestore.getInstance();
        setLoginbtn();
    }
    private void initView()
    {
        backbtn = findViewById(R.id.login_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginbtn = findViewById(R.id.login_loginbtn);
        edtpass = findViewById(R.id.login_pass);
        edtphone = findViewById(R.id.login_phone);
    }
    private void setLoginbtn()
    {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtphone.getText().toString().trim();
                String pass = edtpass.getText().toString().trim();
                db.collection("User").whereEqualTo("phone",phone)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           if(task.getResult().size() < 1)
                               edtphone.setError("Not found this phone number");
                           else
                           {
                               db.collection("User").whereEqualTo("phone",phone)
                               .whereEqualTo("password",pass)
                               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                       if(task.isSuccessful())
                                       {
                                           if(task.getResult().size() > 0)
                                           {
                                               User user = task.getResult().getDocuments().get(0).toObject(User.class);
                                               if(user.getRole().equals("Guest")) {
                                                   MainActivity.user = user;
                                                   finish();
                                               }
                                               else
                                               {
                                                   Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                                   i.putExtra("user",user);
                                                   startActivity(i);
                                               }
                                           }
                                           else
                                               edtpass.setError("Wrong password");
                                       }
                                   }
                               });
                           }
                       }
                    }
                });
            }
        });
    }
}