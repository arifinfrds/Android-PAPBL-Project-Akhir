package com.arifinfrds.papblprojectakhir.ui.main.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.Toko;
import com.arifinfrds.papblprojectakhir.ui.main.TokoEditActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.arifinfrds.papblprojectakhir.util.Constant.KEY.KEY_ID_TOKO;
import static com.arifinfrds.papblprojectakhir.util.Constant.TAG.TAG_ADMIN;

public class AdminActivity extends AppCompatActivity implements AdminListener {

    RecyclerView mRvAdmin;

    DatabaseReference databaseToko;

    List<Toko> tokos;

    TokoListAdapter tokoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseToko = FirebaseDatabase.getInstance().getReference("toko");

        tokos = new ArrayList<>();

        mRvAdmin = (RecyclerView) findViewById(R.id.rv_AdminLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setupRecyclerView() {
        tokoListAdapter = new TokoListAdapter(AdminActivity.this, tokos, this);
        mRvAdmin.setAdapter(tokoListAdapter);
        mRvAdmin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
        databaseToko.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tokos.clear();

                //Every time it still true, it will keep executed
                for (DataSnapshot tb_tokoSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG_ADMIN, "onDataChange: tb_tokoSnapshot: " + tb_tokoSnapshot.getKey());

                    //set object dosen from Firebase value
                    Toko toko = tb_tokoSnapshot.getValue(Toko.class);

                    //insert object Dosen into ArrayList dosens
                    tokos.add(toko);
                }
                setupRecyclerView();
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Something Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onItemClick(String id) {
        navigateTokoEditActivity(id);
    }

    private void navigateTokoEditActivity(String idToko) {
        Intent intent = new Intent(this, TokoEditActivity.class);
        intent.putExtra(KEY_ID_TOKO, idToko);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
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


}
