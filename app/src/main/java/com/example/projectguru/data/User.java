package com.example.projectguru.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int user_id;
    @ColumnInfo(name = "user_pin")
    private String user_pin;
    @ColumnInfo(name = "salt")
    private byte[] salt;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_pin() {
        return user_pin;
    }

    public void setUser_pin(String user_pin) {
        this.user_pin = user_pin;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
