package com.bunkalogic.bunkalist.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bunkalogic.bunkalist.R
import com.bunkalogic.bunkalist.db.TimelineChat
import java.text.SimpleDateFormat
import java.util.*

class TimelineChatAdapter(val ctx : Context, private val TimelineChatList: MutableList<TimelineChat>):RecyclerView.Adapter<TimelineChatAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineChatAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timeline_chat_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  TimelineChatList.size
    }

    override fun onBindViewHolder(holder: TimelineChatAdapter.ViewHolder, position: Int) {
        val itemChat = TimelineChatList[position]

        holder.username.text = itemChat.username
        holder.content.text = itemChat.contentChat
        holder.sentAt.text = SimpleDateFormat("h:mm, EEE/MMM", Locale.getDefault()).format(itemChat.sentAt)

        if (itemChat.profileImageUrl!!.isEmpty()){
            Glide.with(holder.userImage)
                .load(R.drawable.ic_person_black_24dp)
                .override(60, 60)
                .into(holder.userImage)
        }else{
            Glide.with(holder.userImage)
                .load(itemChat.profileImageUrl)
                .override(60, 60)
                .into(holder.userImage)
        }

    }

    inner class ViewHolder internal constructor(view: View): RecyclerView.ViewHolder(view){
        internal var username: TextView
        internal var userImage: ImageView
        internal var sentAt: TextView
        internal var content: TextView

        init {
            username = view.findViewById(R.id.textViewUsernameChat)
            userImage =view.findViewById(R.id.imageViewUserImageChat)
            sentAt = view.findViewById(R.id.textViewDateChat)
            content = view.findViewById(R.id.textViewContentChat)
        }
    }

}