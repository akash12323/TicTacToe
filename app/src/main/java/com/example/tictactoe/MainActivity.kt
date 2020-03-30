package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val prefs:String = "AppPrefs"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var board: Array<Array<Button>>
    var boardStatus = Array(3) { IntArray(3) }

    var PLAYER = true
    var count = 0
    var player1win:Int = 0
    var player2win:Int = 0
    lateinit var name1:String
    lateinit var name2:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(prefs, Context.MODE_PRIVATE)

        tv1.text = player1win.toString()
        tv2.text = player2win.toString()

        name1 = sharedPreferences.getString("name1","")?:""
        Player1.setText(name1)
        name2 = sharedPreferences.getString("name2","")?:""
        Player2.setText(name2)

        b1.setOnClickListener(){
            Toast.makeText(this,"player1 name saved",Toast.LENGTH_SHORT)
            sharedPreferences.edit().putString("name1",Player1.text.toString()).apply()
        }
        b2.setOnClickListener(){
            Toast.makeText(this,"player2 name saved",Toast.LENGTH_SHORT)
            sharedPreferences.edit().putString("name2",Player2.text.toString()).apply()
        }

        board = arrayOf(
            arrayOf(button1, button2, button3),
            arrayOf(button4, button5, button6),
            arrayOf(button7, button8, button9)
        )

        for (i in board)
            for (button in i)
                button.setOnClickListener(this)

        initializeBoardStatus()

        resetButton.setOnClickListener() {
            PLAYER = true
            count = 0

            //sharedPreferences.edit().putInt("player1", player1win).apply()
            //sharedPreferences.edit().putInt("player2", player2win).apply()

            updateDisplay("Player X Turn")
            initializeBoardStatus()
        }
    }

    private fun initializeBoardStatus() {
        for (i in 0..2)
            for (j in 0..2)
                boardStatus[i][j] = -1

        for (i in board)
            for (button in i) {
                button.isEnabled = true
                button.text = ""
            }
    }
    override fun onClick(view: View) {
        Log.i("CLICK","clicked id : ${view.id}")
        when(view.id){
            R.id.button1->{updateValue(0,0,PLAYER)}
            R.id.button2->{updateValue(0,1,PLAYER)}
            R.id.button3->{updateValue(0,2,PLAYER)}
            R.id.button4->{updateValue(1,0,PLAYER)}
            R.id.button5->{updateValue(1,1,PLAYER)}
            R.id.button6->{updateValue(1,2,PLAYER)}
            R.id.button7->{updateValue(2,0,PLAYER)}
            R.id.button8->{updateValue(2,1,PLAYER)}
            R.id.button9->{updateValue(2,2,PLAYER)}
        }
        count++
        PLAYER = !PLAYER
        if(PLAYER)
            updateDisplay("Player X Turn")
        else
            updateDisplay("Player O Turn")

        if(count == 9)
            updateDisplay("Game Draw")

        checkWinner()
    }

    private fun checkWinner() {

        //Horizontal
        for (i in 0..2) {
            if (boardStatus[i][0] == boardStatus[i][1] && boardStatus[i][0] == boardStatus[i][2]) {
                if (boardStatus[i][0] == 1) {
                    updateDisplay("Player X WINNER")
                    break
                }
                else if(boardStatus[i][0] == 0){
                    updateDisplay("Player O WINNER")
                    break
                }
            }
        }

        //Vertical
        for (i in 0..2) {
            if (boardStatus[0][i] == boardStatus[1][i] && boardStatus[0][i] == boardStatus[2][i]) {
                if (boardStatus[0][i] == 1) {
                    updateDisplay("Player X WINNER")
                    break
                }
                else if(boardStatus[0][i] == 0){
                    updateDisplay("Player O WINNER")
                    break
                }
            }
        }

        //first diagonal
        if (boardStatus[0][0] == boardStatus[1][1] && boardStatus[0][0] == boardStatus[2][2]) {
            if (boardStatus[0][0] == 1) {
                updateDisplay("Player X WINNER")
            }
            else if(boardStatus[0][0] == 0){
                updateDisplay("Player O WINNER")
            }
        }

        //second diagonal
        if (boardStatus[0][2] == boardStatus[1][1] && boardStatus[0][2] == boardStatus[2][0]) {
            if (boardStatus[0][2] == 1) {
                updateDisplay("Player X WINNER")
            }
            else if(boardStatus[0][2] == 0){
                updateDisplay("Player O WINNER")
            }
        }
    }

    private fun updateDisplay(i: String){
        DisplayText.text = i

        if (DisplayText.text.contains("Player X WINNER")){
            ++player1win
        }
        if (DisplayText.text.contains("Player O WINNER")){
            ++player2win
        }
        tv1.text = player1win.toString()
        tv2.text = player2win.toString()

        if(i.contains("WINNER")){
            for(i in board)
                for(button in i)
                    button.isEnabled = false
        }
    }

    private fun updateValue(row:Int, col:Int,player:Boolean){
        val text = if(player) "X" else "O"
        val value = if(player) 1 else 0

        boardStatus[row][col] = value

        board[row][col].text = text
        board[row][col].isEnabled = false
    }

}

