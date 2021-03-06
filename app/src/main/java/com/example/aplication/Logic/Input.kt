package com.example.aplication.Logic

import com.example.aplication.Logic.MainBlock.Companion.consoleOutput
import com.example.aplication.Logic.MainBlock.Companion.index
import com.example.aplication.Logic.MainBlock.Companion.variables
import java.util.*

class Input : MainBlock{
    override var ErrorString = ""
    override var status = true
    val vars = variables
    var textBar: String=""
    var stack: String=""

    override fun start() = input()

    fun input() {
        print(textBar)
        //исключение пользователь ввел переменные
        if (textBar?.contains(Regex("""[a-zA-Z]+[0-9]*(\ *\,*[a-zA-Z]+[0-9]*)*"""))!!) {
            //исключение ввели не цифры и пробелы
            if (stack.contains(Regex("""[^0-9\ ]+"""))) {
                val matches =
                    Regex("""[^0-9\ ]+""").find(
                        stack
                    )
                ErrorString = "the value of the variable was entered incorrectly ${matches?.value}"
                status = false
            }
            if(stack.contains(Regex("""[0-9]+""")))
            //print(stack)
            while (stack.contains(Regex("""[0-9]+""")) && textBar.contains(Regex("""[a-zA-Z]+[0-9]*"""))) {
                var a = Regex("""[a-zA-Z]+[0-9]*""").find(textBar)?.value.toString()
                var b = Regex("""[0-9]+""").find(stack)?.value.toString()
                vars.replace(a, b.toInt())
                textBar = textBar.replaceFirst(a, "", true)
                stack = stack.replaceFirst(b, "", true)
                //print(textBar)
                //print(stack)
            }
            if (textBar.contains(Regex("""[a-zA-Z]+[0-9]*""")))
                index--
        } else {
            //исключение пользователь не ввел переменные
            val text = textBar
            val matches =
                Regex("""([^\w|,|\s]|((^|,)\s*([0-9]+[a-zA-Z]+|\d+))|(\w+\s+\w+)|,{2,})""").find(
                    text
                )
            ErrorString = "empty variables"
            status = false
        }
    }
}