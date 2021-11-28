package com.example.numbersgame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var rvMain : RecyclerView
    private lateinit var submitBtn : Button
    private  lateinit var textFiled : EditText
    private lateinit var clMain : ConstraintLayout
    private var userGusses = ArrayList<String>()
    private var rounds = 3
    private var theWinningNum =  Random.nextInt(1, 16)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userGusses = arrayListOf()
        clMain = findViewById(R.id.clMain)
        rvMain = findViewById(R.id.rvMain)
        submitBtn = findViewById(R.id.btnSubmit)
        textFiled = findViewById(R.id.etUserInput)
        rvMain.adapter = RecycleViewAdapter(userGusses)
        rvMain.layoutManager = LinearLayoutManager(this)
        submitBtn.setOnClickListener {
            println("in onclick $theWinningNum")
            rounds--
            if(rounds > 0){

                val userNumber = NumaricValidation(textFiled.text.toString())
                if (userNumber <= 0) {
                    //less then 0
                    userGusses.add("You Guessed $userNumber")
                    println("in onclick less then 0")


                } else {
                    //greater then 0
                    checkTheNumber(userNumber)

                }

                userGusses.add("You Have $rounds guesses left")

            }else{
                Snackbar.make(clMain, "You Have NO guess left reStart the game!", Snackbar.LENGTH_SHORT).show()

            }
            textFiled.setText("")
            rvMain.adapter?.notifyDataSetChanged()

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("myWinningNumber", theWinningNum)
        outState.putStringArrayList("usersGuesses", userGusses)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        theWinningNum = savedInstanceState.getInt("myWinningNumber")
        userGusses = savedInstanceState.getStringArrayList("usersGuesses") as ArrayList<String>
        rvMain.adapter = RecycleViewAdapter(userGusses)

    }
    private fun NumaricValidation(input:String): Int {
        return try {
            val conNum = input.toInt()
            conNum
        } catch (ex: NumberFormatException) {
            Snackbar.make(clMain, "Please Enter Only Numbers!", Snackbar.LENGTH_SHORT).show()
            0
        }
    }
    private fun checkTheNumber(theNumber: Int){
        userGusses.add("You Guessed $theNumber")

        when {
            theNumber == theWinningNum -> {
                println("in check ==")
                userGusses.add("You Have Guessed the right Number  Yay!!!")
                rounds = 0
            }
            theNumber  < 0 || theNumber > 15 -> {
                println("in check out range")
                userGusses.add("only the numbers in the range please , try again")

            }
            else -> {
                println("in check else")

                userGusses.add("your guessing is wrong , try again")


            }
        }
        println("$userGusses ====")

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.restartItem -> {
                Snackbar.make(clMain, "New Game !", Snackbar.LENGTH_SHORT).show()
                rounds = 3
                theWinningNum =  Random.nextInt(1, 16)
                this.recreate()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}