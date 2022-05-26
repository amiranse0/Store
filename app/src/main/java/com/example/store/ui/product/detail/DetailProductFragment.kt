package com.example.store.ui.product.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.FragmentDetailProductBinding
import com.example.store.ui.product.ProductAdapter
import java.util.ArrayList

class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private lateinit var binding: FragmentDetailProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailProductBinding.bind(view)

        createGallery()
    }

    private fun createGallery() {
        val images = arguments?.getStringArrayList("images")

        recyclerView = binding.galleryRc

        val list1 = arrayListOf(
            "https://i.picsum.photos/id/419/200/300.jpg?hmac=jvSs1zyCZ3ATdTlvdfcTKBBGcrgnCk3EAvZt352Fbco",
            "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"
        )

        if(images != null){
            recyclerAdaptor = GalleryAdapter(images)
        }
        else recyclerAdaptor = GalleryAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.adapter = recyclerAdaptor

        Log.d("TAG", "${images?.size}")
    }
}