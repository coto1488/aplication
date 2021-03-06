package com.example.aplication

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aplication.Logic.*
import com.example.aplication.Logic.MainBlock.Companion.consoleOutput
import com.example.aplication.databinding.ActivitySecondBinding

import com.google.android.material.bottomsheet.BottomSheetBehavior


class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding


    data class Block(
        var view: View,
        var type: String,
        var canHaveElse: Boolean,
        var name: String,
        var startInd: Int,
        var finishInd: Int
    )

    private var listOfBlocks: MutableList<Block> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)

//        val mu = View(this)
//        binding.container.addView(mu, LinearLayout.LayoutParams(10000, 1))
//        mu.x = 1000f
//        mu.y = 600f

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val buttonStop = binding.floating2
        buttonStop.visibility = INVISIBLE

        //консоль и bottom sheet
        val frame = findViewById<FrameLayout>(R.id.sheet)
        val consol = findViewById<FrameLayout>(R.id.sheet2)
        consol.visibility = INVISIBLE
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from<View>(frame)

        val bottomSheetConsol: BottomSheetBehavior<*> =
            BottomSheetBehavior.from<View>(consol)

        bottomSheetConsol.peekHeight = 0

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.peekHeight = 135
        bottomSheetBehavior.isHideable = false
        val buttonPlay = binding.floating

        buttonPlay.setOnClickListener {
            consoleOutput.clear()
            frame.visibility = INVISIBLE
            consol.visibility = VISIBLE
            binding.trash.visibility = INVISIBLE
            bottomSheetConsol.peekHeight = 135
            buttonPlay.visibility = INVISIBLE
            buttonStop.visibility = VISIBLE
            var i = 0

            for (block in listOfBlocks) {
                println(block.name)
                println(block.startInd)

                if (block.name == "ForCustomView") {
                    val string = (block.view as ForCustomView).GetText1()
                    val string2 = (block.view as ForCustomView).GetText2()
                    pushDataForArithmetic(string2, string, i)
                }
                if (block.name == "For_inizalitation") {
                    val string = (block.view as For_inizalitation).GetText2()
                    pushDataForInitialization(string, i)
                }
                if (block.name == "IF") {
                    val string = (block.view as If_block).GetText2()
                    pushDataForIf(string, i, block.finishInd)
                }
                if (block.name == "WHILE") {
                    println(block.finishInd)
                    val string = (block.view as While_block).GetText2()
                    pushDataForWhile(string, i, block.finishInd)
                }
                if (block.name == "PRINT") {
                    val string = (block.view as Print_block).GetText2()
                    pushDataForOutput(string, i)
                }
                if (block.name == "end") {
                    println(block.finishInd)
                    pushDataForEnd(block.finishInd)
                }
                if (block.name == "ARRAY") {
                    val string = (block.view as Array_block).GetText2()
                    pushDataForMassive(string, i)
                }
                if (block.name == "ELSE") {
                    val indexIf = listOfBlocks[i - 1].startInd
                    val string = (listOfBlocks[indexIf].view as If_block).GetText2()
                    pushDataForIfElse(string, indexIf, i, block.finishInd)
                }
                if (block.name == "INPUT") {
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle(R.string.prompt)

                    val input = EditText(this)
                    alert.setView(input)

                    alert.setPositiveButton("Ok") { dialog, whichButton ->
                        val string2 = (block.view as Input_block).GetText2()
                        val string = input.text.toString()
                        pushDataForInput(string2, string, i)
                    }
                    alert.show()
                }
                i++
            }
            main()
            for (count in consoleOutput) {
                val text = TextView(this)
                text.textSize = 20f
                text.setTextColor(Color.parseColor("#e3e3e3"));
                binding.containerForTextView.addView(text)
                text.background = Drawable.createFromPath("drawable/block_button.xml")
                text.text = count
//                binding.scrollForConsol.post{
//                    binding.scrollForConsol.fullScroll(View.FOCUS_DOWN)
//                }
            }
        }

        buttonStop.setOnClickListener {
            consol.visibility = INVISIBLE
            frame.visibility = VISIBLE
            binding.trash.visibility = VISIBLE
            bottomSheetBehavior.peekHeight = 135
            bottomSheetConsol.peekHeight = 135
            buttonPlay.visibility = VISIBLE
            buttonStop.visibility = INVISIBLE
        }

        //обработка нажатий на кнопки создания блоков
        binding.forArifmetic.setOnClickListener {
            createBlock(ForCustomView(this), "ForCustomView", false)
            createArithmetic()
        }
        binding.forCycleWhile.setOnClickListener {
            createBlock(While_block(this), "WHILE", true)
            createWhile()
            createNull()
        }
        binding.forOperatorIf.setOnClickListener {
            createBlock(If_block(this), "IF", true)
            createIf()
            createNull()
        }
        binding.forOperatorIfElse.setOnClickListener {
            val variable = countBlockByName("IF")
            val variable2 = countBlockByName("ELSE")
            if (variable > variable2) {
                createBlock(Else_block(this), "ELSE", true)
            }
            createIfElse()
            createNull()
        }
        binding.forInitialization.setOnClickListener {
            createBlock(For_inizalitation(this), "For_inizalitation", false)
            createInitialization()
        }
        binding.forPrint.setOnClickListener {
            createOutput()
            createBlock(Print_block(this), "PRINT", false)
        }
        binding.forArray.setOnClickListener {
            createMassive()
            createBlock(Array_block(this), "ARRAY", false)
        }
        binding.forInput.setOnClickListener {
            createBlock(Input_block(this), "INPUT", false)
            createIntput()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createBlock(view: View, name: String, isHaveChild: Boolean) {
        var flag = false
        if (name == "IF") {
            flag = true
        }
        var toInd = listOfBlocks.size
        if (listOfBlocks.size < 2 && view is Else_block) return
        if (listOfBlocks.size >= 2 && view is Else_block) {
            toInd = findIndToNewElse()
        }
        binding.container.addView(view, toInd)
        listOfBlocks.add(toInd, Block(view, "", false, name, toInd, toInd))
        view.setOnLongClickListener(choiceTouchListener())
        view.setOnDragListener(choiceDragListener())
        if (isHaveChild) {
            createEnd(toInd + 1, flag)
        }
        calculateNewIndexes()
    }

    private fun findIndToNewElse(): Int {
        for (i in 2..listOfBlocks.size) {
            if (listOfBlocks[i - 1].canHaveElse && i == listOfBlocks.size) return i
            if (listOfBlocks[i - 1].canHaveElse && listOfBlocks[i].view !is Else_block) {
                return i
            }
        }
        return 0
    }

    private fun createEnd(toInd: Int, flag: Boolean) {
        val blockEnd = Block_end(this)
        listOfBlocks[toInd - 1].finishInd = toInd
        listOfBlocks.add(toInd, Block(blockEnd, "", flag, "end", toInd - 1, toInd))
        blockEnd.setOnLongClickListener(choiceTouchListener())
        blockEnd.setOnDragListener(choiceDragListener())
        binding.container.addView(blockEnd, toInd)
    }

    private lateinit var draggingView: View

    @SuppressLint("ClickableViewAccessibility")
    fun choiceTouchListener() = OnLongClickListener { view ->
        val data = ClipData.newPlainText("", "")
        val shadowBuilder = DragShadowBuilder(view)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            view.startDrag(data, shadowBuilder, view, 0)
        } else {
            view.startDragAndDrop(data, shadowBuilder, view, 0)
        }
        draggingView = view as View
        true
    }


    private fun choiceDragListener() = OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                var block = listOfBlocks[0]

                for (i in listOfBlocks) {
                    if (i.view == draggingView) {
                        block = i
                    }
                }
                var finishInd = block.finishInd
                val startInd = block.startInd
                if (listOfBlocks[block.startInd].view is If_block) {
                    if (listOfBlocks.size - 1 > block.finishInd + 1) {
                        if (listOfBlocks[block.finishInd + 1].view is Else_block) {
                            finishInd = listOfBlocks[block.finishInd + 1].finishInd
                        }
                    }
                }
                for (i in finishInd downTo startInd) {

                    listOfBlocks[i].view.visibility = INVISIBLE
//                    if(i != second.finishInd) {
//                        binding.container.removeView(listOfBlocks[i].view)
//                    }
                }
            }
            DragEvent.ACTION_DROP -> {
                if (view != binding.container) {
                    if (view != draggingView) {
                        var dropTo = listOfBlocks[0]
                        var ind = 0
                        var drag = listOfBlocks[0]
                        for (i in listOfBlocks) {
                            if (view != dropTo.view) {
                                ind++
                            }
                            if (i.view == view) {
                                dropTo = i
                            } else if (i.view == draggingView) {
                                drag = i
                            }
                        }

//                        Log.i("baby3","$binding.trash.scaleX")
////                        if (event.x == binding.trash.getPos) {
//
//                            if (event.y == binding.trash.scaleX) {
//                                Remove(ind - 1, drag)
//                            }
//                        }

                        if (listOfBlocks[drag.startInd].view is Else_block && !canAttachElse(
                                ind - 1,
                                Point(event.x, event.y)
                            )
                        ) {
                            return@OnDragListener true
                        }
                        if (ind != listOfBlocks.size && (listOfBlocks[ind].name == "ELSE")) {
                            Log.i("baby", "yoda")
                            return@OnDragListener true
                        }
                        if (ind > 0) {
                            if (event.y < listOfBlocks[ind - 1].view.height / 2) {
                                if (listOfBlocks[ind - 1].name == "ELSE") {
                                    return@OnDragListener true
                                }
                            }
                        }
                        if (dropTo.finishInd != drag.finishInd) {
                            attach(ind - 1, drag, Point(event.x, event.y))
                            calculateNewIndexes()
                            createMargin()
                        }
                    }
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                for (i in listOfBlocks) {
                    i.view.post {
                        i.view.visibility = VISIBLE
                    }
                }
            }
        }
        true
    }

    data class Point(var x: Float, var y: Float)

    private fun attach(toBlockInd: Int, fromBlock: Block, dropPoint: Point) {

        val buff = mutableListOf<Block>()
        var ind = toBlockInd
        if (toBlockInd == -1) {
            ind = 0
        }
        val toBlock = listOfBlocks[ind]


        val size = listOfBlocks.size - 1
        var finishInd = fromBlock.finishInd
        val startInd = fromBlock.startInd
        if (listOfBlocks[fromBlock.startInd].view is If_block) {
            if (listOfBlocks.size - 1 > fromBlock.finishInd + 1) {
                if (listOfBlocks[fromBlock.finishInd + 1].view is Else_block) {
                    finishInd = listOfBlocks[fromBlock.finishInd + 1].finishInd
                }
            }
        }
        for (i in finishInd downTo startInd) {
            buff.add(listOfBlocks[i])
        }

        val newList = mutableListOf<Block>()
        for (i in 0..size) {
            if ((startInd > i || i > finishInd) && i != ind) {
                newList.add(listOfBlocks[i])
            } else if (i == ind) {
                if (dropPoint.y > toBlock.view.height / 2) {
                    newList.add(listOfBlocks[ind])
                    for (j in buff.size - 1 downTo 0) {
                        newList.add(buff[j])
                    }
                } else {
                    for (j in buff.size - 1 downTo 0) {
                        newList.add(buff[j])
                    }
                    newList.add(listOfBlocks[ind])
                }
            }
        }
        for (i in newList.size - 1 /*- (fromBlock.finishInd - fromBlock.startInd)*/ downTo 0) {
            binding.container.removeView(listOfBlocks[i].view)
        }
        for (i in newList) {
            binding.container.addView(i.view)
        }
        listOfBlocks = newList

    }

    private fun calculateNewIndexes() {
        val buffList = mutableListOf<Block>()
        for (i in listOfBlocks) {
            buffList.add(i)
        }
        val checkList = mutableListOf<Boolean>()
        for (i in 0 until listOfBlocks.size) {
            checkList.add(false)
        }
        for (i in 0 until listOfBlocks.size) {
            if (checkList[i]) continue
            for (j in i until listOfBlocks.size) {
                if (checkList[j]) continue
                if (listOfBlocks[i].startInd == listOfBlocks[j].startInd &&
                    listOfBlocks[i].finishInd == listOfBlocks[j].finishInd &&
                    i != j
                ) {
                    buffList[i].startInd = i
                    buffList[j].startInd = i
                    buffList[i].finishInd = j
                    buffList[j].finishInd = j
                    checkList[i] = true
                    checkList[j] = true
                    break
                }
            }
            if (!checkList[i]) {
                buffList[i].startInd = i
                buffList[i].finishInd = i
                checkList[i] = true
            }
        }
        listOfBlocks = buffList
    }

    private fun createMargin() {
        for (block in listOfBlocks) {
            block.view.x = 0f
        }

        for (block in listOfBlocks) {
            if (block.finishInd - block.startInd >= 2) {
                for (i in block.startInd + 1..block.finishInd - 1) {
                    listOfBlocks[i].view.x += 30f
                }
            }
        }

    }

    private fun countBlockByName(name: String): Int {
        var count = 0
        for (block in listOfBlocks) {
            if (block.name == name) {
                count++
            }
        }
        return count
    }

    private fun canAttachElse(toBlockInd: Int, dropPoint: Point): Boolean {
        if (toBlockInd == -1) {
            return false
        }
        return if (dropPoint.y > listOfBlocks[toBlockInd].view.height / 2)
            (listOfBlocks[toBlockInd].canHaveElse)
        else (listOfBlocks[toBlockInd - 1].canHaveElse)
    }

    private fun Remove(index: Int, fromBlock: Block) {
        binding.container.removeViewAt(index)
        listOfBlocks.removeAt(index)
        calculateNewIndexes()
    }
}