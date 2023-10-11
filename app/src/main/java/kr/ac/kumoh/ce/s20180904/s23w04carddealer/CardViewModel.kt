package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CardViewModel : ViewModel() {
    private var _cards=MutableLiveData<IntArray>(IntArray(5){0})
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
}