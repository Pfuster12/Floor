package com.yabu.floor.data.model.portfolio

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yabu.floor.utils.Utils

@Entity
class Portfolio(@PrimaryKey
                val id: String = PORTFOLIO_ID,
                val name: String = "My Portfolio",
                val visibleToUser: Boolean = true,
                val items: MutableList<PortfolioItem> = initialPortfolioItems) {

    companion object {
        const val PORTFOLIO_PREFERENCES_KEY = "floored.PORTFOLIO_PREFERENCES_KEY"

        private const val CURRENT_PORTFOLIO_KEY = "floored.CURRENT_PORTFOLIO_KEY"

        const val PORTFOLIO_ID = "my-portfolio"
        const val PORTFOLIO_VIEW_STATE_KEY = "floored.PORTFOLIO_VIEW_STATE_KEY"
        private val initialPortfolioItems = mutableListOf(
            PortfolioItem(Ticker("MSFT", "Microsoft")),
            PortfolioItem(Ticker("AAPL", "Apple")),
            PortfolioItem(Ticker("GS", "Goldman Sachs")),
            PortfolioItem(Ticker("JPM", "JP Morgan")),
            PortfolioItem(Ticker("TSLA", "Tesla")),
            PortfolioItem(Ticker("MCD", "McDonald's")),
            PortfolioItem(Ticker("HON", "Honeywell"))
        )

        const val INDEX_PREFERENCES_KEY = "floored.INDEX_PREFERENCES_KEY"
        const val INDEX_ID = "index-portfolio"
        val initialIndexItems = mutableListOf(
            PortfolioItem(Ticker("SPY", "S&P500")),
            PortfolioItem(Ticker("DIA", "DOW 30")),
            PortfolioItem(Ticker("QQQ", "NASDAQ"))
        )

        fun getCurrentPortfolio(context: Context?): String {
            if (context == null)
                return PORTFOLIO_ID

            return Utils.getSharedPreferences(context)
                .getString(CURRENT_PORTFOLIO_KEY, PORTFOLIO_ID) ?: PORTFOLIO_ID
        }

        fun saveCurrentPortfolio(context: Context?, id: String) {
            if (context == null)
                return

            Utils.getSharedPreferences(context)
                .edit()
                .putString(CURRENT_PORTFOLIO_KEY, id)
                .apply()
        }
    }

    /**
     * Adds a unique ticker to the Portfolio list.
     */
    fun add(ticker: Ticker): Boolean {
        return if (items.size < 25) {
            // Find if ticker already exists in Portfolio.
            val match = items.find { it.ticker.id == ticker.id }

            if (match == null) {
                items.add(PortfolioItem(ticker))
            } else {
                false
            }
        } else {
            false
        }
    }

    /**
     * Removes a ticker if it exists in the Portfolio.
     * @param ticker
     * @return true if a ticker was removed.
     */
    fun remove(ticker: String): Boolean {
        return items.removeIf { it.ticker.id == ticker}
    }

    /**
     * Move item in the Portfolio.
     * @param from
     * @param to
     */
    fun move(from: Int, to: Int) {
        val item = items[from]
        items.remove(item)
        items.add(to, item)
    }
}