package com.prosa.rivertech.rest.bankservices.utils;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

@Component
public class FilterResponse {

    public final String AccountFilter = "AccountFilter";
    public final String[] AccountFilterMapping= new String[]{"id", "number", "balance", "owner"};


    public MappingJacksonValue getMappingJacksonValue(Object response, String filterName, String[] fieldsToExpose ) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fieldsToExpose);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName, filter);
        MappingJacksonValue mapping = new MappingJacksonValue(response);
        mapping.setFilters(filterProvider);
        return mapping;
    }
}
