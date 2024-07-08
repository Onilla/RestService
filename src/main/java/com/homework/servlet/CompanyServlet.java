package com.homework.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.exception.NotFoundException;
import com.homework.fabric.Fabric;
import com.homework.service.CompanyService;
import com.homework.service.impl.CompanyServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "CompanyServlet", value = "/company")
public class CompanyServlet extends HttpServlet {

    private transient CompanyService companyService;
    private ObjectMapper objectMapper;

    public CompanyServlet(CompanyService companyService){
        this.companyService = companyService;
        this.objectMapper = Fabric.getObjectMapper();
    }

    private static void setJson(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String response;
        try {
            if (req.getParameter("id").equals("all")) {
                List<CompanyOutGoingDto> companyDtoList = companyService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(companyDtoList);
            } else {
                Long companyId = Long.parseLong(req.getParameter("id"));
                CompanyOutGoingDto companyOutGoingDto = companyService.findById(companyId);
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(companyOutGoingDto);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.flush();

    }

    private static String mapToJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String requestBody = mapToJson(req);
        String response;
        Optional<CompanyIncomingDto> companyIncomingDto;
        try {
            companyIncomingDto = Optional.ofNullable(objectMapper.readValue(requestBody, CompanyIncomingDto.class));
            CompanyIncomingDto incomingDto = companyIncomingDto.orElseThrow(IllegalArgumentException::new);
            response = objectMapper.writeValueAsString(companyService.save(incomingDto));
        } catch (JsonProcessingException e) {
            response = "Ошибка при обработке JSON";
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Не удалось сохранить компанию";
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String response = "";
        try {
            Long companyId = Long.parseLong(req.getParameter("id"));
            companyService.delete(companyId);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String response = "";
        String requestBody = mapToJson(req);
        try {
            CompanyUpdateDto companyUpdateDto = objectMapper.readValue(requestBody, CompanyUpdateDto.class);
            companyService.update(companyUpdateDto);
        } catch (JsonProcessingException e) {
            response = "Ошибка при обработке JSON";
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }


}
