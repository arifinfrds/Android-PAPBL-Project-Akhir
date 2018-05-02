package com.arifinfrds.papblprojectakhir.model;

/**
 * Created by arifinfrds on 5/2/18.
 */

public class Toko {

    private String id;
    private String nama;
    private String keterangan;
    private String alamat;
    private double latitude;
    private double longitude;
    private String nomorTelepon;
    private String photoUrl;

    public Toko() {

    }

    public Toko(String id, String nama, String keterangan, String alamat, double latitude, double longitude, String nomorTelepon, String photoUrl) {
        this.id = id;
        this.nama = nama;
        this.keterangan = keterangan;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nomorTelepon = nomorTelepon;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
