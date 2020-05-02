package com.mycompany.tradecapture.dal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author Sudhir Kanala
 * The primary data access class.Encapsulates all the needed methods for the CRUD operations of the Trade
 *
 */

@Repository
public class TradeRepository {
    Logger logger = LoggerFactory.getLogger(TradeRepository.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Trade> findActiveTrades() {
        return jdbcTemplate.query("SELECT * from tc.Trades where maturity_date > current_date()", (rs, rowNum) -> getTrade(rs));
    }

    public void save(Trade trade) {

        logger.info("Saving trade {}", trade.getTradeId());
        String sql = "MERGE INTO TC.TRADES KEY (TRADE_ID) VALUES (?, ?, ?, ?, ?,?,?);";

        int update = jdbcTemplate.update(sql, trade.getTradeId(), trade.getCounterPartyId(), trade.getBookId()
                , getSqlDate(trade.getMaturityDate()), getSqlDate(trade.getCreatedDate()), trade.getExpired(), trade.getVersion());

        if (update != 1) {
            logger.error("No rows updated " + trade.getTradeId());
        }

    }


    public long count() {
        Long aLong = jdbcTemplate.queryForObject("select count(*) from tc.trades", Long.class);
        return null != aLong ? aLong : 0;
    }

    private Date getSqlDate(LocalDate localDate) {
        return new Date(localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
    }

    public Trade findActiveTrade(String tradeId) {
        List<Object> result = jdbcTemplate.query("SELECT * from tc.Trades where trade_id = ? and MATURITY_DATE > current_date()",
                new Object[]{tradeId}, (rs, rowNum) -> getTrade(rs));
        if (result.size() > 0)
            return (Trade) result.get(0);
        else
            return null;
    }

    public void updateExpiredTradesStatus() {
        String sql = "UPDATE tc.trades SET expired = 'Y' WHERE expired='N' and MATURITY_DATE < current_date()";
        jdbcTemplate.update(sql);
    }

    public void updateExpiredTradesStatus(LocalDate currentDate) {
        String sql = "UPDATE tc.trades SET expired = 'Y' WHERE expired='N' and MATURITY_DATE < ?";
        jdbcTemplate.update(sql, new Date(currentDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));
    }

    private Trade getTrade(ResultSet rs) throws SQLException {
        logger.info("rs fetchsize {}", rs.getFetchSize());
        Trade trade = new Trade();
        trade.setTradeId(rs.getString("TRADE_ID"));
        trade.setCounterPartyId(rs.getString("COUNTERPARTY_ID"));
        trade.setVersion(rs.getInt("VERSION"));
        LocalDate maturityDate =
                Instant.ofEpochMilli(rs.getDate("MATURITY_DATE").getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

        trade.setMaturityDate(maturityDate);
        trade.setBookId(rs.getString("BOOK_ID"));

        trade.setExpired(rs.getString("EXPIRED"));

        LocalDate createdDate =
                Instant.ofEpochMilli(rs.getDate("CREATED_DATE").getTime()).atZone(ZoneId.systemDefault()).toLocalDate();


        trade.setCreatedDate(createdDate);

        return trade;
    }


}