package com.example.timepasss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timepasss.Model.RequestPayment;
import com.example.timepasss.Model.Userinformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class GetPaymentActivity extends AppCompatActivity {

    private EditText etUserCnic;
    private EditText etUserChoice;
    private Button btnGetPayment;
    private Button btnRateUs;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference userInfoDatabse,requestDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    String firebaseId;

    String usernameGT,emailGT,numberGT;
    int balanceGT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment);

        etUserCnic=(EditText)findViewById(R.id.etCNIC);
        etUserChoice=(EditText)findViewById(R.id.etChoice);
        btnGetPayment=(Button)findViewById(R.id.btnrequestpayment);
        btnRateUs=(Button)findViewById(R.id.btnrateUs);


        final String currentDataTime= DateFormat.getDateTimeInstance().format(new Date());

        database=FirebaseDatabase.getInstance();
        userInfoDatabse=database.getReference();
        requestDatabase=database.getReference("RequestPayment");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();

        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="+"com.android.chrome")));
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                }
            }
        });

        userInfoDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showingDatabaseFromFirebase(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GetPaymentActivity.this,"ERROR"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        btnGetPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnic=etUserCnic.getText().toString();
                String choice=etUserChoice.getText().toString();

                if (balanceGT >=300){
                    if (!TextUtils.isEmpty(cnic) && !TextUtils.isEmpty(choice)){
                        RequestPayment payment=new RequestPayment(usernameGT,emailGT,numberGT,cnic,choice,currentDataTime,balanceGT);
                        requestDatabase.child(firebaseId).setValue(payment);
                        Toast.makeText(GetPaymentActivity.this,"Request Payment Sent!!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GetPaymentActivity.this,MainMenuActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(GetPaymentActivity.this,"Fill All The Fields First",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(GetPaymentActivity.this,"Your Balance is less than 300",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showingDatabaseFromFirebase(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            Userinformation userinformation=new Userinformation();
            userinformation.setName(ds.child(firebaseId).getValue(Userinformation.class).getName());
            userinformation.setEmail(ds.child(firebaseId).getValue(Userinformation.class).getEmail());
            userinformation.setNumber(ds.child(firebaseId).getValue(Userinformation.class).getNumber());
            userinformation.setRupees(ds.child(firebaseId).getValue(Userinformation.class).getRupees());
            userinformation.setPoints(ds.child(firebaseId).getValue(Userinformation.class).getPoints());
            userinformation.setPassword(ds.child(firebaseId).getValue(Userinformation.class).getPassword());

            usernameGT=userinformation.getName();
            emailGT=userinformation.getEmail();
            numberGT=userinformation.getNumber();
            balanceGT=userinformation.getRupees();

        }
    }
}
