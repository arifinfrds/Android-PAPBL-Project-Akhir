package com.arifinfrds.papblprojectakhir.model;

/**
 * Created by arifinfrds on 5/2/18.
 */

public class User {

    private String id;
    private String nama;
    private String email;
    private String photoUrl;

    // dari interace UserType
    private String userType;

    public User() {

    }

    public User(String id, String nama, String email, String photoUrl, String userType) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.photoUrl = photoUrl;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUserType() {
        return userType;
    }
}
