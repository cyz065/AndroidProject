package com.management.winwin.workList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.management.winwin.R
import com.management.winwin.databinding.ItemBinding


class Adapter(private val models: List<Model>, private val context: Context) : PagerAdapter() {
    //private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding:ItemBinding

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemBinding.inflate(inflater)
        binding.workSite.text = models[position].workSite
        binding.money.text = models[position].money
        /*
        view.setOnClickListener {
            if (position == 0) {
                val intent = Intent(context, TargetActivity::class.java)
                //intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(context, HomeActivity::class.java)
                //intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent)
            } else if (position == 2) {
                val intent = Intent(context, MartActivity::class.java)
                //intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent)
            } else if (position == 3) {
                val intent = Intent(context, CarActivity::class.java)
                //intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent)
            }
        }*/
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}