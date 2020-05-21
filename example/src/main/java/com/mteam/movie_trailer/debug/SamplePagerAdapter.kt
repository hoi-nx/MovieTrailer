package com.mteam.movie_trailer.debug

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.viewpager.widget.PagerAdapter
import com.mteam.movie_trailer.R

internal class SamplePagerAdapter(private val context: Context) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val views =
        arrayOfNulls<ListView>(3)

    override fun getCount(): Int {
        return views.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = views[position]
        if (view == null) {
            view =
                inflater.inflate(R.layout.sample_page, container, false) as ListView
            view.adapter = SampleAdapter(context)
        }
        container.addView(view)
        return view!!
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

}