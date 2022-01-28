package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var tvScreenInput: TextView? = null
    private var tvScreenOutput: TextView? = null
    var lastNumeric: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Input
        tvScreenInput = findViewById(R.id.tv_screen_operation)

        // Adding OnClickListener to operation buttons
        findViewById<Button>(R.id.btn_cls).setOnClickListener(this)
        findViewById<Button>(R.id.btn_dot).setOnClickListener(this)
        findViewById<Button>(R.id.btn_divide).setOnClickListener(this)
        findViewById<Button>(R.id.btn_mult).setOnClickListener(this)
        findViewById<Button>(R.id.btn_substract).setOnClickListener(this)
        findViewById<Button>(R.id.btn_add).setOnClickListener(this)
        findViewById<Button>(R.id.btn_equal).setOnClickListener(this)

        // Adding OnClickListener to the numbers
        for(i in 0..9) {
            val id: Int = this.resources.getIdentifier("btn_$i", "id", "com.example.calculator")
            findViewById<Button>(id).setOnClickListener(this)
        }

        // Output
        tvScreenOutput = findViewById(R.id.tv_screen_result)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_cls -> {
                tvScreenInput?.text = ""
                tvScreenOutput?.text = ""
                lastDot = false
                lastNumeric = false
            }
            R.id.btn_dot -> {
                if (lastNumeric && !lastDot) {
                    tvScreenInput?.append(".")
                    lastNumeric = false
                    lastDot = true
                }
            }
            R.id.btn_add -> {onOperator(view)}
            R.id.btn_substract -> {onOperator(view)}
            R.id.btn_mult -> {onOperator(view)}
            R.id.btn_divide -> {onOperator(view)}
            R.id.btn_equal -> {onEqual(view)}
            else -> {
                tvScreenInput?.append((view as Button).text)
                lastNumeric = true
            }

        }
    }

    private fun onOperator(view: View) {
        tvScreenInput?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvScreenInput?.append(" ${(view as Button).text} ")
                lastNumeric = false
                lastDot = false
            } else if (!tvScreenOutput?.text.isNullOrEmpty()) {
                val text = "${tvScreenOutput?.text} ${(view as Button).text} "
                tvScreenInput?.text = text
                tvScreenOutput?.text = ""
                lastNumeric = false
                lastDot = false
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if(value.startsWith("-")) {
            false
        } else {
            arrayOf('+', '-', '*', '/').any{
                value.contains(it)
            }
        }
    }

    private fun onEqual(view: View) {
        if (lastNumeric) {
            var inputValue = tvScreenInput?.text.toString()
            var prefix = ""
            try {
                // Handle negative numbers
                if (inputValue.startsWith('-')) {
                    prefix = "-"
                    inputValue = inputValue.substring(1)
                }
                // Substraction
                if (inputValue.contains("-")) {
                    var (one, two) = inputValue.split('-')
                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvScreenOutput?.text = (one.toDouble().minus(two.toDouble())).toString()

                }
                // Addition
                else if (inputValue.contains("+")) {
                    var (one, two) = inputValue.split('+')
                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvScreenOutput?.text = (one.toDouble().plus(two.toDouble())).toString()
                }
                // Multiplication
                else if (inputValue.contains("*")) {
                    var (one, two) = inputValue.split('*')
                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvScreenOutput?.text = (one.toDouble().times(two.toDouble())).toString()
                }
                // Division
                else if (inputValue.contains("/")) {
                    var (one, two) = inputValue.split('/')
                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvScreenOutput?.text = (one.toDouble().div(two.toDouble())).toString()
                } else {
                    tvScreenOutput?.text = inputValue
                }

            } catch (e: ArithmeticException) {
                Toast.makeText(this, "Invalid Operation", Toast.LENGTH_LONG).show()
            }
            lastNumeric = false
        }
    }
}