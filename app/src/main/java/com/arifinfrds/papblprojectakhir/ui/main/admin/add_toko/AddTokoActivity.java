package com.arifinfrds.papblprojectakhir.ui.main.admin.add_toko;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.Toko;
import com.arifinfrds.papblprojectakhir.ui.main.admin.edit_toko.TokoEditActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.arifinfrds.papblprojectakhir.util.Constant.CHILD.CHILD_TOKO;

public class AddTokoActivity extends AppCompatActivity {

    private ImageView imageView2;
    private EditText et_EditTokoNama;
    private EditText et_EditTokoKeterangan;
    private EditText et_EditTokoTelp;
    private EditText et_EditTokoAlamat;

    private Button btn_add_toko;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toko);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFirebase();

        imageView2 = findViewById(R.id.imageView2);
        et_EditTokoNama = findViewById(R.id.et_EditTokoNama);
        et_EditTokoKeterangan = findViewById(R.id.et_EditTokoKeterangan);
        et_EditTokoTelp = findViewById(R.id.et_EditTokoTelp);
        et_EditTokoAlamat = findViewById(R.id.et_EditTokoAlamat);

        btn_add_toko = findViewById(R.id.btn_add_toko);
        btn_add_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToko();
            }
        });


    }

    private void addToko() {
        String namaToko = et_EditTokoNama.getText().toString();
        String keterangan = et_EditTokoKeterangan.getText().toString();
        String nomorTelepon = et_EditTokoTelp.getText().toString();
        String alamat = et_EditTokoAlamat.getText().toString();

        if (isInputEmpty(namaToko) || isInputEmpty(keterangan) || isInputEmpty(nomorTelepon) || isInputEmpty(alamat)) {
            Toast.makeText(this, "Input tidak boleh kosong.", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude = 0;
        double longitude = 0;

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocationName(alamat, 100);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();
            } else {
                // do your stuff
                Toast.makeText(this, "Alamat tidak ditemukan.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Toast.makeText(this, "Latitude: " + latitude + ". Longitude: " + longitude, Toast.LENGTH_SHORT).show();
        String noImageUrl = "https://vignette.wikia.nocookie.net/pandorahearts/images/7/70/No_image.jpg.png/revision/latest?cb=20121025132440";
        String idTokoBaru = mDatabaseReference.push().getKey();
        Toko toko = new Toko(idTokoBaru, namaToko, keterangan, alamat, latitude,
                longitude, nomorTelepon, noImageUrl);

        showProgressDialog();
        mDatabaseReference.child(CHILD_TOKO).child(idTokoBaru).setValue(toko)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        Toast.makeText(AddTokoActivity.this, "Toko berhasil di tambah.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        Toast.makeText(AddTokoActivity.this, "Error tambah toko: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
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
