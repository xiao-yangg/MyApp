package com.example.myapp.activity

import android.os.Bundle
import android.widget.Toast
import com.example.myapp.databinding.ActivityCalculatorBinding
import java.lang.ArithmeticException

class CalculatorActivity : BaseActivity() {
    private lateinit var binding: ActivityCalculatorBinding

    private var lastNumeric = true
    private var lastDot = false
    private var lastOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOne.setOnClickListener{
            onDigit("1")
        }
        binding.btnTwo.setOnClickListener{
            onDigit("2")
        }
        binding.btnThree.setOnClickListener{
            onDigit("3")
        }
        binding.btnFour.setOnClickListener{
            onDigit("4")
        }
        binding.btnFive.setOnClickListener{
            onDigit("5")
        }
        binding.btnSix.setOnClickListener{
            onDigit("6")
        }
        binding.btnSeven.setOnClickListener{
            onDigit("7")
        }
        binding.btnEight.setOnClickListener{
            onDigit("8")
        }
        binding.btnNine.setOnClickListener{
            onDigit("9")
        }
        binding.btnZero.setOnClickListener{
            onDigit("0")
        }
        binding.btnAc.setOnClickListener{
            onClear()
        }
        binding.btnDelete.setOnClickListener{
            onDelete()
        }
        binding.btnPosNeg.setOnClickListener{
            onPosNeg()
        }
        binding.btnDot.setOnClickListener{
            onDecimal()
        }
        binding.btnPlus.setOnClickListener{
            onOperator("+")
        }
        binding.btnMinus.setOnClickListener{
            onOperator("-")
        }
        binding.btnMultiply.setOnClickListener{
            onOperator("x")
        }
        binding.btnDivide.setOnClickListener{
            onOperator("/")
        }
        binding.btnModulus.setOnClickListener{
            onOperator("%")
        }
        binding.btnEqual.setOnClickListener{
            onEqual()
        }
    }

    private fun onDigit(value: String) {
        if (binding.tvInput.text.startsWith('0')) {
            binding.tvInput.text = ""
        }

        binding.tvInput.append(value)
        setLastAction(numeric=true, dot=false, operator=false)
    }

    private fun onClear() {
        binding.tvInput.text = "0"
        setLastAction(numeric=true, dot=false, operator=false)
    }

    private fun onDelete() {
        var tvValue = binding.tvInput.text.toString()
        tvValue = if (tvValue.length == 1) { "0" } else { tvValue.dropLast(1) }

        if (tvValue.last() == '+' || tvValue.last() == '-' || tvValue.last() == '*' ||
            tvValue.last() == '/' || tvValue.last() == '%')
            setLastAction(numeric=false, dot=false, operator=true)
        else if (tvValue.last() == '.')
            setLastAction(numeric=false, dot=true, operator=false)
        else
            setLastAction(numeric=true, dot=false, operator=false)

        binding.tvInput.text = tvValue
    }

    private fun onPosNeg() {
        val tvValue = binding.tvInput.text.toString()
        if (validOperator(tvValue) && tvValue != "0") {
            if (tvValue.startsWith("-"))
                binding.tvInput.text = tvValue.substring(1)
            else
                binding.tvInput.text = "-$tvValue"
        } else showErrorToast()
    }

    private fun onDecimal() {
        if (lastNumeric && !lastDot) {
            binding.tvInput.append(".")
            setLastAction(numeric=false, dot=true, operator=false)
        } else showErrorToast()
    }

    private fun onOperator(operator: String) {
        if (lastNumeric && !lastOperator && validOperator(binding.tvInput.text.toString())) {
            binding.tvInput.append(operator)
            setLastAction(numeric=false, dot=false, operator=true)
        } else showErrorToast()
    }

    private fun onEqual() {
        if (lastNumeric) {
            var tvValue = binding.tvInput.text.toString()
            var prefixMinus = false
            try {
                // handle negative numbers
                if (tvValue.startsWith("-")) {
                    prefixMinus = true
                    tvValue = tvValue.substring(1)
                }

                // handler operation
                if (tvValue.contains("+"))
                    binding.tvInput.text = compute(tvValue, "+", prefixMinus)
                else if (tvValue.contains("-"))
                    binding.tvInput.text = compute(tvValue, "-", prefixMinus)
                else if (tvValue.contains("x"))
                    binding.tvInput.text = compute(tvValue, "x", prefixMinus)
                else if (tvValue.contains("/"))
                    binding.tvInput.text = compute(tvValue, "/", prefixMinus)
                else if (tvValue.contains("%"))
                    binding.tvInput.text = compute(tvValue, "%", prefixMinus)
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        } else showErrorToast()
    }

    private fun validOperator(value: String): Boolean {
        if (value.startsWith("-")) return true
        else if (value.contains("+") || value.contains("-") ||
                 value.contains("x") || value.contains("/") ||
                 value.contains("%")) return false
        return true
    }

    private fun compute(value: String, operator: String, prefixMinus: Boolean): String {
        val splitValue = value.split(operator)

        var one = splitValue[0]
        var two = splitValue[1]

        if (prefixMinus) one = "-$one"

        var tempValue = when (operator) {
            "+" -> (one.toDouble() + two.toDouble()).toString()
            "-" -> (one.toDouble() - two.toDouble()).toString()
            "x" -> (one.toDouble() * two.toDouble()).toString()
            "/" -> (one.toDouble() / two.toDouble()).toString()
            "%" -> (one.toDouble() % two.toDouble()).toString()
            else -> "0"
        }

        if (operator != "/") {
            tempValue = tempValue.dropLast(2) // remove '.0'
        }
        return tempValue
    }

    private fun setLastAction(numeric: Boolean, dot: Boolean, operator: Boolean) {
        lastNumeric = numeric
        lastDot = dot
        lastOperator = operator
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Invalid input", Toast.LENGTH_LONG).show()
    }
}