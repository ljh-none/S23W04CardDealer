package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {    //시뮬레이션 페이지의 activity
    private lateinit var main : ActivitySecondBinding
    private lateinit var model: CardViewModel
    private lateinit var txt_list: Array<TextView?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main=ActivitySecondBinding.inflate(layoutInflater)
        model= ViewModelProvider(this)[CardViewModel::class.java]
        model.probability.observe(this, Observer{addText()})
        txt_list= arrayOf(main.txt1, main.txt2, main.txt3, main.txt4,main.txt5,main.txt6,
            main.txt7,main.txt8,main.txt9,main.txt10,main.txt11,main.txt12,main.txt13)

        setDefaultText()
        setContentView(main.root)

        main.btnSimul!!.setOnClickListener{
            if(main.edit?.text?.isEmpty() == true)
                return@setOnClickListener
            model.times.value=main.edit?.text.toString().toInt()    //viewmodel에 시행 횟수 전달
            model.simulate()    //view모델에서 시뮬레이션 시행
        }
    }

    private fun setDefaultText(){   //디폴트 문자열
        for(i in 0..12){
            when(i){
                0 -> txt_list[i]!!.text = HIGH + " 확률 : "
                1 -> txt_list[i]!!.text = ONE_PAIR + " 확률 : "
                2 -> txt_list[i]!!.text = TWO_PAIR + " 확률 : "
                3 -> txt_list[i]!!.text = TRIPLE + " 확률 : "
                4 -> txt_list[i]!!.text = STRAIGHT + " 확률 : "
                5 -> txt_list[i]!!.text = BACK_STRAIGHT + " 확률 : "
                6 -> txt_list[i]!!.text = MOUNTAIN + " 확률 : "
                7 -> txt_list[i]!!.text = FLUSH + " 확률 : "
                8 -> txt_list[i]!!.text = FULL_H + " 확률 : "
                9 -> txt_list[i]!!.text = FOUR_CARD + " 확률 : "
                10 -> txt_list[i]!!.text = ST_FLUSH + " 확률 : "
                11 -> txt_list[i]!!.text = BACK_ST_FL + " 확률 : "
                12 -> txt_list[i]!!.text = ROYAL_ST_FL + " 확률 : "
                else -> return
            }
        }
    }
    private fun addText(){  //옵저버 함수. 디폴트 텍스트 입력 후 확률 입력
        setDefaultText()
        var temp=model.probability.value!!
        for(i in 0..12){
            txt_list[i]!!.text =txt_list[i]!!.text.toString() + temp[i].toString()
        }
    }
}