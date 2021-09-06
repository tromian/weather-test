package com.tromian.test.weather.ui.week

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.ui.MainActivity
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentWeekBinding
import javax.inject.Inject

class WeekFragment : Fragment(R.layout.fragment_week) {

    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: WeatherRepository

    private val weekViewModel: WeekViewModel by viewModels {
        ViewModelsFactory(repository)
    }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        WeekListAdapter() { itemId ->
            openFragment(itemId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as MainActivity).autocompletePlaceResult.observe(viewLifecycleOwner, {
            weekViewModel.loadWeeklyWeather(it)
        })
        val rvWeekList = binding.recyclerViewDaily
        rvWeekList.adapter = adapter
        weekViewModel.dailyWeatherList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFragment(itemId: Int) {
        val dailyWeather = weekViewModel.dailyWeatherList.value?.get(itemId)
        if (dailyWeather != null) {
            val action = WeekFragmentDirections.actionNavigationWeekToDetailsFragment(dailyWeather)
            findNavController().navigate(action)
        }
    }
}