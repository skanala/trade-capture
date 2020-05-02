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
 * @author: Sudhir Kanala
 * This class ensures that a Trade in the store cannot be overridden by a lower version Trade
 */
public class VersionCheckTrigger implements Trigger {
    Logger logger = LoggerFactory.getLogger(VersionCheckTrigger.class);

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {

    }

    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) throws SQLException {

        if (objects != null && objects1 != null) {
            Integer oldVersion = (Integer) objects[6];
            Integer newVersion = (Integer) objects1[6];

            if (newVersion < oldVersion) {
                String errorMessage = String.format("Cannot update with an older version %d",oldVersion);
                logger.error(errorMessage);
                throw new SQLException(errorMessage);
            }


            java.sql.Date maturitySqlDate = (java.sql.Date)objects1[3];
            LocalDate maturityDate =
                    Instant.ofEpochMilli(maturitySqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            if(maturityDate.isBefore(LocalDate.now())){
                String errorMessage = String.format("Maturity Date cannot be in the past %s" , maturityDate);
                throw new SQLException(errorMessage);
            }

        }

    }

    @Override
    public void close() throws SQLException {
        System.out.println("firing the close......");
    }

    @Override
    public void remove() throws SQLException {

    }
}
