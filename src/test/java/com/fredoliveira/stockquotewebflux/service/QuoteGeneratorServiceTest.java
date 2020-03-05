package com.fredoliveira.stockquotewebflux.service;

import com.fredoliveira.stockquotewebflux.model.Quote;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class QuoteGeneratorServiceTest {

    QuoteGeneratorService quoteGeneratorService = new QuoteGeneratorServiceImpl();

    @Before
    public void setup() {

    }

    @Test
    public void fetchQuoteStream()  {

        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        quoteFlux.take(10).subscribe(System.out::println);
    }

    @Test
    public void fetchQuoteStreamCountDown() throws InterruptedException {

        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        Consumer<Quote> println = System.out::println;

        Consumer<Throwable> errorHandler = e -> System.out.println("An error occurred.");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable allDone = countDownLatch::countDown;

        quoteFlux.take(10)
                .subscribe(println, errorHandler, allDone);

        countDownLatch.await();
    }
}
