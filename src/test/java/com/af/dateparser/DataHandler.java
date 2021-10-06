package com.af.dateparser;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class DataHandler {

    static final String readFilePath = "src/main/resources/stocks.csv";
    static final String writeFP = "src/main/resources/updated-stocks.csv";

    public static Stream<Stock> getAllStocks() {

        Stream.Builder<Stock> empStreamBuilder = Stream.builder();

        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(readFilePath));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {

                if (!nextRecord[0].equals("Symbol")) {

                    Stock stock = new Stock();
                    stock.setSymbol(nextRecord[0]);

                    List<DateGroup> parsed = parser.parse(nextRecord[1]);
                    Date date = parsed.get(0).getDates().get(0);
                    calendar.setTime(date);

                    stock.setDate(date);

                    stock.setPrice(Double.parseDouble(nextRecord[2]));
                    stock.setVolume(Long.parseLong(nextRecord[3]));

                    empStreamBuilder.accept(stock);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return empStreamBuilder.build();
    }


    public static void writeDataLineByLine(Stream<Stock> stockStream) {
        // first create file object for file placed at location
        // specified by filepath
        Stock[] stocks = stockStream.toArray(Stock[]::new);
        File file = new File("src/main/resources/updated-stocks.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(writeFP);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"Symbol", "Date", "Price", "Volume"};
            writer.writeNext(header);

            // add data to csv
            for (Stock s :
                    stocks) {
                String[] data = {s.getSymbol(), s.getDate().toString(), s.getPrice() + "", s.getVolume() + ""};
                writer.writeNext(data);
            }

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}