package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//족보값
const val NONE = "none"
const val HIGH = "하이카드"
const val ONE_PAIR = "원 페어"
const val TWO_PAIR = "투 페어"
const val TRIPLE = "트리플"
const val STRAIGHT = "스트레이트"
const val BACK_STRAIGHT = "백 스트레이트"
const val MOUNTAIN = "마운틴"
const val FLUSH = "플러시"
const val FULL_H = "풀 하우스"
const val FOUR_CARD = "포 카드"
const val ST_FLUSH = "스트레이트 플러시"
const val BACK_ST_FL = "백 스트레이트 플러시"
const val ROYAL_ST_FL = "로얄 스트레이트 플러시"

class CardViewModel : ViewModel() {
    private var _cards=MutableLiveData<IntArray>(IntArray(5){-1})
    private var _ranks = MutableLiveData<String>("족보") //족보값 유지용
    private var _probability= MutableLiveData<DoubleArray>(DoubleArray(13){0.0})
    val cards: LiveData<IntArray>
        get() = _cards
   val ranks : LiveData<String>
       get() = _ranks
    val probability :LiveData<DoubleArray>
        get() = _probability
    fun generateCard(){ //기존 5장 생성 기능에 족보 판단 함수 추가
//        이하 주석은 검증용 카드
//        var tempCard = intArrayOf(51, 50, 49, 48, 47) //로플검증
//        var tempCard = intArrayOf(26, 27, 36, 37, 38) //스플검증
//        var tempCard = intArrayOf(5, 3, 16, 29, 42) //포카검증
//        var tempCard = intArrayOf(5, 18, 31, 6, 19) //풀하검증
//        var tempCard = intArrayOf(40, 42, 44, 45, 50) //플러검즏
//        var tempCard = intArrayOf(15, 29, 4, 31, 32)   //스트검증
        var temp : Int
        val tempCard = IntArray(5){0}
        for (i in tempCard.indices) {
            do {
                temp = Random.nextInt(0, 52)
            } while (tempCard.contains(temp))
            tempCard[i] = temp
        }
        _cards.value=tempCard
        judgeCard(_cards.value!!.copyOf(),_cards.value!!.copyOf()) //족보 판단 함수
    }

    fun simulate(i : Int){
        Log.i("ErrorLog??", "init simulate")
        var ary=IntArray(13){0}
        var tempCard = IntArray(5){0}
        var temp : Int

        for (j in 1..i){
            for (i in tempCard.indices) {
                do {
                    temp = Random.nextInt(0, 52)
                } while (tempCard.contains(temp))
                tempCard[i] = temp
            }
            when(judgeCard(tempCard,tempCard)){
                HIGH -> ary[0]++
                ONE_PAIR -> ary[1]++
                TWO_PAIR -> ary[2]++
                TRIPLE -> ary[3]++
                STRAIGHT -> ary[4]++
                BACK_STRAIGHT -> ary[5]++
                MOUNTAIN -> ary[6]++
                FLUSH -> ary[7]++
                FULL_H -> ary[8]++
                FOUR_CARD -> ary[9]++
                ST_FLUSH -> ary[10]++
                BACK_ST_FL -> ary[11]++
                ROYAL_ST_FL -> ary[12]++
            }
        }
        Log.i("ErrorLog??", "complete for")
        _probability.value=isRate(ary, i)
        return
    }
    private fun isRate(ary : IntArray, time : Int) : DoubleArray{
        var temp= DoubleArray(13){0.0}
        for(j in 0..12){
            temp[j]= (ary[j]/time).toDouble()
        }
        return temp
    }

    private fun judgeCard(p_cardNum : IntArray, p_cardShape : IntArray) : String{    //족보 판단 함수
        // oper -> 0 모듈러(카드넘버), 1 -> 나눗셈(카드모양)
        var result : String
        val cardNum= Normalization(13, 0, p_cardNum)
        val cardShape= Normalization(13, 1, p_cardShape)
        result=isOverlap(cardNum)   //숫자 중복 족보 검사 함수
        if(result == NONE){
            result=isAnother(cardShape, cardNum)   //나머지 족보 판별 함수
        }
        _ranks.value=result
        return result
    }
    private fun isOverlap(temp : IntArray) : String{   //숫자 중복 족보 검사 함수
        var count = 0
        var n : Int //임시변수

        for(i in 0..12) {    //각 숫자(ace~king)에 대한 중복검사
            n = temp.count { it == i }
            if (n == 4) return FOUR_CARD    //포카드(4+0)와 투페어(2+2)는 최종 카운트가 4로 겹침.
            if (n != 1) count += n  //중복이 아닌 요소는 카운트하지 않음.
        }
        when(count){
            2 -> return ONE_PAIR
            3 -> return TRIPLE
            4 -> return TWO_PAIR    //n=2+2가 투페어. 4+0은 위에서 아예 리턴시킴.
            5 -> return FULL_H
            else -> return NONE    //아무것도 해당하지 않을 때
        }
    }
    private fun isAnother(p_shape : IntArray, p_straight : IntArray) : String{   //나머지 족보 판별 함수
        val shape = isSame(p_shape) //같은 문양 검사
        val straight = isStraight(p_straight) //숫자 연속성 검사
        val sum=p_straight.sum()

        if(shape == true && straight == true){    //모두 같은 문양이고, 스트레이트일 때
            if(sum==42) return ROYAL_ST_FL  //로얄인지 검사
            if(sum==10) return BACK_ST_FL
            return ST_FLUSH
        }
        else if(shape == false && straight == true){   //문양이 다르고 스트레이트일 때
            if(sum==42) return MOUNTAIN
            if(sum == 10) return BACK_STRAIGHT
            return STRAIGHT
        }
        else if(shape == true && straight == false){   //문양만 모두 같을 때
            return FLUSH
        }
        else    //나머지
            return HIGH
    }
    private fun isSame(temp : IntArray) : Boolean {  //같은 문양 검사 함수
        var count: Int
        for(i in 0..4) {
            count=temp.count{ it == i } //중복검사
            if(count == 5) return true //카운트가 5면 같은요소가 5개있다 -> 모두 같은 문양
        }
        return false
    }
    private fun isStraight(temp : IntArray) : Boolean{  //스트레이트 검사 함수
        var count = 0
        temp.sort() //이 함수에서의 스트레이트 검사는 정렬이 필수

        if(temp[0] == 0 && temp[4] == 12){  //ace와 king이 같이 나왔을 때
            for(i in 0..3){ //순전파
                if(temp[i+1] - temp[i] == 1) count += 1
                else break
            }
            for(i in 4 downTo 1){   //역전파
                if(temp[i] - temp[i - 1] == 1) count += 1
                else break
            }
            if(count == 3) return true
        }
        else{   //ace와 킹이 붙어있지 않을 때
            for(i in 0..3){
                if(temp[i+1] - temp[i] == 1) {
                    count += 1
                }
            }
            if(count == 4) return true
        }
        return false
    }

    private fun Normalization(div : Int, oper : Int, temp : IntArray) : IntArray{
        //정규화 함수. oper : 0 -> 모듈러(카드넘버), 1 -> 나눗셈(카드모양)
        if(oper == 0) {
            for (i in 0..(temp.size - 1)) {
                temp[i] = temp[i] % div
            }
            return temp
        }
        else if(oper == 1){
            for (i in 0..(temp.size - 1)) {
                temp[i] = temp[i] / div
            }
            return temp
        }
        else return temp
    }
}
