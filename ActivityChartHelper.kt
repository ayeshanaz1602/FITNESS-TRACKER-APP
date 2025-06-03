    val data = BarData(dataSet)
    barChart.data = data
    barChart.description.isEnabled = false
    barChart.xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(listOf("Steps", "Distance", "Calories"))
        position = XAxis.XAxisPosition.BOTTOM
        granularity = 1f
    }
    barChart.invalidate()
}
