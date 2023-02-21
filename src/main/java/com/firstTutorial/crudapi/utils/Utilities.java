package com.firstTutorial.crudapi.utils;

import org.springframework.data.domain.Sort;

public class Utilities {

    public static Sort.Direction getSortDirection(String directionOrder) {

        if (directionOrder.matches("asc")){
            return Sort.Direction.ASC;
        }
        else{
            return Sort.Direction.DESC;
        }

    }

}
