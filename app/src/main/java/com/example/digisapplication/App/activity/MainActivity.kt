package com.example.digisapplication.Activity

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.digisapplication.Network.Coroutines
import com.example.digisapplication.Network.MyApi
import com.example.digisapplication.Network.NetworkState
import com.example.digisapplication.Network.utils.add
import com.example.digisapplication.Network.utils.remove
import com.example.digisapplication.R
import com.example.digisapplication.databinding.ActivityMainBinding
import com.example.digisapplication.userrepo.DataRepository
import com.example.digisapplication.viewmodefac.MainActivityModelFactory
import com.example.digisapplication.viewmodel.MainActivityViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , NetworkState.NetworkStateReceiverListener{

    lateinit var model: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    private val mHandler: Handler = Handler()
    private var networkState: NetworkState? = null

    var countNoRSRP_P: Int = 0
    var countNoRSRP_S1: Int = 0
    var countNoRSRP_S2: Int = 0

    var countNoRSRQ_P: Int = 0
    var countNoRSRQ_S1: Int = 0
    var countNoRSRQ_S2: Int = 0

    var countNoSINR_P: Int = 0
    var countNoSINR_S1: Int = 0
    var countNoSINR_S2: Int = 0
    var RSRP_No: Float = 0f

    var values1 = ArrayList<Entry>()
    var values2 = ArrayList<Entry>()
    var values3 = ArrayList<Entry>()

    var values4 = ArrayList<Entry>()
    var values5 = ArrayList<Entry>()
    var values6 = ArrayList<Entry>()

    var values7 = ArrayList<Entry>()
    var values8 = ArrayList<Entry>()
    var values9 = ArrayList<Entry>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        model = ViewModelProvider(
            this,
            MainActivityModelFactory(DataRepository(MyApi()))
        ).get(MainActivityViewModel::class.java)

        binding.model = model
        binding.lifecycleOwner = this


        setChartRSRP()
        setChartRSRQ()
        setChartSINR()

        //Network state
        networkState = NetworkState()
        networkState!!.addListener(this)

        startRepeating()




    }

    private fun setChartSINR() {
        binding.chart3.getDescription().setEnabled(false)
        binding.chart3.setTouchEnabled(true)
        binding.chart3.setDragDecelerationFrictionCoef(0.9f)

        binding.chart3.setDragEnabled(true)
        binding.chart3.setScaleEnabled(true)
        binding.chart3.setDrawGridBackground(false)
        binding.chart3.setHighlightPerDragEnabled(true)
        binding.chart3.setPinchZoom(true)
        binding.chart3.setBackgroundColor(Color.LTGRAY)

        binding.chart3.animateX(1500)

        val l: Legend = binding.chart3.getLegend()

        l.form = LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val xAxis: XAxis = binding.chart3.getXAxis()
        xAxis.axisMaximum = 120f
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis: YAxis = binding.chart3.getAxisLeft()
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 30f
        leftAxis.axisMinimum = -10f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis: YAxis = binding.chart3.getAxisRight()
        rightAxis.textColor = Color.RED
        rightAxis.axisMaximum = 30f
        rightAxis.axisMinimum = -10f
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.isGranularityEnabled = false
    }

    private fun setChartRSRQ() {
        binding.chart2.getDescription().setEnabled(false)
        binding.chart2.setTouchEnabled(true)
        binding.chart2.setDragDecelerationFrictionCoef(0.9f)

        binding.chart2.setDragEnabled(true)
        binding.chart2.setScaleEnabled(true)
        binding.chart2.setDrawGridBackground(false)
        binding.chart2.setHighlightPerDragEnabled(true)
        binding.chart2.setPinchZoom(true)
        binding.chart2.setBackgroundColor(Color.LTGRAY)

        binding.chart2.animateX(1500)

        val l: Legend = binding.chart2.getLegend()

        l.form = LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val xAxis: XAxis = binding.chart2.getXAxis()
        xAxis.axisMaximum = 120f
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis: YAxis = binding.chart2.getAxisLeft()
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 0f
        leftAxis.axisMinimum = -30f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis: YAxis = binding.chart2.getAxisRight()
        rightAxis.textColor = Color.RED
        rightAxis.axisMaximum = 0f
        rightAxis.axisMinimum = -30f
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.isGranularityEnabled = false
    }

    private fun setChartRSRP() {
        binding.chart1.getDescription().setEnabled(false)
        binding.chart1.setTouchEnabled(true)
        binding.chart1.setDragDecelerationFrictionCoef(0.9f)

        binding.chart1.setDragEnabled(true)
        binding.chart1.setScaleEnabled(true)
        binding.chart1.setDrawGridBackground(false)
        binding.chart1.setHighlightPerDragEnabled(true)
        binding.chart1.setPinchZoom(true)
        binding.chart1.setBackgroundColor(Color.LTGRAY)

        binding.chart1.animateX(1500)

        val l: Legend = binding.chart1.getLegend()

        l.form = LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val xAxis: XAxis = binding.chart1.getXAxis()
        xAxis.axisMaximum = 120f
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis: YAxis = binding.chart1.getAxisLeft()
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = -60f
        leftAxis.axisMinimum = -140f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis: YAxis = binding.chart1.getAxisRight()
        rightAxis.textColor = Color.RED
        rightAxis.axisMaximum = -60f
        rightAxis.axisMinimum = -140f
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.isGranularityEnabled = false
    }

    private fun setData(
        count: Int,
        range: Float,
        Chart: LineChart,
        values_1: ArrayList<Entry>,
        values_2: ArrayList<Entry>,
        values_3: ArrayList<Entry>,
        valueName1: String,
        valueName2: String,
        valueName3: String
    ) {                 // data is exist ...

        var set1: LineDataSet
        var set2: LineDataSet
        var set3: LineDataSet
        if (Chart.getData() != null &&
            Chart.getData().getDataSetCount() > 0
        ) {
            set1 = Chart.getData().getDataSetByIndex(0) as LineDataSet
            set2 = Chart.getData().getDataSetByIndex(1) as LineDataSet
            set3 = Chart.getData().getDataSetByIndex(2) as LineDataSet
            set1.values = values_1
            set2.values = values_2
            set3.values = values_3
            Chart.getData().notifyDataChanged()
            Chart.notifyDataSetChanged()
            Chart.invalidate()

        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values_1, valueName1)
            set1.axisDependency = AxisDependency.LEFT
            set1.color = ColorTemplate.getHoloBlue()
            set1.setCircleColor(Color.WHITE)
            set1.lineWidth = 2f
            set1.circleRadius = 3f
            set1.fillAlpha = 65
            set1.fillColor = ColorTemplate.getHoloBlue()
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.setDrawCircleHole(false)
            set1.setDrawValues(false)

            set2 = LineDataSet(values_2, valueName2)
            set2.axisDependency = AxisDependency.RIGHT
            set2.color = Color.RED
            set2.setCircleColor(Color.WHITE)
            set2.lineWidth = 2f
            set2.circleRadius = 3f
            set2.fillAlpha = 65
            set2.fillColor = Color.RED
            set2.setDrawCircleHole(false)
            set2.highLightColor = Color.rgb(244, 117, 117)
            set2.setDrawValues(false)

            set3 = LineDataSet(values_3, valueName3)
            set3.axisDependency = AxisDependency.RIGHT
            set3.color = Color.YELLOW
            set3.setCircleColor(Color.WHITE)
            set3.lineWidth = 2f
            set3.circleRadius = 3f
            set3.fillAlpha = 65
            set3.fillColor = ColorTemplate.colorWithAlpha(Color.YELLOW, 200)
            set3.setDrawCircleHole(false)
            set3.highLightColor = Color.rgb(244, 117, 117)
            set3.setDrawValues(false)

            val data = LineData(set1, set2, set3)
            data.setValueTextColor(Color.WHITE)
            data.setValueTextSize(9f)

            // set data
            Chart.setData(data)
        }
    }


    private fun getData() {
        Coroutines.main {
            val response = model.getData().value.await()
            response?.observe(this, Observer {
                if (it != null) {
                  //  Toast.makeText(this, "Success ...." + it.RSRP, Toast.LENGTH_SHORT).show()

                    //--- Data RSRP ...
                    DataRSRPClassification(it.RSRP.toFloat())
                    //--- Data RSRQ...
                    DataRSRQClassification(it.RSRQ.toFloat())
                    //--- Data SINR...
                    DataSINRClassification(it.SINR.toFloat())

                    //--- Progress Bar Coloring...
                    ProgressBarCustumiation(it.RSRP.toFloat(), it.RSRQ.toFloat(), it.SINR.toFloat())

                    response.removeObservers(this)
                }
            })
        }
    }

    private fun ProgressBarCustumiation(RSRP: Float, RSRQ: Float, SINR: Float) {

        //--- Coloring progress for RSRP
        binding.pbRSRP.value = RSRP.toInt()
        binding.pbRSRP.setProgressStartColor(resources.getColor(R.color.yellowCol))
        binding.pbRSRP.setProgressCompleteColor(resources.getColor(R.color.greenCol))
        //--- Coloring progress for RSRQ
        binding.pbRSRQ.value = RSRQ.toInt()
        binding.pbRSRQ.setProgressStartColor(resources.getColor(R.color.yellowCol))
        binding.pbRSRQ.setProgressCompleteColor(resources.getColor(R.color.greenCol))
        //--- Coloring progress for SINR
        binding.pbSNIR.value = SINR.toInt()
        binding.pbSNIR.setProgressStartColor(resources.getColor(R.color.yellowCol))
        binding.pbSNIR.setProgressCompleteColor(resources.getColor(R.color.greenCol))
    }

    private fun DataSINRClassification(SINR_Value: Float) {
        if (SINR_Value > 10.0) {
            if (countNoSINR_P <= 120) {
                RSRP_No = SINR_Value
                values7.add(Entry(countNoSINR_P.toFloat(), RSRP_No))
                setData(
                    countNoSINR_P,
                    RSRP_No,
                    binding.chart3,
                    values7,
                    values8,
                    values9,
                    "SINR P",
                    "SINR S1",
                    "SINR S2"
                )
                countNoSINR_P++
            }
        } else if (SINR_Value <= 10.0 && SINR_Value >= 0) {
            if (countNoSINR_S1 <= 120) {
                RSRP_No = SINR_Value
                values8.add(Entry(countNoSINR_S1.toFloat(), RSRP_No))
                setData(
                    countNoSINR_S1,
                    RSRP_No,
                    binding.chart3,
                    values7,
                    values8,
                    values9,
                    "SINR P",
                    "SINR S1",
                    "SINR S2"
                )
                countNoSINR_S1++
            }

        } else if (SINR_Value < 0) {
            if (countNoSINR_S2 <= 120) {
                RSRP_No = SINR_Value
                values9.add(Entry(countNoSINR_S2.toFloat(), RSRP_No))
                setData(
                    countNoSINR_S2,
                    RSRP_No,
                    binding.chart3,
                    values7,
                    values8,
                    values9,
                    "SINR P",
                    "SINR S1",
                    "SINR S2"
                )
                countNoSINR_S2++
            }
        }
    }

    private fun DataRSRQClassification(RSRQ_Value: Float) {
        if (RSRQ_Value > -10.0) {
            if (countNoRSRQ_P <= 120) {
                RSRP_No = RSRQ_Value
                values4.add(Entry(countNoRSRQ_P.toFloat(), RSRP_No))
                setData(
                    countNoRSRQ_P,
                    RSRP_No,
                    binding.chart2,
                    values4,
                    values5,
                    values6,
                    "RSRQ P",
                    "RSRQ S1",
                    "RSRQ S2"
                )
                countNoRSRQ_P++
            }
        } else if (RSRQ_Value <= -10.0 && RSRQ_Value >= -20.0) {
            if (countNoRSRQ_S1 <= 120) {
                RSRP_No = RSRQ_Value
                values5.add(Entry(countNoRSRQ_S1.toFloat(), RSRP_No))
                setData(
                    countNoRSRQ_S1,
                    RSRP_No,
                    binding.chart2,
                    values4,
                    values5,
                    values6,
                    "RSRQ P",
                    "RSRQ S1",
                    "RSRQ S2"
                )
                countNoRSRQ_S1++
            }

        } else if (RSRQ_Value < -20.0) {
            if (countNoRSRQ_S2 <= 120) {
                RSRP_No = RSRQ_Value
                values6.add(Entry(countNoRSRQ_S2.toFloat(), RSRP_No))
                setData(
                    countNoRSRQ_S2,
                    RSRP_No,
                    binding.chart2,
                    values4,
                    values5,
                    values6,
                    "RSRQ P",
                    "RSRQ S1",
                    "RSRQ S2"
                )
                countNoRSRQ_S2++
            }
        }
    }

    private fun DataRSRPClassification(RSRP_Value: Float) {
        if (RSRP_Value > -80.0) {
            if (countNoRSRP_P <= 120) {
                RSRP_No = RSRP_Value
                values1.add(Entry(countNoRSRP_P.toFloat(), RSRP_No))
                setData(
                    countNoRSRP_P,
                    RSRP_No,
                    binding.chart1,
                    values1,
                    values2,
                    values3,
                    "RSRP P",
                    "RSRP S1",
                    "RSRP S2"
                )
                countNoRSRP_P++
            }
        } else if (RSRP_Value <= -80.0 && RSRP_Value >= -90.0) {
            if (countNoRSRP_S1 <= 120) {
                RSRP_No = RSRP_Value
                values2.add(Entry(countNoRSRP_S1.toFloat(), RSRP_No))
                setData(
                    countNoRSRP_S1,
                    RSRP_No,
                    binding.chart1,
                    values1,
                    values2,
                    values3,
                    "RSRP P",
                    "RSRP S1",
                    "RSRP S2"
                )
                countNoRSRP_S1++
            }

        } else if (RSRP_Value < -90.0) {
            if (countNoRSRP_S2 <= 120) {
                RSRP_No = RSRP_Value
                values3.add(Entry(countNoRSRP_S2.toFloat(), RSRP_No))
                setData(
                    countNoRSRP_S2,
                    RSRP_No,
                    binding.chart1,
                    values1,
                    values2,
                    values3,
                    "RSRP P",
                    "RSRP S1",
                    "RSRP S2"
                )
                countNoRSRP_S2++
            }
        }
    }

    fun startRepeating() {
        mToastRunnable.run()
    }

    private val mToastRunnable: Runnable = object : Runnable {

        override fun run() {
            this@MainActivity.runOnUiThread({
                getData()
                mHandler.postDelayed(this, 2000)
            })
        }
    }

    fun stopRepeating() {
        mHandler.removeCallbacks(mToastRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeating()
    }

    override fun onPause() {
        super.onPause()
        stopRepeating()
        try {
            unregisterReceiver(networkState)
        }catch (e:Exception){
            Log.e("AllDebug:NetworkState","Error:${e.message}")
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(networkState, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun networkAvailable() {
        no_internet_message.remove()
        Log.d("network Status","network not available")
        Toast.makeText(this, "is avaliable", Toast.LENGTH_SHORT).show()
    }

    override fun networkUnavailable() {
        no_internet_message?.add()
        Log.d("network Status","network available")
        Toast.makeText(this, "Not avaliable", Toast.LENGTH_SHORT).show()
    }

}

