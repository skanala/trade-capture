package com.mycompany.tradecapture.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * @author Sudhir Kanala
 * This class is used to create the Trade object from the Trade messages, as part of the stream processing
 */
public class TradeSupplier implements Supplier<Trade> {
    static Logger logger = LoggerFactory.getLogger(TradeSupplier.class);

    public static Trade createTrade(String tradeLine) {
        logger.info("Processing trade {}", tradeLine);

        String[] tradeParts = tradeLine.split(",");
        String tradeId = tradeParts[0];
        int version = Integer.parseInt(tradeParts[1]);
        String counterPartyId = tradeParts[2];
        String bookId = tradeParts[3];
        LocalDate maturityDate = LocalDate.parse(tradeParts[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate createdDate = LocalDate.now();
        boolean isExpired = maturityDate.isBefore(createdDate);

        Trade trade = Trade.of(tradeId, version, counterPartyId, bookId, maturityDate, createdDate, isExpired);
        return trade;
    }


    @Override
    public Trade get() {
        return null;
    }
}
