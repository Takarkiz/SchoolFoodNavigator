package com.takhaki.schoolfoodnavigator.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.FirestorageRepository

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val repository = FirestorageRepository("Shops")
    private val DETAIL_VIEW_TYPE = 0
    private val USER_VIEW_TYPE = 1

    var dataComment = listOf<AssessmentEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var dataAboutShop = ShopEntity()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return DETAIL_VIEW_TYPE
            else -> return USER_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        when (viewType) {
            DETAIL_VIEW_TYPE -> {
                val view = layoutInflater.inflate(R.layout.view_cell_detail_shop, parent, false)
                return ShopDetailViewHolder(view)
            }

            USER_VIEW_TYPE -> {
                val view = layoutInflater.inflate(R.layout.view_cell_user_comment, parent, false)
                return ShopResultViewHolder(view)
            }

            else -> throw IllegalAccessException("存在しないセル")
        }
    }

    override fun getItemCount(): Int {
        return dataComment.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            DETAIL_VIEW_TYPE -> {

            }

            USER_VIEW_TYPE -> {

            }
        }
    }

    class ShopDetailViewHolder(detailItemView: View) : RecyclerView.ViewHolder(detailItemView) {

    }

    class ShopResultViewHolder(resultItemView: View) : RecyclerView.ViewHolder(resultItemView) {

    }
}

