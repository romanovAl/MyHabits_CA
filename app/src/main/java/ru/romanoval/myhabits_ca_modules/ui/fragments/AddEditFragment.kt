package ru.romanoval.myhabits_ca_modules.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_edit.*
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.R
import ru.romanoval.myhabits_ca_modules.core.Lists
import ru.romanoval.myhabits_ca_modules.ui.viewModels.AddEditViewModel
import java.util.*
import javax.inject.Inject

class AddEditFragment : DaggerFragment(R.layout.fragment_add_edit) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AddEditViewModel
    private lateinit var priorities: List<String>
    private lateinit var periods: List<String>

    companion object {
        @JvmStatic
        fun newInstance() = AddEditFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AddEditViewModel::class.java)

        val habitToEdit = AddEditFragmentArgs.fromBundle(requireArguments()).habitToEdit

        priorities = resources.getStringArray(R.array.priorities).toList()
        periods = resources.getStringArray(R.array.periods).toList()

        if (habitToEdit == null) {
            initAdding()
        } else {
            initEditing(habitToEdit)
        }

    }

    private fun initAdding() {
        init()

        addAndEditFab.setOnClickListener {

            if (canAddOrEdit()) {
                val newHabit = Habit(
                    uid = null,
                    bdId = null,
                    title = habitNameAddAndEdit.text.toString(),
                    description = habitDescriptionAddAndEdit.text.toString(),
                    priority = if (habitPriorityAddAndEdit.text.toString().isNotEmpty()) {
                        priorities.indexOf(habitPriorityAddAndEdit.text.toString())
                    } else {
                        0
                    },
                    type = radioButtonGood.isChecked.toInt(),
                    count = if (habitDoneAddEdit.text.toString() == "") {
                        0
                    } else {
                        habitDoneAddEdit.text.toString().toInt()
                    },
                    frequency = if (habitPeriodAddAndEdit.text.toString().isNotEmpty()) {
                        periods.indexOf(habitPeriodAddAndEdit.text.toString())
                    } else {
                        2
                    },
                    color = 234,
                    date = Calendar.getInstance().time.time,
                    done_dates = mutableListOf()
                )

                viewModel.insertHabit(newHabit)
                it.hideKeyboard()
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun initEditing(habitToEdit: Habit) {
        addAndEditFab.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_check_24))

        habitPriorityAddAndEdit(
            priorities[habitToEdit.priority]
        )

        habitPeriodAddAndEdit(
            periods[habitToEdit.frequency]
        )

        habitNameAddAndEdit.setText(habitToEdit.title)
        habitDescriptionAddAndEdit.setText(habitToEdit.description)

        if (habitToEdit.type.toBoolean()) {
            radioButtonGood.isChecked = true
        } else {
            radioButtonBad.isChecked = true
        }

        habitDoneAddEdit.setText(habitToEdit.count.toString())

        init()
        initDeleteButton(habitToEdit)

        addAndEditFab.setOnClickListener {

            if (canAddOrEdit()) {
                val newHabit = Habit(
                    uid = habitToEdit.uid,
                    bdId = habitToEdit.bdId,
                    title = habitNameAddAndEdit.text.toString(),
                    description = habitDescriptionAddAndEdit.text.toString(),
                    priority = if(habitPriorityAddAndEdit.text.toString().isNotEmpty()){
                        priorities.indexOf(habitPriorityAddAndEdit.text.toString())
                    }else{
                        0
                    },
                    type = radioButtonGood.isChecked.toInt(),
                    count = if (habitDoneAddEdit.text.toString() == "") {
                        0
                    } else {
                        habitDoneAddEdit.text.toString().toInt()
                    },
                    frequency = if (habitPeriodAddAndEdit.text.toString().isNotEmpty()) {
                        periods.indexOf(habitPeriodAddAndEdit.text.toString())
                    } else {
                        2
                    },
                    color = 234,
                    date = habitToEdit.date,
                    done_dates = habitToEdit.done_dates
                )

                viewModel.updateHabit(newHabit)
                it.hideKeyboard()
                Navigation.findNavController(it).popBackStack()
            }


        }


    }

    private fun init() {

        val adapterPriority = context?.let { ArrayAdapter(it, R.layout.list_item, priorities) }
        (habitPriorityInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)
        habitPriorityAddAndEdit.keyListener = null

        val adapterPeriod = context?.let { ArrayAdapter(it, R.layout.list_item, periods) }
        (habitPeriodInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)
        habitPeriodAddAndEdit.keyListener = null

        addAndEditFab.setColorFilter(Color.argb(255, 255, 255, 255))

    }

    private fun canAddOrEdit(): Boolean {
        var isAble = true
        if (habitNameAddAndEdit.text?.length?.compareTo(0) == 0) {
            habitNameInputLayout.error = resources.getString(R.string.Field_must_not_be_empty)
            isAble = false
        } else {
            habitNameInputLayout.error = ""
        }
        if (habitDescriptionAddAndEdit.text?.length?.compareTo(0) == 0) {
            habitDescriptionInputLayout.error =
                resources.getString(R.string.Field_must_not_be_empty)
            isAble = false
        } else {
            habitDescriptionInputLayout.error = ""
        }
        if (habitPeriodAddAndEdit.text?.length?.compareTo(0) == 0) {
            habitPeriodInputLayout.error = resources.getString(R.string.Field_must_not_be_empty)
            isAble = false
        } else {
            habitPeriodInputLayout.error = ""
        }
        return isAble
    }

    private fun initDeleteButton(habit: Habit) {

        deleteButton.visibility = View.VISIBLE

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(resources.getString(R.string.deleting_habbit))

            builder.setMessage("""${resources.getString(R.string.are_you_sure_you_want_to_delete)} "${habit.title}" ? """)

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteHabit(habit)
                Navigation.findNavController(requireView()).popBackStack()
            }

            builder.setNeutralButton(resources.getString(R.string.no)) { _, _ -> }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private operator fun AutoCompleteTextView.invoke(priority: String) {
        setText(priority)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun Int.toBoolean(): Boolean {
        return this == 1
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}