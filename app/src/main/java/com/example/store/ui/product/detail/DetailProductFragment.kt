package com.example.store.ui.product.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.databinding.FragmentDetailProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private lateinit var binding: FragmentDetailProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailProductBinding.bind(view)

        createGallery()
        detailProduct()
    }

    private fun detailProduct() {
        val title = arguments?.getString("title")
        binding.titleProduct.text = title

        var description = arguments?.getString("description")?:""
        description = description.replace("<[^>]*>".toRegex(), "")
        binding.descriptionProduct.text = getString(R.string.description_format, description)

        val price = arguments?.getString("price")
        binding.priceProduct.text = getString(R.string.price_format, price)
    }

    private fun createGallery() {
        val images = arguments?.getStringArrayList("images")

        recyclerView = binding.galleryRc

        if (images != null) {
            recyclerAdaptor = GalleryAdapter(images)
        } else recyclerAdaptor = GalleryAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.adapter = recyclerAdaptor
    }
}