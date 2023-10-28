package kr.ac.kumoh.ce.s20180904.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20180904.s23w04carddealer.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var main : ActivitySecondBinding
    private lateinit var model: CardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main=ActivitySecondBinding.inflate(layoutInflater)
        model= ViewModelProvider(this)[CardViewModel::class.java]
        setContentView(main.root)

        main.btnSimul!!.setOnClickListener{
            if(main.edit?.text?.isEmpty() == true)
                return@setOnClickListener
            model.times.value=main.edit?.text.toString().toInt()
            model.simulate()
            var temp=model.probability.value!!
            main.txt1.text=temp[0].toString()
            main.txt2!!.text=temp[1].toString()
            main.txt3!!.text=temp[2].toString()
            main.txt4!!.text=temp[3].toString()
            main.txt5!!.text=temp[4].toString()
            main.txt6!!.text=temp[5].toString()
            main.txt7!!.text=temp[6].toString()
            main.txt8!!.text=temp[7].toString()
            main.txt9!!.text=temp[8].toString()
            main.txt10!!.text=temp[9].toString()
            main.txt11!!.text=temp[10].toString()
            main.txt12!!.text=temp[11].toString()
            main.txt13!!.text=temp[12].toString()
        }
    }
}