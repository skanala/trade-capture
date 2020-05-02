package com.mycompany.tradecapture.service;

import com.mycompany.tradecapture.dal.Trade;

import java.time.LocalDate;
import java.util.List;

/**
 * Primary service for the business logic. In this case mostly DAL operations
 */
public interface TradeCaptureService {

    Trade saveTrade(Trade trade);
    Trade findActiveTrade(String tradeId);
    List<Trade> findAllActiveTrades();
    void updatedExpiredTradesStatus();
    void updatedExpiredTradesStatus(LocalDate date);

}
