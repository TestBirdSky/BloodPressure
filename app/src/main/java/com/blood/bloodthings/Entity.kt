package com.blood.bloodthings

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class PressureData(
    @Id var id: Long = 0,
    val title: String? = null,
    val time: Long? = null,
    val sys: String? = null,
    val dia: String? = null,
    val pul: String? = null,
    val note: String? = null,
    val mark: String? = null
)

@Entity
data class SweetData(
    @Id var id: Long = 0,
    val title: String? = null,
    val time: Long? = null,
    val dataMg: Double? = null,
    val dataMMol: Double? = null,
    val note: String? = null,
    val mark: String? = null
)