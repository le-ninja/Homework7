package com.example.android.homework7;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jake on 11/22/16.
 */

public class Message implements Parcelable {
    String subject, from, dateRecieved, userId, imageUrl, message;
    public Message() {
    }


    public Message(String from, String subject, String message, String userId) {
        this.subject = from;
        this.from = subject;
        this.message = message;
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserAvatarUrl() {
        return imageUrl;
    }

    public void setUserAvatarUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(String dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Message(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.subject = data[0];
        this.from = data[1];
        this.message = data[2];
        this.userId = data[3];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.subject, this.from, this.message, this.userId});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}

