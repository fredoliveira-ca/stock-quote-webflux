package com.fredoliveira.stockquotewebflux.service;

import com.fredoliveira.stockquotewebflux.model.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);

}
