package com.bank.services;

import com.bank.db.DatabaseManager;
import com.bank.db.LogDAO;
import com.bank.entity.LogEntity;
import com.bank.mapper.LogMapper;

import java.util.HashMap;
import java.util.Map;

public class LogService {

    public static void logintoDB(Long user_id, LogDAO.Action action, String details, String ip_address, LogDAO.Status status){
        Map<String,Object> row=new HashMap<>();
        row.put("user_id", user_id);
        row.put("action",action);
        row.put("details",details);
        row.put("ip_address",ip_address);
        row.put("status",status);
        LogMapper logMapper=new LogMapper();
        LogEntity log=LogMapper.mapTOLogEntity(row);
        LogDAO logDAO=new LogDAO(DatabaseManager.getInstance(),logMapper);
        logDAO.insertlog(log);
    }
}
