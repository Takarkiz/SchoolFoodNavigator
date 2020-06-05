package com.takhaki.schoolfoodnavigator.screen.memberList.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takhaki.schoolfoodnavigator.Model.UserEntity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository

class MemberListAdapter :
    RecyclerView.Adapter<MemberListViewHolder>() {

    fun setMemberList(members: List<UserEntity>) {
        memberList = members
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: MemberListClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_cell_member, parent, false)

        return MemberListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    override fun onBindViewHolder(holder: MemberListViewHolder, position: Int) {
        val item = memberList[position]

        holder.apply {
            userNameTextView.text = item.name
            userPointTextView.text = String.format(
                itemView.context.resources.getString(R.string.member_list_user_point),
                item.score
            )

            item.iconUrl?.let { imagePath ->
                val storageUrl = FirestorageRepository.getGSReference(imagePath)
                Glide.with(itemView)
                    .load(storageUrl)
                    .placeholder(R.drawable.default_person)
                    .into(imageView)
            }

            itemView.setOnClickListener {
                clickListener?.onClickMember(item.id)
            }
        }

    }

    private var memberList: List<UserEntity> = listOf()
    private var clickListener: MemberListClickListener? = null

    interface MemberListClickListener {

        fun onClickMember(userId: String)
    }

}

class MemberListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.memberListItemUserIcon)
    val userNameTextView: TextView = itemView.findViewById(R.id.userNameText)
    val userPointTextView: TextView = itemView.findViewById(R.id.userPointTextView)
}