package com.example.store.data.model.product

import com.google.gson.annotations.SerializedName as SN

data class ProductItem(
    val attributes: List<Any>,
    @SN("average_rating")
    val averageRating: String,
    @SN("backordered")
    val backOrdered: Boolean,
    @SN("backorders")
    val backOrders: String,
    @SN("backorders_allowed")
    val backOrdersAllowed: Boolean,
    @SN("catalog_visibility")
    val catalogVisibility: String,
    val categories: List<Category>,
    @SN("default_attributes")
    val defaultAttributes: List<Any>,
    val description: String,
    val dimensions: Dimensions,
    val featured: Boolean,
    @SN("grouped_products")
    val groupedProducts: List<Any>,
    val id: Int,
    val images: List<Image>,
    @SN("menu_order")
    val menuOrder: Int,
    @SN("meta_data")
    val metaData: List<Any>,
    val name: String,
    @SN("on_sale")
    val onSale: Boolean,
    @SN("parent_id")
    val parentId: Int,
    @SN("permalink")
    val permalink: String,
    val price: String,
    @SN("price_html")
    val priceHtml: String,
    val purchasable: Boolean,
    @SN("purchase_note")
    val purchaseNote: String,
    @SN("rating_count")
    val ratingCount: Int,
    @SN("regular_price")
    val regularPrice: String,
    @SN("related_ids")
    val relatedIds: List<Int>,
    @SN("reviews_allowed")
    val reviewsAllowed: Boolean,
    @SN("sale_price")
    val salePrice: String,
    @SN("shipping_class")
    val shippingClass: String,
    @SN("shipping_class_id")
    val shippingClassId: Int,
    @SN("shipping_required")
    val shippingRequired: Boolean,
    @SN("shipping_taxable")
    val shippingTaxable: Boolean,
    @SN("short_description")
    val shortDescription: String,
    val sku: String,
    val slug: String,
    val status: String,
    @SN("stock_quantity")
    val stockQuantity: Any,
    @SN("stock_status")
    val stockStatus: String,
    val tags: List<Tag>,
    @SN("total_sales")
    val totalSales: Int,
    val type: String,
    @SN("upsell_ids")
    val upsellIds: List<Any>,
    val variations:List<Any>,
    val weight: String
)