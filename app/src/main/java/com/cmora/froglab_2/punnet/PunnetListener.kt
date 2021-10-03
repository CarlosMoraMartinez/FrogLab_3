package com.cmora.froglab_2.punnet

interface PunnetListener {
    fun onAnswer(right: Boolean)
    fun onEvent(event: Int, info:String)
}