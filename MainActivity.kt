private var totalSteps = 0
private var previousTotalSteps = 0

private lateinit var stepsText: TextView
private lateinit var distanceText: TextView
private lateinit var caloriesText: TextView
private lateinit var chart: BarChart

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    stepsText = findViewById(R.id.stepsText)
    distanceText = findViewById(R.id.distanceText)
    caloriesText = findViewById(R.id.caloriesText)
    chart = findViewById(R.id.barChart)

    loadData()
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    if (stepSensor == null) {
        Toast.makeText(this, "Step Sensor not available!", Toast.LENGTH_SHORT).show()
    }
}

override fun onResume() {
    super.onResume()
    stepSensor?.also {
        sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
    }
}

override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
}

override fun onSensorChanged(event: SensorEvent?) {
    if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
        totalSteps = event.values[0].toInt()
        val todaySteps = totalSteps - previousTotalSteps

        val distance = CalorieUtils.stepsToDistance(todaySteps)
        val calories = CalorieUtils.distanceToCalories(distance)

        stepsText.text = "$todaySteps Steps"
        distanceText.text = String.format("%.2f m", distance)
        caloriesText.text = String.format("%.1f kcal", calories)

        ActivityChartHelper.setupBarChart(chart, todaySteps, distance, calories)
    }
}

override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

private fun loadData() {
    val sharedPref = getSharedPreferences("fitnessPrefs", Context.MODE_PRIVATE)
    previousTotalSteps = sharedPref.getInt("prevSteps", 0)
}

private fun saveData() {
    val sharedPref = getSharedPreferences("fitnessPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putInt("prevSteps", totalSteps)
        apply()
    }
}

override fun onDestroy() {
    super.onDestroy()
    saveData()
}
