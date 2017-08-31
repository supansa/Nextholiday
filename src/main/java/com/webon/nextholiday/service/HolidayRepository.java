package com.webon.nextholiday.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class HolidayRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${holiday.api.key}")
    private String apiKey;

    @Cacheable("holidays")
    public Map<String, String> send(String countryCode, String year) throws Exception{
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        simpleClientHttpRequestFactory.setConnectTimeout(60000);
        simpleClientHttpRequestFactory.setReadTimeout(60000);

        String url = "https://holidayapi.com/v1/holidays";
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();

        try{
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("year", year)
                    .queryParam("country", countryCode)
                    .queryParam("key", apiKey);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            JsonNode node = mapper.readValue(response.getBody(), JsonNode.class);
            return prepareResult(node);
        } catch (HttpClientErrorException ex){
            LOGGER.error("CountryCode:" + countryCode + " Year:" + year);
            ex.printStackTrace();

            ObjectNode object = mapper.readValue(ex.getResponseBodyAsString(), ObjectNode.class);
            Map<String, String> error = new HashMap<>();
            error.put("status",  object.get("status").asText());
            error.put("error",  object.get("error").textValue());
            error.put("namespace", "holidayapi");
            return error;
        }
    }

    private Map<String, String> prepareResult(JsonNode jNode){
        Map<String, String> result = new HashMap<>();
        Iterator<JsonNode> nodes = jNode.get("holidays").elements();
        while(nodes.hasNext()){
            JsonNode node = nodes.next();
            List<String> nameList = node.findValuesAsText("name");
            result.put(node.findPath("date").textValue(), String.join(",", nameList));
        }
        return  result;
    }
 }
