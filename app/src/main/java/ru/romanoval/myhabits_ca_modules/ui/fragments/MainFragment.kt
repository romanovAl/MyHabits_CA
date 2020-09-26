package ru.romanoval.myhabits_ca_modules.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.bottom_sheet_main_fragment.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.R
import ru.romanoval.myhabits_ca_modules.core.Lists
import ru.romanoval.myhabits_ca_modules.ui.adapters.ViewPagerAdapter
import ru.romanoval.myhabits_ca_modules.ui.viewModels.MainViewModel
import javax.inject.Inject

class MainFragment : DaggerFragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val cm =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val netCap = cm.getNetworkCapabilities(activeNetwork)

        val isConnected =
            (netCap != null && netCap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

        return when (item.itemId) {
            R.id.uploadToServer -> {
                if (isConnected) {
                    viewModel.uploadToApi()
                    true
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                    true
                }

            }
            R.id.downloadFromServer -> {
                if (isConnected) {
                    viewModel.downloadFromApi()
                    true
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                    true
                }

            }
            else -> {
                if (bottomSheetMainFragment != null) {
                    val behavior = BottomSheetBehavior.from(bottomSheetMainFragment)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                true
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        curView = view

        init()

    }

    private fun init() {
        viewPager2.adapter = ViewPagerAdapter(this)

        val tabNames = listOf(
            this.resources.getString(R.string.view_pager_good),
            this.resources.getString(R.string.view_pager_bad)
        )

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        val filterTypes = Lists.getFilterTypes(requireContext())

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)



        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->

            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                mainFragmentConstraint.visibility = View.INVISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
                mainFragmentConstraint.visibility = View.VISIBLE
            }

        })

        viewModel.error.observe(viewLifecycleOwner, Observer {

            Toast.makeText(requireContext(), "ERROR - ${it.message}", Toast.LENGTH_LONG).show()

        })

        var sortedHabits: List<Habit>? = null

        var currentSortedHabits: List<Habit>? = null

        viewModel.setSortedHabits(emptyList())

        viewModel.habits?.observe(viewLifecycleOwner, Observer {
            sortedHabits = it
            currentSortedHabits = it
        })

        fabMainFragment.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragment3ToAddEditFragment(
                    this.resources.getString(R.string.label_add)
                )
            Navigation.findNavController(curView).navigate(action)
        }

        val behavior = BottomSheetBehavior.from(bottomSheetMainFragment)

        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        behavior.peekHeight = 0

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fabMainFragment.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset)
                    .setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.hideKeyboard()
                }
                fabMainFragment?.isClickable = newState == BottomSheetBehavior.STATE_COLLAPSED
            }
        })


        card.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        val adapterFilterTypes = context?.let { ArrayAdapter(it, R.layout.list_item, filterTypes) }
        (filterInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterFilterTypes)

        filterTypeSpinner.keyListener = null

        filterFind.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

                if (text != null) {

                    if (text.isEmpty()) {
                        currentSortedHabits = sortedHabits

                        if (currentSortedHabits != null) {
                            viewModel.setSortedHabits(currentSortedHabits!!)
                        }
                    } else {
                        currentSortedHabits = sortedHabits?.filter {
                            it.title.contains(text.toString(), ignoreCase = true)
                        }

                        if (currentSortedHabits != null) {
                            viewModel.setSortedHabits(currentSortedHabits!!)
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        filterSortUp.setOnClickListener { //Сортировка вверх

            filterSortUp.setImageResource(R.drawable.ic_baseline_arrow_upward_24_activated)
            filterSortDown.setImageResource(R.drawable.ic_baseline_arrow_downward_24)

            when (filterTypeSpinner.text.toString()) {

                filterTypes[0] -> { //По приоритету

                    currentSortedHabits = currentSortedHabits?.sortedByDescending { it.priority }
                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }

                }

                filterTypes[1] -> { //По периоду
                    currentSortedHabits = currentSortedHabits?.sortedByDescending { it.frequency }

                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }
                }

                filterTypes[2] -> { //По количеству раз
                    currentSortedHabits = currentSortedHabits?.sortedByDescending { it.count }

                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }

                }

                filterTypes[3] -> {//По дате
                    currentSortedHabits = currentSortedHabits?.sortedByDescending { it.date }

                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }
                }

            }
        }

        filterSortDown.setOnClickListener {

            filterSortDown.setImageResource(R.drawable.ic_baseline_arrow_downward_24_activated)
            filterSortUp.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

            when (filterTypeSpinner.text.toString()) {

                filterTypes[0] -> { //По приоритету

                    currentSortedHabits = currentSortedHabits?.sortedBy { it.priority }

                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }

                }

                filterTypes[1] -> { //По периоду
                    currentSortedHabits = currentSortedHabits?.sortedBy { it.frequency }
                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }
                }

                filterTypes[2] -> { //По количеству раз
                    currentSortedHabits = currentSortedHabits?.sortedBy { it.count }
                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }
                }

                filterTypes[3] -> {//По дате
                    currentSortedHabits = currentSortedHabits?.sortedBy { it.date }
                    if(currentSortedHabits != null){
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }
                }

            }
        }

        filterTypeSpinner.onItemClickListener = AdapterView.OnItemClickListener()
        {
                _, _, p, _ ->

            if (p == 4) {

                filterSortUp.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                filterSortDown.setImageResource(R.drawable.ic_baseline_arrow_downward_24)


                if (sortedHabits != null) {
                    currentSortedHabits = currentSortedHabits?.sortedBy { it.bdId }
                    if (currentSortedHabits != null) {
                        viewModel.setSortedHabits(currentSortedHabits!!)
                    }

                }
                //TODO сделать поле неактивным

            }

        }

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}