package com.example.colormaker

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    private var boxColor = intArrayOf(255, 0, 0)
    private lateinit var ResultColor: TextView
    private val seekBarIds = arrayOf(R.id.seekBar, R.id.seekBar2, R.id.seekBar3)
    private val textViewIds = arrayOf(R.id.textView, R.id.textView2, R.id.textView3)
    private val switchIds = arrayOf(R.id.switch1, R.id.switch2, R.id.switch3)
    private val memColor = intArrayOf(0, 0, 0)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ResultColor = findViewById<TextView>(R.id.Rclr)

        seekBarIds.forEachIndexed { index, sbId ->
            val mySeekBar = findViewById<SeekBar>(sbId)
            mySeekBar.isEnabled = false
            mySeekBar.setOnSeekBarChangeListener(createSeekBarListener(index))
        }

        switchIds.forEachIndexed { index, switchId ->
            val switchButton = findViewById<Switch>(switchId)
            switchButton.isChecked = false
            switchButton.setOnCheckedChangeListener(createSwitchListener(index))
        }
        for (i in 0..2){
            var rsed=resources.getIdentifier(textViewIds[i].toString(), "id", packageName)
            var edt=findViewById<View>(rsed) as EditText
            edt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                    var rs=resources.getIdentifier(seekBarIds[i].toString(), "id", packageName)
                    var isb=findViewById<View>(rs) as SeekBar
                    try {
                        var stt=s.toString().toFloat()
                        if (stt>1){
                            stt=1F

                            val message = "Max Value is 1!"
                            val duration = Toast.LENGTH_SHORT // or Toast.LENGTH_LONG
                            val toast = Toast.makeText(applicationContext, message, duration)
                            edt.setText("1.00")
                            toast.show()

                        }
                        var rbgvalue=(stt*255).toInt()
                        boxColor[i]=rbgvalue
                        isb.setProgress(rbgvalue)
                        ResultColor?.setBackgroundColor(Color.argb(255, boxColor[0], boxColor[1], boxColor[2]))

                    }
                    catch (e:Exception){

                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {

                }
            })

        }
        val resetButton = findViewById<Button>(R.id.button)
        resetButton.setOnClickListener { reset() }
    }

    private fun createSeekBarListener(index: Int): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                boxColor[index] = progress
                memColor[index] = boxColor[index]
                val myTv = findViewById<TextView>(textViewIds[index])
                myTv.text = String.format("%.2f", progress.toDouble() / 255.0)
                ResultColor.setBackgroundColor(Color.argb(255, boxColor[0], boxColor[1], boxColor[2]))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    private fun createSwitchListener(index: Int): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val sb = findViewById<SeekBar>(seekBarIds[index])

            if (!isChecked) {
                sb.isEnabled=false
                memColor[index] = boxColor[index]
                boxColor[index] = 0
            } else {
                sb.isEnabled=true
                boxColor[index] = memColor[index]
            }
            ResultColor.setBackgroundColor(Color.argb(255, boxColor[0], boxColor[1], boxColor[2]))
        }.also { switchListener ->
            val switchButton = findViewById<Switch>(switchIds[index])
            switchButton.isChecked = false
            switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
                switchListener.onCheckedChanged(buttonView, isChecked)
                findViewById<SeekBar>(seekBarIds[index]).isEnabled = isChecked
            }
        }
    }
    private fun reset() {
        boxColor = intArrayOf(0, 0, 0)
        ResultColor.setBackgroundColor(Color.argb(255, boxColor[0], boxColor[1], boxColor[2]))
        seekBarIds.forEach { findViewById<SeekBar>(it).progress = 0 }
        textViewIds.forEach { findViewById<TextView>(it).text = "0" }
    }
}

