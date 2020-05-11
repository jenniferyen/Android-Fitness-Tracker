package com.example.fitness_tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_tracker.adapter.PlansListAdapter
import com.example.fitness_tracker.data.AppDatabase
import com.example.fitness_tracker.data.Item
import kotlinx.android.synthetic.main.activity_plans.*

class PlansActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
        const val TAG_ITEM_DIALOG = "TAG_ITEM_DIALOG"
    }

    private lateinit var plansListAdapter: PlansListAdapter

    private var editIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)
        setSupportActionBar(toolbar)

        initRecyclerView()

        fabAddItem.setOnClickListener {
            showAddItemDialog()
        }

        fabDeleteAll.setOnClickListener {
            plansListAdapter.deleteAll()
        }
    }

    private fun initRecyclerView() {
        Thread {
            val plansList = AppDatabase.getInstance(this).plansListDAO().getAllItems()
            runOnUiThread {
                plansListAdapter = PlansListAdapter(this, plansList)
                recyclerPlansList.adapter = plansListAdapter
            }
        }.start()
    }

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    fun showEditItemDialog(itemToEdit: Item, index: Int) {
        editIndex = index
        val editDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToEdit)

        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    override fun itemAdded(item: Item) {
        Thread {
            item.itemId = AppDatabase.getInstance(this).plansListDAO().addItem(item)
            runOnUiThread {
                plansListAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this).plansListDAO().updateItem(item)
            runOnUiThread {
                plansListAdapter.updateItem(item, editIndex)
            }
        }.start()
    }

}
