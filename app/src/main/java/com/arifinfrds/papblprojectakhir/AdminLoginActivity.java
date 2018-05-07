package com.arifinfrds.papblprojectakhir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //    private EditText inputAdminPassword, inputAdminEmail;
//    private TextView viewAppTitle, viewDescription;
//    private Button btnAdminLogin;
//    private ProgressDialog showProgressLoginAdmin;
//
//    private FirebaseAuth mAdminAuth;
//
//    private String adminEmailAuth, adminPswdAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_loginadmin);
//
//        mAdminAuth = FirebaseAuth.getInstance();
//
//        viewAppTitle = findViewById(R.id.txtAppTitleAdmin);
//        viewDescription = findViewById(R.id.txtViewDescriptionAdmin);
//        inputAdminEmail = findViewById(R.id.inputAdminEmail);
//        inputAdminPassword = findViewById(R.id.inputAdminPassword);
//        btnAdminLogin = findViewById(R.id.btnAdminLogin);
//
//        showProgressLoginAdmin = new ProgressDialog(this);
//
//        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adminEmailAuth = inputAdminEmail.getText().toString();
//                adminPswdAuth = inputAdminPassword.getText().toString();
//
//                if(TextUtils.isEmpty(adminEmailAuth)){
//                    Toast.makeText(AdminLoginActivity.this, "Email atau password kosong. Harus diisi", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    adminLogin(adminEmailAuth, adminPswdAuth);
//                }
//            }
//        });
//    }
//
//    private void adminLogin(String txtAdminEmail, String txtAdminPassword){
//        showProgressLoginAdmin.setMessage("Sedang masuk, harap tunggu...");
//        showProgressLoginAdmin.show();
//
//        mAdminAuth.signInWithEmailAndPassword(txtAdminEmail, txtAdminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                showProgressLoginAdmin.dismiss();
//
//                if (task.isSuccessful()){
//                    finish();
//                    //intent to admin activity
//                }
//                else{
//                    Toast.makeText(AdminLoginActivity.this, "Mohon maaf, login gagal. Silahkan coba lagi", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
// }
}
