package com.example.android.homework7;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private List<User> list;
    private static Firebase mRef = new Firebase("https://homework7-425f5.firebaseio.com/Messages");

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView nameTV, genderTV;
        ImageView avatar;
        ImageButton addUser, messageUser;

        private MyViewHolder(View v) {
            super(v);

            card = (CardView) v.findViewById(R.id.users_card);
            genderTV = (TextView) v.findViewById(R.id.users_gender_tv);
            nameTV = (TextView) v.findViewById(R.id.users_name_tv);
            avatar = (ImageView) v.findViewById(R.id.users_avatar_iv);

            messageUser = (ImageButton) v.findViewById(R.id.users_message_user_btn);
        }
    }

    UsersAdapter(List <User> list) {
        this.list = list;
    }

    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int vt) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout,
                parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int p) {
        holder.genderTV.setText(list.get(p).getGender());
        holder.nameTV.setText(list.get(p).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
