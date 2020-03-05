package com.fredoliveira.stockquotewebflux.service;

import com.fredoliveira.stockquotewebflux.model.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class QuoteGeneratorServiceImpl implements QuoteGeneratorService {

    private final MathContext mathContext = new MathContext(2);
    private final Random random = new Random();
    private final List<Quote> prices = new ArrayList<>();

    public QuoteGeneratorServiceImpl() {
        this.prices.add(Quote.builder().ticker("RYAOF").price(new BigDecimal(15.41)).build());
        this.prices.add(Quote.builder().ticker("PSMMF").price(new BigDecimal(31.77)).build());
        this.prices.add(Quote.builder().ticker("AMSYF").price(new BigDecimal(17.04)).build());
        this.prices.add(Quote.builder().ticker("QRTEA").price(new BigDecimal(10.47)).build());
        this.prices.add(Quote.builder().ticker("TTD").price(new BigDecimal(217.01)).build());
    }

    @Override
    public Flux<Quote> fetchQuoteStream(Duration period) {
        return Flux.generate(() -> 0,
                (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) -> {
                    Quote updatedQuote = updateQuote(this.prices.get(index));
                    sink.next(updatedQuote);
                    return ++index % this.prices.size();
                })
                .zipWith(Flux.interval(period))
                 .map(Tuple2::getT1)
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                })
                .log("Service Quote Generator");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange = quote.getPrice()
                .multiply(new BigDecimal(0.05 * this.random.nextDouble()), this.mathContext);
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }

}
