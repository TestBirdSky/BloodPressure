package com.tele.core.observer

import com.tele.core.AppObserver
import com.tele.core.tools.edg
import com.tele.core.tools.g2

object ConfigObserver : AppObserver {
    var card = false
    var notify = false
    var out = true
    var log = true
    var detect = 60 * 1000L
    var interval = 60 * 1000L
    var first = 30 * 1000L
    var failed = 120
    var last = 0L

    override fun observe() {
        card = g2.contains("c")
        notify = g2.contains("n")
        out = g2.contains("w")
        log = g2.contains("r")
        val times = edg.split(">")
        detect = times[0].toInt() * 1000L
        interval = times[1].toInt() * 1000L
        first = times[2].toInt() * 1000L
        failed = times[3].toInt()
    }
}