package com.example.android.homework7;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_users, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.users_frag_recycler);
        rv.setHasFixedSize(true);

        List <User> list = new ArrayList<>();
        User u1 = new User("Jake Pecoraro", "Male");
        User u2 = new User("Eric Ko", "Male");
        User u3 = new User("Leigh-Ann Pecoraro", "Female");
        User u4 = new User("Jake Pecoraro", "Male");
        User u5 = new User("Jake Pecoraro", "Male");
        User u6 = new User("Jake Pecoraro", "Male");
        list.add(u1);
        list.add(u2);
        list.add(u3);
        list.add(u4);
        list.add(u5);
        list.add(u6);

        Log.d("test", "" + list.size());

        UsersAdapter adapter = new UsersAdapter(list);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }
}
