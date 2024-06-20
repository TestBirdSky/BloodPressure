package com.blood.bloodthings

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.databinding.CardBloodBinding
import com.blood.bloodthings.databinding.CardSweetBinding
import com.blood.bloodthings.databinding.DialogMarkBinding
import com.blood.bloodthings.databinding.ItemPressureTypeBinding
import com.blood.bloodthings.databinding.ItemSweetTypeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date


class PressureVh(val bView: CardBloodBinding) : RecyclerView.ViewHolder(bView.root)

class SweetVh(val bView: CardSweetBinding) : RecyclerView.ViewHolder(bView.root)

class PressureTypeVh(val bView: ItemPressureTypeBinding) : RecyclerView.ViewHolder(bView.root)

class SweetTypeVh(val bView: ItemSweetTypeBinding) : RecyclerView.ViewHolder(bView.root)

fun Long.toDate1(): String {
    return SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date(this))
}

fun Long.toDate2(): String {
    return SimpleDateFormat("yyyy.MM.dd HH:mm").format(Date(this))
}

fun Long.toDate3(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(this))
}

fun TextView.getType(type: String) {
    when (type) {
        "1" -> {
            this.text = bluePressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator1))
        }

        "2" -> {
            this.text = greenPressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator2))
        }

        "3" -> {
            this.text = yellowPressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator3))
        }

        "4" -> {
            this.text = orangePressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator4))
        }

        "5" -> {
            this.text = lighterRedPressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator5))
        }

        "6" -> {
            this.text = redPressureStateStr
            this.setTextColor(ContextCompat.getColor(context, R.color.startIndicator6))
        }
    }
}


fun TextView.getTypeDesc(type: String) {
    when (type) {
        "1" -> {
            this.text = bluePressureStr
        }

        "2" -> {
            this.text = greenPressureStr
        }

        "3" -> {
            this.text = yellowPressureStr
        }

        "4" -> {
            this.text = orangePressureStr
        }

        "5" -> {
            this.text = lighterRedPressureStr
        }

        "6" -> {
            this.text = redPressureStr
        }
    }
}

fun View.loadState(type: String) {
    when (type) {
        "1" -> this.setBackgroundResource(R.drawable.circle_bg_1)
        "2" -> this.setBackgroundResource(R.drawable.circle_bg_2)
        "3" -> this.setBackgroundResource(R.drawable.circle_bg_3)
        "4" -> this.setBackgroundResource(R.drawable.circle_bg_4)
        "5" -> this.setBackgroundResource(R.drawable.circle_bg_5)
        "6" -> this.setBackgroundResource(R.drawable.circle_bg_6)
    }
}

fun getPressureState(sys: Int, dia: Int): String {
    return if (sys >= 180 || dia >= 120) {
        "6"
    } else if ((sys in 140..180) || (dia in 90..120)) {
        "5"
    } else if ((sys in 130..139) || (dia in 80..89)) {
        "4"
    } else if ((sys in 120..129) && dia < 80) {
        "3"
    } else if (sys < 120 && sys >= 90 && dia < 80 && dia >= 60) {
        "2"
    } else if (sys < 90 || dia < 60) {
        "1"
    } else {
        "1"
    }
}


fun Activity.showSecondConfirm(action: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage("Confirm delete?")
        .setPositiveButton(
            "Yes"
        ) { _, _ -> action?.invoke() }
        .setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.dismiss() }
        .show()
}


