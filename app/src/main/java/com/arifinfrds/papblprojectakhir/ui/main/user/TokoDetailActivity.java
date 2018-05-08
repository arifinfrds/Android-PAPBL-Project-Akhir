package com.arifinfrds.papblprojectakhir.ui.main.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.Toko;
import com.arifinfrds.papblprojectakhir.ui.main.admin.edit_toko.TokoEditActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.arifinfrds.papblprojectakhir.util.Constant.CHILD.CHILD_TOKO;
import static com.arifinfrds.papblprojectakhir.util.Constant.KEY.KEY_ID_TOKO;
import static com.arifinfrds.papblprojectakhir.util.Constant.KEY.KEY_NAMA_TOKO;

public class TokoDetailActivity extends AppCompatActivity {

    private ImageView iv_produk_detail_produk;
    private TextView tv_nama_detail_produk;
    private TextView tv_alamat_detail_produk;
    private TextView tv_description_detail_produk;

    private TextView tv_profile_detail_produk;

    private ImageView iv_phone;

    private Button directionButton;

    private Bundle mExtras;
    private String mTokoId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ActionBar actionBar;

    private Toko mCurrentToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_produk_detail_produk = findViewById(R.id.iv_produk_detail_produk);
        tv_nama_detail_produk = findViewById(R.id.tv_nama_detail_produk);
        tv_alamat_detail_produk = findViewById(R.id.tv_alamat_detail_produk);
        tv_description_detail_produk = findViewById(R.id.tv_description_detail_produk);
        tv_profile_detail_produk = findViewById(R.id.tv_profile_detail_produk);
        directionButton = findViewById(R.id.directionButton);
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToGoogleMapsApps(mCurrentToko);
            }
        });

        tv_profile_detail_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPhoneCallActivity(mCurrentToko.getNomorTelepon());
            }
        });

        iv_phone = findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPhoneCallActivity(mCurrentToko.getNomorTelepon());
            }
        });


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Some title");
        }

        setupFirebase();

        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mTokoId = mExtras.getString(KEY_ID_TOKO);
            String namaToko = mExtras.getString(KEY_NAMA_TOKO);
            showProgressDialog();
            fetchToko(mTokoId);
            actionBar.setTitle(namaToko);
        }
    }

    private void navigateToPhoneCallActivity(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }

    private void navigateToGoogleMapsApps(Toko toko) {
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + toko.getLatitude() + "," + toko.getLongitude() + "&daddr=" +
                toko.getLatitude() + "," + toko.getLongitude();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));
    }


    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void fetchToko(String id) {
        mDatabaseReference.child(CHILD_TOKO).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toko toko = dataSnapshot.getValue(Toko.class);
                hideProgressDialog();
                updateUI(toko);
                mCurrentToko = toko;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TokoDetailActivity.this, "onCancelled: dbError: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void updateUI(Toko toko) {
        Picasso.get().load(toko.getPhotoUrl()).into(iv_produk_detail_produk);
        tv_nama_detail_produk.setText(toko.getNama());
        tv_description_detail_produk.setText(toko.getKeterangan());
        tv_alamat_detail_produk.setText(toko.getAlamat());
        tv_profile_detail_produk.setText(toko.getNomorTelepon());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
