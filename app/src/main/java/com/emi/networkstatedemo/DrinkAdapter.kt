package com.emi.networkstatedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class DrinkAdapter constructor(private var drinkList : MutableList<Cocktails>, private var context : Context): RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.content_main, parent, false)
        return DrinkViewHolder(layout)
    }
    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.names.text = drinkList[position].name
        Picasso.with(context).load(drinkList[position].thumbnail)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemCount(): Int {
        return drinkList.size ?: 0
    }


    fun updateDrinks(list : MutableList<Cocktails>){
        this.drinkList = list
    }
    inner class DrinkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val names = itemView.findViewById<TextView>(R.id.name)
        val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    }
}