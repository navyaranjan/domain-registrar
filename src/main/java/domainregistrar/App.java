package domainregistrar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.*;
public class App {

    private List<DomainType> domainTypes = new ArrayList<>();

    public static void main(String[] args) {

        printPriceList();
        addNewDomainType();
    }

    public static void printPriceList() {
        System.out.println("\nDomain regstrar's price list (per year) of domain registration per in a zone");
        System.out.println("========================================");

    }

    public static void addNewDomainType() {
        ObjectMapper mapper = new ObjectMapper();

        InputStream stream = App.class.getResourceAsStream("domaintypes.json");

        // try {
        // stream = new FileInputStream(file1);

        // BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        // rd.lines().forEach(System.out::println);
        // // List<DomainType> domainTypes = Arrays.asList(mapper.readValue(stream,
        // // DomainType[].class));
        // // domainTypes.forEach(System.out::println);
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // // } catch (JsonParseException e) {

        // // e.printStackTrace();
        // // } catch (JsonMappingException e) {

        // e.printStackTrace();
        // } catch (IOException e) {

        // e.printStackTrace();
        // }

    }

}
