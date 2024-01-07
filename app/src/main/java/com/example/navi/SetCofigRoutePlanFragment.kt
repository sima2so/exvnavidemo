package com.example.navi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.time.LocalDateTime

class SetCofigRoutePlanFragment : Fragment() {
    private val args: SetCofigRoutePlanFragmentArgs by navArgs()

    // 選択された目的地情報を取得
    private val selectedGoalItem: Item = args.selectedGoalItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_cofig_route_plan, container, false)

//      ボタンクリックで出発予定時刻を設定
        var typeSetTime: String = "STARTTIME"
        val radiobutton_set_start_time = view.findViewById<RadioButton>(R.id.radiobutton_set_start_time)
        radiobutton_set_start_time.setOnClickListener {typeSetTime = "STARTTIME"}

//      ボタンクリックで到着予定時刻を設定
        val radiobutton_set_goal_time = view.findViewById<RadioButton>(R.id.radiobutton_set_goal_time)
        radiobutton_set_start_time.setOnClickListener {typeSetTime = "GOALTIME"}

//      時刻設定ボックス
        val button_edit_time = view.findViewById<Button>(R.id.button_edit_time)
        val textView_edit_time = view.findViewById<TextView>(R.id.textView_edit_time)
//      時刻設定ボックスの初期値として現在時刻を表示
        val ldt = LocalDateTime.now()
        val hourOfDay = ldt.hour
        val minutes = ldt.minute
        textView_edit_time.text = String.format("%02d:%02d", hourOfDay, minutes)
        var setTime:String = textView_edit_time.text.toString()
//      ボタンクリックでドラムロール式TimePickerを呼び出す
        button_edit_time.setOnClickListener {
            MyTimePicker.showTimePicker(textView_edit_time)
            setTime = textView_edit_time.text.toString()
        }

        // エネルギー利用設定
        val array = listOf("到着時間優先", "空調優先")
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, array)
        spinner.adapter = arrayAdapter

        // 選択されたエネルギー利用設定を取得する
        var energyUsageMode: String = array[0]
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                energyUsageMode = array[pos]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                energyUsageMode = array[0]
            }
        }

        val buttonSearchRoute = view.findViewById<Button>(R.id.button_search_route)
        buttonSearchRoute.setOnClickListener {
            val naviRouteData = NaviRouteData(selectedGoalItem, typeSetTime, setTime, energyUsageMode)
            val action =
                SetCofigRoutePlanFragmentDirections
                    .actionSetCofigRoutePlanFragmentToShowRoutePlanFragment(
                        naviRouteData = naviRouteData
                    )
            findNavController().navigate(action)

        }
        return view
    }

}