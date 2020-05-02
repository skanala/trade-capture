# trade-capture
Spring boot application that captures trades and stores in h2

1. On startup application reads the trades.dat file , creates the Trade objects and stores in H2 (TradeCaptureApplicationRunner)
2. As the messages are read from the file, they are converted into streams to allow for efficient parallel processing. Assumes that the order of messages does not matter. This could be made lot more sophisticated than what it is now  to handle more advanced nuances like, concurrently processing same trade ids etc.Becomes a very scalable solution (TradeCaptureEngine)
3.CompletableFutures are used to process the stream elements asynchronously. This allows for each of the end to end steps to happen asynchronously and in parallel. Also avoids the threads in the thread pool to be blocked by database calls. Flow API or Reactive Spring is an overkill for this use case.
4.The schema is defines in schema.sql which has the table and the triggers. Gets created on startup.
5.H2 triggers are written in java (VersionCheckTrigger and MaturityDateCheckTrigger). They are used to validate Trades are not overwritten with the older versions and for ensuring that trades with expired maturity date are not added to the store.
6.The dal layer is implemented using straight Spring JDBC. Spring data jdbc or Spring data jpa is probably an overkill for this use case. Dal layer ensures that only active trades are ever returned to the client layers.
7. A scheduled job runs to update the expired flag every few seconds. This along with the fact that the find methods would return only non-expired Trades ensures that stale/expired trades are never seen in the client layers.
8. All error handling is routed to a central place (TradeErrorHandler).
