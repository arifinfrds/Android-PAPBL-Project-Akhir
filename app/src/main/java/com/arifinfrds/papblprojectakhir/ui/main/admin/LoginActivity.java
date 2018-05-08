package com.arifinfrds.papblprojectakhir.ui.main.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.ui.AdminLoginActivity;
import com.arifinfrds.papblprojectakhir.ui.RegisterActivity;
import com.arifinfrds.papblprojectakhir.ui.ResetPasswordActivity;
import com.arifinfrds.papblprojectakhir.ui.main.user.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtAppTitle, txtAppWelcome, txtIsAdmin;
    private EditText inputEmailUser, inputPassword;
    private ProgressDialog showLoginProgress;

    private FirebaseAuth mAuth;

    private String emailAuth, pswdAuth;

    private Button mRegisterButton;

    private Button mResetPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        txtAppTitle = findViewById(R.id.txtAppTitle);
        txtAppWelcome = findViewById(R.id.txtViewDescription);
        inputEmailUser = findViewById(R.id.inputEmailUser);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtIsAdmin = findViewById(R.id.txtIsAdmin);

        showLoginProgress = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAuth = inputEmailUser.getText().toString();
                pswdAuth = inputPassword.getText().toString();

                if (TextUtils.isEmpty(emailAuth)) {
                    Toast.makeText(LoginActivity.this, "Email atau password kosong. Harus diisi", Toast.LENGTH_LONG).show();
                } else {
                    userLogin(emailAuth, pswdAuth);
                }
            }
        });

        txtIsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
                finish();
            }
        });

        mRegisterButton = findViewById(R.id.registerButton);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToRegisterActivity();
            }
        });

        mResetPasswordButton = findViewById(R.id.resetPasswordButton);
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToResetPasswordActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            // intent to next page
            navigateToMapsActivity();
        }
    }

    private void userLogin(final String textEmail, String textPassword) {
        showLoginProgress.setMessage("Sedang masuk, harap tunggu...");
        showLoginProgress.show();

        mAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showLoginProgress.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    //intent to next activity
                    navigateToMapsActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon maaf, login gagal. Silahkan coba lagi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void navigateToMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void navigateToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToResetPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}
