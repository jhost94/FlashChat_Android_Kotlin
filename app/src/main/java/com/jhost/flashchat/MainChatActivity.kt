package com.jhost.flashchat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jhost.flashchat.model.InstantMessage
import com.jhost.flashchat.service.ChatListAdapter

class MainChatActivity: AppCompatActivity(){

    var chatAdapter: ChatListAdapter? = null
    var chatListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)

        this.chatListView = findViewById(R.id.chat_list_view)
        val inputText: EditText = findViewById(R.id.messageInput)
        val sendButton: ImageButton = findViewById(R.id.sendButton)

        inputText.setOnEditorActionListener { textView, i, keyEvent ->
            sendMessage(inputText, getDisplayName(), getDatabaseReference())
        }

        sendButton.setOnClickListener { sendMessage(inputText, getDisplayName(), getDatabaseReference()) }
    }



    private fun sendMessage(view: EditText, displayName: String, dbRef: DatabaseReference): Boolean{
        val message: String = view.text.toString()

        if (message.trim().isNotEmpty()) {
            val chat: InstantMessage = InstantMessage(message, displayName)
            dbRef.child("message").push().setValue(chat)
            view.setText("")
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        this.chatAdapter = ChatListAdapter(this, getDatabaseReference(), getDisplayName())
        this.chatListView?.adapter = this.chatAdapter
    }

    override fun onStop() {
        super.onStop()
        this.chatAdapter?.cleanup()
    }

    private fun getDisplayName(): String{
        val name: String? = FirebaseAuth.getInstance().currentUser?.displayName
        if (name != null) {
            return name
        }
        return "Anonymous"
    }

    private fun getDatabaseReference(): DatabaseReference{
        return FirebaseDatabase.getInstance().getReference()
    }
}