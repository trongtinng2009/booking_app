package com.example.bookingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bookingapp.Model.User;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtphone,edtname,edtpass,edtrepass,edtmail;
    private Button btnsignup;
    private FirebaseFirestore db;
    private ImageButton backbtn;
    boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        setSignupBtn();
        db = FirebaseFirestore.getInstance();
        valid = true;
    }
    private void initView()
    {
        edtmail = findViewById(R.id.signup_mailedt);
        edtname = findViewById(R.id.signup_nameedt);
        edtpass = findViewById(R.id.signup_passedt);
        edtrepass = findViewById(R.id.signup_repassedt);
        edtphone = findViewById(R.id.signup_phoneedt);
        btnsignup = findViewById(R.id.signup_signupbtn);
        backbtn = findViewById(R.id.signup_backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setSignupBtn()
    {
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String phone = edtphone.getText().toString().trim();
               if(phone.isEmpty() || phone.length() == 0)
               {
                   edtphone.setError("Must have value");
                   valid = false;
               }
               else
               {
                   db.collection("User").whereEqualTo("phone",phone)
                           .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                  if(task.isSuccessful())
                                  {
                                      if(task.getResult().size() > 0)
                                      {
                                          valid = false;
                                          edtphone.setError("Existing phone number");
                                      }
                                      else
                                      {
                                          if(phone.length() < 9)
                                          {
                                              valid = false;
                                              edtphone.setError("Must have more than 8 characters");
                                          }
                                          String email = edtmail.getText().toString().trim();
                                          if(email.isEmpty() || email.length() == 0)
                                          {
                                              valid = false;
                                              edtmail.setError("Must have value");
                                          }
                                          else
                                          {
                                              Pattern emailregex =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                                              if(!emailregex.matcher(email).matches())
                                              {
                                                  edtmail.setError("Not an email");
                                                  valid = false;
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
                                              if(name.length() < 10)
                                              {
                                                  valid = false;
                                                  edtname.setError("Must have more than 9 characters");
                                              }
                                          }
                                          String pass = edtpass.getText().toString().trim();
                                          if(pass.isEmpty() || pass.length() == 0)
                                          {
                                              valid = false;
                                              edtpass.setError("Must have value");
                                          }
                                          else
                                          {
                                              if(pass.length() < 5)
                                              {
                                                  valid = false;
                                                  edtpass.setError("Must have more than 5 characters");
                                              }
                                          }
                                          String repass = edtrepass.getText().toString().trim();
                                          if(repass.isEmpty() || repass.length() == 0)
                                          {
                                              valid = false;
                                              edtrepass.setError("Must have value");
                                          }
                                          else
                                          {
                                              if(!repass.equals(pass))
                                              {
                                                  valid = false;
                                                  edtrepass.setError("Not equal to password");
                                              }
                                          }
                                          if(valid)
                                          {
                                              User user = new User(phone,name,email,"Guest",pass, Timestamp.now());
                                              DocumentReference doc = db.collection("User")
                                                      .document();
                                              user.setId(doc.getId());
                                              doc.set(user);
                                              setSuccessPopup();
                                          }

                                      }
                                  }
                               }
                           });
               }
                valid = true;
            }
        });
    }
    private void setSuccessPopup()
    {
        Dialog dialog = Utils.getPopup(this,R.layout.success_popup);
        Button btn = dialog.findViewById(R.id.successpopup_btn);
        ImageButton imgbtn = dialog.findViewById(R.id.successpopup_cancel);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }
}