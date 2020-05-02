package com.mycompany.tradecapture.dal;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;

/**
 * @author Sudhir Kanala
 * This is the main domain class used in the application
 * lombok annotation generates the getters, setters, all args constructor etc
 */
@Data
public class Trade {

    private String tradeId;

    private Long internalVersion;

    private int version;

    private String counterPartyId;

    private String bookId;

    private LocalDate maturityDate;

    private LocalDate createdDate;


    private String expired;

    public static Trade of(String tradeId, int version, String counterPartyId,
                           String bookId, LocalDate maturityDate, LocalDate createdDate,
                           boolean isExpired) {
        Trade trade = new Trade();
        trade.tradeId = tradeId;
        trade.version = version;
        trade.counterPartyId = counterPartyId;
        trade.bookId = bookId;
        trade.maturityDate = maturityDate;
        trade.createdDate = createdDate;
        trade.expired = isExpired ? "Y" : "N";
        return trade;
    }

    public boolean isExpired() {
        return !Strings.isEmpty(expired) && expired.equalsIgnoreCase("Y");
    }
}
