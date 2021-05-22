package com.epam.sber.dto

import com.epam.sber.model.Quote

data class QuoteDto(
    val isin: String,
    val bid: Float?,
    val ask: Float?
) {
    fun toQuote() = Quote(isin = this.isin, bid = this.bid, ask = this.ask)

    /**
     * Реализовать простую валидацию входных данных для котировки:
     * •bid должен быть меньшеask
     * •isin –12 символов
     */
    fun isValid(): Boolean {
        //TODO узнать, что делать с неверными
        return isin.length == 12 &&
               bid != null &&
               ask != null &&
               bid < ask
    }

    /**
     * elvl (energy level) –лучшая цена по данному инструменту (isin).
     * Правила расчёта elvl на основе котировки:
     * 1.Если bid > elvl, то elvl = bid
     * 2.Если ask < elvl, то elvl = ask
     * 3.Если значение elvl для данной бумаги отсутствует, то elvl = bid
     * 4.Если bid отсутствует, то elvl = ask
     */
    //TODO null safe read
    fun getElvl():Float {
        var elvl = 0F
        //1
        if ( bid != null && bid > elvl ) {
            elvl = bid
        }
        //2
        if (ask != null && ask < elvl) {
            elvl = ask
        }
        //3
        if (elvl == null && bid != null) {
            elvl = bid
        }
        //4
        if (bid == null && ask != null) {
            elvl = ask
        }

        return elvl
    }
}
