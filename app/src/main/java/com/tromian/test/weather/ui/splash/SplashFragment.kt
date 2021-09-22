package com.tromian.test.weather.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: WeatherRepository
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelsFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

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
            delay(500)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

}