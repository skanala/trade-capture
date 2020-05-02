package com.mycompany.tradecapture;

import com.mycompany.tradecapture.dal.Trade;
import com.mycompany.tradecapture.dal.TradeErrorHandler;
import com.mycompany.tradecapture.dal.TradeSupplier;
import com.mycompany.tradecapture.service.TradeCaptureServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * The core part of the application that processes the trades
 * implemented using streams and CompletableFutures
 * streams to enable parallel process and CompletableFutures to asynchronously process multiple steps
 * without blocking of the threads in the thread pool
 * Assumes tha the order in which the records are processed does not matter
 * could enhance this further to group by the tradeid and process all messages
 * of a given trade sequentially
 * Exceptions while processing are caught and redirected to a ErrorHandler.
 * */
@Component
class TradeCaptureEngine {
    private Logger logger = LoggerFactory.getLogger(TradeCaptureApplicationRunner.class);

    @Autowired
    private TradeCaptureServiceImpl tradeCaptureService;

    long processTrades(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        String filePath = resource.getFile().getPath();
        Path path = Paths.get(filePath);
        logger.info("reading from data file {}", filePath);


        List<CompletableFuture<Void>> collect = Files.readAllLines(path).stream().skip(1)
                .map(tradeLine ->
                        CompletableFuture.supplyAsync(() -> TradeSupplier.createTrade(tradeLine))
                                .handle(TradeErrorHandler::processTradeError)
                )
                .map((CompletableFuture<Trade> future) ->
                        future.thenAccept(tradeCaptureService::saveTrade)
                                .handle(TradeErrorHandler::processTradeError)).collect(Collectors.toList());

        collect.forEach(future -> {

            try {
                future.get(60000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.error("Error waiting for the trade processing");
            }


        });

        return collect.size();
    }
}
