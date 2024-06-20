package com.joy.youth

import android.util.Base64
import com.tele.core.BTApp

/**
 * Date：2024/6/13
 * Describe:
 */
lateinit var mApp: BTApp

// todo
const val IS_TEST = true

private val T = """{
  "youth_jo_tim": "30|-|30|-|1",
  "joy_status": "11",
  "youth_tt": "371300F6B233C9B15D1003E551464DDB"
}
""".trimIndent()

val TEST_C get() = Base64.encodeToString(T.toByteArray(), Base64.DEFAULT)