package com.webon.nextholiday.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

@Service
public class HolidayServiceImpl {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HolidayRepository repository;

    public Map<String,String> getNextHoliday(String date, String countryCode1, String countryCode2){
        Map<String,String> result;
        try{
            String year = date.substring(0, date.indexOf('-'));

            Map<String,String> result1 = repository.send(countryCode1, year);
            if(isError(result1)){
                return result1;
            }
            Map<String,String> result2 = repository.send(countryCode2, year);
            if(isError(result2)){
                return result2;
            }
            result = mapResultByDate(date, countryCode1, countryCode2, result1, result2);
            return result;
        } catch(Exception ex){
            LOGGER.error("Date:" + date + " CountryCode1:" + countryCode1 + " CountryCode2:" + countryCode2);
            ex.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            error.put("error",  ex.getMessage());
            error.put("namespace", "nextholiday");
            return error;
        }
    }

    public String validateDate(String dateStr){
        String dateResult = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date date = formatter.parse(dateStr);
            dateResult = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateResult;
    }

    private Map<String,String> mapResultByDate(String dateStr, String countryCode1, String countryCode2,
                                               Map<String,String> result1, Map<String,String> result2){
        LocalDate date = LocalDate.parse(dateStr);
        Map<String, String> result = new HashMap<>();

        for (String d: result1.keySet()) {
            LocalDate holiday = LocalDate.parse(d);
            if(date.isBefore(holiday) && (result2.get(holiday.toString()) != null)){
                String targetDate = holiday.toString();
                result.put("date", targetDate);
                result.put(countryCode1, result1.get(targetDate));
                result.put(countryCode2, result2.get(targetDate));
                break;
            }
        }
        return result;
    }

    private boolean isError(Map<String,String> data){
        return (data.get("error") != null)? true : false;
    }

}
