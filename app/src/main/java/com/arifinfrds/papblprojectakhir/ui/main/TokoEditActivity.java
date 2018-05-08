package com.arifinfrds.papblprojectakhir.ui.main;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.Toko;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.arifinfrds.papblprojectakhir.util.Constant.CHILD.CHILD_TOKO;
import static com.arifinfrds.papblprojectakhir.util.Constant.KEY.KEY_ID_TOKO;

public class TokoEditActivity extends AppCompatActivity {

    private ImageView imageView2;
    private EditText et_EditTokoNama;
    private EditText et_EditTokoKeterangan;
    private EditText et_EditTokoTelp;
    private EditText et_EditTokoAlamat;

    private Button btn_EditToko;
    private Button btn_HapusToko;

    private Bundle mExtras;

    private String mTokoId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private Toko mCurrentToko;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko_edit);

        imageView2 = findViewById(R.id.imageView2);
        et_EditTokoNama = findViewById(R.id.et_EditTokoNama);
        et_EditTokoKeterangan = findViewById(R.id.et_EditTokoKeterangan);
        et_EditTokoTelp = findViewById(R.id.et_EditTokoTelp);
        et_EditTokoAlamat = findViewById(R.id.et_EditTokoAlamat);

        btn_EditToko = findViewById(R.id.btn_EditToko);
        btn_HapusToko = findViewById(R.id.btn_HapusToko);

        setupFirebase();

        btn_EditToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToko();
            }
        });

        btn_HapusToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mTokoId = mExtras.getString(KEY_ID_TOKO);
            showProgressDialog();
            fetchToko(mTokoId);
        }
    }

    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void fetchToko(String id) {
        mDatabaseReference.child(CHILD_TOKO).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toko toko = dataSnapshot.getValue(Toko.class);
                Toast.makeText(TokoEditActivity.this, "nama toko: " + toko.getNama(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                updateUI(toko);
                mCurrentToko = toko;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TokoEditActivity.this, "onCancelled: dbError: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void updateToko() {
        String namaToko = et_EditTokoNama.getText().toString();
        String keterangan = et_EditTokoKeterangan.getText().toString();
        String nomorTelepon = et_EditTokoTelp.getText().toString();
        String alamat = et_EditTokoAlamat.getText().toString();

        if (isInputEmpty(namaToko) || isInputEmpty(keterangan) || isInputEmpty(nomorTelepon) || isInputEmpty(alamat)) {
            Toast.makeText(this, "Input tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toko toko = new Toko(mCurrentToko.getId(), namaToko, keterangan, alamat, 9.9,
                9.9, nomorTelepon, mCurrentToko.getPhotoUrl());

        showProgressDialog();
        mDatabaseReference.child(CHILD_TOKO).child(mCurrentToko.getId()).setValue(toko)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        Toast.makeText(TokoEditActivity.this, "Toko berhasil di update.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        Toast.makeText(TokoEditActivity.this, "Error update toko: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void updateUI(Toko toko) {
        et_EditTokoNama.setText(toko.getNama());
        et_EditTokoKeterangan.setText(toko.getKeterangan());
        et_EditTokoTelp.setText(toko.getNomorTelepon());
        et_EditTokoAlamat.setText(toko.getAlamat());
        Picasso.get().load(toko.getPhotoUrl()).into(imageView2);
    }


    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.title_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean isInputEmpty(String text) {
        if (TextUtils.isEmpty(text) || text.length() == 0 || text.equals("")) {
            return true;
        }
        return false;
    }
}
