package com.example.runto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.runto.databinding.ActivityRunBinding
import java.util.Timer

class RunActivity : Activity() {

    // 멈춘 시각을 저장하는 속성
    var pauseTime = 0L
    //바인딩 객체 획득
    lateinit var binding : ActivityRunBinding

    private var time = 0
    private var timerTask : Timer? = null
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRunBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        //화면 출력 XML 명시
        setContentView(binding.root)

        binding.btTimeStart.setOnClickListener{
            isRunning = !isRunning
            if (isRunning) start() else pause()
        }
        binding.btTimeReset.setOnClickListener{
            reset()
        }

        //종료
        binding.btRunClose.setOnClickListener{
            val recordIntent = Intent()
            val resultRecord = "${time/3600}:${time/60}:${time}"
            recordIntent.putExtra("result", resultRecord)
            setResult(RESULT_OK, recordIntent)
            finish()
        }
    }

    private fun start(){
        binding.btTimeStart.text="중지"
        timerTask = kotlin.concurrent.timer(period = 1000){
            time++
            val hour = time/3600
            val min = time/60
            val sec = time%60

            runOnUiThread{
                binding.txHour.text = "$hour"
                binding.txMin.text = "$min"
                binding.txSec.text = "$sec"
            }
        }
    }
    private fun pause(){
        binding.btTimeStart.text="계속"
        timerTask?.cancel();
    }
    private fun reset() {
        timerTask?.cancel() // timerTask가 null이 아니라면 cancel() 호출

        time = 0 // 시간저장 변수 초기화
        isRunning = false // 현재 진행중인지 판별하기 위한 Boolean변수 false 세팅
        binding.txSec.text = "0" // 시간(초) 초기화
        binding.txMin.text = "0" // 시간(초) 초기화
        binding.txHour.text = "0" // 시간 초기화

        binding.btTimeStart.text ="시작"
    }

}