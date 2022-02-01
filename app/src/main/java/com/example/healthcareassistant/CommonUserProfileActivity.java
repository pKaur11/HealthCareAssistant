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

public class CommonUserProfileActivity extends AppCompatActivity {

    private TextView nameTxtVwUserP, emailTxtVwUserP, medicalHistoryTxtVwUserP,
            genderTxtVwUserP, bloodGroupTxtVwUserP, heightTxtVwUserP,
            weightTxtVwUserP, ageTxtVwUserP;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_user_profile);

        nameTxtVwUserP = findViewById(R.id.nameTxtVwUserP);
        emailTxtVwUserP = findViewById(R.id.emailTxtVwUserP);
        medicalHistoryTxtVwUserP = findViewById(R.id.medicalHistoryTxtVwUserP);
        genderTxtVwUserP = findViewById(R.id.genderTxtVwUserP);
        bloodGroupTxtVwUserP = findViewById(R.id.bloodGroupTxtVwUserP);
        heightTxtVwUserP = findViewById(R.id.heightTxtVwUserP);
        weightTxtVwUserP = findViewById(R.id.weightTxtVwUserP);
        ageTxtVwUserP = findViewById(R.id.ageTxtVwUserP);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User2 userProfile = snapshot.getValue(User2.class);

                if (userProfile != null) {
                    nameTxtVwUserP.setText(userProfile.getName());
                    emailTxtVwUserP.setText(userProfile.getEmail());
                    genderTxtVwUserP.setText(userProfile.getGender());
                    medicalHistoryTxtVwUserP.setText(userProfile.getMedicalHistory());
                    bloodGroupTxtVwUserP.setText(userProfile.getBloodGroup());
                    heightTxtVwUserP.setText(userProfile.getHeight() + " cm");
                    weightTxtVwUserP.setText(userProfile.getWeight() + " kg");
                    ageTxtVwUserP.setText(userProfile.getAge() + " years");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommonUserProfileActivity.this, "Something went wrong!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}