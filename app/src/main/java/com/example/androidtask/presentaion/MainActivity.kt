package com.example.androidtask.presentaion

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtask.R
import com.google.gson.Gson
import com.mikesu.horizontalexpcalendar.HorizontalExpCalendar.HorizontalExpCalListener
import com.mikesu.horizontalexpcalendar.common.Config.ViewPagerType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}