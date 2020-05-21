package com.mteam.movie_trailer.debug

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mteam.movie_trailer.R
import com.squareup.picasso.Picasso

internal class SampleAdapter(context: Context) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val picasso: Picasso = Picasso.get()
    override fun getCount(): Int {
        return ITEMS.size
    }

    override fun getItem(position: Int): Item {
        return ITEMS[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sample_item, viewGroup, false)
            convertView.tag = Holder(convertView)
        }
        val holder = convertView?.tag as Holder
        val item = getItem(position)
        holder.titleView.text = item.title
        holder.subtitleView.text = item.user
        picasso.load(item.url).fit().centerCrop().into(holder.imageView)
        return convertView
    }

    internal class Item(val url: String, val title: String, val user: String)

    internal class Holder(view: View) {
        var imageView: ImageView = view.findViewById(R.id.item_image)
        var titleView: TextView = view.findViewById(R.id.item_title)
        var subtitleView: TextView = view.findViewById(R.id.item_subtitle)

    }

    companion object {
        private val ITEMS =
            arrayOf(
                Item(
                    "http://i.imgur.com/CqmBjo5.jpg",
                    "Lorem ipsum",
                    "Et vulputate"
                ),
                Item(
                    "http://i.imgur.com/zkaAooq.jpg",
                    "Dolor sit",
                    "Felis placerat"
                ),
                Item(
                    "http://i.imgur.com/0gqnEaY.jpg",
                    "Amet consectetur",
                    "Dolor non"
                ),
                Item(
                    "http://i.imgur.com/9gbQ7YR.jpg",
                    "Adipiscing elit",
                    "Aliquam fringilla"
                ),
                Item(
                    "http://i.imgur.com/aFhEEby.jpg",
                    "Viverra neque",
                    "Tellus velit"
                ),
                Item(
                    "http://i.imgur.com/0E2tgV7.jpg",
                    "Risus vitae",
                    "Malesuada urna"
                ),
                Item(
                    "http://i.imgur.com/P5JLfjk.jpg",
                    "Lacinia enim",
                    "Nec lobortis"
                ),
                Item(
                    "http://i.imgur.com/nz67a4F.jpg",
                    "Pretium ac consequat",
                    "Est tellus malesuada"
                ),
                Item(
                    "http://i.imgur.com/dFH34N5.jpg",
                    "Tincidunt tempor",
                    "A libero"
                ),
                Item(
                    "http://i.imgur.com/FI49ftb.jpg",
                    "Aliquet potenti",
                    "Feugiat non"
                ),
                Item(
                    "http://i.imgur.com/DvpvklR.jpg",
                    "Sed turpis",
                    "Dolor vel ut"
                ),
                Item(
                    "http://i.imgur.com/DNKnbG8.jpg",
                    "Eu magna",
                    "Auctor nec"
                ),
                Item(
                    "http://i.imgur.com/yAdbrLp.jpg",
                    "Varius venenatis",
                    "Pulvinar libero"
                ),
                Item(
                    "http://i.imgur.com/55w5Km7.jpg",
                    "Sit amet",
                    "Ut viverra"
                ),
                Item(
                    "http://i.imgur.com/NIwNTMR.jpg",
                    "Luctus enim",
                    "Eros vel"
                ),
                Item(
                    "http://i.imgur.com/DAl0KB8.jpg",
                    "Vitae erat",
                    "Fringilla sapien"
                ),
                Item(
                    "http://i.imgur.com/xZLIYFV.jpg",
                    "Condimentum congue",
                    "Ante ipsum"
                ),
                Item(
                    "http://i.imgur.com/HvTyeh3.jpg",
                    "Felis ut pellentesque",
                    "Primis in"
                ),
                Item(
                    "http://i.imgur.com/Ig9oHCM.jpg",
                    "Egestas ipsum",
                    "Faucibus orci"
                ),
                Item(
                    "http://i.imgur.com/7GUv9qa.jpg",
                    "Facilisi enim",
                    "Luctus et"
                ),
                Item(
                    "http://i.imgur.com/i5vXmXp.jpg",
                    "Odio convallis",
                    "Ultrices posuere"
                ),
                Item(
                    "http://i.imgur.com/glyvuXg.jpg",
                    "Luctus lacinia",
                    "Cubilia Curae; velit sapien"
                ),
                Item(
                    "http://i.imgur.com/u6JF6JZ.jpg",
                    "Id lacinia",
                    "Venenatis vehicula"
                ),
                Item(
                    "http://i.imgur.com/ExwR7ap.jpg",
                    "Consectetur lectus",
                    "Lacus eu"
                ),
                Item(
                    "http://i.imgur.com/Q54zMKT.jpg",
                    "Tristique in",
                    "Feugiat molestie"
                ),
                Item(
                    "http://i.imgur.com/9t6hLbm.jpg",
                    "Felis in justo",
                    "Libero pretium"
                ),
                Item(
                    "http://i.imgur.com/F8n3Ic6.jpg",
                    "Posuere quis",
                    "Mi eget convallis"
                ),
                Item(
                    "http://i.imgur.com/P5ZRSvT.jpg",
                    "Diam in",
                    "Egestas commodo"
                ),
                Item(
                    "http://i.imgur.com/jbemFzr.jpg",
                    "Tortor hendrerit",
                    "Leo massa"
                ),
                Item(
                    "http://i.imgur.com/8B7haIK.jpg",
                    "Feugiat porttitor",
                    "Gravida mi"
                ),
                Item(
                    "http://i.imgur.com/aSeTYQr.jpg",
                    "Vel lorem",
                    "In ullamcorper"
                ),
                Item(
                    "http://i.imgur.com/OKvWoTh.jpg",
                    "Sit amet",
                    "Velit justo condimentum"
                ),
                Item(
                    "http://i.imgur.com/zD3gT4Z.jpg",
                    "Convallis vel",
                    "A lectus suscipit"
                ),
                Item(
                    "http://i.imgur.com/z77CaIt.jpg",
                    "Scelerisque felis",
                    "Eu bibendum"
                )
            )
    }

}