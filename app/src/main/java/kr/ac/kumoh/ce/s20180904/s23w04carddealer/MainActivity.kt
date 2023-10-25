package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main : ActivityMainBinding
    private lateinit var model: CardViewModel
    private lateinit var card_list: Array<ImageView?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Lifecycle!!!", "onCreate")
        //setContentView(R.layout.activity_main)
        main=ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        card_list=arrayOf(main.card1, main.card2, main.card3, main.card4, main.card5)

        model= ViewModelProvider(this)[CardViewModel::class.java]
        model.cards.observe(this, Observer { SetCard() })
        model.ranks.observe(this,{main.text.text=model.ranks.value!!})  //화면 회전 시 족보 유지

        main.btnShuffle.setOnClickListener{
            model.generateCard()
        }
        main.btnRate.setOnClickListener{
            var intent= Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun SetCard(){
        var temp = 0
        for(i in 0 until 5){
            temp=model.cards.value!![i]
            val res = resources.getIdentifier(
                getCardName(temp),
                "drawable",
                packageName
            )
            card_list[i]?.setImageResource(res)
        }
    }
    private fun getCardName(c : Int): String{
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

        if(num=="Jack" || num=="queen" || num=="king") {
            shape = "${shape}2"
        }

        if(c == -1)
            return "c_black_joker"
        else
            return "c_${num}_of_${shape}"
    }
    override fun onStart() {
        super.onStart()
        Log.i("Lifecycle!!!", "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.i("Lifecycle!!!", "onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.i("Lifecycle!!!", "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.i("Lifecycle!!!", "onStop")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("Lifecycle!!!", "onRestart")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("Lifecycle!!!", "onDestroy")
    }
}

