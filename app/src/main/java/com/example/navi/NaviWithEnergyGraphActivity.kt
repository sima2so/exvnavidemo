package com.example.navi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
class NaviWithEnergyGraphActivity : AppCompatActivity() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var cnt=0
    val hnd0= Handler(Looper.getMainLooper())

    private lateinit var energyGraph: EnergyGraph
    private lateinit var lineChart: LineChart

    val rnb0 = object : Runnable{
        override fun run() {
            cnt++

            energyGraph.updateEnergyGraph(lineChart)
            if(cnt<50){
                hnd0.postDelayed(this,1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi_with_energygraph)

        lineChart = findViewById<LineChart>(R.id.line_chart);
        energyGraph = EnergyGraph(lineChart)
        hnd0.post(rnb0)

    }

}
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_navi_with_energygraph, container, false)
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.d("TAG", "onViewCreated: called")
//        super.onViewCreated(view, savedInstanceState)
//
//        lineChart = view.findViewById<LineChart>(R.id.line_chart);
//        energyGraph = EnergyGraph(lineChart)
//        hnd0.post(rnb0)
//    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ShowRoutePlan.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ShowRoutePlanFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }