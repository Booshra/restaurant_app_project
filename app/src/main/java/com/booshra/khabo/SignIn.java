package com.booshra.khabo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booshra.khabo.Common.Common;
import com.booshra.khabo.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText editPhone,editPass;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final Intent intent = getIntent();

        editPass = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        //Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait a moment");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {//check user
                        if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            Log.d("FOR_LOG2", "exists");
                            //get info
                            mDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());
                            if(Boolean.parseBoolean(user.getIsStaff()))//if IsStaff is true
                            {
                                Log.d("FOR_LOG2", "isStaff true");
                                if (user.getPassword().equals(editPass.getText().toString())) {
                                    Log.d("FOR_LOG3", "pass correct");
                                    Intent homeAdminIntent = new Intent(SignIn.this, HomeAdmin.class);
                                    Common.currentUser = user;
                                    startActivity(homeAdminIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if (user.getPassword().equals(editPass.getText().toString())) {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
