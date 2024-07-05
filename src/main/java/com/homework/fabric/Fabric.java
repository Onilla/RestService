package com.homework.fabric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.dto.mappers.impl.CompanyDtoMapperImpl;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.dto.mappers.impl.UserDtoMapperImpl;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.CompanyService;
import com.homework.service.PositionService;
import com.homework.service.UserService;
import com.homework.service.impl.CompanyServiceImpl;
import com.homework.service.impl.PositionServiceImpl;
import com.homework.service.impl.UserServiceImpl;

public class Fabric {
    public static CompanyService getCompanyService(){
        return new CompanyServiceImpl();
    }

    public static CompanyRepositoryImpl getCompanyRepository(){
        return new CompanyRepositoryImpl();
    }

    public static ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    public static CompanyDtoMapper getDtoMapper(){
        return new CompanyDtoMapperImpl();
    }

    public static UserDtoMapper getUserDtoMapper(){
        return new UserDtoMapperImpl();
    }

    public static ConnectionManager getConnectionManager(){
        return new ConnectionManagerImpl();
    }

    public static UserRepositoryImpl getUserRepository(){
        return new UserRepositoryImpl();
    }

    public static UserService getUserService(){
        return new UserServiceImpl();
    }

    public static PositionRepositoryImpl getPositionRepository(){
        return new PositionRepositoryImpl();
    }

    public static PositionService getPositionService(){
        return new PositionServiceImpl();
    }

    public static PositionDtoMapper getPositionDtoMapper(){
        return new PositionDtoMapperImpl();
    }

}
