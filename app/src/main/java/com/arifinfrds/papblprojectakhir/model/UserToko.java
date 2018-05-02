package com.arifinfrds.papblprojectakhir.model;

/**
 * Created by arifinfrds on 5/2/18.
 */

public class UserToko {

    private String idUser;
    private String idToko;

    public UserToko() {
    }

    public UserToko(String idUser, String idToko) {
        this.idUser = idUser;
        this.idToko = idToko;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdToko() {
        return idToko;
    }
}
