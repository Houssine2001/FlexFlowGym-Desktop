package com.example.bty.Entities;

import java.time.Instant;

public class Validation {
    int id;
    int code ;  // code eli bch yetb3ath lel user par mail w bch ykoun random
    Instant created_at; // date de creation de code ba3ed ma tssir signup b s7i7
    Instant expired_at; // date d'expiration de code bch ykoun baaed date de creation de 10min
    User user; // user li 3mal signup

    @Override
    public String toString() {
        return "Validation{" +
                "id=" + id +
                ", code=" + code +
                ", created_at=" + created_at +
                ", expired_at=" + expired_at +
                ", user=" + user +
                '}';
    }

    public Validation() {
    }

    public Validation(int code, Instant created_at, Instant expired_at, User user) {
        this.code = code;
        this.created_at = created_at;
        this.expired_at = expired_at;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getExpired_at() {
        return expired_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExpired_at(Instant expired_at) {
        this.expired_at = expired_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}