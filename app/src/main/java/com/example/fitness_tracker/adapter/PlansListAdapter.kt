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
import java.text.SimpleDateFormat
import java.util.*


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
        holder.tvDate.text = item.date

        val df = SimpleDateFormat("yyyy-MM-dd")
        val dateInput = df.parse(item.date)

        val cal = Calendar.getInstance()
        cal.time = dateInput
        val startDate = cal.timeInMillis
        // val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        for (run in runs) {
            // get runs within the week
            val temp = Calendar.getInstance()
            temp.set(run.year, run.month - 1, run.day)
            val runDate = temp.timeInMillis
            val diff = runDate - startDate

            if (diff in 0..604800000L) {
                d("valid_run", "distance: ${run.distance}, time: ${run.minutes}")

                // get day of week of run
                when (temp.get(Calendar.DAY_OF_WEEK)) {
                    // Sunday
                    1 -> {
                        if (runs[0].distance * 1.609f > item.Sun.toInt()) {
                            holder.tvSun.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvSun.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Monday
                    2 -> {
                        if (runs[0].distance * 1.609f > item.Mon.toInt()) {
                            holder.tvMon.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvMon.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Tuesday
                    3 -> {
                        if (runs[0].distance * 1.609f > item.Tues.toInt()) {
                            holder.tvTues.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvTues.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Wednesday
                    4 -> {
                        if (runs[0].distance * 1.609f > item.Wed.toInt()) {
                            holder.tvWed.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvWed.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Thursday
                    5 -> {
                        if (runs[0].distance * 1.609f > item.Thurs.toInt()) {
                            holder.tvThurs.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvThurs.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Friday
                    6 -> {
                        if (runs[0].distance * 1.609f > item.Fri.toInt()) {
                            holder.tvFri.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvFri.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Saturday
                    7 -> {
                        d("sat_run", "distance: ${run.distance}, time: ${run.minutes}")
                        if (runs[0].distance * 1.609f > item.Sat.toInt()) {
                            d("setting_green", "Sat")
                            holder.tvSat.setTextColor(Color.parseColor("#008000"))
                        } else {
                            d("setting_red", "Sat")
                            holder.tvSat.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                }
            } else {
                break
            }
        }
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
        val tvDate = itemView.tvDate

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