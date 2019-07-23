package domainregistrar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import junit.framework.Assert;

public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testReadJsonFileWithClassLoader() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("domaintypes.json").getFile());
        assertTrue(file.exists());
    }

    @Test
    public void test_jsonFileParsing_thenCheckJsonwithGivenJson()
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream stream = AppTest.class.getResourceAsStream("/domaintypes.json");
        List<DomainType> domainTypes = Arrays.asList(mapper.readValue(stream, DomainType[].class));
        List<DomainType> arraystoList = Arrays.asList(new DomainType(".com", 10, Type.NORMAL),
                new DomainType(".net", 9, Type.NORMAL), new DomainType(".com.au", 20, Type.NORMAL),
                new DomainType("apple.com.au", 1000, Type.PREMIUM), new DomainType("dict.com", 800, Type.PREMIUM),
                new DomainType("education.nwt", 300, Type.PREMIUM));
        assertEquals(domainTypes.get(0).getDomain(), arraystoList.get(0).getDomain());
        assertEquals(domainTypes.get(1).getDomain(), arraystoList.get(1).getDomain());
        Assert.assertEquals(domainTypes.get(1).getPrice(), arraystoList.get(1).getPrice());
        stream.close();
    }

    @Test
    public void check_domainNames_with_premiumDomains() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream stream = AppTest.class.getResourceAsStream("/domaintypes.json");
        List<DomainType> domainTypes = Arrays.asList(mapper.readValue(stream, DomainType[].class));
        Map<String, Double> premium = domainTypes.stream().filter(typ -> typ.getType().equals(Type.PREMIUM))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));
        Assert.assertEquals(premium.get("apple.com.au"), 1000.0);
        Assert.assertEquals(premium.get("dict.com"), 800.0);
        stream.close();

    }

    @Test
    public void check_domainNames_with_normalDomains() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream stream = AppTest.class.getResourceAsStream("/domaintypes.json");
        List<DomainType> domainTypes = Arrays.asList(mapper.readValue(stream, DomainType[].class));
        Map<String, Double> normal = domainTypes.stream().filter(typ -> typ.getType().equals(Type.NORMAL))
                .collect(Collectors.toMap(DomainType::getDomain, DomainType::getPrice));
        Assert.assertEquals(normal.get(".net"), 9.0);
        Assert.assertEquals(normal.get(".com"), 10.0);
        stream.close();

    }

}
