package com.example.runto

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.example.runto.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.util.Calendar

class MainActivity : Activity(){
    private var backPressedTime: Long = 0
    lateinit var binding : ActivityMainBinding
    var fName:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //바인딩 객체 획득
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btRun.setOnClickListener {
            val recordIntent = Intent(this, RunActivity::class.java)
            startActivityForResult(recordIntent,10)
        }

        binding.btModify.setOnClickListener{
            val dialog = AlertDialog.Builder(this).create()
            val edialog: LayoutInflater = LayoutInflater.from(this)
            val mView: View = edialog.inflate(R.layout.time_setting, null)

            val h: NumberPicker = mView.findViewById(R.id.npkHour)
            val m: NumberPicker = mView.findViewById(R.id.npkMin)
            val s: NumberPicker = mView.findViewById(R.id.npkSec)
            val cancel: TextView = mView.findViewById(R.id.btBack)
            val ok: TextView = mView.findViewById(R.id.btStore)

            //  순환 안되게 막기
            h.wrapSelectorWheel = false
            m.wrapSelectorWheel = false
            s.wrapSelectorWheel = false

            //  최소값 설정
            h.minValue = 0
            m.minValue = 0
            s.minValue = 0

            //  최대값 설정
            h.maxValue = 23
            m.maxValue = 59
            s.maxValue = 59

            //초기값 세팅
            h.value = 0
            m.value = 0
            s.value = 0

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            ok.setOnClickListener {
                binding.txRecord.text = "${(h.value).toString()}:${(m.value).toString()}:${(s.value).toString()}"
                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()

        }

        binding.btDelete.setOnClickListener{
            binding.txRecord.text = "오늘의 기록이 없습니다"
            binding.txRecord.textSize = 20f
        }

        binding.btStoreCal.setOnClickListener{
            val calendar = Calendar.getInstance()
            var cYear = calendar.get(Calendar.YEAR)
            var cMonth = calendar.get(Calendar.MONTH)
            var cDay = calendar.get(Calendar.DAY_OF_MONTH)

            fName = "$cYear$cMonth$cDay.txt"

            var fos: FileOutputStream? = null

            try{
                fos = openFileOutput(fName, MODE_PRIVATE)
                var content: String = binding.txRecord.text.toString()
                fos.write(content.toByteArray())
                fos.close()

                Toast.makeText(this, "기록이 저장되었습니다", Toast.LENGTH_SHORT).show();
            }catch(e:Exception){
                e.printStackTrace()
            }


        }

        binding.btCal.setOnClickListener{
            val calIntent = Intent(this, CalActivity::class.java)
            startActivity(calIntent)
        }

    }



    //뒤로 2번 누르면 종료
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("result")
                binding.txRecord.text = result
                binding.txRecord.textSize = 40f
            }
        }
    }
}