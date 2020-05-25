package com.example.webApp;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordController {

    private static String template = "";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/record")
    public Record record(@RequestParam(value = "mcc", defaultValue = "-1") String mcc,
            @RequestParam(value = "mnc", defaultValue = "-1") String mnc) {

        if (mcc.equals("-1") || mnc.equals("-1")) {
            template = "Please input valid MCC and MNC";
            return new Record(counter.incrementAndGet(), template);
        }

        
        try {
            File file = new File("C:\\Users\\HP\\Desktop\\data.json");
            Scanner scanner = new Scanner(file);
            JsonParser jsonParser = new JsonParser();
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                JsonObject objectFromString = jsonParser.parse(line).getAsJsonObject();
                JsonElement mccElement = objectFromString.get("MCC");
                JsonElement mncElement = objectFromString.get("MNC");
                JsonElement countryElement = objectFromString.get("Country");
                JsonElement networkElement = objectFromString.get("Network");

                if(mccElement.getAsString().equals(mcc) && mncElement.getAsString().equals(mnc)){
                    template = "Country: " + countryElement.getAsString() + ", Network: " + networkElement.getAsString();
                    return new Record(counter.incrementAndGet(), template);
                } 
            }
            scanner.close(); 
        } catch (Exception e){
            e.printStackTrace();
        }

        template = "Error";
        return new Record(counter.incrementAndGet(), template);
    }
    
    @GetMapping("/recordNetworks")
    public Record recordNetworks(@RequestParam(value = "mcc", defaultValue = "-1") String mcc,
            @RequestParam(value = "country", defaultValue = "-1") String country) {

        if (mcc.equals("-1") && country.equals("-1")) {
            template = "Please input valid MCC and MNC";
            return new Record(counter.incrementAndGet(), template);
        }

        
        String resultString = "Networks: "; 
        try {
            File file = new File("C:\\Users\\HP\\Desktop\\data.json");
            Scanner scanner = new Scanner(file);
            JsonParser jsonParser = new JsonParser();
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                JsonObject objectFromString = jsonParser.parse(line).getAsJsonObject();
                JsonElement mccElement = objectFromString.get("MCC");
                JsonElement countryElement = objectFromString.get("Country");
                JsonElement networkElement = objectFromString.get("Network");

                if(mcc.equals("-1") && (!country.equals("-1"))){
                    if(countryElement.getAsString().equals(country)){
                        resultString += networkElement.getAsString() + ", ";
                    } 
                } else if(country.equals("-1") && (!mcc.equals("-1"))){
                    if(mccElement.getAsString().equals(mcc)){
                        resultString += networkElement.getAsString() + ", ";
                    }
                } else {
                    if(mccElement.getAsString().equals(mcc)){
                        resultString += networkElement.getAsString() + ", ";
                    }
                }
            }
            scanner.close(); 
        } catch (Exception e){
            e.printStackTrace();
        }

        template = resultString; 
        return new Record(counter.incrementAndGet(), template);
	}
}