package com.arifinfrds.papblprojectakhir.ui.main.user

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.arifinfrds.papblprojectakhir.R
import com.arifinfrds.papblprojectakhir.extension.toast
import com.arifinfrds.papblprojectakhir.model.Toko
import com.arifinfrds.papblprojectakhir.model.User
import com.arifinfrds.papblprojectakhir.model.UserType
import com.arifinfrds.papblprojectakhir.util.Constant

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.arifinfrds.papblprojectakhir.util.Constant.CHILD.CHILD_USER
import com.arifinfrds.papblprojectakhir.util.Constant.DEFAULT.DEFAULT_NOT_SET
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseUser


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap

    private var mAuth: FirebaseAuth? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mUser: FirebaseUser? = null

    private var items: ArrayList<Toko>? = null

    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        items = arrayListOf()

        setupFirebase()

        checkIfUserExistInDatabase()
    }


    // MARK: - Networking

    private fun fetchTokoList() {
        showProgressDialog()
        mDatabaseReference?.child(Constant.CHILD.CHILD_TOKO)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0!!.message}")
                hideProgressDialog()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                if (!items!!.isEmpty()) {
                    items?.clear()
                }
                for (childDataSnapshot in dataSnapshot!!.children) {
                    val toko = childDataSnapshot.getValue(Toko::class.java)
                    items!!.add(toko!!)
                }
                placeMarkers()
                hideProgressDialog()
            }
        })
    }

    private fun checkIfUserExistInDatabase() {
        mDatabaseReference?.child(CHILD_USER)?.child(mUser?.getUid())
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            createUser()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }

    private fun createUser() {
        val user = User(
                mUser?.uid,
                DEFAULT_NOT_SET,
                mUser?.email,
                "https://images.unsplash.com/photo-1483738264799-0a03c7a9889f?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=8b5bad6d8d268a3ca592ec2ad7cbdb70&auto=format&fit=crop&w=2104&q=80",
                UserType.TYPE_USER
        )

        mDatabaseReference?.child(CHILD_USER)?.child(mUser?.uid)?.setValue(user)
                ?.addOnSuccessListener {
                    toast("tabel user sukses diupdate")
                }?.addOnFailureListener {
                    toast("tabel user gagal diupdate")
                }
    }


    private fun placeMarkers() {
        items?.forEach { toko ->
            val latLng = LatLng(toko.latitude, toko.longitude)
            val marker = MarkerOptions()
                    .position(latLng)
                    .title(toko.nama)
                    .snippet("Nomor telepon ${toko.nomorTelepon}")
            mMap.addMarker(marker)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    private fun createDummyData() {

//        for (i in 1..10) {
//            val randomUserId = mDatabaseReference?.push()?.key
//            val user = User(
//                    randomUserId,
//                    "user biasa ${i}",
//                    "userBiasa${i}@gmail.com",
//                    "https://images.unsplash.com/photo-1483738264799-0a03c7a9889f?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=8b5bad6d8d268a3ca592ec2ad7cbdb70&auto=format&fit=crop&w=2104&q=80",
//                    UserType.TYPE_USER
//            )
//            mDatabaseReference?.child(Constant.CHILD.CHILD_USER)?.child(randomUserId)?.setValue(user)
//        }

        for (i in 1..3) {
            val rn = Random()
            val randomLatitude = (rn.nextInt(50).toDouble() + 1) - ((rn.nextInt(10).toDouble() + 1) * 1 / 2)
            val randomLongitude = rn.nextInt(60).toDouble() + 1

            val randomTokoId = mDatabaseReference?.push()?.key
            val toko = Toko()
            toko.id = randomTokoId
            toko.keterangan = "Toko random ${randomTokoId}"
            if (i % 3 == 0) {
                toko.alamat = "Universitas Brawijaya"
                toko.nama = "Toko UB ${randomTokoId}"
                toko.latitude = randomLatitude
                toko.longitude = randomLongitude
                toko.nomorTelepon = "0099123123"
                toko.photoUrl = "https://unsplash.com/photos/uThsqghes2U"

            }
            if (i % 3 == 1) {
                toko.alamat = "Kembang kertas IV"
                toko.nama = "Toko Kebang Kertas ${randomTokoId}"
                toko.latitude = randomLatitude
                toko.longitude = randomLongitude
                toko.nomorTelepon = "00123123"
                toko.photoUrl = "https://images.unsplash.com/photo-1505275350441-83dcda8eeef5?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6cb59df05776385d412fadc06423597f&auto=format&fit=crop&w=1567&q=80"
            }
            if (i % 3 == 2) {
                toko.alamat = "Kalpataru"
                toko.nama = "Toko Kalpataru ${randomTokoId}"
                toko.latitude = randomLatitude
                toko.longitude = randomLongitude
                toko.nomorTelepon = "981273123"
                toko.photoUrl = "https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=a4deb7be2c7c79941884cdf35c1cee8e&auto=format&fit=crop&w=2100&q=80"
            }
            mDatabaseReference?.child(Constant.CHILD.CHILD_TOKO)?.child(randomTokoId)?.setValue(toko)
        }
    }

    private val myMarker: Marker? = null

    override fun onInfoWindowClick(p0: Marker?) {
        val markerId = p0!!.id
        val splittedMarkerId = markerId.split("m".toRegex())
        val markerIndexString = splittedMarkerId[1]
        // toast("info window clicked ${markerIndexString}")

        val toko = items!![markerIndexString.toInt()];
        // toast("toko : " + toko.nama)
        navigateToTokoDetailActivity(toko)
    }

    private fun navigateToTokoDetailActivity(toko: Toko) {
        val intent = Intent(this, TokoDetailActivity::class.java)
        intent.putExtra(Constant.KEY.KEY_ID_TOKO, toko.id)
        intent.putExtra(Constant.KEY.KEY_NAMA_TOKO, toko.nama)
        startActivity(intent)
    }

    private fun setupFirebase() {
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth?.currentUser
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.reference
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)
        fetchTokoList();

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    private fun searchToko(p0: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_map_type_hybrid -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.action_map_type_normal -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.action_map_type_satellite -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.action_map_type_terrain -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            R.id.action_logout -> {
                logout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        mAuth?.signOut()
        finish()
        toast("Logout")
    }

    private var exit: Boolean? = false

    override fun onBackPressed() {
        if (exit!!) {
            finish() // finish activity
        } else {
            toast("Press Back again to Exit")
            exit = true
            Handler().postDelayed(Runnable { exit = false }, 3 * 1000)
        }
    }

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage(getString(R.string.title_loading))
            mProgressDialog?.setIndeterminate(true)
        }
        mProgressDialog?.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog?.dismiss()
        }
    }
}
