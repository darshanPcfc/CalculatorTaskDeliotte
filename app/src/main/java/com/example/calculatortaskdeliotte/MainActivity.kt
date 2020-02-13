package com.example.calculatortaskdeliotte

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    //set button calculation listener
    private fun setListeners() {
        btn_calculate.setOnClickListener(this)
        edit_text_expression.requestFocus()
    }

    //on click function for button calculate
    //this will fetch the input string from edit text and will call outputResult Method
    override fun onClick(view: View?) {
        if (view!!.id == R.id.btn_calculate) {
            val extractedStr = edit_text_expression.text.toString()
            if (extractedStr.isEmpty()) {
                showError(getString(R.string.error_exception_empty_text))
            } else {
                tip_expression.isErrorEnabled = false
                outputResult(extractedStr)
            }
        }
    }

    //input is string of expression entered in edit_text_expression
    //wil call a static class BodmasOperation for fetching output of expression
    private fun outputResult(editTextString: String) {
        try {
            var functionalString = ""
            var count = 0
            while (count < editTextString.length) {
                if (editTextString[count] == '*' || editTextString[count] == '/' || editTextString[count] == '+' || editTextString[count] == '-')
                    functionalString = functionalString + " " + editTextString[count] + " "
                else
                    functionalString += editTextString[count]
                count++
            }
            showresult(
                editTextString,
                BodmasOperation.evaluateResult(functionalString).toString(),
                true
            )
        } catch (e: ArithmeticException) {
            showError(getString(R.string.error_exception_zero))
            showresult(getString(R.string.error_empty), getString(R.string.error_empty), false)
        } catch (e: EmptyStackException) {
            showError(getString(R.string.error_exception_general))
            showresult(getString(R.string.error_empty), getString(R.string.error_empty), false)
        } catch (e: Exception) {
            showError(getString(R.string.error_exception_general))
            showresult(getString(R.string.error_empty), getString(R.string.error_empty), false)
        }
    }

    //display the error for input provided
    private fun showError(message: String) {
        tip_expression.isErrorEnabled = true
        tip_expression.error = message
    }

    //display the result of expression on screen
    private fun showresult(inputText: String, result: String, showResult: Boolean) {
        if (showResult) {
            resultDisplay.visibility = View.VISIBLE
            resultDisplay.text = getString(R.string.result_text, inputText, result)
        }
        else
            resultDisplay.visibility = View.INVISIBLE
    }
}