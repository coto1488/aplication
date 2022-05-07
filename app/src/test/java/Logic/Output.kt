package Logic

import Logic.MainBlock.Companion.variables

class Output : MainBlock {
    val vars = variables
    val name :String? = null
    val previousBlock : MainBlock? = null
    val nextBlock : MainBlock? = null
    var textBar: String?=null

    override fun start() = output()
    fun output(){
        var text = textBar
        if (!textBar?.contains(Regex("""([^\w|,|\s]|((^|,)\s*([0-9]+[a-zA-Z]|\d+))|(\w+\s+\w+)|,{2,})"""))!!) {
            val matches = Regex("""[a-zA-Z]+[0-9]*""").findAll(textBar!!)
            for(name in matches){
                if (vars.containsKey(name.value)) {
                   println(vars.getValue(name.value).toString()) // вывести пользователю в консоль эту переменную(хз как потому что эта функция не может возращать значения)
                }
                else{
                    // исключение : тут пользователь ввел переменную которую не задавал(к примеру 1+2+a+c)(словарь: a=0,b=0)
                    println("Variable is not exist : ${name.value}") // та самая переменная c
                    break
                }
            }

        }
        else{
            //исключение(тут надо в UX выдать пользователю ошибку типо ввел невозможную переменную e.g "12awd","@#!aue" и тд)
            println("false")
        }
    }



}