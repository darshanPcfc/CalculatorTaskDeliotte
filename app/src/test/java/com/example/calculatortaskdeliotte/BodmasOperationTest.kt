package com.example.calculatortaskdeliotte

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

//Three test cases related to expression tested
@RunWith(JUnit4::class)
class BodmasOperationTest {

    @Test
    fun checkExpression() {
        val value = evaluateResult("2 * 8")
        Assert.assertEquals(16, value)
    }

    @Test(expected = ArithmeticException::class)
    fun checkException() {
        print("Excpetion")
        evaluateResult("1 / 0")
    }

    @Test(expected = EmptyStackException::class)
    fun checkiEmptyStackException() {
        evaluateResult("1 * * 8")
    }

    fun evaluateResult(expression: String): Int {
        val tokens = expression.toCharArray()

        // Stack for numbers: 'values'
        val values: Stack<Int>
        values = Stack()

        // Stack for Operators: 'ops'
        val ops: Stack<Char>
        ops = Stack()

        var i = 0
        while (i < tokens.size) {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ') {
                i++
                continue
            }

            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                val addToStack = StringBuilder()
                // There may be more than one digits in number
                while (i < tokens.size && tokens[i] >= '0' && tokens[i] <= '9')
                    addToStack.append(tokens[i++])
                values.push(Integer.parseInt(addToStack.toString()))
            } else if (tokens[i] == '(')
                ops.push(tokens[i])
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                ops.pop()
            } else if (tokens[i] == '+' || tokens[i] == '-' ||
                tokens[i] == '*' || tokens[i] == '/'
            ) {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                try {
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                } catch (e: EmptyStackException) {
                    throw EmptyStackException()
                }
                // Push current token to 'ops'.
                ops.push(tokens[i])
            }// Current token is an operator.
            // Closing brace encountered, solve entire brace
            // Current token is an opening brace, push it to 'ops'
            i++
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()))

        // Top of 'values' contains result, return it
        return values.pop()
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        return if (op2 == '(' || op2 == ')') false else op1 != '*' && op1 != '/' || op2 != '+' && op2 != '-'
    }

    // A utility method to apply an operator 'op' on operands 'a' and 'b'.
    // Return the result.
    private fun applyOp(op: Char, b: Int, a: Int): Int {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '*' -> return a * b
            '/' -> {
                if (b == 0)
                    throw ArithmeticException("Cannot divide by zero")
                return a / b
            }
            else -> throw IllegalStateException("Unexpected value: ")
        }
    }
}