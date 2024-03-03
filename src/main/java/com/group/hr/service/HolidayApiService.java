package com.group.hr.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

@Service
public class HolidayApiService {

    @Value("${openHoliday.key}")
    private String KEY;
    private static final String URL = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?serviceKey={0}&solYear={1}&solMonth={2}";

    public JSONArray getHolidaysResponse(int year, int month) throws IOException, InterruptedException {
        String url = getHolidaysUrl(year, month);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject xmlJSONObj = XML.toJSONObject(response.body());
        JSONObject responseObj = xmlJSONObj.getJSONObject("response");
        JSONObject body = responseObj.getJSONObject("body");
        Object items = body.get("items");

        // 'items'가 JSONObject인지 확인
        if (items instanceof JSONObject) {
            JSONObject itemsObj = (JSONObject) items;
            Object item = itemsObj.get("item");
            // 'item'이 JSONArray인 경우 그대로 반환
            if (item instanceof JSONArray) {
                return (JSONArray) item;
            } else if (item instanceof JSONObject) {
                // 'item'이 JSONObject인 경우, JSONArray로 변환
                JSONArray array = new JSONArray();
                array.put(item);
                return array;
            }
        }
        return new JSONArray();
    }

    public String getHolidaysUrl(int year, int month) {
        String formattedUrl = MessageFormat.format(URL, KEY, year, String.format("%02d", month));
        return formattedUrl;
    }
}
