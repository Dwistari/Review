package com.example.review.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.review.R
import com.example.review.data.model.Review
import kotlinx.android.synthetic.main.item_review.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.HomeViewHolder>() {

    private var review: MutableList<Review> = ArrayList()

    override fun getItemCount(): Int {
        return review.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemView.tv_name.text = review[position].name
        holder.itemView.tv_review.text = review[position].description

//        Picasso.with(holder.itemView.getContext()).load(review[position].gambarPariwisata)
//            .into(holder.itemView.image)

        holder.itemView.setOnClickListener {

        }
    }


    class HomeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }

    fun setData(des: MutableList<Review>) {
        this.review = des
        notifyDataSetChanged()
    }
}
