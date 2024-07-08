package com.homework.fabric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.impl.CompanyDtoMapperImpl;


public class Fabric {

    public static ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    public static CompanyDtoMapper getDtoMapper(){
        return new CompanyDtoMapperImpl();
    }

}
