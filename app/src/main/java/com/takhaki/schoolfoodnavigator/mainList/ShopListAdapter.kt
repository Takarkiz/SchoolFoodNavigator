package com.takhaki.schoolfoodnavigator.mainList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takhaki.schoolfoodnavigator.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class ShopListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    var data = listOf<ShopListItemModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_cell_main_list, parent, false)

        return ShopItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = data[position]

        holder.shopTitleTextView.text = item.name
        holder.shopGenreTextView.text = item.shopGenre
        holder.scoreRatingBar.rating = item.score
        holder.scoreTextView.text = item.score.toString()
        Glide.with(holder.itemView).load(item.imageUrl).into(holder.shopImageView)

    }

}

class ShopItemViewHolder(shopItemView: View) : RecyclerView.ViewHolder(shopItemView) {

    val shopTitleTextView: TextView = shopItemView.findViewById(R.id.shopTitleText)
    val shopGenreTextView: TextView = shopItemView.findViewById(R.id.shopGenreTitle)
    val shopImageView: ImageView = shopItemView.findViewById(R.id.shopImageView)
    val scoreTextView: TextView = shopItemView.findViewById(R.id.scoreText)
    val scoreRatingBar: MaterialRatingBar = shopItemView.findViewById(R.id.ratingStar)
}