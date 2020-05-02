package com.mycompany.tradecapture.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sudhir Kanala
 * This class handles any errors that may happen either during the Trade processing or saving
 * At the moment it just logs the error. Could potentially handle retries etc
 */
public class TradeErrorHandler {

    private static Logger logger = LoggerFactory.getLogger(TradeErrorHandler.class);

    public static <T> T processTradeError(T object, Throwable throwable) {
        if (throwable != null) {
            logger.error("Error saving trade {}", throwable.getMessage());
        }
        return object;
    }
}
