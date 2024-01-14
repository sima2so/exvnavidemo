package com.example.navi

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private lateinit var fusedLocationClient: FusedLocationProviderClient
    fun getLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // ActionBar表示
        val activity = activity as AppCompatActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        toolbar.setTitle("Menu")

        val buttonSetRoute = view.findViewById<Button>(R.id.button_SetRoute)
        buttonSetRoute.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_inputGoalSearchWordFragment)
        }

        val button_StartNavi = view.findViewById<Button>(R.id.button_StartNavi)


        val webView: WebView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

//        val jsapi_url = ItsumoNaviApiClient().gen_itumonavi_JSAPI_loader_url()

        val itsumoNaviApiClient = ItsumoNaviApiClient()
        webView.addJavascriptInterface(itsumoNaviApiClient, "Android_itsumoNaviApiClient")

//        val location = getLocation(activity)
        val pointFrom = Point(35.6806275, 139.8015336)
        val pointTo = Point(35.6659792, 139.74036)
        val itsumoNaviApiClientJS = itsumoNaviApiClient.ItsumoNaviApiClientJS(pointFrom,pointTo)
        webView.addJavascriptInterface(itsumoNaviApiClientJS, "Android_itsumoNaviApiClientJS")
        val params = itsumoNaviApiClientJS.gen_itumonavi_JSAPI_route3_params()

        webView.loadUrl("file:///android_asset/index.html")
//        // WebView内で新しいリンクがクリックされた時に新しいWebViewで開かないようにする
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                // ページの読み込み完了後に<script>要素を挿入する
//                val script = "javascript:(function() {" +
//                        "var script = document.createElement('script');" +
//                        "script.type = 'text/javascript';" +
//                        "script.src = '" + jsapi_url + "';" +
//                        "document.body.appendChild(script);" +
//                        "})();"
//                view?.loadUrl(script)
//            }
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(webView, url)
//
//                view?.evaluateJavascript("loadMap()", null)
//            }
//        }


        button_StartNavi.setOnClickListener {
//            val selectedGoalItem = Item(null,null,null,null,null,null,null,null,null,null,null)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNaviWithEnergyGraphActivity())
//            val intent = Intent(this,NaviWithEnergyGraphActivity::class.java)
//            startActivity(intent)
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
