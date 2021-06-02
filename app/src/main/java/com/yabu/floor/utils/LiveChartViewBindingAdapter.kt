package com.yabu.floor.utils

import androidx.databinding.BindingAdapter
import com.yabu.floor.data.model.portfolio.PortfolioItem
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChartView

/**
 * Binding Adapter for a [LiveChartView] to set the Dataset with a [PortfolioItem].
 * @param livechart
 * @param portfolioItem [PortfolioItem]
 */
@BindingAdapter("portfolioItem")
fun setPortfolioItem(livechart: LiveChartView, portfolioItem: PortfolioItem?) {
    if (portfolioItem?.historicalData == null
        || portfolioItem.intradayData == null) {
        return
    }

    // Create a Dataset from the historical data points.
    val historicalDataset = Dataset(portfolioItem.historicalData?.mapIndexedNotNull { index, it ->
        if (it.close == null && it.average == null) {
            return@mapIndexedNotNull null
        }

        DataPoint(index.toFloat(),
            if (it.close == null) {
                it.average!!.toFloat()
            } else {
                it.close.toFloat()
            })
    }?.toMutableList() ?: mutableListOf())

    val intradayDataset = Dataset(portfolioItem.intradayData?.mapIndexedNotNull { index, it ->
        if (it.close == null && it.average == null) {
            return@mapIndexedNotNull null
        }

        DataPoint(index.toFloat() + (portfolioItem.historicalData?.size ?: 0).toFloat(),
            if (it.close == null) {
                it.average!!.toFloat()
            } else {
                it.close.toFloat()
            })
    }?.toMutableList() ?: mutableListOf())

    if (historicalDataset.points.isNotEmpty()) {
        intradayDataset.points.add(0,
            DataPoint(
                x = (historicalDataset.points.size - 1).toFloat(),
                y = historicalDataset.points.last().y
            )
        )
    }

    if (intradayDataset.points.size == 0) {
        livechart.setDataset(historicalDataset)
            .drawBaselineConditionalColor()
            .drawDataset()
    } else {
        livechart.setDataset(intradayDataset)
            .setSecondDataset(historicalDataset)
            .drawBaselineConditionalColor()
            .drawDataset()
    }
}