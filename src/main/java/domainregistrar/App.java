package domainregistrar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {

                formatter.printHelp("java -jar domain-registrar-all.jar", options);

            } else if (cmd.hasOption("f")) {
                parseDomainCostList();
                String domainFile = cmd.getOptionValue("f");
                registration(domainFile);

            } else {

                formatter.printHelp("java -jar domain-registrar-all.jar", options);
            }
        } catch (Exception e) {
            System.out.println("CommandLine Argument Missing. Aborting!");
        }
    }

    // method for parsing domain cost list from json file to List using Jackson
    public static void parseDomainCostList() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream stream = App.class.getResourceAsStream("/domaintypes.json");
            domainTypes = Arrays.asList(mapper.readValue(stream, DomainType[].class));
        } catch (FileNotFoundException e) {
            System.out.println("Error reading from the cost backend. FileNotFound Exception. Aborting!");
        } catch (JsonMappingException e) {
            System.out.println("Could not read from the cost backend. JsonMappingException.Aborting!");
        } catch (IOException e) {
            System.out.println("Could not read from the cost backend. IOException. Aborting!");
        }

    }

    // method for user's domain registration
    public static void registration(String requestFilePath) {
        Double totalCost = 0.0;
        Map<String, Integer> domains = new HashMap<>();

        // Parsing the request CSV file using CSV Parser
        try {
            Reader reader = Files.newBufferedReader(Paths.get(requestFilePath));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                domains.put(csvRecord.get(0), Integer.parseInt(csvRecord.get(1)));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        // Map for premium domains
        Map<String, Double> premium = domainTypes.stream().filter(typ -> typ.getType().equals(Type.PREMIUM))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));

        // Map for Top level domains
        Map<String, Double> normal = domainTypes.stream().filter(typ -> typ.getType().equals(Type.NORMAL))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));

        try {
            for (Map.Entry<String, Integer> entry : domains.entrySet()) {

                // Check whether the domain name belongs to premium domain
                if (premium.containsKey(entry.getKey()))
                    totalCost = totalCost + calculatePriceByYear(entry, premium, Type.PREMIUM);

                // Domain doesn't belong to premium domain, so check with TLD
                else if (normal.containsKey(getSuffix(entry.getKey())))
                    totalCost = totalCost + calculatePriceByYear(entry, normal, Type.NORMAL);

                else
                    System.out.println(getSuffix(entry.getKey()) + " Not a valid domain name");

            }
            System.out.println("\nTotal Cost of the request = " + totalCost);
        } catch (Exception e) {
            System.out.println("CSV Format Error");
        }
           
    }

    // Fetch public suffixx
    public static String getSuffix(String key) {
        StringBuilder builder = new StringBuilder();
        String suffix = builder.append(".").append(InternetDomainName.from(key).publicSuffix()).toString();
        return suffix;
    }

    // Calculate and display price for requested years
    public static double calculatePriceByYear(Map.Entry<String, Integer> entry, Map<String, Double> domainMap,
            Type type) {
        Double domainPrice = 0.0;
        double pricePerYear = 0.0;
        if (type.equals(Type.PREMIUM)) {
            pricePerYear = domainMap.get(entry.getKey());
            domainPrice = entry.getValue() * pricePerYear;
        } else {
            pricePerYear = domainMap.get(getSuffix(entry.getKey()));
            domainPrice = entry.getValue() * pricePerYear;
        }

        System.out.println("\n" + entry.getKey() + " registered for " + entry.getValue() + " year at $" + pricePerYear
                + " per year = " + domainPrice);
        return domainPrice;
    }
}
