package com.takhaki.schoolfoodnavigator.screen.detailReward.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Utility.RewardUtil

class RewardListAdapter : RecyclerView.Adapter<RewardListViewHolder>() {

    fun setUserGrade(grade: RewardUtil.Grade) {
        userGrade = grade
        notifyDataSetChanged()
    }

    private val items = listOf(
        RewardItem(RewardUtil.Grade.NORMAL, RewardUtil.gradeToRange(RewardUtil.Grade.NORMAL)),
        RewardItem(
            RewardUtil.Grade.NORMAL2,
            RewardUtil.gradeToRange(RewardUtil.Grade.NORMAL2)
        ),
        RewardItem(RewardUtil.Grade.BASIC, RewardUtil.gradeToRange(RewardUtil.Grade.BASIC)),
        RewardItem(RewardUtil.Grade.BASIC2, RewardUtil.gradeToRange(RewardUtil.Grade.BASIC2)),
        RewardItem(RewardUtil.Grade.BASIC3, RewardUtil.gradeToRange(RewardUtil.Grade.BASIC3)),
        RewardItem(RewardUtil.Grade.SUPER, RewardUtil.gradeToRange(RewardUtil.Grade.SUPER)),
        RewardItem(RewardUtil.Grade.SUPER2, RewardUtil.gradeToRange(RewardUtil.Grade.SUPER2)),
        RewardItem(RewardUtil.Grade.SUPER3, RewardUtil.gradeToRange(RewardUtil.Grade.SUPER3))
    )

    private var userGrade: RewardUtil.Grade? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_cell_reward, parent, false)

        return RewardListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RewardListViewHolder, position: Int) {
        val item = items[position]

        holder.rewardTitleTextView.text = item.title.text
        val start = item.range.first
        val last = item.range.last
        holder.rangeTextView.apply {
            text = String.format(context.getString(R.string.detail_reward_point_text), start, last)
        }

        userGrade?.let { grade ->
            if (item.title == grade) {
                holder.itemView.apply {
                    setBackgroundColor(context.getColor(R.color.colorPrimaryLight))
                    holder.userRankTextView.isVisible = true
                }
            }
        }
    }

}

data class RewardItem(val title: RewardUtil.Grade, val range: IntRange)

class RewardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val rewardTitleTextView: TextView = itemView.findViewById(R.id.rewardTitleTextView)
    val rangeTextView: TextView = itemView.findViewById(R.id.rangeTextView)
    val userRankTextView: TextView = itemView.findViewById(R.id.userRankText)
}