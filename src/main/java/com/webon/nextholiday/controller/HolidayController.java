package com.webon.nextholiday.controller;

import com.webon.nextholiday.model.HolidayResponseEntity;
import com.webon.nextholiday.service.HolidayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class HolidayController {
    @Autowired
    private HolidayServiceImpl holidayService;

    @RequestMapping(value = "/getNextHoliday", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    HolidayResponseEntity getNextHoliday(String date, String countryCode1, String countryCode2) throws Exception {
        if (date == null || holidayService.validateDate(date).isEmpty()) {
            return new HolidayResponseEntity(HttpStatus.BAD_REQUEST.value(), "date format is not valid. (e.g. yyyy-mm-dd)", "nextholiday");
        }

        Map<String, String> result = holidayService.getNextHoliday(date, countryCode1, countryCode2);
        if(result.get("error") != null){
            return new HolidayResponseEntity(Integer.parseInt(result.get("status")), result.get("error"), result.get("namespace"));
        }
        return new HolidayResponseEntity(HttpStatus.OK.value(), result);
    }

}
