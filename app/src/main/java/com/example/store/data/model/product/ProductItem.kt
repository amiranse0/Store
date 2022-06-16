package com.example.store.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.google.gson.annotations.SerializedName as SN

@Parcelize
data class ProductItem(
    val attributes: @RawValue List<Any>,
    @SN("average_rating")
    val averageRating: String,
    @SN("catalog_visibility")
    val catalogVisibility: String,
    val categories: @RawValue List<Category>,
    @SN("default_attributes")
    val defaultAttributes: @RawValue List<Any>,
    val description: String,
    val dimensions: @RawValue Dimensions,
    @SN("grouped_products")
    val groupedProducts: @RawValue List<Any>,
    val id: Int,
    val images: @RawValue List<Image>,
    val name: String,
    @SN("on_sale")
    val onSale: Boolean,
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
    @SN("short_description")
    val shortDescription: String,
    val sku: String,
    val slug: String,
    val status: String,
    @SN("stock_quantity")
    val stockQuantity: @RawValue Any,
    @SN("stock_status")
    val stockStatus: String,
    val tags: @RawValue List<Tag>,
    @SN("total_sales")
    val totalSales: Int,
    val type: String,
    @SN("upsell_ids")
    val upsellIds: @RawValue List<Any>,
    val variations: @RawValue List<Any>,
    val weight: String
) : Parcelable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductItem

        if (attributes != other.attributes) return false
        if (averageRating != other.averageRating) return false
        if (catalogVisibility != other.catalogVisibility) return false
        if (categories != other.categories) return false
        if (defaultAttributes != other.defaultAttributes) return false
        if (description != other.description) return false
        if (dimensions != other.dimensions) return false
        if (groupedProducts != other.groupedProducts) return false
        if (id != other.id) return false
        if (images != other.images) return false
        if (name != other.name) return false
        if (onSale != other.onSale) return false
        if (price != other.price) return false
        if (priceHtml != other.priceHtml) return false
        if (purchasable != other.purchasable) return false
        if (purchaseNote != other.purchaseNote) return false
        if (ratingCount != other.ratingCount) return false
        if (regularPrice != other.regularPrice) return false
        if (relatedIds != other.relatedIds) return false
        if (reviewsAllowed != other.reviewsAllowed) return false
        if (salePrice != other.salePrice) return false
        if (shortDescription != other.shortDescription) return false
        if (sku != other.sku) return false
        if (slug != other.slug) return false
        if (status != other.status) return false
        if (stockQuantity != other.stockQuantity) return false
        if (stockStatus != other.stockStatus) return false
        if (tags != other.tags) return false
        if (totalSales != other.totalSales) return false
        if (type != other.type) return false
        if (upsellIds != other.upsellIds) return false
        if (variations != other.variations) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        if (attributes.isNotEmpty())
            result = 31 * result + attributes.hashCode()
        if (averageRating.isNotBlank())
            result = 31 * result + averageRating.hashCode()
        if (catalogVisibility.isNotEmpty())
            result = 31 * result + catalogVisibility.hashCode()
        if (categories.isNotEmpty())
            result = 31 * result + categories.hashCode()
        if (defaultAttributes.isNotEmpty())
            result = 31 * result + defaultAttributes.hashCode()
        if (description.isNotEmpty())
            result = 31 * result + description.hashCode()
        result = 31 * result + dimensions.hashCode()
        if (groupedProducts.isNotEmpty())
            result = 31 * result + groupedProducts.hashCode()
        if (images.isNotEmpty())
            result = 31 * result + images.hashCode()
        if (name.isNotEmpty())
            result = 31 * result + name.hashCode()
        result = 31 * result + onSale.hashCode()
        if (price.isNotEmpty())
            result = 31 * result + price.hashCode()
        if (priceHtml.isNotEmpty())
            result = 31 * result + priceHtml.hashCode()
        result = 31 * result + purchasable.hashCode()
        if (purchaseNote.isNotEmpty())
            result = 31 * result + purchaseNote.hashCode()
        result = 31 * result + ratingCount.hashCode()
        if (regularPrice.isNotEmpty())
            result = 31 * result + regularPrice.hashCode()
        if (relatedIds.isNotEmpty())
            result = 31 * result + relatedIds.hashCode()
        result = 31 * result + reviewsAllowed.hashCode()
        if (salePrice.isNotEmpty())
            result = 31 * result + salePrice.hashCode()
        if (shortDescription.isNotEmpty())
            result = 31 * result + shortDescription.hashCode()
        if (sku.isNotEmpty())
            result = 31 * result + sku.hashCode()
        if (slug.isNotEmpty())
            result = 31 * result + slug.hashCode()
        if (status.isNotEmpty())
            result = 31 * result + status.hashCode()
        //result = 31 * result + stockQuantity.hashCode()
        if (stockStatus.isNotEmpty())
            result = 31 * result + stockStatus.hashCode()
        if (tags.isNotEmpty())
            result = 31 * result + tags.hashCode()
        result = 31 * result + totalSales
        if (type.isNotEmpty())
            result = 31 * result + type.hashCode()
        if (upsellIds.isNotEmpty())
            result = 31 * result + upsellIds.hashCode()
        if (variations.isNotEmpty())
            result = 31 * result + variations.hashCode()
        if (weight.isNotEmpty())
            result = 31 * result + weight.hashCode()
        return result
    }
}