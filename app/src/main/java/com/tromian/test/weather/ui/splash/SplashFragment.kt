package com.tromian.test.weather.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)
        loadSplash()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadSplash() {
        binding.progressBar.display
        lifecycleScope.launchWhenStarted {
            delay(3000)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

}