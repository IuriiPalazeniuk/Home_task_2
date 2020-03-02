package com.contrutors;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.AbstractMap.SimpleEntry;
import static java.util.stream.Collectors.toMap;

public class Executer {

    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("src/config.properties");
        Properties properties = new Properties();
        properties.load(fis);
        Map<String, Integer> reverted = properties.entrySet().stream()
            .map(entry -> new SimpleEntry<>(Integer.parseInt(String.valueOf(entry.getKey())),
                                            String.valueOf(entry.getValue())))
            .collect(toMap(SimpleEntry::getValue, SimpleEntry::getKey, (first, second) -> {
                throw new RuntimeException("My custom exception");
            }));
        Map<String, Integer> sortedMap = new HashMap<>();
        reverted.entrySet().stream().sorted(Map.Entry.comparingByKey())
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        sortedMap.forEach((key, value) -> properties.put(key, value));

        Files.write(Paths.get("src/newConf.properties"), () -> sortedMap.entrySet().stream()
            .<CharSequence>map(e -> e.getKey() + " = " + e.getValue()).iterator());
    }
}
