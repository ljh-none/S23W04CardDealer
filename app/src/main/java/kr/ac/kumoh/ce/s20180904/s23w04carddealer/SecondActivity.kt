package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var main : ActivitySecondBinding
    private lateinit var model: CardViewModel
    private lateinit var txtAry: Array<TextView?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main=ActivitySecondBinding.inflate(layoutInflater)
        model= ViewModelProvider(this)[CardViewModel::class.java]
        txtAry= arrayOf(main.txt1, main.txt2, main.txt3, main.txt4,main.txt5,main.txt6,
            main.txt7,main.txt8,main.txt9,main.txt10,main.txt11,main.txt12,main.txt13)
        setText()
        setContentView(main.root)

        main.btnSimul!!.setOnClickListener{
            if(main.edit?.text?.isEmpty() == true)
                return@setOnClickListener
            model.times.value=main.edit?.text.toString().toInt()
            model.simulate()
            setText()
            var temp=model.probability.value!!
           for(i in 0..12){
               txtAry[i]!!.text =txtAry[i]!!.text.toString() + temp[i].toString()
           }
        }
    }

    fun setText(){
        for(i in 0..12){
            when(i){
                0 -> txtAry[i]!!.text = HIGH + " 확률 : "
                1 -> txtAry[i]!!.text = ONE_PAIR + " 확률 : "
                2 -> txtAry[i]!!.text = TWO_PAIR + " 확률 : "
                3 -> txtAry[i]!!.text = TRIPLE + " 확률 : "
                4 -> txtAry[i]!!.text = STRAIGHT + " 확률 : "
                5 -> txtAry[i]!!.text = BACK_STRAIGHT + " 확률 : "
                6 -> txtAry[i]!!.text = MOUNTAIN + " 확률 : "
                7 -> txtAry[i]!!.text = FLUSH + " 확률 : "
                8 -> txtAry[i]!!.text = FULL_H + " 확률 : "
                9 -> txtAry[i]!!.text = FOUR_CARD + " 확률 : "
                10 -> txtAry[i]!!.text = ST_FLUSH + " 확률 : "
                11 -> txtAry[i]!!.text = BACK_ST_FL + " 확률 : "
                12 -> txtAry[i]!!.text = ROYAL_ST_FL + " 확률 : "
                else -> return
            }
        }
    }
}