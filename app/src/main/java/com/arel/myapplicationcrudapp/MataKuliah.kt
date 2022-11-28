package com.arel.myapplicationcrudapp

data class MataKuliah(
    val id: String,
    val nama: String,
    val sks: Int
){
    constructor(): this("", "", 0){

    }
}