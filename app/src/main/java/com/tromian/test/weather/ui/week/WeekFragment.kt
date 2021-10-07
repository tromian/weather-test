package com.tromian.test.weather.ui.week

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.daily.DailyWeather
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.weather.ui.activityViewModel
import com.tromian.test.wether.NavGraphApplicationDirections
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentWeekBinding
import javax.inject.Inject

class WeekFragment : Fragment(R.layout.fragment_week) {

    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: WeatherRepository

    private val viewModel: WeekViewModel by viewModels {
        ViewModelsFactory(repository)
    }
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        WeekListAdapter() { itemId ->
            openFragment(itemId)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rvWeekList = binding.recyclerViewDaily
        rvWeekList.adapter = adapter
        setupObservers()
        return root
    }

    private fun setupObservers() {
        activityViewModel().place.observe(viewLifecycleOwner, {
            viewModel.loadWeeklyWeather(it)
        })
        viewModel.dailyWeatherList.observe(viewLifecycleOwner, { result ->
            when (result) {
                is PendingResult -> showPending()
                is ErrorResult -> showError()
                is SuccessResult -> showSuccess(result.data)
            }
        })
    }

    private fun showSuccess(data: List<DailyWeather>) {
        binding.root.children.forEach {
            if (it.id == R.id.error_layout || it.id == R.id.progressBarWeek) {
                binding.errorLayout?.root?.visibility = View.GONE
                it.visibility = View.GONE
            } else it.visibility = View.VISIBLE
        }
        adapter.submitList(data)
    }

    private fun showError() {
        binding.root.children.forEach {
            if (it.id == R.id.error_layout) {
                binding.errorLayout?.root?.visibility = View.VISIBLE
                binding.errorLayout?.btnRetry?.setOnClickListener {
                    val place = activityViewModel().place.value ?: return@setOnClickListener
                    viewModel.loadWeeklyWeather(place)
                }
            } else it.visibility = View.GONE
        }
    }

    private fun showPending() {
        binding.root.children.forEach {
            if (it.id == R.id.progressBarWeek) {
                it.visibility = View.VISIBLE
            } else it.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFragment(itemId: Int) {
        viewModel.dailyWeatherList.value?.map {
            val dailyWeather = it[itemId]
            val action = NavGraphApplicationDirections.actionGlobalDetailsFragment(dailyWeather)
            requireActivity().findNavController(R.id.main_activity_container_view).navigate(action)
        }
    }
}