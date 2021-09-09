package com.jhost.flashchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jhost.flashchat.model.InstantMessage;
import com.jhost.flashchat.service.ChatListAdapter;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference databaseReference;
    private ChatListAdapter chatAdapter;

    public static void log(String msg) {
        Log.d("FlashChat", msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        getDisplayName();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(((textView, i, keyEvent) -> {
            sendMessage();
            return true;
        }));

        // TODO: Add an OnClickListener to the sendButton to send a message

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.

    @Override
    protected void onStart() {
        super.onStart();
        this.chatAdapter = new ChatListAdapter(this, databaseReference, mDisplayName);
        mChatListView.setAdapter(this.chatAdapter);
    }


    // TODO: Retrieve the display name from the Shared Preferences
    private void getDisplayName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDisplayName = user.getDisplayName();
    }

    private void sendMessage() {
        log("I sent something");
        // TODO: Grab the text the user typed in and push the message to Firebase
        String message = mInputText.getText().toString();
        if (message.trim().length() > 0 ){
            InstantMessage chat = new InstantMessage(message, mDisplayName);
            databaseReference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }



    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.
        chatAdapter.cleanup();
    }

}
