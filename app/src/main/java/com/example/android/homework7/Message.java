package com.example.android.homework7;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jake on 11/22/16.
 */

public class Message implements Parcelable {
    String subject, from, dateRecieved, userId, userAvatarUrl;
    public Message(String subject, String from, String dateRecieved, String userId,
                   String userAvatarUrl) {
        this.subject = subject;
        this.from = from;
        this.dateRecieved = dateRecieved;
        this.userId = userId;
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
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
        String[] data = new String[3];

        in.readStringArray(data);
        this.subject = data[0];
        this.from = data[1];
        this.dateRecieved = data[2];
        this.userId = data[3];
        this.userAvatarUrl = data[4];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.subject, this.from, this.dateRecieved, this.userId,
        this.userAvatarUrl});
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

