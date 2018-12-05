package org.quick.component.sample.callback

interface Callback<T : Class<T>> {

    fun onRespone(value:T?)
}