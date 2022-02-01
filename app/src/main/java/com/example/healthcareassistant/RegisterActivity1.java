package com.example.healthcareassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity1 extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private EditText nameOfSpec, mspNumOfSpec, emailOfSpec, passwordOfSpec;

    private Spinner availabilityOfSpec, departmentOfSpec;
    private String selectedSpinner, selectedSpinner1;

    private RadioGroup genderRdGroupS;
    private RadioButton maleRdBtnS, femaleRdBtnS;

    private Button registerBtn, goToLogin;

    private static final String TAG = "FIREBASE AUTHENTICATION";

    private Register1ViewModel register1ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        nameOfSpec = findViewById(R.id.nameOfSpec);
        mspNumOfSpec = findViewById(R.id.mspNumOfSpec);
        emailOfSpec = findViewById(R.id.emailOfSpec);
        passwordOfSpec = findViewById(R.id.passwordOfSpec);

        availabilityOfSpec = findViewById(R.id.availabilityOfSpec);
        departmentOfSpec = findViewById(R.id.departmentOfSpec);

        genderRdGroupS = findViewById(R.id.genderRdGroupS);
        maleRdBtnS = findViewById(R.id.maleRdBtnS);
        femaleRdBtnS = findViewById(R.id.femaleRdBtnS);

        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.goToLogIn);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        register1ViewModel = new ViewModelProvider(this).get(Register1ViewModel.class);

        nameOfSpec.setText(register1ViewModel.getName());
        mspNumOfSpec.setText(register1ViewModel.getMspNum());

        if (availabilityOfSpec != null) {
            availabilityOfSpec.setOnItemSelectedListener(new availabilitySpinnerHandler());
        }

        if (departmentOfSpec != null) {
            departmentOfSpec.setOnItemSelectedListener(new departmentSpinnerHandler());
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.availability_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        if (availabilityOfSpec != null) {
            availabilityOfSpec.setAdapter(adapter);
        }

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.department_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        if (departmentOfSpec != null) {
            departmentOfSpec.setAdapter(adapter1);
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameOfSpec.getText().toString().isEmpty()) {
                    nameOfSpec.setError("Name is required!");
                    nameOfSpec.requestFocus();
                    return;
                }

                if (!nameOfSpec.getText().toString().contains(" ")) {
                    nameOfSpec.setError("Please enter your full name!");
                    nameOfSpec.requestFocus();
                    return;
                }

                if (mspNumOfSpec.getText().toString().isEmpty()) {
                    mspNumOfSpec.setError("MSP Number is required!");
                    mspNumOfSpec.requestFocus();
                    return;
                }

                if (genderRdGroupS.getCheckedRadioButtonId() == -1) {
                    femaleRdBtnS.setError("Select Gender!");
                    return;
                } else {
                    femaleRdBtnS.setError(null);
                }

                if (availabilityOfSpec.getSelectedItemPosition() < 1) {
                    setSpinnerError(availabilityOfSpec, "Select Availability!");
                    return;
                }

                if (departmentOfSpec.getSelectedItemPosition() < 1) {
                    setSpinnerError(departmentOfSpec, "Select Department!");
                    return;
                }

                if (emailOfSpec.getText().toString().isEmpty()) {
                    emailOfSpec.setError("Email is required!");
                    emailOfSpec.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailOfSpec.getText().toString()).matches()) {
                    emailOfSpec.setError("Please provide valid email!");
                    emailOfSpec.requestFocus();
                    return;
                }

                if (passwordOfSpec.getText().toString().isEmpty()) {
                    passwordOfSpec.setError("Password is required!");
                    passwordOfSpec.requestFocus();
                    return;
                }

                if (checkPassword()) {
                    register1ViewModel.setName(nameOfSpec.getText().toString());
                    register1ViewModel.setMspNum(mspNumOfSpec.getText().toString());
                    register1ViewModel.setEmail(emailOfSpec.getText().toString());
                    register1ViewModel.setPassword(passwordOfSpec.getText().toString());

                    submitForm();
                } else {
                    passwordOfSpec.setError("Password must include capital and small letters, digit, " +
                            "special character, and have at least 8 characters");
                    passwordOfSpec.requestFocus();
                }

            }
        });


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitForm() {
        String email = emailOfSpec.getText().toString().trim();
        String password = passwordOfSpec.getText().toString().trim();

        if (email.length() != 0 && password.length() != 0) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity1.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (!task.isSuccessful()) {
                                        Log.d(TAG, "Authentication failed." + task.getException());

                                    } else {
                                        User1 user = new User1(register1ViewModel.getName(),
                                                register1ViewModel.getMspNum(),
                                                register1ViewModel.getGender(),
                                                register1ViewModel.getAvailability(),
                                                register1ViewModel.getDepartment(),
                                                register1ViewModel.getEmail());

                                        mFirebaseDatabase.child(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid())
                                                .setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();

                                                        } else {
                                                            Toast.makeText(RegisterActivity1.this,
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
                    register1ViewModel.setGender("Male");
                }
                break;
            case R.id.femaleRdBtn:
                if (checked) {
                    register1ViewModel.setGender("Female");
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

    private class availabilitySpinnerHandler implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedSpinner = adapterView.getItemAtPosition(i).toString();
            register1ViewModel.setAvailability(selectedSpinner);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class departmentSpinnerHandler implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedSpinner1 = adapterView.getItemAtPosition(i).toString();
            register1ViewModel.setDepartment(selectedSpinner1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error);
            selectedTextView.setTextColor(Color.RED);
            selectedTextView.setText(error);
        }
    }

    public boolean checkPassword() {
        boolean valid = false;
        int count = 0;

        String password = passwordOfSpec.getText().toString().trim();
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