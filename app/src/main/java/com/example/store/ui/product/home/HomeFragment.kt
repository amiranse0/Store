package com.example.store.ui.product.home

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.HomeProductBinding
import com.example.store.databinding.NotificationDialogBinding
import com.example.store.ui.product.home.slider.SpecialAdaptor
import com.example.store.workers.NotificationWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_product) {


    private lateinit var binding: HomeProductBinding

    private lateinit var mainBestRecyclerView: RecyclerView
    private lateinit var mainFavouriteRecyclerView: RecyclerView
    private lateinit var mainLatestRecyclerView: RecyclerView

    private lateinit var bestAdaptor: MainRowHomeAdaptor
    private lateinit var favouriteAdaptor: MainRowHomeAdaptor
    private lateinit var latestAdaptor: MainRowHomeAdaptor

    private lateinit var sliderAdaptor: SpecialAdaptor

    private val viewModel by viewModels<HomeViewModel>()
    var currentPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeProductBinding.bind(view)

        initValues()

        getLatestProduct()
        getBestProduct()
        getFavouriteProduct()

        putDataInRecyclerView()
        goToDetail()
        goToSeeAll()

        slider()

        goToSearchFragment()

        showNotification()

    }

    private fun showNotification() {
        binding.notificationCustomize.setOnClickListener {

            val bindingDialog = NotificationDialogBinding.inflate(layoutInflater)

            val notifDialogFragment =
                Dialog(requireContext(), androidx.transition.R.style.Base_ThemeOverlay_AppCompat)
            notifDialogFragment.setContentView(bindingDialog.root)
            notifDialogFragment.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            notifDialogFragment.show()

            bindingDialog.dismissBtn.setOnClickListener {
                notifDialogFragment.dismiss()
            }

            bindingDialog.submitBtn.setOnClickListener {
                var activeNotification = false
                if (bindingDialog.notificationSwitch.isChecked) {
                    activeNotification = true
                }

                var timePeriod = when {
                    bindingDialog.notifRb3.isChecked -> 3
                    bindingDialog.notifRb5.isChecked -> 5
                    bindingDialog.notifRb8.isChecked -> 8
                    bindingDialog.notifRb12.isChecked -> 12
                    else -> 8
                }

                setNotification(activeNotification, timePeriod)
            }

        }
    }

    private fun setNotification(activeNotification: Boolean, timePeriod: Int) {
        if (activeNotification) {
            val constraints = Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)
                .build()

            val myRequest = PeriodicWorkRequest.Builder(
                NotificationWorker::class.java,
                1,
                TimeUnit.MINUTES
            ).setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork(
                    "my_id",
                    ExistingPeriodicWorkPolicy.KEEP,
                    myRequest
                )
        } else{

        }
    }


    private fun goToSearchFragment() {
        binding.homeSearchCardView.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun goToSeeAll() {
        binding.seeAllBestTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bestProductFragment)
        }
        binding.seeAllFavouriteTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favouriteProductFragment)
        }
        binding.seeAllLatestTv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newProductFragment)
        }
    }

    private fun goToDetail() {
        bestAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = bestAdaptor.oldList[position]

                val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(item)
                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        favouriteAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = favouriteAdaptor.oldList[position]

                val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(item)
                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        latestAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = latestAdaptor.oldList[position]

                val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(item)

                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }

        })
    }

    private fun initValues() {
        mainBestRecyclerView = binding.bestProductRc
        mainFavouriteRecyclerView = binding.favouriteProductRc
        mainLatestRecyclerView = binding.latestProductRc

        bestAdaptor = MainRowHomeAdaptor()
        favouriteAdaptor = MainRowHomeAdaptor()
        latestAdaptor = MainRowHomeAdaptor()
    }

    private fun putDataInRecyclerView() {
        mainBestRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainBestRecyclerView.adapter = bestAdaptor

        mainFavouriteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainFavouriteRecyclerView.adapter = favouriteAdaptor

        mainLatestRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainLatestRecyclerView.adapter = latestAdaptor
    }

    private fun slider() {

        sliderAdaptor =
            SpecialAdaptor(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultSpecialOffersStateFlow.collect {
                    when (it) {
                        is Result.Success -> {
                            val images = it.data.images.map { it.src }

                            sliderAdaptor.images = images

                            sliderAdaptor.notifyDataSetChanged()

                            binding.specialOffersVp.adapter = sliderAdaptor

                            binding.tabLayout.setupWithViewPager(binding.specialOffersVp, true)

                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    Handler(Looper.getMainLooper()).post(Runnable {
                                        if (currentPage == images.size - 1) {
                                            currentPage = 0
                                        }
                                        binding.specialOffersVp.setCurrentItem(
                                            currentPage++,
                                            true
                                        )
                                    })
                                }
                            }, 1000, 4000)
                        }
                        is Result.Error -> {

                        }
                        is Result.Loading -> {
                        }
                    }
                }
            }
        }
    }


    private fun getFavouriteProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            favouriteAdaptor.setData(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun getBestProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            bestAdaptor.setData(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun getLatestProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lastProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                                View.INVISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                                View.VISIBLE
                        }
                        is Result.Success -> {
                            latestAdaptor.setData(it.data)
                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                                View.VISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                                View.INVISIBLE
                        }
                    }
                }
            }

        }
    }
}