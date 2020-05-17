package com.example.timepasss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timepasss.Model.Userinformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;


public class MainMenuActivity extends AppCompatActivity {
    private Button btnProfile;
    private Button btnEarningOne;
    private Button btnEarningTwo;
    private Button btnEarningThree;
    private Button btnGetPayment;
    private TextView currentPoints;
    private TextView currentRupees;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    String firebaseId;

    SharedPreferences preferences;
    long hours,previousTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();
        currentPoints=(TextView)findViewById(R.id.userPoints);
        currentRupees=(TextView)findViewById(R.id.userRupees);
        btnEarningOne=(Button)findViewById(R.id.btnEarning1);
        btnEarningTwo=(Button)findViewById(R.id.btnEarning2);
        btnEarningThree=(Button)findViewById(R.id.btnEarning3);
        btnGetPayment=(Button)findViewById(R.id.btnGetPayment) ;

        preferences=getSharedPreferences("SAVING_TIME",MODE_PRIVATE);
        hours=new Time(System.currentTimeMillis()).getHours();
        buttonEnableMethod();



        authstateListener();
        showingDatafromDatabase();

        btnProfile=(Button)findViewById(R.id.btnProfile);

        allOnClickListeners();


    }

    private void allOnClickListeners(){
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this,ProfileActivity.class));
            }
        });
        btnEarningOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this,EarningOne.class));
            }
        });
        btnEarningTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this,EarningTwo.class));
            }
        });
        btnEarningThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this,EarningThree.class));
            }
        });
        btnGetPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this,GetPaymentActivity.class));
            }
        });
    }
    private void authstateListener(){
        stateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainMenuActivity.this,MainActivity.class));
                }

            }
        };
    }

    private void showingDatafromDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Userinformation info=new Userinformation();
                    info.setRupees(ds.child(firebaseId).getValue(Userinformation.class).getRupees());
                    info.setPoints(ds.child(firebaseId).getValue(Userinformation.class).getPoints());
                    currentRupees.setText(Integer.toString(info.getRupees()));
                    currentPoints.setText(Integer.toString(info.getPoints()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainMenuActivity.this,"ERROR"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (stateListener!=null){
            mAuth.removeAuthStateListener(stateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonEnableMethod();

    }

    private void buttonEnableMethod(){
        hours=new Time(System.currentTimeMillis()).getHours();
        previousTime = preferences.getLong("hours",0);
        if (hours-previousTime>2 || hours-previousTime>2 ){
            btnEarningOne.setEnabled(true);
            btnEarningOne.setBackgroundResource(R.drawable.back_btn);
        }
        else{
            btnEarningOne.setEnabled(false);
            btnEarningOne.setBackgroundResource(R.color.orange);
        }
    }
}
