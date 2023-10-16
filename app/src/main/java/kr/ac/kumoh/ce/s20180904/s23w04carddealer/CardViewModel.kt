package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import android.content.res.Resources
import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//족보값
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

class CardViewModel : ViewModel() {
    private var _cards=MutableLiveData<IntArray>(IntArray(5){-1})
    private var _rank =""   //회전해도 족보 출력되도록
    val cards: LiveData<IntArray>
        get() = _cards
   
    fun generateCard(){
        var temp = 0
        var tempCard = IntArray(5){0}
        for (i in tempCard.indices) {
            do {
                temp = Random.nextInt(0, 52)
            } while (tempCard.contains(temp))
            tempCard[i] = temp
        }
        _cards.value=tempCard
    }

    fun judgeCard() : String {
        val overlap = _cards.value!!.copyOf()
        val another = _cards.value!!.copyOf()
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
    private fun isAnother(temp : IntArray) : Int{
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

    private fun sameShape(temp : IntArray) : Int {
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
    private fun isStraight(temp : IntArray) : Int{
        var straight = Normalization(13, temp);
        var min = straight.min()

        straight=Normalization(min, straight);

        //최솟값으로 모듈러 연산을 한 값을 모두 더했을 때, 10이 나오면 스트레이트
        if(straight.sum()==10) {
            return 1;
        }
        else return 0
    }

    private fun Normalization(divisor : Int, temp : IntArray) : IntArray{
        var normal=temp
        for(i in 0..(normal.size-1)){
            normal[i]=normal[i]%divisor
        }
        return normal
    }
}
