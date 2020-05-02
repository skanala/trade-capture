package com.mycompany.tradecapture;

import com.mycompany.tradecapture.dal.Trade;
import com.mycompany.tradecapture.service.TradeCaptureServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TradeCaptureServiceTests {

    @Autowired
    TradeCaptureServiceImpl tradeCaptureService;


    @Test
    void checkExpiredMaturityDateInserts() {
        Trade trade = new Trade();
        trade.setTradeId("tradeid1");
        trade.setBookId("bookid1");
        trade.setCounterPartyId("ctryprtyid1");
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N");
        trade.setMaturityDate(LocalDate.ofYearDay(2020, 21));
        trade.setVersion(1);


        boolean exceptionOccurred = false;
        try {
            tradeCaptureService.saveTrade(trade);
        }catch (Throwable t){
            exceptionOccurred=true;
        }

        assertEquals(true, exceptionOccurred);

    }


    @Test
    void checkExpiredMaturityDateUpdates() {
        Trade trade = new Trade();
        trade.setTradeId("tradeid1");
        trade.setBookId("bookid1");
        trade.setCounterPartyId("ctryprtyid1");
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N");
        trade.setMaturityDate(LocalDate.ofYearDay(2021, 21));
        trade.setVersion(1);

        tradeCaptureService.saveTrade(trade);

        List<Trade> allActiveTrades = tradeCaptureService.findAllActiveTrades();

        Trade trade1 = tradeCaptureService.findActiveTrade("tradeid1");
        trade1.setVersion(2);
        trade1.setMaturityDate(LocalDate.ofYearDay(2020,1));

        boolean exceptionOccurred = false;
        try {
            tradeCaptureService.saveTrade(trade1);
        }catch (Throwable t){
            exceptionOccurred=true;
        }

        assertEquals(true, exceptionOccurred);

    }

    @Test
    void updateExpiredFlagTest() {
        Trade trade = new Trade();
        trade.setTradeId("tradeid1");
        trade.setBookId("bookid1");
        trade.setCounterPartyId("ctryprtyid1");
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N");
        trade.setMaturityDate(LocalDate.ofYearDay(2021, 21));
        trade.setVersion(1);

        tradeCaptureService.saveTrade(trade);

        tradeCaptureService.updatedExpiredTradesStatus(LocalDate.ofYearDay(2021, 22));

        Trade trade1 = tradeCaptureService.findActiveTrade("tradeid1");

        assertEquals("Y", trade1.getExpired());

    }

}