package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var main : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        main=ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        //TODO:하드코딩 없앨 것
        val card=Random.nextInt(52)
        val res=resources.getIdentifier(
            getCardNumber(card),
            "drawable",
            packageName
        )
        main.card1.setImageResource(res)
        main.card2?.setImageResource(res)
        main.card3?.setImageResource(res)
        main.card4?.setImageResource(res)
        main.card5?.setImageResource(res)

    }

    private fun getCardNumber(c : Int):String{
        var shape = when (c/13){
            0->"spades"
            1->"diamonds"
            2->"hearts"
            3->"clubs"
            else->"error"
        }
        val num=when(c%13){
            0->"ace"
            in 1..9-> (c%13+1).toString()
            10->"jack"
            11->"queen"
            12->"king"
            else->"error"
        }

        if(num=="Jack" || num=="queen" || num=="king"){
            shape="${shape}2"
        }
        return "c_${num}_of_${shape}"
    }

}

