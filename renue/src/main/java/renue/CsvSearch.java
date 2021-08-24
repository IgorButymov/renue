package renue;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class CsvSearch extends RowListProcessor {

    private static String stringToMatch;
    private static int columnToMatch;

    @Override
    public void rowProcessed(String[] row, ParsingContext context) {
        String value = row[columnToMatch];
        if (value != null && value.startsWith(stringToMatch)) {
            super.rowProcessed(row, context);
        }
    }

    public void search(String[] args) {


        if (args.length != 0) {

            if (args.length > 1) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    System.out.println("Вы должны ввести только один аргумент - номер строки, по которой будет осуществляться поиск");
                }
            } else {

                columnToMatch = Integer.parseInt(args[0]) - 2;
            }
        } else {
            Properties properties = new Properties();

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")){
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            columnToMatch = Integer.parseInt(properties.getProperty("column")) - 2;
        }

        if (columnToMatch < 0 || columnToMatch > 12) {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("Номер строки должен быть в промежутке от 2 до 14");
            }
        }

        Scanner scanner = new Scanner(System.in);
        stringToMatch = scanner.nextLine();

        long start = System.currentTimeMillis();
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaders("id", "airport", "city","country","IATA_airport_code","ICAO_airport_code","latitude","longitude","height_above_sea_level","time_zone","class","port", "airport_field","OurAirports_field");

        CsvSearch search = new CsvSearch();

        settings.setProcessor(search);
        settings.selectFields("airport", "city","country","IATA_airport_code","ICAO_airport_code","latitude","longitude","height_above_sea_level","time_zone","class","port", "airport_field","OurAirports_field");
        CsvParser parser = new CsvParser(settings);

        parser.parse(new File("data/airports.dat"));

        List<String[]> results = search.getRows();

        results.sort(new CsvComparator(columnToMatch));

        for (String[] result : results) {
            for (String s : result) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

        System.out.println("Количество найденных строк: " + results.size());
        System.out.println("Время, затраченное на поиск: " + (System.currentTimeMillis() - start) + " мс");
    }
}
