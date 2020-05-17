package com.example.timepasss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timepasss.Model.Userinformation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EarningThree extends AppCompatActivity {

    private Button btnTaskOne;
    private Button btnGoBack;

    private InterstitialAd adOne;

    private int currentRupees=0,previousRupees=0;
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
        setContentView(R.layout.activity_earning_three);

        btnTaskOne=(Button)findViewById(R.id.taskOne);
        btnGoBack=(Button)findViewById(R.id.goBack);

        adOne=new InterstitialAd(this);
        adOne.setAdUnitId("ca-app-pub-3940256099942544/5224354917");
        adOne.loadAd(new AdRequest.Builder().build());



        settingUpTheFirebase();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showingDatabaseFromFirebase(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EarningThree.this,"ERROR"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


        adOne.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication(){
                Toast.makeText(EarningThree.this,"You Got 5 Rupees",Toast.LENGTH_SHORT).show();
                currentRupees =5;
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
                    btnGoBack.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(EarningThree.this,"Click Again After 10 Seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingTheNewDataInFirebase();
                startActivity(new Intent(EarningThree.this,MainMenuActivity.class));
                finish();
            }
        });
    }

    private void savingTheNewDataInFirebase() {
        previousRupees += currentRupees;
        Userinformation information=new Userinformation(email,password,number,name,previousRupees,0);
        userInfoDatabase.child(firebaseId).setValue(information);
    }

    private void showingDatabaseFromFirebase(DataSnapshot ds){

        for (DataSnapshot dataSnapshot:ds.getChildren()){
            Userinformation userinformation=new Userinformation();
            userinformation.setName(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getName());
            userinformation.setEmail(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getEmail());
            userinformation.setNumber(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getNumber());
            userinformation.setRupees(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getRupees());
            userinformation.setPassword(dataSnapshot.child(firebaseId).getValue(Userinformation.class).getPassword());

            name=userinformation.getName();
            email=userinformation.getEmail();
            password=userinformation.getPassword();
            number=userinformation.getNumber();
            previousRupees=userinformation.getRupees();
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
