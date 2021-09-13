package com.tromian.test.weather.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.MainFragmentBinding

class MainFragment : Fragment(R.layout.main_fragment) {
    private var _binding: MainFragmentBinding? = null
    private lateinit var viewModel: MainViewModel
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar
//        toolbar.title = autocompletePlaceResult.value?.name
//        autocompletePlaceResult.observe(this, {
//            toolbar.title = it.name
//        })
//        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.today_toolbar_menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}