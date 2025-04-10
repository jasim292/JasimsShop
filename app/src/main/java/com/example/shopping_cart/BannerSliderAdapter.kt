package com.example.shopping_cart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BannerSliderAdapter(
    private val bannerItems: List<BannerItem>,
    private val onBannerClick: (BannerItem) -> Unit = {} // Default empty click handler
) : RecyclerView.Adapter<BannerSliderAdapter.BannerViewHolder>() {

    data class BannerItem(
        val imageUrl: String,
        val offerText: String,
        val isActive: Boolean = true
    )

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.slideImage)
        private val textView: TextView = itemView.findViewById(R.id.slideText)

        fun bind(item: BannerItem) {

            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(imageView)

            textView.text = item.offerText
            textView.visibility = if (item.offerText.isNotEmpty()) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onBannerClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner_slide, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerItems[position])
    }

    override fun getItemCount(): Int = bannerItems.size
}