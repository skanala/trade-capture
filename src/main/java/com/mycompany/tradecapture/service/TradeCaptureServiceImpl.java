package com.mycompany.tradecapture.service;

import com.mycompany.tradecapture.dal.Trade;
import com.mycompany.tradecapture.dal.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring JDBC implementation of the TradeCaptureService.
 * Standard JDCTemplate usage for the CRUD operations
 */

@Service
@EnableScheduling
public class TradeCaptureServiceImpl implements TradeCaptureService {
    private Logger logger = LoggerFactory.getLogger(TradeCaptureServiceImpl.class);

    private final TradeRepository tradeRepository;

    public TradeCaptureServiceImpl(final TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    @Transactional
    @Override
    public Trade saveTrade(Trade trade) {

        logger.info("Saving trade {}", trade);
        tradeRepository.save(trade);
        return trade;
    }

    /**
     *
     * @param tradeId
     * @return returns only if the trade has not expired
     */
    @Transactional
    @Override
    public Trade findActiveTrade(String tradeId) {
        return tradeRepository.findActiveTrade(tradeId);
    }

    /**
     *
     * @return returns only the trades that have not expired
     */
    @Transactional
    @Override
    public List<Trade> findAllActiveTrades() {
        return tradeRepository.findActiveTrades();
    }

    /**
     * Scheduled 'task' to updated the expired column for the trades whose maturity date is the past
     */
    @Scheduled(fixedDelay = 10000)
    @Transactional
    @Override
    public void updatedExpiredTradesStatus() {
        tradeRepository.updateExpiredTradesStatus();
    }

    /**
     * Scheduled 'task' to updated the expired column for the trades whose maturity date
     * is before the given date
     */
    @Transactional
    @Override
    public void updatedExpiredTradesStatus(LocalDate date) {
        tradeRepository.updateExpiredTradesStatus(date);
    }
}
