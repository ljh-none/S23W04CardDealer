package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivityMainBinding

val HIGH = 1
val ONE_PAIR = 2
val TWO_PAIR = 3
val TRIPLE = 4
val STRAIGHT = 5
val FLUSH = 6
val FULL_H = 7
val FOUR_CARD = 8
val ST_FLUSH = 9
val ROYAL_FLUSH = 10

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

        main.btnShuffle.setOnClickListener{
            Log.i("Lifecycle!!!", "ButtonClicked")
            model.generateCard()
            Log.i("Lifecycle!!!", "generated")
            main.text.text=judgeCard()
            Log.i("Lifecycle!!!", "Judged")
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
    private fun judgeCard() : String {
        val overlap = model.cards.value!!.copyOf()
        val another = model.cards.value!!.copyOf()

        var result = 0
        Log.i("MyCal", "judge state - ${overlap[0]}")
        Log.i("MyCal", "judge state - ${overlap[1]}")
        Log.i("MyCal", "judge state - ${overlap[2]}")
        Log.i("MyCal", "judge state - ${overlap[3]}")
        Log.i("MyCal", "judge state - ${overlap[4]}")
        result=isOverlap(overlap)
        Log.i("MyCal", "after Overlap - ${another[0]}")
        Log.i("MyCal", "after Overlap - ${another[1]}")
        Log.i("MyCal", "after Overlap - ${another[2]}")
        Log.i("MyCal", "after Overlap - ${another[3]}")
        Log.i("MyCal", "after Overlap - ${another[4]}")
        Log.i("MyCal", "result - ${result}")
        if(result == 0){
            //로플, 스플, 플, 스트, 하이 판단
            result=isAnother(another)
        }
        Log.i("MyLog", "LogLogLogLogLogLogLogLog")
        when(result){
            HIGH -> return "하이카드"
            ONE_PAIR -> return "원 페어"
            TWO_PAIR -> return "투 페어"
            TRIPLE -> return "트리플"
            STRAIGHT -> return "스트레이트"
            FLUSH -> return "플러시"
            FULL_H -> return "풀 하우스"
            FOUR_CARD -> return "포 카드"
            ST_FLUSH -> return "스트레이트 플러시"
            ROYAL_FLUSH -> return "!!로얄 플러시!!"
            else -> return "error"
        }
    }

    private fun isOverlap(temp : IntArray) : Int{
        var count = 0
        var temp = Normalization(13,temp);    //13으로 모듈러 정규화

        var n = 0 //임시변수
        for(i in 0..12) {    //각 숫자에 대한 중복검사
            n = temp.count { it == i }
            if (n == 4) {   //투페랑 포카는 최종 카운트가 4로 겹침. 그래서 넣은 조건문
                return FOUR_CARD
            } else if (n != 1) {    //중복 아닌 요소는 기록하지 않음
                count += n
            }
        }
        when(count){
            2 -> return ONE_PAIR
            3 -> return TRIPLE
            4 -> return TWO_PAIR
            5 -> return FULL_H
            else -> return 0
        }
    }

    fun isAnother(temp : IntArray) : Int{
        var shape = sameShape(temp)
        var straight = isStraight(temp)

        if(shape == 1 && straight == 1){
            val temp=Normalization(13,temp);
            if(temp.sum()==42) return ROYAL_FLUSH;
            else return ST_FLUSH
        }
        else if(shape == 0 && straight == 1){
            return STRAIGHT
        }
        else if(shape == 1 && straight == 0){
            return FLUSH
        }
        else
            return HIGH
    }

    fun Normalization(divisor : Int, temp : IntArray) : IntArray{
        var normal=temp
        for(i in 0..(normal.size-1)){
            normal[i]=normal[i]%divisor
        }
        return normal
    }

    fun sameShape(temp : IntArray) : Int {
        var count = 0
        var shape = temp

        for(i in 0..4){     //나누기 연산으로 정규화. 0~3
            count += shape[i]/13
            Log.i("MyCal", "${i} - ${shape[i]} - ${shape[i]/13}")
        }

        for(i in 0..4) {    //각 숫자에 대한 중복검사
            count=shape.count { it == i }
        }
        if(count == 5) return 1
        else return 0
    }

    fun isStraight(temp : IntArray) : Int{
        var straight = Normalization(13, temp);
        var min = straight.min()

        straight=Normalization(min, straight);
        //최솟값으로 모듈러 연산을 한 값을 모두 더했을 때, 10이 나오면 스트레이트
        if(straight.sum()==10) {
            return 1;
        }
        else return 0
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

