package com.example.fitness_tracker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.fitness_tracker.data.Item
import kotlinx.android.synthetic.main.item_dialog.view.*
import java.lang.RuntimeException

class ItemDialog : DialogFragment() {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
    }

    interface ItemHandler {
        fun itemAdded(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException("Activity does not implement ItemHandler interface")
        }
    }

    private lateinit var etName: EditText
    private lateinit var etDate: EditText

    private lateinit var etMon: EditText
    private lateinit var etTues: EditText
    private lateinit var etWed: EditText
    private lateinit var etThurs: EditText
    private lateinit var etFri: EditText
    private lateinit var etSat: EditText
    private lateinit var etSun: EditText

    private var editMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Create new plan")
        val dialogView: View = requireActivity().layoutInflater.inflate(
            R.layout.item_dialog, null
        )

        etName = dialogView.etName
        etDate = dialogView.etDate

        etMon = dialogView.etMon
        etTues = dialogView.etTues
        etWed = dialogView.etWed
        etThurs = dialogView.etThurs
        etFri = dialogView.etFri
        etSat = dialogView.etSat
        etSun = dialogView.etSun

        builder.setView(dialogView)

        editMode = ((arguments != null) && arguments!!.containsKey(KEY_ITEM))

        if (editMode) {
            val item: Item = (arguments?.getSerializable(KEY_ITEM) as Item)
            etName.setText(item.name)
            etDate.setText(item.date)

            etMon.setText(item.Mon)
            etTues.setText(item.Tues)
            etWed.setText(item.Wed)
            etThurs.setText(item.Thurs)
            etFri.setText(item.Fri)
            etSat.setText(item.Sat)
            etSun.setText(item.Sun)
        }

        builder.setPositiveButton("OK") { _, _ ->
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton: Button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                if (editMode) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }
                dialog!!.dismiss()
            } else {
                etName.error = "This field cannot be empty"
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemAdded(
            Item(
                null,
                etName.text.toString(),
                etDate.text.toString(),

                etMon.text.toString(),
                etTues.text.toString(),
                etWed.text.toString(),
                etThurs.text.toString(),
                etFri.text.toString(),
                etSat.text.toString(),
                etSun.text.toString()
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit: Item = arguments?.getSerializable(KEY_ITEM) as Item
        itemToEdit.name = etName.text.toString()
        itemToEdit.date = etDate.text.toString()

        itemToEdit.Mon = etMon.text.toString()
        itemToEdit.Tues = etTues.text.toString()
        itemToEdit.Wed = etWed.text.toString()
        itemToEdit.Thurs = etThurs.text.toString()
        itemToEdit.Fri = etFri.text.toString()
        itemToEdit.Sat = etSat.text.toString()
        itemToEdit.Sun = etSun.text.toString()

        itemHandler.itemUpdated(itemToEdit)
    }

}