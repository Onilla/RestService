package com.homework.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dto.*;
import com.homework.exception.NotFoundException;
import com.homework.service.PositionService;
import com.homework.service.impl.PositionServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "PositionServlet", value = "/position")
public class PositionServlet extends HttpServlet {
    private final transient PositionService positionService = new PositionServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
                List<PositionOutGoingDto> positions = positionService.findAll();
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(positions);
            }
            else {
                Long companyId = Long.parseLong(req.getParameter("id"));
                PositionOutGoingDto positionOutGoingDto = positionService.findById(companyId);
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(positionOutGoingDto);
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
        Optional<PositionIncomingDto> positionIncomingDto;
        try {
            positionIncomingDto = Optional.ofNullable(objectMapper.readValue(requestBody, PositionIncomingDto.class));
            PositionIncomingDto incomingDto = positionIncomingDto.orElseThrow(IllegalArgumentException::new);
            response = objectMapper.writeValueAsString(positionService.save(incomingDto));
        } catch (JsonProcessingException e) {
            response = "Ошибка при обработке JSON";
        } catch (NullPointerException e){
            response = "Не удалось сохранить должность";
        }
        catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Неверный аргумент";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String response = "";
        try {
            Long positionId = Long.parseLong(req.getParameter("id"));
            if (positionService.delete(positionId)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                response = "Должность удалена";
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
        printWriter.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJson(resp);
        String response = "";
        String requestBody = mapToJson(req);
        try {
            PositionUpdateDto positionUpdateDto = objectMapper.readValue(requestBody, PositionUpdateDto.class);
            positionService.update(positionUpdateDto);
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
