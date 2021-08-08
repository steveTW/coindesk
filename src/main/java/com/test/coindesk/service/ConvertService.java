package com.test.coindesk.service;

import com.google.gson.*;
import com.test.coindesk.currencymapping.CurrencyNameMapping;
import com.test.coindesk.repository.CurrencyNameMappingRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class ConvertService {
    @Autowired
    private CurrencyNameMappingRepository repository;


    public ResponseEntity getOrigin() {
        String coindesk = this.getCoindesk();
        return ResponseEntity.status(200).body(coindesk);
    }

    public ResponseEntity getConvert() {
        String converted = this.convert();
        return ResponseEntity.status(200).body(converted);
    }

    public String convert() {
        String origin = this.getCoindesk();
        try {
            JsonObject root =JsonParser.parseString(origin).getAsJsonObject();
            String dateISO = root.get("time").getAsJsonObject().get("updatedISO").getAsString();
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateISO);
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
            JsonObject bpi = root.getAsJsonObject("bpi");
            HashMap<String, String> usdMap = new LinkedHashMap<>();
            usdMap.put("幣別", "USD");
            Optional<CurrencyNameMapping> mapping = repository.findById("USD");
            usdMap.put("幣別中文名稱", mapping.isPresent()?mapping.get().getName(): "");
            usdMap.put("匯率", bpi.get("USD").getAsJsonObject().get("rate").getAsString());
            HashMap<String, String> gbpMap = new LinkedHashMap<>();
            gbpMap.put("幣別", "GBP");
            mapping = repository.findById("GBP");
            gbpMap.put("幣別中文名稱", mapping.isPresent()?mapping.get().getName(): "");
            gbpMap.put("匯率", bpi.get("GBP").getAsJsonObject().get("rate").getAsString());
            HashMap<String, String> eurMap = new LinkedHashMap<>();
            eurMap.put("幣別", "EUR");
            mapping = repository.findById("EUR");
            eurMap.put("幣別中文名稱", mapping.isPresent()?mapping.get().getName(): "");
            eurMap.put("匯率", bpi.get("EUR").getAsJsonObject().get("rate").getAsString());

            HashMap<String, Object> convertResult = new LinkedHashMap<>();
            convertResult.put("更新時間", DateTimeFormatter.ofPattern("y/M/d HH:MM:SS").format(localDateTime));
            convertResult.put("幣別相關資訊", Arrays.asList(usdMap,gbpMap,eurMap));


            return new Gson().toJson(convertResult);
        } catch (Exception e) {
            throw new CoindeskException("轉換coindesk失敗", e);
        }
    }

    public String getCoindesk() {

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();) {
            HttpGet get = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice.json");
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                return result;
            }
            else {
                throw new RuntimeException("連接coindesk失敗 status: "+response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            throw new CoindeskException("連接coindesk失敗", e);
        }
    }

}
