package com.example.android.homework7;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jake on 11/22/16.
 */

public class MessagesAdapter  extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder>{
    private List<Message> list;
    static private Context context;

    public MessagesAdapter(List<Message> list) {
        this.list = list;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView subject, from;
        ImageView avatar;
        ImageButton remove, reply;

        private MyViewHolder(View v) {
            super(v);
            context = v.getContext();

            card = (CardView) v.findViewById(R.id.message_card);
            subject = (TextView) v.findViewById(R.id.message_subject_tv);
            from = (TextView) v.findViewById(R.id.message_from_tv);
            avatar = (ImageView) v.findViewById(R.id.message_user_iv);
            remove = (ImageButton) v.findViewById(R.id.message_remove_btn);
            reply = (ImageButton) v.findViewById(R.id.message_reply_btn);
        }
    }

    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int vt) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int p) {
        holder.subject.setText(list.get(p).getSubject());
        holder.from.setText(list.get(p).getFrom());
        Picasso.with(context).load(list.get(p).getUserAvatarUrl()).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
