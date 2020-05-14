package com.example.timepasss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timepasss.Model.Userinformation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;





public class EarningOne extends AppCompatActivity {

    private Button btnTaskOne;
    private Button btnTaskTwo;
    private Button btnTaskThree;
    private Button btnTaskFour;
    private Button btnTaskFive;
    private Button btnGoBack;

    private InterstitialAd adOne;

    private int currentRupees,previousRupees;
    private int currentPoints,previousPoints;

    private String email,name,number,password;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference ref,userInfoDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    String firebaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_one);

        btnTaskOne=(Button)findViewById(R.id.taskOne);
        btnTaskTwo=(Button)findViewById(R.id.taskTwo);
        btnTaskThree=(Button)findViewById(R.id.taskThree);
        btnTaskFour=(Button)findViewById(R.id.taskFour);
        btnTaskFive=(Button)findViewById(R.id.taskFive);
        btnGoBack=(Button)findViewById(R.id.goBack);



        adOne=new InterstitialAd(this);
        adOne.setAdUnitId("ca-app-pub-6447107122173317/3188294873");
        adOne.loadAd(new AdRequest.Builder().build());



        settingUpTheFirebase();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showingDatabaseFromFirebase(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EarningOne.this,"ERROR"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


        adOne.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                Toast.makeText(EarningOne.this,"You Got 5 Points",Toast.LENGTH_SHORT).show();
                currentPoints+=5;
                if (currentPoints >=10){
                    currentRupees +=1;
                }

            }
        });
        onClickListeners();

    }

    private void onClickListeners() {
        btnTaskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskOne.setVisibility(View.INVISIBLE);
                    btnTaskTwo.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningOne.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTaskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskTwo.setVisibility(View.INVISIBLE);
                    btnTaskThree.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningOne.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTaskThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskThree.setVisibility(View.INVISIBLE);
                    btnTaskFour.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningOne.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTaskFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskFour.setVisibility(View.INVISIBLE);
                    btnTaskFive.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningOne.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTaskFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adOne.loadAd(new AdRequest.Builder().build());
                if (adOne.isLoaded()){
                    adOne.show();
                    btnTaskFour.setVisibility(View.INVISIBLE);
                    btnGoBack.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningOne.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingTheNewDataInFirebase();
                startActivity(new Intent(EarningOne.this,MainMenuActivity.class));
                finish();
            }
        });
    }

    private void savingTheNewDataInFirebase() {
        previousRupees += currentRupees;
        Userinformation information=new Userinformation(email,password,number,name,previousRupees,previousPoints);
        userInfoDatabase.child(firebaseId).setValue(information);
    }

    private void showingDatabaseFromFirebase(DataSnapshot ds){

        for (DataSnapshot dataSnapshot:ds.getChildren()){
            Userinformation userinformation=new Userinformation();
            userinformation.setName(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getName());
            userinformation.setEmail(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getEmail());
            userinformation.setNumber(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getNumber());
            userinformation.setRupees(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getRupees());
            userinformation.setPoints(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getPoints());
            userinformation.setPassword(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getPassword());

            name=userinformation.getName();
            email=userinformation.getEmail();
            password=userinformation.getPassword();
            number=userinformation.getNumber();
            previousRupees=userinformation.getRupees();
            previousPoints=userinformation.getPoints();
        }


    }

    private void settingUpTheFirebase(){

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
        userInfoDatabase=database.getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        firebaseId=user.getUid();

    }

}
