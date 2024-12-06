package com.example.myalarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tvTime = findViewById<TextView>(R.id.tvTime)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Установка времени будильника
        btnSetAlarm.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    // Отображение времени в TextView
                    tvTime.text = String.format("Будильник установлен на %02d:%02d", hourOfDay, minute)

                    // Установка будильника
                    val intent = Intent(this, AlarmReceiver::class.java)
                    pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exit -> {
                finishAffinity() // Закрыть приложение
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}