package com.af.dateparser;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Stream;

public class Task4 {

    public static void main(String[] args) throws IOException {

        Stream<Stock> stocks = DataHandler.getAllStocks();

        Stream<Stock> sortedStocks = stocks.sorted(Comparator.comparing(Stock::getDate)).distinct();

        DataHandler.writeDataLineByLine(sortedStocks);
    }
}



