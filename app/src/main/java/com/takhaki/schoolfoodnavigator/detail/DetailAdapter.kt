package com.takhaki.schoolfoodnavigator.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.takhaki.schoolfoodnavigator.R
import com.takhaki.schoolfoodnavigator.Repository.FirestorageRepository
import com.takhaki.schoolfoodnavigator.Repository.UserAuth
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val repository = FirestorageRepository("Shops")
    private val DETAIL_VIEW_TYPE = 0
    private val USER_VIEW_TYPE = 1
    private var isFavorite = false

    var shopId: String = ""

    var dataComment = listOf<CommentDetailModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var dataAboutShop = AboutShopDetailModel()
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
                val shopViewHolder = ShopDetailViewHolder(holder.itemView)
                dataAboutShop.imageUrl?.let { url ->
                    val imageRef = repository.getGSReference(url)
                    Glide.with(holder.itemView)
                        .load(imageRef)
                        .placeholder(R.drawable.ic_add_shop_mall)
                        .into(shopViewHolder.shopImageView)
                }

                shopViewHolder.nameTextView.text = dataAboutShop.name
                shopViewHolder.genreTextView.text = dataAboutShop.genre
                shopViewHolder.totalScore.rating = dataAboutShop.score
                shopViewHolder.totalScore.isEnabled = false
                shopViewHolder.scoreTextView.text = String.format("%1$.1f", dataAboutShop.score)
                shopViewHolder.goodRatingStar.rating = dataAboutShop.goodScore
                shopViewHolder.goodRatingStar.isEnabled = false
                shopViewHolder.distanceRatingStar.rating = dataAboutShop.distance
                shopViewHolder.distanceRatingStar.isEnabled = false
                shopViewHolder.cheepRatingStar.rating = dataAboutShop.cheep
                shopViewHolder.cheepRatingStar.isEnabled = false

                val auth = UserAuth()
                auth.checkFavoriteShop(shopId) { containFav ->
                    if (containFav) {
                        shopViewHolder.favoriteIconImageView.setImageResource(R.drawable.ic_nav_fill_favorite)
                        isFavorite = containFav
                    } else {
                        shopViewHolder.favoriteIconImageView.setImageResource(R.drawable.ic_nav_favorite)
                        isFavorite = containFav
                    }
                }

                shopViewHolder.favoriteIconImageView.setOnClickListener { view ->
                    if (isFavorite) {
                        auth.deleteFavoriteShop(shopId) {
                            Snackbar.make(view, "お気に入りから削除しました", Snackbar.LENGTH_SHORT).show()
                            shopViewHolder.favoriteIconImageView.setImageResource(R.drawable.ic_nav_favorite)
                            isFavorite = false
                        }
                    } else {
                        auth.addFavoriteShop(shopId) {
                            // お気に入りリストに追加成功
                            Snackbar.make(view, "お気に入りに追加しました", Snackbar.LENGTH_SHORT).show()
                            shopViewHolder.favoriteIconImageView.setImageResource(R.drawable.ic_nav_fill_favorite)
                            isFavorite = true
                        }
                    }
                }
            }

            USER_VIEW_TYPE -> {
                val commentHolder = ShopResultViewHolder(holder.itemView)
                val item = dataComment[position - 1]
                commentHolder.userNameTextView.text = item.name
                commentHolder.totalRating.text =
                    String.format("%1$.1f", (item.gScore + item.dScore + item.cScore) / 3)
                commentHolder.gRatingBar.rating = item.gScore
                commentHolder.gRatingBar.isEnabled = false
                commentHolder.dRatingBar.rating = item.dScore
                commentHolder.dRatingBar.isEnabled = false
                commentHolder.cRatingBar.rating = item.cScore
                commentHolder.cRatingBar.isEnabled = false
                commentHolder.commentTextView.text = item.comment
                commentHolder.totalRatingSampleBar.isEnabled = false

                item.userIcon?.let { url ->
                    val storage = FirestorageRepository("User")
                    Glide.with(holder.itemView)
                        .load(storage.getGSReference(url))
                        .placeholder(R.drawable.ic_default_user)
                        .into(commentHolder.iconImageView)
                }
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
        val distanceRatingStar: MaterialRatingBar =
            detailItemView.findViewById(R.id.distanceRatingStar)
        val cheepRatingStar: MaterialRatingBar = detailItemView.findViewById(R.id.cheepRatingStar)
        val favoriteIconImageView: ImageView = detailItemView.findViewById(R.id.favoriteIcon)
    }

    class ShopResultViewHolder(resultItemView: View) : RecyclerView.ViewHolder(resultItemView) {

        val iconImageView: ImageView = resultItemView.findViewById(R.id.userIconImageView)
        val userNameTextView: TextView = resultItemView.findViewById(R.id.userName)
        val totalRating: TextView = resultItemView.findViewById(R.id.totalScore)
        val gRatingBar: MaterialRatingBar = resultItemView.findViewById(R.id.grate)
        val dRatingBar: MaterialRatingBar = resultItemView.findViewById(R.id.drate)
        val cRatingBar: MaterialRatingBar = resultItemView.findViewById(R.id.crate)
        val commentTextView: TextView = resultItemView.findViewById(R.id.commentContentTextView)
        val totalRatingSampleBar: MaterialRatingBar = resultItemView.findViewById(R.id.totalRating)
    }
}

