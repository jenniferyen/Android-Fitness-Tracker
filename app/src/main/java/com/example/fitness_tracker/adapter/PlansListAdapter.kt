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
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // starting on Mon, Tues, etc.
        d("start_date", dayOfWeek.toString())

        setVisibility(dayOfWeek, holder)
        setDistanceColors(startDate, item, holder)
    }

    private fun setDistanceColors(
        startDate: Long,
        item: Item,
        holder: ViewHolder
    ) {
        var distSun = 0f
        var distMon = 0f
        var distTues = 0f
        var distWed = 0f
        var distThurs = 0f
        var distFri = 0f
        var distSat = 0f

        for (run in runs) {
            d("processing_run", "distance: ${run.distance}, time: ${run.minutes}")
            val temp = Calendar.getInstance()
            temp.set(run.year, run.month - 1, run.day)
            val runDate = temp.timeInMillis
            val diff = runDate - startDate

            // get runs within the week
            if (diff in 0..604800000L) {
                d("valid_run", "distance: ${run.distance}, time: ${run.minutes}")

                // get day of week of run
                when (temp.get(Calendar.DAY_OF_WEEK)) {
                    // Sunday
                    1 -> {
                        distSun += run.distance

                        d("sun_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("sun_total", distSun.toString())
                        if (item.Sun.isNotBlank() && distSun * 1.609f > item.Sun.toDouble()) {
                            holder.tvSun.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvSun.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Monday
                    2 -> {
                        distMon += run.distance

                        d("mon_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("mon_total", distMon.toString())
                        if (item.Mon.isNotBlank() && distMon * 1.609f > item.Mon.toDouble()) {
                            d("setting_green", "Mon")
                            holder.tvMon.setTextColor(Color.parseColor("#008000"))
                        } else {
                            d("setting_red", "Mon")
                            holder.tvMon.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Tuesday
                    3 -> {
                        distTues += run.distance

                        d("tues_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("tues_total", distTues.toString())
                        if (item.Tues.isNotBlank() && distTues * 1.609f > item.Tues.toDouble()) {
                            holder.tvTues.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvTues.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Wednesday
                    4 -> {
                        distWed += run.distance

                        d("wed_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("wed_total", distWed.toString())
                        if (item.Wed.isNotBlank() && distWed * 1.609f > item.Wed.toDouble()) {
                            holder.tvWed.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvWed.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Thursday
                    5 -> {
                        distThurs += run.distance

                        d("thurs_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("thurs_total", distThurs.toString())
                        if (item.Thurs.isNotBlank() && distThurs * 1.609f > item.Thurs.toDouble()) {
                            holder.tvThurs.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvThurs.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Friday
                    6 -> {
                        distFri += run.distance

                        d("fri_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("fri_total", distFri.toString())
                        if (item.Fri.isNotBlank() && distFri * 1.609f > item.Fri.toDouble()) {
                            holder.tvFri.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvFri.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                    // Saturday
                    7 -> {
                        distSat += run.distance

                        d("sat_run", "distance: ${run.distance}, time: ${run.minutes}")
                        d("sat_total", distSat.toString())
                        if (item.Sat.isNotBlank() && distSat * 1.609f > item.Sat.toDouble()) {
                            holder.tvSat.setTextColor(Color.parseColor("#008000"))
                        } else {
                            holder.tvSat.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                }
            }
        }
    }

    private fun setVisibility(
        dayOfWeek: Int,
        holder: ViewHolder
    ) {
        // Sunday
        if (dayOfWeek == 1) {
            holder.tvMon.visibility = View.INVISIBLE
            holder.tvTues.visibility = View.INVISIBLE
            holder.tvWed.visibility = View.INVISIBLE
            holder.tvThurs.visibility = View.INVISIBLE
            holder.tvFri.visibility = View.INVISIBLE
            holder.tvSat.visibility = View.INVISIBLE
        }

        // all other days
        for (i in 2 until dayOfWeek) {
            when (i) {
                2 -> {
                    holder.tvMon.visibility = View.INVISIBLE
                }
                3 -> {
                    holder.tvTues.visibility = View.INVISIBLE
                }
                4 -> {
                    holder.tvWed.visibility = View.INVISIBLE
                }
                5 -> {
                    holder.tvThurs.visibility = View.INVISIBLE
                }
                6 -> {
                    holder.tvFri.visibility = View.INVISIBLE
                }
                7 -> {
                    holder.tvSat.visibility = View.INVISIBLE
                }
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