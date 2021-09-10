package com.jhost.flashchat.service

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.jhost.flashchat.R
import com.jhost.flashchat.model.InstantMessage

class ChatListAdapter(activity: Activity, databaseReference: DatabaseReference, name: String): BaseAdapter(){
    private val snapShotList: MutableList<DataSnapshot> = mutableListOf<DataSnapshot>()
    private var displayName: String? = null
    private var activity: Activity? = null
    private var databaseReference: DatabaseReference? = null
    private var childEventListener: ChildEventListener? = null

    init {
        this.displayName = name
        this.activity = activity
        this.databaseReference = databaseReference.child("messages")

        this.databaseReference?.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0 == null){
                    return
                }
                snapShotList.add(p0)
                notifyDataSetChanged()
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getCount(): Int {
        return snapShotList.size
    }

    override fun getItem(p0: Int): InstantMessage {
        return snapShotList[p0].getValue(InstantMessage::class.java)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, viewGroup: ViewGroup): View {
        var view: View? = p1
        if (view == null) {
            val inflater: LayoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false)

            val viewHolder: ViewHolder = ViewHolder()
            viewHolder.authorName = view.findViewById(R.id.author)
            viewHolder.body = view.findViewById(R.id.message)
            viewHolder.params = viewHolder.authorName?.layoutParams as LinearLayout.LayoutParams

            view.setTag(viewHolder)
        }
        val message: InstantMessage = getItem(p0)
        val holder: ViewHolder = view!!.tag as ViewHolder

        val isMe: Boolean = message.author.equals(displayName)
        setChatRowAppearance(isMe, holder)

        holder.authorName?.setText(message.author)
        holder.body?.setText(message.message)

        return view
    }

    private fun setChatRowAppearance(isMe: Boolean, holder: ViewHolder){
        if (isMe) {
            holder.params?.gravity = Gravity.END
            holder.authorName?.setTextColor(Color.GREEN)
            holder.body?.setBackgroundResource(R.drawable.bubble2)
        } else {
            holder.params?.gravity = Gravity.START
            holder.authorName?.setTextColor(Color.BLUE)
            holder.body?.setBackgroundResource(R.drawable.bubble1)
        }

        holder.authorName?.layoutParams = holder.params
        holder.body?.layoutParams = holder.params
    }

    class ViewHolder{
        var authorName: TextView? = null
        var body: TextView? = null
        var params: LinearLayout.LayoutParams? = null
    }

    fun cleanup(){
        databaseReference?.removeEventListener(childEventListener)
    }
}