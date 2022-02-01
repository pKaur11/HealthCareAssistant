package com.example.healthcareassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity2 extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private EditText nameOfUser, weightOfUser, heightOfUser, ageOfUser,
            bloodGroupOfUser, emailOfUser, passwordOfUser, medicalHistoryOfUser;

    private RadioGroup genderRdGroup;
    private RadioButton maleRdBtn, femaleRdBtn;

    private Button registerBtn, goToLogin;

    private static final String TAG = "FIREBASE AUTHENTICATION";

    private Register2ViewModel register2ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        nameOfUser = findViewById(R.id.nameOfUser);
        weightOfUser = findViewById(R.id.weightOfUser);
        heightOfUser = findViewById(R.id.heightOfUser);
        ageOfUser = findViewById(R.id.ageOfUser);
        bloodGroupOfUser = findViewById(R.id.bloodGroupOfUser);
        emailOfUser = findViewById(R.id.emailOfUser);
        passwordOfUser = findViewById(R.id.passwordOfUser);
        medicalHistoryOfUser = findViewById(R.id.medicalHistoryOfUser);

        genderRdGroup = findViewById(R.id.genderRdGroup);
        maleRdBtn = findViewById(R.id.maleRdBtn);
        femaleRdBtn = findViewById(R.id.femaleRdBtn);

        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.goToLogIn);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        register2ViewModel = new ViewModelProvider(this).get(Register2ViewModel.class);


        nameOfUser.setText(register2ViewModel.getName());
        weightOfUser.setText(register2ViewModel.getWeight());
        heightOfUser.setText(register2ViewModel.getHeight());
        ageOfUser.setText(register2ViewModel.getAge());
        bloodGroupOfUser.setText(register2ViewModel.getBloodGroup());
        emailOfUser.setText(register2ViewModel.getEmail());
        passwordOfUser.setText(register2ViewModel.getPassword());
        medicalHistoryOfUser.setText(register2ViewModel.getMedicalHistory());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameOfUser.getText().toString().isEmpty()) {
                    nameOfUser.setError("Name is required!");
                    nameOfUser.requestFocus();
                    return;
                }

                if (!nameOfUser.getText().toString().contains(" ")) {
                    nameOfUser.setError("Please enter your full name!");
                    nameOfUser.requestFocus();
                    return;
                }

                if (genderRdGroup.getCheckedRadioButtonId() == -1) {
                    femaleRdBtn.setError("Select Gender!");
                    return;
                } else {
                    femaleRdBtn.setError(null);
                }

                if (weightOfUser.getText().toString().isEmpty()) {
                    weightOfUser.setError("Weight is required!");
                    weightOfUser.requestFocus();
                    return;
                }

                if (heightOfUser.getText().toString().isEmpty()) {
                    heightOfUser.setError("Height is required!");
                    heightOfUser.requestFocus();
                    return;
                }

                if (ageOfUser.getText().toString().isEmpty()) {
                    ageOfUser.setError("Age is required!");
                    ageOfUser.requestFocus();
                    return;
                }

                if (bloodGroupOfUser.getText().toString().isEmpty()) {
                    bloodGroupOfUser.setError("Blood Group is required!");
                    bloodGroupOfUser.requestFocus();
                    return;
                }

                if (emailOfUser.getText().toString().isEmpty()) {
                    emailOfUser.setError("Email is required!");
                    emailOfUser.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailOfUser.getText().toString()).matches()) {
                    emailOfUser.setError("Please provide valid email!");
                    emailOfUser.requestFocus();
                    return;
                }

                if (passwordOfUser.getText().toString().isEmpty()) {
                    passwordOfUser.setError("Password is required!");
                    passwordOfUser.requestFocus();
                    return;
                }

                if (medicalHistoryOfUser.getText().toString().isEmpty()) {
                    medicalHistoryOfUser.setError("Medical History is required!");
                    medicalHistoryOfUser.requestFocus();
                    return;
                }

                if (checkPassword()) {
                    register2ViewModel.setName(nameOfUser.getText().toString());
                    register2ViewModel.setWeight(weightOfUser.getText().toString());
                    register2ViewModel.setHeight(heightOfUser.getText().toString());
                    register2ViewModel.setAge(ageOfUser.getText().toString());
                    register2ViewModel.setBloodGroup(bloodGroupOfUser.getText().toString());
                    register2ViewModel.setEmail(emailOfUser.getText().toString());
                    register2ViewModel.setPassword(passwordOfUser.getText().toString());
                    register2ViewModel.setMedicalHistory(medicalHistoryOfUser.getText().toString());

                    submitForm();

                } else {
                    passwordOfUser.setError("Password must include capital and small letters, digit, " +
                            "special character, and have at least 8 characters");
                    passwordOfUser.requestFocus();
                }

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitForm() {

        String email = emailOfUser.getText().toString().trim();
        String password = passwordOfUser.getText().toString().trim();

        if (email.length() != 0 && password.length() != 0) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity2.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (!task.isSuccessful()) {
                                        Log.d(TAG, "Authentication failed." + task.getException());

                                    } else {
                                        User2 user = new User2(register2ViewModel.getName(),
                                                register2ViewModel.getGender(),
                                                register2ViewModel.getWeight(),
                                                register2ViewModel.getHeight(),
                                                register2ViewModel.getAge(),
                                                register2ViewModel.getBloodGroup(),
                                                register2ViewModel.getEmail(),
                                                register2ViewModel.getMedicalHistory());

                                        mFirebaseDatabase.child(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid())
                                                .setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();

                                                        } else {
                                                            Toast.makeText(RegisterActivity2.this,
                                                                    "Failed to register. Try again!",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please fill the data properly", Toast.LENGTH_SHORT).show();
        }

    }


    public void onRadioBtnClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.maleRdBtn:
                if (checked) {
                    register2ViewModel.setGender("Male");
                }
                break;
            case R.id.femaleRdBtn:
                if (checked) {
                    register2ViewModel.setGender("Female");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public boolean checkPassword() {
        boolean valid = false;
        int count = 0;

        String password = passwordOfUser.getText().toString().trim();
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";

        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                count++;
                break;
            }
        }
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                count++;
                break;
            }
        }

        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                count++;
                break;
            }
        }

        for (int i = 0; i < password.length(); i++) {
            Character ch = password.charAt(i);
            if (specialCharactersString.contains(Character.toString(ch))) {
                count++;
                break;
            }
        }

        if (password.length() >= 8 && count == 4) {
            valid = true;
        } else {
            valid = false;
        }

        return valid;
    }
}