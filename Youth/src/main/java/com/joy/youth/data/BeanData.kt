package com.joy.youth.data

/**
 * Date：2024/6/17
 * Describe:
 */
data class BeanData(
    val status: String = "", val timePeriod: Long = 0L, val tWait: Long = 0L, val timeC: Long = 0L
) {

    fun isInfoGo(lastShowTime: Long): Boolean {
        if (status.contains("youth_pop")) return false
        if (System.currentTimeMillis() - lastShowTime < timePeriod) return false
        return true
    }

}
