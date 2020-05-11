package com.example.fitness_tracker.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_tracker.MainActivity.Companion.runs
import com.example.fitness_tracker.PlansActivity
import com.example.fitness_tracker.R
import com.example.fitness_tracker.data.AppDatabase
import com.example.fitness_tracker.data.Item
import kotlinx.android.synthetic.main.item_layout.view.*

class PlansListAdapter(
    private val context: Context,
    plansListItems: List<Item>
) : RecyclerView.Adapter<PlansListAdapter.ViewHolder>() {

    private val plansList: MutableList<Item> = mutableListOf()

    init {
        plansList.addAll(plansListItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRow = LayoutInflater.from(context).inflate(
            R.layout.item_layout, parent, false
        )
        return ViewHolder(itemRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Item = plansList[holder.adapterPosition]

        bindProperties(holder, item)
        // bindIcons(item, holder)

        holder.btnEdit.setOnClickListener {
            (context as PlansActivity).showEditItemDialog(item, holder.adapterPosition)
        }
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return plansList.size
    }

    private fun bindProperties(holder: ViewHolder, item: Item) {
        holder.tvName.text = item.name

        if (runs[0].distance * 1.609f > item.Mon.toInt()) {
            d("setting_green", "Mon")
            holder.tvMon.setTextColor(Color.parseColor("#008000"))
        } else {
            d("setting_red", "Mon")
            holder.tvMon.setTextColor(Color.parseColor("#FF0000"))
        }

//        holder.tvMon.text = item.Mon
//        holder.tvTues.text = item.Tues
//        holder.tvWed.text = item.Wed
//        holder.tvThurs.text = item.Thurs
//        holder.tvFri.text = item.Fri
//        holder.tvSat.text = item.Sat
//        holder.tvSun.text = item.Sun
    }

    private fun deleteItem(index: Int) {
        Thread {
            AppDatabase.getInstance(context).plansListDAO().deleteItem(
                plansList[index]
            )
            (context as PlansActivity).runOnUiThread {
                plansList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAll() {
        Thread {
            AppDatabase.getInstance(context).plansListDAO().deleteAll()
            (context as PlansActivity).runOnUiThread {
                plansList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addItem(item: Item) {
        plansList.add(item)
        notifyItemInserted(plansList.lastIndex)
    }

    fun updateItem(item: Item, editIndex: Int) {
        plansList[editIndex] = item
        notifyItemChanged(editIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.tvName
        val tvMon = itemView.tvMon
        val tvTues = itemView.tvTues
        val tvWed = itemView.tvWed
        val tvThurs = itemView.tvThurs
        val tvFri = itemView.tvFri
        val tvSat = itemView.tvSat
        val tvSun = itemView.tvSun

        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }
}