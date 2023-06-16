package com.example.runto

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.runto.databinding.ActivityCalBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalActivity : Activity() {
    lateinit var binding : ActivityCalBinding
    var fname : String=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            fname = "$year$month$dayOfMonth.txt"
            checkedDay(year,month,dayOfMonth)
        }

        binding.btTodayDel.setOnClickListener{

            var fos: FileOutputStream? = null

            try{
                binding.txToday.text = "오늘의 기록이 없습니다"
                fos = openFileOutput(fname, MODE_PRIVATE)
                var content: String = binding.txToday.text.toString()
                fos.write(content.toByteArray())
                fos.close()

                Toast.makeText(this, "기록이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
    }
    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "$cYear$cMonth$cDay.txt"
// 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
            fis = openFileInput(fname) // fname 파일 오픈!!

            val fileData = ByteArray(fis.available())
            fis.read(fileData) // fileData를 읽음
            fis.close()

            var str = String(fileData) // str 변수에 fileData를 저장

            binding.txToday.text = "${str}" // textView에 str 출력

        }catch(e: Exception){
            binding.txToday.text = "오늘의 기록이 없습니다"
            e.printStackTrace()
        }
        }
    }