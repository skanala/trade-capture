package com.mycompany.tradecapture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TradeCaptureEngineTests {

    @Autowired
    TradeCaptureEngine tradeCaptureEngine;

    @Test
    void testTradesProcessing(){
        try {
            long count = tradeCaptureEngine.processTrades("testtrades.dat");
            assertEquals(4,count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}