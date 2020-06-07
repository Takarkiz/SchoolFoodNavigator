package com.takhaki.schoolfoodnavigator.screen.mainList.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.screen.mainList.model.ShopListItemModel
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class ShopListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    private lateinit var listener: OnItemClickListener
    var data = listOf<ShopListItemModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_cell_main_list, parent, false)

        return ShopItemViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = data[position]

        holder.shopTitleTextView.text = item.name
        holder.shopGenreTextView.text = item.shopGenre
        holder.scoreRatingBar.rating = item.score
        holder.scoreRatingBar.isEnabled = false
        holder.scoreTextView.text = String.format("%1$.1f", item.score)
        item.imageUrl?.let { url ->
            Glide.with(holder.itemView)
                .load(FirestorageRepository.getGSReference(url))
                .placeholder(R.drawable.ic_add_shop_mall)
                .into(holder.shopImageView)
        }

        holder.itemView.setOnClickListener {
            listener.onClick(it, item.id, item.name)
        }

        // お気に入りアイコンの設定
        holder.loveIconImageView.apply {
            visibility = when (item.isFavorite) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, shopId: String, name: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}

class ShopItemViewHolder(shopItemView: View) : RecyclerView.ViewHolder(shopItemView) {

    val shopTitleTextView: TextView = shopItemView.findViewById(R.id.shopTitleText)
    val shopGenreTextView: TextView = shopItemView.findViewById(R.id.shopGenreTitle)
    val shopImageView: ImageView = shopItemView.findViewById(R.id.shopImageView)
    val scoreTextView: TextView = shopItemView.findViewById(R.id.scoreText)
    val scoreRatingBar: MaterialRatingBar = shopItemView.findViewById(R.id.ratingStar)
    val loveIconImageView: ImageView = shopItemView.findViewById(R.id.loveIcon)
}