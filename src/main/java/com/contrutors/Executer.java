package com.contrutors;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.SneakyThrows;

import static java.util.AbstractMap.SimpleEntry;


public class Executer {

    @SneakyThrows
    public static void main(String[] args) {

        FileInputStream fis = new FileInputStream("src/config.properties");
        Properties properties = new Properties();
        properties.load(fis);
        Map<String, Integer> reverted = new HashMap<>();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {

            SimpleEntry<Integer, String> integerStringSimpleEntry =
                new SimpleEntry<>(Integer.parseInt(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()));

            if (integerStringSimpleEntry.getValue()
                .equalsIgnoreCase(String.valueOf(integerStringSimpleEntry.getKey()))) {
                throw new Exception("My custom exception");
            }

            reverted.put(String.valueOf(entry.getValue()), integerStringSimpleEntry.getKey());
        }

        Files.write(Paths.get("src/newConf.properties"),
                    () -> reverted.entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .<CharSequence>map(e -> e.getKey() + " = " + e.getValue()).iterator());
    }
}
