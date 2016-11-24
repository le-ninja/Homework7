package com.example.android.homework7;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UsersFragment extends Fragment {
    List<User> list;
    UsersAdapter adapter;

    public UsersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if (extras != null) {
            list = extras.getParcelableArrayList("USERS_LIST");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_users, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.users_frag_recycler);
        rv.setHasFixedSize(true);

        adapter = new UsersAdapter(list);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    public void updateList(List<User> list) {
        this.list = list;
        adapter.notifyDataSetChanged();
    }
}
