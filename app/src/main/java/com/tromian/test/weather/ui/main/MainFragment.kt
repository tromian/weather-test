package com.tromian.test.weather.ui.main

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.ui.MainActivity
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.MainFragmentBinding
import javax.inject.Inject

class MainFragment : Fragment(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    @Inject
    lateinit var repository: WeatherRepository

    private val viewModel by viewModels<MainViewModel> {
        ViewModelsFactory(repository)
    }
    val placeActivityLauncher = registerForActivityResult(PlaceActivityContract()) {
        if (it != null) {
            viewModel.updatePlace(place = it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)
        setupNavigation()
        setupToolbar()
        setHasOptionsMenu(true)
        val hostPalace = (activity as MainActivity).hostPlace
        viewModel.place.observe(viewLifecycleOwner, {
            toolbar.title = it.name
            hostPalace.postValue(it)
        })

    }

    private fun autoCompleteRun() {
        placeActivityLauncher.launch("")
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.today_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.today_toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            val id = it.itemId
            if (id == R.id.search_item) {
                autoCompleteRun()
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}