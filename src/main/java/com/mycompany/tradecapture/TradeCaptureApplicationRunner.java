package com.mycompany.tradecapture;

import com.mycompany.tradecapture.dal.Trade;
import com.mycompany.tradecapture.service.TradeCaptureServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sudhir Kanala
 * Gets called on startup after the Spring context has been initialized.
 */
@Component
class TradeCaptureApplicationRunner implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(TradeCaptureApplicationRunner.class);

    @Autowired
    private TradeCaptureEngine tradeCaptureEngine;

    @Autowired
    private TradeCaptureServiceImpl tradeCaptureService;

    /**
     * Reads the trades from a file and 'processes' them
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        tradeCaptureEngine.processTrades("trades.dat");

        List<Trade> allActiveTrades = tradeCaptureService.findAllActiveTrades();
        logger.info("Finished processing all trades");
        logger.info("Trades in the store are ");
        allActiveTrades.forEach(trade -> logger.info("{}",trade));
    }


}