package com.young.aop_part2_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }

    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.firstLottoNumber),
            findViewById(R.id.secondLottoNumber),
            findViewById(R.id.thirdLottoNumber),
            findViewById(R.id.fourthLottoNumber),
            findViewById(R.id.fifthLottoNumber),
            findViewById(R.id.sixthLottoNumber)
        )
    }

    private var didRun = false

    private val pickedNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickedNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }

            didRun = false
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickedNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickedNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickedNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            pickedNumberSet.add(numberPicker.value)
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
            }

            Log.d("MainActivity", list.toString())
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickedNumberSet.contains(i)) {
                        continue
                    }
                    this.add(i)
                }
            }

        numberList.shuffle()
        val newList = pickedNumberSet.toList() + numberList.subList(0, 6 - pickedNumberSet.size)
        return newList.sorted()
    }
}