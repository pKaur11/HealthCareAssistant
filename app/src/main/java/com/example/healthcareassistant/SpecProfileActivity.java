package com.example.healthcareassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpecProfileActivity extends AppCompatActivity {

    private TextView nameTxtVwSpecP, emailTxtVwSpecP, genderTxtVwSpecP,
            mspTxtVwSpecP, availabilityTxtVwSpecP, departmentTxtVwSpecP;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_profile);

        nameTxtVwSpecP = findViewById(R.id.nameTxtVwSpecP);
        emailTxtVwSpecP = findViewById(R.id.emailTxtVwSpecP);
        genderTxtVwSpecP = findViewById(R.id.genderTxtVwSpecP);
        mspTxtVwSpecP = findViewById(R.id.mspTxtVwSpecP);
        availabilityTxtVwSpecP = findViewById(R.id.availabilityTxtVwSpecP);
        departmentTxtVwSpecP = findViewById(R.id.departmentTxtVwSpecP);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User1 userProfile = snapshot.getValue(User1.class);

                if (userProfile != null) {
                    nameTxtVwSpecP.setText(userProfile.getName());
                    emailTxtVwSpecP.setText(userProfile.getEmail());
                    genderTxtVwSpecP.setText(userProfile.getGender());
                    mspTxtVwSpecP.setText(userProfile.getMspNum());
                    availabilityTxtVwSpecP.setText(userProfile.getAvailability());
                    departmentTxtVwSpecP.setText(userProfile.getDepartment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecProfileActivity.this, "Something went wrong!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}