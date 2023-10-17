package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.lifecycle.ViewModel
import kotlin.random.Random
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//족보값
const val HIGH = 1
const val ONE_PAIR = 2
const val TWO_PAIR = 3
const val TRIPLE = 4
const val STRAIGHT = 5
const val FLUSH = 6
const val FULL_H = 7
const val FOUR_CARD = 8
const val ST_FLUSH = 9
const val ROYAL_FLUSH = 10

class CardViewModel : ViewModel() {
    private var _cards=MutableLiveData<IntArray>(IntArray(5){-1})
    private var _ranks = MutableLiveData<String>("족보") //족보값 유지용
    val cards: LiveData<IntArray>
        get() = _cards
   val ranks : LiveData<String>
       get() = _ranks

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
        judgeCard() //족보 판단 함수
    }

    private fun judgeCard(){    //족보 판단 함수
        // oper -> 0 모듈러(카드넘버), 1 -> 나눗셈(카드모양)
        var result : Int
        val cardNum= Normalization(13, 0, _cards.value!!.copyOf())
        val cardShape= Normalization(13, 1, _cards.value!!.copyOf())
        result=isOverlap(cardNum)   //숫자 중복 족보 검사 함수
        if(result == 0){
            result=isAnother(cardShape, cardNum)   //나머지 족보 판별 함수
        }
        when(result){
            HIGH -> _ranks.value = "하이카드"
            ONE_PAIR -> _ranks.value = "원 페어"
            TWO_PAIR -> _ranks.value = "투 페어"
            TRIPLE -> _ranks.value = "트리플"
            STRAIGHT -> _ranks.value = "스트레이트"
            FLUSH -> _ranks.value = "플러시"
            FULL_H -> _ranks.value = "풀 하우스"
            FOUR_CARD -> _ranks.value = "포 카드"
            ST_FLUSH -> _ranks.value = "스트레이트 플러시"
            ROYAL_FLUSH -> _ranks.value = "!!로얄 플러시!!"
            else -> _ranks.value = "error"
        }
    }
    private fun isOverlap(temp : IntArray) : Int{   //숫자 중복 족보 검사 함수
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
            else -> return 0    //아무것도 해당하지 않을 때
        }
    }
    private fun isAnother(isShape : IntArray, isstr : IntArray) : Int{   //나머지 족보 판별 함수
        val shape = sameShape(isShape) //같은 문양 검사
        val straight = isStraight(isstr) //숫자 연속성 검사

        if(shape == 1 && straight == 1){    //모두 같은 문양이고, 스트레이트일 때
            if(isstr.sum()==42) return ROYAL_FLUSH  //로얄인지 검사
            return ST_FLUSH
        }
        else if(shape == 0 && straight == 1){   //문양이 다르고 스트레이트일 때
            return STRAIGHT
        }
        else if(shape == 1 && straight == 0){   //문양만 모두 같을 때
            return FLUSH
        }
        else    //나머지
            return HIGH
    }
    private fun sameShape(temp : IntArray) : Int {  //같은 문양 검사 함수
        var count: Int
        for(i in 0..4) {
            count=temp.count{ it == i } //중복검사
            if(count == 5) return 1 //카운트가 5면 같은요소가 5개있다 -> 모두 같은 문양
        }
        return 0
    }
    private fun isStraight(temp : IntArray) : Int{  //스트레이트 검사 함수
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
            if(count == 3) return 1
        }
        else{   //ace와 킹이 붙어있지 않을 때
            for(i in 0..3){
                if(temp[i+1] - temp[i] == 1) {
                    count += 1
                }
            }
            if(count == 4) return 1
        }
        return 0
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
