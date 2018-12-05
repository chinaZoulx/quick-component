package org.quick.component.sample.callback

import kotlin.math.abs

abstract class OnRequest<in T> {
    abstract fun onRespone(value: T?)

    fun getClassT(){
        javaClass
    }
}