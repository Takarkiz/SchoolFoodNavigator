package com.takhaki.schoolfoodnavigator.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takhaki.schoolfoodnavigator.Model.AssessmentEntity
import com.takhaki.schoolfoodnavigator.Model.ShopEntity
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.FirestorageRepository
import me.zhanghai.android.materialratingbar.MaterialRatingBar

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
//                val shopViewHolder = ShopDetailViewHolder(holder.itemView)
//                val imageRef = repository.getGSReference(dataAboutShop.images[0])
//                Glide.with(holder.itemView)
//                    .load(imageRef)
//                    .placeholder(R.drawable.ic_add_shop_mall)
//                    .into(shopViewHolder.shopImageView)
//                shopViewHolder.nameTextView.text = dataAboutShop.name
//                shopViewHolder.genreTextView.text = dataAboutShop.genre
//                shopViewHolder.totalScore.rating = 4.0f
//                shopViewHolder.scoreTextView.text = "(4.0)"
//                shopViewHolder.goodRatingStar.rating = 3.0f
//                shopViewHolder.distanceRatingStar.rating = 5.0f
//                shopViewHolder.cheepRatingStar.rating = 4.0f
            }

            USER_VIEW_TYPE -> {

            }
        }
    }

    class ShopDetailViewHolder(detailItemView: View) : RecyclerView.ViewHolder(detailItemView) {

        val shopImageView: ImageView = detailItemView.findViewById(R.id.shopImage)
        val nameTextView: TextView = detailItemView.findViewById(R.id.shopName)
        val genreTextView: TextView = detailItemView.findViewById(R.id.genreTextView)
        val totalScore: MaterialRatingBar = detailItemView.findViewById(R.id.totalRatingScore)
        val scoreTextView: TextView = detailItemView.findViewById(R.id.scoreText)
        val goodRatingStar: MaterialRatingBar = detailItemView.findViewById(R.id.goodRatingStar)
        val distanceRatingStar: MaterialRatingBar = detailItemView.findViewById(R.id.distanceRatingStar)
        val cheepRatingStar: MaterialRatingBar = detailItemView.findViewById(R.id.cheepRatingStar)
    }

    class ShopResultViewHolder(resultItemView: View) : RecyclerView.ViewHolder(resultItemView) {

    }
}

