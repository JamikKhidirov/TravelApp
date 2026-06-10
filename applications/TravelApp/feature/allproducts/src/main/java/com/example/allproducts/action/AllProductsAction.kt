package com.example.allproducts.action

import com.example.network.wegodata.productpopular.Tour

sealed interface AllProductsAction {
    object LoadMore : AllProductsAction
    data class OnTourClick(val tour: Tour) : AllProductsAction
    object Retry : AllProductsAction
}
