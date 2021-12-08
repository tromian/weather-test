package com.tromian.test.weather.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.tromian.test.weather.ui.activityViewModel
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.MainFragmentBinding

class MainFragment : Fragment(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    private val placeActivityLauncher = registerForActivityResult(PlaceActivityContract()) {
        if (it != null) {
            activityViewModel().updatePlace(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)
        setupNavigation()
        setupToolbar()
        setHasOptionsMenu(true)
    }

    private fun autoCompleteRun() {
        placeActivityLauncher.launch("")
    }

    private fun setupNavigation() {
        val bottomNavMenu: BottomNavigationView = binding.navView
        val railView: NavigationRailView = binding.navigationRail
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavMenu.setupWithNavController(navController)
        railView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.today_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.today_toolbar_menu)
        activityViewModel().place.observe(viewLifecycleOwner, {
            toolbar.title = it.name
        })
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