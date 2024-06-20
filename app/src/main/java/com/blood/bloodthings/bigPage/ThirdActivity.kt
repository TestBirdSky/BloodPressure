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
import com.blood.bloodthings.PressureData
import com.blood.bloodthings.R
import com.blood.bloodthings.adapter.FourthAdapter
import com.blood.bloodthings.currentPressureEntity
import com.blood.bloodthings.databinding.ActivityThirdBinding
import com.blood.bloodthings.defaultDia
import com.blood.bloodthings.defaultPul
import com.blood.bloodthings.defaultSys
import com.blood.bloodthings.getPressureState
import com.blood.bloodthings.getType
import com.blood.bloodthings.getTypeDesc
import com.blood.bloodthings.loadState
import com.blood.bloodthings.showSecondConfirm
import com.blood.bloodthings.toDate1
import com.blood.bloodthings.toDate2

class ThirdActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityThirdBinding

    private val typeAdapter: FourthAdapter by lazy {
        FourthAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(viewBinding) {
            with(colorRv) {
                adapter = typeAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }

        val nowTime: Long
        if (currentPressureEntity != null) {
            viewBinding.deleteBtn.visibility = View.VISIBLE
            nowTime = currentPressureEntity?.time ?: 0L
            viewBinding.sysInput.setText(currentPressureEntity?.sys.toString())
            viewBinding.diaInput.setText(currentPressureEntity?.dia.toString())
            viewBinding.pulInput.setText(currentPressureEntity?.pul.toString())
            viewBinding.edtNotes.setText(currentPressureEntity?.note)

            val type = getPressureState(
                currentPressureEntity?.sys!!.toInt(),
                currentPressureEntity?.dia!!.toInt()
            )
            viewBinding.colorTxt.getType(type)
            viewBinding.stateTxt.getTypeDesc(type)
            viewBinding.colorPoint.loadState(type)
            typeAdapter.currentType = type
            typeAdapter.notifyDataSetChanged()
        } else {
            viewBinding.deleteBtn.visibility = View.GONE
            nowTime = System.currentTimeMillis()
        }

        with(viewBinding) {
            backBtn.setOnClickListener {
                finish()
            }
            dateTxt.text = "Datetime:" + nowTime.toDate1()
            colorDateTxt.text = nowTime.toDate2()


            sysInput.doAfterTextChanged {
                if (it?.isNotEmpty() == true) {
                    val input = it.toString().toDoubleOrNull()
                    if (input != null && input > 200) {
                        sysInput.setText("200")
                        sysInput.setSelection(sysInput.text?.length ?: 0)
                    }
                }

                val sys = if (it.toString().isEmpty()) defaultSys else it.toString().toInt()
                val dia =
                    if (diaInput.text.toString().isEmpty()) defaultDia else diaInput.text.toString()
                        .toInt()
                val type = getPressureState(sys, dia)
                colorTxt.getType(type)
                stateTxt.getTypeDesc(type)
                colorPoint.loadState(type)
                typeAdapter.currentType = type
                typeAdapter.notifyDataSetChanged()
            }
            diaInput.doAfterTextChanged {
                if (it?.isNotEmpty() == true) {
                    val input = it.toString().toDoubleOrNull()
                    if (input != null && input > 150) {
                        diaInput.setText("150")
                        diaInput.setSelection(diaInput.text?.length ?: 0)
                    }
                }
                val sys =
                    if (sysInput.text.toString().isEmpty()) defaultSys else sysInput.text.toString()
                        .toInt()
                val dia = if (it.toString().isEmpty()) defaultDia else it.toString().toInt()
                val type = getPressureState(sys, dia)
                colorTxt.getType(type)
                stateTxt.getTypeDesc(type)
                colorPoint.loadState(type)
                typeAdapter.currentType = type
                typeAdapter.notifyDataSetChanged()
            }
            pulInput.doAfterTextChanged {
                if (it?.isNotEmpty() == true) {
                    val input = it.toString().toDoubleOrNull()
                    if (input != null && input > 250) {
                        pulInput.setText("250")
                        pulInput.setSelection(pulInput.text?.length ?: 0)
                    }
                }
            }
            deleteBtn.setOnClickListener {
                showSecondConfirm {
                    PApp.PressDao.remove(currentPressureEntity?.id!!)
                    finish()
                }
            }
            saveBtn.setOnClickListener {
                if (sysInput.text.toString().isEmpty() && diaInput.text.toString()
                        .isEmpty() && pulInput.text.toString().isEmpty()
                ) {
                    Toast.makeText(this@ThirdActivity, "Please enter data!", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                if (currentPressureEntity != null) {
                    val pressure = PApp.PressDao.get(currentPressureEntity!!.id)
                    if (pressure != null) {
                        PApp.PressDao.put(
                            PressureData(
                                id = currentPressureEntity!!.id,
                                title = colorTxt.text.toString(),
                                time = nowTime,
                                sys = if (sysInput.text.toString()
                                        .isNullOrEmpty()
                                ) defaultSys.toString() else sysInput.text.toString(),
                                dia = if (diaInput.text.toString()
                                        .isNullOrEmpty()
                                ) defaultDia.toString() else diaInput.text.toString(),
                                pul = if (pulInput.text.toString()
                                        .isNullOrEmpty()
                                ) defaultPul.toString() else pulInput.text.toString(),
                                note = edtNotes.text.toString(),
                            )
                        )
                    }
                } else {
                    PApp.PressDao.put(
                        PressureData(
                            title = colorTxt.text.toString(),
                            time = nowTime,
                            sys = if (sysInput.text.toString()
                                    .isNullOrEmpty()
                            ) defaultSys.toString() else sysInput.text.toString(),
                            dia = if (diaInput.text.toString()
                                    .isNullOrEmpty()
                            ) defaultDia.toString() else diaInput.text.toString(),
                            pul = if (pulInput.text.toString()
                                    .isNullOrEmpty()
                            ) defaultPul.toString() else pulInput.text.toString(),
                            note = edtNotes.text.toString(),
                        )
                    )
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPressureEntity = null
    }
}