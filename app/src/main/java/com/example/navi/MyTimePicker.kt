package com.example.navi

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object MyTimePicker {

    fun showTimePicker (eText : TextView) {

        // 時刻入力フィールドが空白など日付ではないときの初期値として、12時を設定
        var hourOfDay = 12
        var minutes = 0

        // 時刻入力フィールドの値が時刻の場合はその値を設定
        val ldt = toLocaleTime(eText.text.toString())
//        val ldt = toLocaleTime(eText.text.toString())
        if (ldt != null) {
            hourOfDay = ldt.hour
            minutes = ldt.minute
        }

        // ドラム式TimePicker表示
        val picker = TimePickerDialog(
            eText.rootView.context,

            // ↓この行をコメントアウトすると標準の時刻入力ダイアログになります。
            AlertDialog.THEME_HOLO_LIGHT,   // テーマ：ドラム式 背景白

            // ダイアログでOKをクリックされたときの処理 時刻入力フィールドへ値を設定
            { _, getHour, getMinutes
                -> eText.setText(String.format("%02d:%02d",
                getHour,
                getMinutes))
            },

            // TimePickerが初期表示する時刻
            hourOfDay,
            minutes,

            // 将来選択できるようにするかもだけど、とりあえず24時間表記
            true
        )
        picker.show()

    }

    private const val TIME_FORMAT = "yyyy/MM/dd HH:mm"

    fun toLocaleTime(stringTime : String) : LocalDateTime? {
        val df = DateTimeFormatter.ofPattern(TIME_FORMAT)
        // 時刻だけだとLocalDateTimeにうまく変換できないので、仮で2001年1月1日にしてる。
        return try { LocalDateTime.parse("2001/01/01 " + stringTime, df) } catch (t: Throwable) { null }
    }

}