package com.mteam.movie_trailer.debug

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mteam.movie_trailer.R
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity() {
    private var first = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        item_pager.setAdapter(SamplePagerAdapter(this))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Draw Views")
            .setCheckable(true)
            .setChecked(scalpel.isDrawingViews)
            .setOnMenuItemClickListener { item ->
                val checked = !item.isChecked
                item.isChecked = checked
                scalpel.setDrawViews(checked)
                true
            }
        menu.add("Draw IDs")
            .setCheckable(true)
            .setChecked(scalpel.isDrawingIds)
            .setOnMenuItemClickListener { item ->
                val checked = !item.isChecked
                item.isChecked = checked
                scalpel.setDrawIds(checked)
                true
            }
        menu.add("DebugView")
            .setCheckable(true)
            .setChecked(scalpel.isDrawingIds)
            .setOnMenuItemClickListener { item ->
                val checked = !item.isChecked
                item.isChecked = checked
                if (first) {
                    first = false
                    Toast.makeText(this, "First to run", Toast.LENGTH_LONG)
                        .show()
                }
                scalpel.isLayerInteractionEnabled = checked
                true
            }
        return true
    }
}