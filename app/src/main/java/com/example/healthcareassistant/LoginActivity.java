package com.example.healthcareassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth auth;

    private Button loginBtnSpec, loginBtnUser, goToSignUp1, goToSignUp2;
    private EditText emailLogin, passwordLogin;
    private TextView forgotPasswordTxtVw;
    private CheckBox showPasswordChkBox;

    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        forgotPasswordTxtVw = findViewById(R.id.forgotPasswordTxtVw);
        showPasswordChkBox = findViewById(R.id.showPasswordChkBox);

        loginBtnSpec = findViewById(R.id.loginBtnSpec);
        loginBtnUser = findViewById(R.id.loginBtnUser);
        goToSignUp1 = findViewById(R.id.goToSignup1);
        goToSignUp2 = findViewById(R.id.goToSignup2);

        auth = FirebaseAuth.getInstance();

        loginBtnSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailLogin.getText().toString().isEmpty()) {
                    emailLogin.setError("Email is required!");
                    emailLogin.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin.getText().toString().trim()).matches()) {
                    emailLogin.setError("Please provide valid email!");
                    emailLogin.requestFocus();
                    return;
                }

                if (passwordLogin.getText().toString().isEmpty()) {
                    passwordLogin.setError("Password is required!");
                    passwordLogin.requestFocus();
                    return;
                }

                if (checkPassword()) {
                    submitForm();
                } else {
                    passwordLogin.setError("Password must include capital and small letters, digit, " +
                            "special character, and have at least 8 characters");
                    passwordLogin.requestFocus();
                }
            }
        });

        loginBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailLogin.getText().toString().isEmpty()) {
                    emailLogin.setError("Email is required!");
                    emailLogin.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin.getText().toString().trim()).matches()) {
                    emailLogin.setError("Please provide valid email!");
                    emailLogin.requestFocus();
                    return;
                }

                if (passwordLogin.getText().toString().isEmpty()) {
                    passwordLogin.setError("Password is required!");
                    passwordLogin.requestFocus();
                    return;
                }

                if (checkPassword()) {
                    submitForm1();
                } else {
                    passwordLogin.setError("Password must include capital and small letters, digit, " +
                            "special character, and have at least 8 characters");
                    passwordLogin.requestFocus();
                }
            }
        });

        forgotPasswordTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive Reset Link");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetEmail.getText().toString();
                        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this,
                                        "Reset Link sent to your email", Toast.LENGTH_LONG).show();
                                loginBtnSpec.setEnabled(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,
                                        "Error! Reset Link is not sent " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

        goToSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity1.class);
                startActivity(intent);
            }
        });

        goToSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        showPasswordChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

    }

    private void submitForm1() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            passwordLogin.setText("");
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_error),
                                    Toast.LENGTH_LONG).show();
                            counter--;
                            if (counter <= 0) {
                                loginBtnUser.setEnabled(false);
                                loginBtnUser.setBackgroundColor(getResources().getColor(R.color.disabled_color));
                                Toast.makeText(LoginActivity.this,
                                        "Please Reset the password or try again after 30 seconds.",
                                        Toast.LENGTH_LONG).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loginBtnUser.setEnabled(true);
                                        loginBtnUser.setBackgroundColor(getResources().getColor(R.color.cyan));
                                        counter = 3;
                                    }
                                }, 30000);

                            }

                        } else {
                            Intent intent = new Intent(LoginActivity.this, CommonUserProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void submitForm() {

        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            passwordLogin.setText("");
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_error),
                                    Toast.LENGTH_LONG).show();
                            counter--;
                            if (counter <= 0) {
                                loginBtnSpec.setEnabled(false);
                                loginBtnSpec.setBackgroundColor(getResources().getColor(R.color.disabled_color));
                                Toast.makeText(LoginActivity.this,
                                        "Please Reset the password or try again after 30 seconds.",
                                        Toast.LENGTH_LONG).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loginBtnSpec.setEnabled(true);
                                        loginBtnSpec.setBackgroundColor(getResources().getColor(R.color.cyan));
                                        counter = 3;
                                    }
                                }, 30000);

                            }

                        } else {
                            Intent intent = new Intent(LoginActivity.this, SpecProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public boolean checkPassword() {
        boolean valid = false;
        int count = 0;

        String password = passwordLogin.getText().toString().trim();
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
