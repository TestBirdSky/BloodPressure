package com.blood.bloodthings.bigPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.PApp
import com.blood.bloodthings.R
import com.blood.bloodthings.SweetData
import com.blood.bloodthings.adapter.FifthAdapter
import com.blood.bloodthings.beforeErrorSweet
import com.blood.bloodthings.currentSweetEntity
import com.blood.bloodthings.currentSweetMark
import com.blood.bloodthings.currentSweetUnit
import com.blood.bloodthings.databinding.ActivityFourthBinding
import com.blood.bloodthings.defaultMg
import com.blood.bloodthings.defaultMmol
import com.blood.bloodthings.errorSweet
import com.blood.bloodthings.formatPoint1
import com.blood.bloodthings.lowSweet
import com.blood.bloodthings.markStr1
import com.blood.bloodthings.mg2mol
import com.blood.bloodthings.mgUnit
import com.blood.bloodthings.mmolUnit
import com.blood.bloodthings.mol2mg
import com.blood.bloodthings.normalSweet
import com.blood.bloodthings.refreshSweetLine
import com.blood.bloodthings.showBottomSheetDialog
import com.blood.bloodthings.showSecondConfirm
import com.blood.bloodthings.sweetState1
import com.blood.bloodthings.sweetState2
import com.blood.bloodthings.sweetState3
import com.blood.bloodthings.sweetState4
import com.blood.bloodthings.toDate1

class FourthActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityFourthBinding

    private val typeAdapter: FifthAdapter by lazy {
        FifthAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nowTime: Long
        if (currentSweetEntity != null) {
            viewBinding.deleteBtn.visibility = View.VISIBLE
            nowTime = currentSweetEntity?.time ?: 0L
            viewBinding.edtNotes.setText(currentSweetEntity?.note)
            viewBinding.gluInput.setText(if (currentSweetUnit == mgUnit) currentSweetEntity?.dataMg.toString() else currentSweetEntity?.dataMMol.toString())
            currentSweetMark = currentSweetEntity?.mark.toString()
            viewBinding.markState.text = currentSweetMark
            refreshSweetLine()
            val current = if (currentSweetUnit == mgUnit) (currentSweetEntity?.dataMg
                ?: 0.0) else (currentSweetEntity?.dataMMol ?: 0.0)
            if (currentSweetUnit == mgUnit) {
                when {
                    current < lowSweet.mol2mg() -> {
                        typeAdapter.currentType = "1"
                        viewBinding.sweetLineTxt.text =
                            sweetState1 + ":< ${
                                lowSweet.mol2mg().formatPoint1()
                            } ${currentSweetUnit}"
                    }

                    current in lowSweet.mol2mg()..normalSweet.mol2mg() -> {
                        typeAdapter.currentType = "2"
                        viewBinding.sweetLineTxt.text =
                            sweetState2 + ":${lowSweet.mol2mg().formatPoint1()}-${
                                normalSweet.mol2mg().formatPoint1()
                            } ${currentSweetUnit}"
                    }

                    current > normalSweet.mol2mg() && current < beforeErrorSweet.mol2mg() -> {
                        typeAdapter.currentType = "3"
                        viewBinding.sweetLineTxt.text =
                            sweetState3 + ":${
                                normalSweet.mol2mg().formatPoint1()
                            }-${beforeErrorSweet.mol2mg().formatPoint1()} ${currentSweetUnit}"
                    }

                    current >= errorSweet.mol2mg() -> {
                        typeAdapter.currentType = "4"
                        viewBinding.sweetLineTxt.text =
                            sweetState4 + ":>= ${
                                errorSweet.mol2mg().formatPoint1()
                            } ${currentSweetUnit}"
                    }
                }
            } else {
                when {
                    current < lowSweet -> {
                        typeAdapter.currentType = "1"
                        viewBinding.sweetLineTxt.text =
                            sweetState1 + ":< ${lowSweet.formatPoint1()} ${currentSweetUnit}"
                    }

                    current in lowSweet..normalSweet -> {
                        typeAdapter.currentType = "2"
                        viewBinding.sweetLineTxt.text =
                            sweetState2 + ":${lowSweet.formatPoint1()}-${normalSweet.formatPoint1()} ${currentSweetUnit}"
                    }

                    current > normalSweet && current < beforeErrorSweet -> {
                        typeAdapter.currentType = "3"
                        viewBinding.sweetLineTxt.text =
                            sweetState3 + ":${normalSweet.formatPoint1()}-${beforeErrorSweet.formatPoint1()} ${currentSweetUnit}"
                    }

                    current >= errorSweet -> {
                        typeAdapter.currentType = "4"
                        viewBinding.sweetLineTxt.text =
                            sweetState4 + ":>= ${errorSweet.formatPoint1()} ${currentSweetUnit}"
                    }
                }
            }
            typeAdapter.notifyDataSetChanged()

        } else {
            viewBinding.deleteBtn.visibility = View.GONE
            nowTime = System.currentTimeMillis()
            currentSweetMark = markStr1
            viewBinding.markState.text = currentSweetMark
            refreshSweetLine()
        }


        with(viewBinding) {
            backBtn.setOnClickListener {
                finish()
            }
            deleteBtn.setOnClickListener {
                showSecondConfirm {
                    PApp.SweetDao.remove(currentSweetEntity?.id!!)
                    finish()
                }
            }
            with(stateRv) {
                adapter = typeAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            dateTxt.text = "Datetime:" + System.currentTimeMillis().toDate1()

            gluInput.doAfterTextChanged {
                if (it?.isNotEmpty() == true) {
                    val input = it.toString().toDoubleOrNull()
                    if (input != null && input > 200) {
                        gluInput.setText("200")
                        gluInput.setSelection(gluInput.text?.length ?: 0)
                    }
                }

                val current = if (it.toString().isEmpty()) {
                    if (currentSweetUnit == mgUnit) {
                        defaultMg
                    } else {
                        defaultMmol
                    }
                } else {
                    it.toString().toDouble()
                }
                if (currentSweetUnit == mgUnit) {
                    when {
                        current < lowSweet.mol2mg() -> {
                            typeAdapter.currentType = "1"
                            sweetLineTxt.text =
                                sweetState1 + ":< ${
                                    lowSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }

                        current in lowSweet.mol2mg()..normalSweet.mol2mg() -> {
                            typeAdapter.currentType = "2"
                            sweetLineTxt.text =
                                sweetState2 + ":${lowSweet.mol2mg().formatPoint1()}-${
                                    normalSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }

                        current > normalSweet.mol2mg() && current < beforeErrorSweet.mol2mg() -> {
                            typeAdapter.currentType = "3"
                            sweetLineTxt.text =
                                sweetState3 + ":${
                                    normalSweet.mol2mg().formatPoint1()
                                }-${beforeErrorSweet.mol2mg().formatPoint1()} ${currentSweetUnit}"
                        }

                        current >= errorSweet.mol2mg() -> {
                            typeAdapter.currentType = "4"
                            sweetLineTxt.text =
                                sweetState4 + ":>= ${
                                    errorSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }
                    }
                } else {
                    when {
                        current < lowSweet -> {
                            typeAdapter.currentType = "1"
                            sweetLineTxt.text =
                                sweetState1 + ":< ${lowSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current in lowSweet..normalSweet -> {
                            typeAdapter.currentType = "2"
                            sweetLineTxt.text =
                                sweetState2 + ":${lowSweet.formatPoint1()}-${normalSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current > normalSweet && current < beforeErrorSweet -> {
                            typeAdapter.currentType = "3"
                            sweetLineTxt.text =
                                sweetState3 + ":${normalSweet.formatPoint1()}-${beforeErrorSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current >= errorSweet -> {
                            typeAdapter.currentType = "4"
                            sweetLineTxt.text =
                                sweetState4 + ":>= ${errorSweet.formatPoint1()} ${currentSweetUnit}"
                        }
                    }
                }
                typeAdapter.notifyDataSetChanged()
            }
            switchImg.setOnClickListener {
                if (currentSweetUnit == mgUnit) {
                    currentSweetUnit = mmolUnit
                    gluInput.hint = mmolUnit
                } else {
                    currentSweetUnit = mgUnit
                    gluInput.hint = mgUnit
                }
                val current = if (gluInput.text.toString().isEmpty()) {
                    if (currentSweetUnit == mgUnit) {
                        defaultMg
                    } else {
                        defaultMmol
                    }
                } else {
                    gluInput.text.toString().toDouble()
                }
                if (currentSweetUnit == mgUnit) {
                    when {
                        current < lowSweet.mol2mg() -> {
                            typeAdapter.currentType = "1"
                            viewBinding.sweetLineTxt.text =
                                sweetState1 + ":< ${
                                    lowSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }

                        current in lowSweet.mol2mg()..normalSweet.mol2mg() -> {
                            typeAdapter.currentType = "2"
                            viewBinding.sweetLineTxt.text =
                                sweetState2 + ":${lowSweet.mol2mg().formatPoint1()}-${
                                    normalSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }

                        current > normalSweet.mol2mg() && current < beforeErrorSweet.mol2mg() -> {
                            typeAdapter.currentType = "3"
                            viewBinding.sweetLineTxt.text =
                                sweetState3 + ":${
                                    normalSweet.mol2mg().formatPoint1()
                                }-${beforeErrorSweet.mol2mg().formatPoint1()} ${currentSweetUnit}"
                        }

                        current >= errorSweet.mol2mg() -> {
                            typeAdapter.currentType = "4"
                            viewBinding.sweetLineTxt.text =
                                sweetState4 + ":>= ${
                                    errorSweet.mol2mg().formatPoint1()
                                } ${currentSweetUnit}"
                        }
                    }
                } else {
                    when {
                        current < lowSweet -> {
                            typeAdapter.currentType = "1"
                            viewBinding.sweetLineTxt.text =
                                sweetState1 + ":< ${lowSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current in lowSweet..normalSweet -> {
                            typeAdapter.currentType = "2"
                            viewBinding.sweetLineTxt.text =
                                sweetState2 + ":${lowSweet.formatPoint1()}-${normalSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current > normalSweet && current < beforeErrorSweet -> {
                            typeAdapter.currentType = "3"
                            viewBinding.sweetLineTxt.text =
                                sweetState3 + ":${normalSweet.formatPoint1()}-${beforeErrorSweet.formatPoint1()} ${currentSweetUnit}"
                        }

                        current >= errorSweet -> {
                            typeAdapter.currentType = "4"
                            viewBinding.sweetLineTxt.text =
                                sweetState4 + ":>= ${errorSweet.formatPoint1()} ${currentSweetUnit}"
                        }
                    }
                }
            }
            markBtn.setOnClickListener {
                showBottomSheetDialog(currentSweetMark) {
                    markState.text = currentSweetMark
                    refreshSweetLine()
                    val current = if (gluInput.text.toString().isEmpty()) {
                        if (currentSweetUnit == mgUnit) {
                            defaultMg
                        } else {
                            defaultMmol
                        }
                    } else {
                        gluInput.text.toString().toDouble()
                    }
                    if (currentSweetUnit == mgUnit) {
                        when {
                            current < lowSweet.mol2mg() -> {
                                typeAdapter.currentType = "1"
                                viewBinding.sweetLineTxt.text =
                                    sweetState1 + ":< ${
                                        lowSweet.mol2mg().formatPoint1()
                                    } ${currentSweetUnit}"
                            }

                            current in lowSweet.mol2mg()..normalSweet.mol2mg() -> {
                                typeAdapter.currentType = "2"
                                viewBinding.sweetLineTxt.text =
                                    sweetState2 + ":${lowSweet.mol2mg().formatPoint1()}-${
                                        normalSweet.mol2mg().formatPoint1()
                                    } ${currentSweetUnit}"
                            }

                            current > normalSweet.mol2mg() && current < beforeErrorSweet.mol2mg() -> {
                                typeAdapter.currentType = "3"
                                viewBinding.sweetLineTxt.text =
                                    sweetState3 + ":${
                                        normalSweet.mol2mg().formatPoint1()
                                    }-${
                                        beforeErrorSweet.mol2mg().formatPoint1()
                                    } ${currentSweetUnit}"
                            }

                            current >= errorSweet.mol2mg() -> {
                                typeAdapter.currentType = "4"
                                viewBinding.sweetLineTxt.text =
                                    sweetState4 + ":>= ${
                                        errorSweet.mol2mg().formatPoint1()
                                    } ${currentSweetUnit}"
                            }
                        }
                    } else {
                        when {
                            current < lowSweet -> {
                                typeAdapter.currentType = "1"
                                viewBinding.sweetLineTxt.text =
                                    sweetState1 + ":< ${lowSweet.formatPoint1()} ${currentSweetUnit}"
                            }

                            current in lowSweet..normalSweet -> {
                                typeAdapter.currentType = "2"
                                viewBinding.sweetLineTxt.text =
                                    sweetState2 + ":${lowSweet.formatPoint1()}-${normalSweet.formatPoint1()} ${currentSweetUnit}"
                            }

                            current > normalSweet && current < beforeErrorSweet -> {
                                typeAdapter.currentType = "3"
                                viewBinding.sweetLineTxt.text =
                                    sweetState3 + ":${normalSweet.formatPoint1()}-${beforeErrorSweet.formatPoint1()} ${currentSweetUnit}"
                            }

                            current >= errorSweet -> {
                                typeAdapter.currentType = "4"
                                viewBinding.sweetLineTxt.text =
                                    sweetState4 + ":>= ${errorSweet.formatPoint1()} ${currentSweetUnit}"
                            }
                        }
                    }
                    typeAdapter.notifyDataSetChanged()
                }
            }
            saveBtn.setOnClickListener {
                if (gluInput.text.toString().isNullOrEmpty()) {
                    Toast.makeText(this@FourthActivity, "Please enter data!", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                val title = when (typeAdapter.currentType) {
                    "1" -> {
                        sweetState1
                    }

                    "2" -> {
                        sweetState2
                    }

                    "3" -> {
                        sweetState3
                    }

                    else -> {
                        sweetState4
                    }
                }
                if (currentSweetEntity != null) {
                    val pressure = PApp.SweetDao.get(currentSweetEntity!!.id)
                    if (pressure != null) {
                        PApp.SweetDao.put(
                            SweetData(
                                id = currentSweetEntity!!.id,
                                title = title,
                                time = nowTime,
                                dataMg = if (currentSweetUnit == mgUnit) gluInput.text.toString()
                                    .toDouble() else gluInput.text.toString().toDouble().mol2mg(),
                                dataMMol = if (currentSweetUnit == mmolUnit) gluInput.text.toString()
                                    .toDouble() else gluInput.text.toString().toDouble().mg2mol(),
                                note = edtNotes.text.toString(),
                                mark = currentSweetMark
                            )
                        )
                    }
                } else {
                    PApp.SweetDao.put(
                        SweetData(
                            title = title,
                            time = nowTime,
                            dataMg = if (currentSweetUnit == mgUnit) gluInput.text.toString()
                                .toDouble() else gluInput.text.toString().toDouble().mol2mg(),
                            dataMMol = if (currentSweetUnit == mmolUnit) gluInput.text.toString()
                                .toDouble() else gluInput.text.toString().toDouble().mg2mol(),
                            note = edtNotes.text.toString(),
                            mark = currentSweetMark
                        )
                    )
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentSweetEntity = null
    }
}