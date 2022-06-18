package com.example.store.ui.product.home.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.store.R
import java.util.*

class SpecialAdaptor(private val context: Context, var images: List<String> = emptyList()) :
    PagerAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = layoutInflater.inflate(R.layout.special_offers_card_view, container, false)

        val imageView: ImageView = itemView.findViewById(R.id.special_offers_iv)

        Glide.with(context).load(images[position]).fitCenter().into(imageView)

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as CardView)
    }
}