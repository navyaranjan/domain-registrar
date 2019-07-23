package domainregistrar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.*;
import com.google.common.net.InternetDomainName;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.apache.commons.cli.*;

public class App {

    private static List<DomainType> domainTypes = new ArrayList<>();

    public static void main(String[] args) throws ParseException {

        Options options = new Options();

        options.addOption("h", "help", false, "Prints this help message");
        options.addOption("f", "file", true, "Path to file containing Domain list");
        // options.addOption("c", "count", true, "Number of Domains to Calculate");

        // Parse options
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Help Formatter to print help messages
        HelpFormatter formatter = new HelpFormatter();

        if (cmd.hasOption("h")) {
            formatter.printHelp("java -jar domain-registrar-all.jar", options);
        } else if (cmd.hasOption("f")) {
            String domainFile = cmd.getOptionValue("f");

            addNewDomainSuffix();
            registration(domainFile);
        } else {
            formatter.printHelp("java -jar domain-registrar-all.jar", options);
        }

        // System.out.println("\nDomain registrar's price list (per year) of domain
        // registration per in a zone");
        // System.out.println("========================================");
        // printPriceList(Type.NORMAL);
        // System.out.println("\nDomain registrar's price list (per year) of premium
        // domain registrations");
        // System.out.println("========================================");
        // printPriceList(Type.PREMIUM);
        // // if (args[0] != null)
        // registration(args[0]);

    }

    public static void printPriceList(Type type) {

        domainTypes.stream().filter(typ -> typ.getType().equals(type))
                .forEach(typ -> System.out.println(typ.getDomain() + " - " + typ.getPrice()));

    }

    public static void addNewDomainSuffix() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream stream = App.class.getResourceAsStream("/domaintypes.json");
            domainTypes = Arrays.asList(mapper.readValue(stream, DomainType[].class));
            // domainTypes.stream().forEach(x -> System.out.println(x.getDomain()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static void registration(String str) {
        Double totalPrice = 0.0;
        Reader reader;
        CSVParser csvParser;
        Map<String, Integer> domains = new HashMap<>();
        try {
            reader = Files.newBufferedReader(Paths.get(str));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                domains.put(csvRecord.get(0), Integer.parseInt(csvRecord.get(1)));
                System.out.println(csvRecord.get(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Double> premium = domainTypes.stream().filter(typ -> typ.getType().equals(Type.PREMIUM))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));

        Map<String, Double> normal = domainTypes.stream().filter(typ -> typ.getType().equals(Type.NORMAL))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));

        for (Map.Entry<String, Integer> entry : domains.entrySet()) {
            System.out.println("Inside entries");
            if (premium.containsKey(entry.getKey())) {
                totalPrice = totalPrice + entry.getValue() * premium.get(entry.getKey());
                System.out.println("Total price");
            } else if (normal.containsKey(getSuffix(entry.getKey()))) {
                totalPrice = totalPrice + entry.getValue() * normal.get(getSuffix(entry.getKey()));
                System.out.println("Total price " + totalPrice);
            } else {
                System.out.println(getSuffix(entry.getKey()) + " Not a valid domain name");
            }
        }

    }

    public static String getSuffix(String key) {
        StringBuilder builder = new StringBuilder();
        String suffix = builder.append(".").append(InternetDomainName.from(key).publicSuffix()).toString();
        return suffix;
    }
}
