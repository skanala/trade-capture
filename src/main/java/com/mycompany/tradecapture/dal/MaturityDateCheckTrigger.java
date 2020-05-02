package com.mycompany.tradecapture.dal;

import org.h2.api.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * MaturityDateCheckTrigger class ensures that the maturity date cannot be in the past while inserting rows
 */
public class MaturityDateCheckTrigger implements Trigger {
    Logger logger = LoggerFactory.getLogger(MaturityDateCheckTrigger.class);

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
    }

    @Override
    public void fire(Connection connection, Object[] oldRows, Object[] newRows) throws SQLException {

        if(newRows != null) {

            logger.debug("maturity date type {} " , newRows[3].getClass().getName());
            java.sql.Date maturitySqlDate = (java.sql.Date)newRows[3];

            LocalDate maturityDate =
                    Instant.ofEpochMilli(maturitySqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            if(maturityDate.isBefore(LocalDate.now())){
                String errorMessage = String.format("Maturity Date cannot be in the past %s" , maturityDate);
                logger.error(errorMessage);
                throw new SQLException(errorMessage);
            }
        }

    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public void remove() throws SQLException {

    }
}
