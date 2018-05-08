package com.arifinfrds.papblprojectakhir.ui;

import android.content.Intent;
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

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.User;
import com.arifinfrds.papblprojectakhir.model.UserType;
import com.arifinfrds.papblprojectakhir.ui.main.admin.admin_main.AdminActivity;
import com.arifinfrds.papblprojectakhir.util.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText inputAdminPassword, inputAdminEmail;
    private TextView viewAppTitle, viewDescription;
    private Button btnAdminLogin;
    private ProgressDialog showProgressLoginAdmin;

    private FirebaseAuth mAdminAuth;

    private String adminEmailAuth, adminPswdAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);

        mAdminAuth = FirebaseAuth.getInstance();

        viewAppTitle = findViewById(R.id.txtAppTitle);
        viewDescription = findViewById(R.id.txtViewDescription);
        inputAdminEmail = findViewById(R.id.inputEmailUser);
        inputAdminPassword = findViewById(R.id.inputPassword);
        btnAdminLogin = findViewById(R.id.btnLogin);

        showProgressLoginAdmin = new ProgressDialog(this);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminEmailAuth = inputAdminEmail.getText().toString();
                adminPswdAuth = inputAdminPassword.getText().toString();

                if (TextUtils.isEmpty(adminEmailAuth)) {
                    Toast.makeText(AdminLoginActivity.this, "Email atau password kosong. Harus diisi", Toast.LENGTH_LONG).show();
                } else {
                    adminLogin(adminEmailAuth, adminPswdAuth);
                }
            }
        });
    }

    private void adminLogin(String txtAdminEmail, String txtAdminPassword) {
        showProgressLoginAdmin.setMessage("Sedang masuk, harap tunggu...");
        showProgressLoginAdmin.show();

        mAdminAuth.signInWithEmailAndPassword(txtAdminEmail, txtAdminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                showProgressLoginAdmin.dismiss();

                if (task.isSuccessful()) {
                    //intent to admin activity
                    checkIfUserIsAdmin(mAdminAuth.getUid());
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Mohon maaf, login gagal. Silahkan coba lagi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkIfUserIsAdmin(String id) {
        showProgressLoginAdmin.setMessage("Cek admin...");
        showProgressLoginAdmin.show();

        FirebaseDatabase.getInstance().getReference().child(Constant.CHILD.CHILD_USER).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user.getUserType().equals(UserType.TYPE_ADMIN)) {
                            showProgressLoginAdmin.hide();
                            String message = "admin: " + mAdminAuth.getCurrentUser().getEmail();
                            Toast.makeText(AdminLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            navigateToAdminActivity();
                        } else {
                            showProgressLoginAdmin.hide();
                            Toast.makeText(AdminLoginActivity.this, "User bukan admin!", Toast.LENGTH_SHORT).show();
                            mAdminAuth.signOut();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showProgressLoginAdmin.hide();
                    }
                });
    }

    private void navigateToAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
