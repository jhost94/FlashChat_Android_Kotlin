package com.jhost.flashchat.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jhost.flashchat.R;
import com.jhost.flashchat.model.InstantMessage;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity activity;
    private DatabaseReference databaseReference;
    private String displayName;
    private ArrayList<DataSnapshot> snapshotList;

    private ChildEventListener childEventListener;

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String name){
        this.activity = activity;
        this.databaseReference = databaseReference.child("messages");
        this.displayName = name;
        this.snapshotList = new ArrayList<>();

        setChildEventListener();
        this.databaseReference.addChildEventListener(childEventListener);
    }

    private void setChildEventListener(){
        this.childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                snapshotList.add(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public int getCount() {
        return snapshotList.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = snapshotList.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            //ViewHolder holder = new ViewHolder();
            holder.authorName = view.findViewById(R.id.author);
            holder.body = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();

            view.setTag(holder);
        }
        final InstantMessage message = getItem(i);
        //InstantMessage message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();
        //ViewHolder holder = (ViewHolder) view.getTag();

        boolean isMe = message.getAuthor().equals(displayName);

        setChatRowAppearance(isMe, holder);

        holder.authorName.setText(message.getAuthor());
        holder.body.setText(message.getMessage());

        return view;
    }

//    @Nullable
//    @Override
//    public CharSequence[] getAutofillOptions() {
//        return new CharSequence[0];
//    }


    private void setChatRowAppearance(boolean isMe, ViewHolder holder){
        if (isMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanup(){
        databaseReference.removeEventListener(childEventListener);
    }

    //Nested class
    static class ViewHolder {
        private TextView authorName;
        private TextView body;
        private LinearLayout.LayoutParams params;
    }
}
