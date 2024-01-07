package com.example.navi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs

class ShowSearchGoalResultFragment : Fragment() {
    private val args: ShowSearchGoalResultFragmentArgs by navArgs()

    // 目的地検索ワード取得
    private val goalSearchWord: String = args.goalSearchWord

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_search_goal_result, container, false)

        // ActionBar表示
        val activity = activity as AppCompatActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        toolbar.setTitle("目的地候補")

        // itumonavi_api_search_goal関数で目的地検索結果のitemListを生成
        val itsumoNaviApiClient = ItsumoNaviApiClient()
        val responseSearchGoal = itsumoNaviApiClient.search_goal(goalSearchWord)

        val goalList = mutableListOf<String?>()
        for (i in 0 until (responseSearchGoal.item?.size ?:0)) {
            responseSearchGoal.item?.get(i)?.let { goalList.add(it.text) }
        }

//        デバッグ用
//        var goalList = mutableListOf<String?>("目的地1", "目的地2")

        // 目的地検索結果リストをListViewにセット
        val listViewGoalSearchResult =
            view.findViewById<ListView>(R.id.list_view_goal_search_result)
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, goalList)
        listViewGoalSearchResult.adapter = adapter

        // 項目をタップしたときの処理
        listViewGoalSearchResult.setOnItemClickListener { parent, listview, position, id ->
//          選択された目的地を渡す
            val selectedGoalItem = responseSearchGoal.item?.get(position)
                ?: Item(null,null,null,null,null,null,
                    null,null,null,"EMPTY",null)
            val action =
                ShowSearchGoalResultFragmentDirections
                    .actionShowSearchGoalResultFragmentToSetCofigRoutePlanFragment(
                        selectedGoalItem = selectedGoalItem
                    )
            findNavController().navigate(action)
        }

        return view
    }
}