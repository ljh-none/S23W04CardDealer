package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var main : ActivitySecondBinding
    private lateinit var model: CardViewModel
    private val times = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main=ActivitySecondBinding.inflate(layoutInflater)
        model= ViewModelProvider(this)[CardViewModel::class.java]
        model.simulate(times)
        var temp=model.probability.value!!
        main.txt1.text=temp[5].toString()
        setContentView(main.root)
    }

}