fun Activity.showBottomSheetDialog(mark: String, refresh: (() -> Unit)? = null) {
    val bottomSheetDialog = BottomSheetDialog(this)
    val view = DialogMarkBinding.inflate(LayoutInflater.from(this), null, false)

    with(view) {
        val grayTxt = ContextCompat.getColor(this@showBottomSheetDialog, R.color.unMark)
        val whiteTxt = ContextCompat.getColor(this@showBottomSheetDialog, R.color.white)

        mark1.setTextColor(grayTxt)
        mark1.setBackgroundResource(R.drawable.gray_btn_bg)
        mark2.setTextColor(grayTxt)
        mark2.setBackgroundResource(R.drawable.gray_btn_bg)
        mark3.setTextColor(grayTxt)
        mark3.setBackgroundResource(R.drawable.gray_btn_bg)
        mark4.setTextColor(grayTxt)
        mark4.setBackgroundResource(R.drawable.gray_btn_bg)
        mark5.setTextColor(grayTxt)
        mark5.setBackgroundResource(R.drawable.gray_btn_bg)
        mark6.setTextColor(grayTxt)
        mark6.setBackgroundResource(R.drawable.gray_btn_bg)
        mark7.setTextColor(grayTxt)
        mark7.setBackgroundResource(R.drawable.gray_btn_bg)
        mark8.setTextColor(grayTxt)
        mark8.setBackgroundResource(R.drawable.gray_btn_bg)
        when (mark) {
            mark1.text -> {
                mark1.setTextColor(
                    whiteTxt
                )
                mark1.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark2.text -> {
                mark2.setTextColor(
                    whiteTxt
                )
                mark2.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark3.text -> {
                mark3.setTextColor(
                    whiteTxt
                )
                mark3.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark4.text -> {
                mark4.setTextColor(
                    whiteTxt
                )
                mark4.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark5.text -> {
                mark5.setTextColor(
                    whiteTxt
                )
                mark5.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark6.text -> {
                mark6.setTextColor(
                    whiteTxt
                )
                mark6.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark7.text -> {
                mark7.setTextColor(
                    whiteTxt
                )
                mark7.setBackgroundResource(R.drawable.green_btn_bg)
            }

            mark8.text -> {
                mark8.setTextColor(
                    whiteTxt
                )
                mark8.setBackgroundResource(R.drawable.green_btn_bg)
            }
        }
        mark1.setOnClickListener {
            currentSweetMark = mark1.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark2.setOnClickListener {
            currentSweetMark = mark2.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark3.setOnClickListener {
            currentSweetMark = mark3.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark4.setOnClickListener {
            currentSweetMark = mark4.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark5.setOnClickListener {
            currentSweetMark = mark5.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark6.setOnClickListener {
            currentSweetMark = mark6.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark7.setOnClickListener {
            currentSweetMark = mark7.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
        mark8.setOnClickListener {
            currentSweetMark = mark8.text.toString()
            refresh?.invoke()
            bottomSheetDialog.dismiss()
        }
    }
    bottomSheetDialog.setContentView(view.root)
    bottomSheetDialog.show()
}


val defaultSys = 100
val defaultDia = 70
val defaultPul = 70

val bluePressureStateStr = "Hypotension"
val greenPressureStateStr = "Normal"
val yellowPressureStateStr = "Elevated"
val orangePressureStateStr = "Hypertension Stage1"
val lighterRedPressureStateStr = "Hypertension Stage2"
val redPressureStateStr = "Hypertension Crisis"

val bluePressureStr = "Please contact your doctor."
val greenPressureStr = "Your blood pressure is within the normal range."
val yellowPressureStr = "Please watch your blood pressure. It's a little high."
val orangePressureStr = "Please contact your doctor to schedule an appropriate medical examination."
val lighterRedPressureStr = "Please contact your doctor as soon as possible."
val redPressureStr = "Please call the hospital."


var currentPressureEntity: PressureData? = null
var currentSweetEntity: SweetData? = null


val sweetState1 = "Low"
val sweetState2 = "Normal"
val sweetState3 = "Prediabetes"
val sweetState4 = "Diabetes"


var currentSweetMark: String = "Normal"
var mgUnit = "mg/dl"
var mmolUnit = "mmol/l"
var currentSweetUnit: String = mgUnit
var defaultMg = 80.0
var defaultMmol = 4.4


var markStr1 = "Normal"
var markStr2 = "Fasting"
var markStr3 = "Before Meals"
var markStr4 = "1 Hour After Meals"
var markStr5 = "2 Hours After Meals"
var markStr6 = "Before Exercise"
var markStr7 = "After Exercise"
var markStr8 = "Sleep"

var lowSweet = 4.0
var normalSweet = 5.5
var beforeErrorSweet = 7.0
var errorSweet = 7.0

var spId = ""
var consoleId = ""
var gaid = ""

fun debugLog(content: String) {
    if (BuildConfig.DEBUG) {
        Log.d("debugPrint", content)
    }
}

fun Double.mg2mol(): Double {
    return this * 0.0555
}

fun Double.mol2mg(): Double {
    return this / 0.0555
}

fun Double.formatPoint1(): String {
    return String.format("%.1f", this)
}


fun refreshSweetLine() {
    when (currentSweetMark) {
        markStr1 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr2 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr3 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr4 -> {
            lowSweet = 4.0
            normalSweet = 7.8
            beforeErrorSweet = 8.5
            errorSweet = 8.5
        }

        markStr5 -> {
            lowSweet = 4.0
            normalSweet = 4.7
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr6 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr7 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }

        markStr8 -> {
            lowSweet = 4.0
            normalSweet = 5.5
            beforeErrorSweet = 7.0
            errorSweet = 7.0
        }
    }
}