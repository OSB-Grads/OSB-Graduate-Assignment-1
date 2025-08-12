package com.bank.db;
import com.bank.db.DatabaseManager;
import com.bank.entity.LogEntity;
import com.bank.mapper.LogMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LogDAO {
    public enum Status{
        SUCCESS,
        FAILURE,
        ERROR
    }
    public enum Action{
        PROFILE_MANAGEMENT,
        AUTHENTICATION,
        TRANSACTIONS,
        CREATION_MANAGEMENT
    }

    private final DatabaseManager dm;

    private final LogMapper lm;
    public LogDAO(DatabaseManager dm,LogMapper lm) {
        this.dm = dm;
        this.lm=lm;
    }

    public void insertlog(LogEntity logEntity,Action action,Status status) throws SQLException {
        String act= action.name();
        String st=status.name();
            String sql=String.format(
                    "INSERT INTO LOGS (id,user_id,action,details,ip_address,status,created_at) VALUES ('%d','%d','%s','%s','%s','%s','%s')",
                    logEntity.getId(),
                    logEntity.getUser_id(),
                    act,
                    logEntity.getDetails(),
                    logEntity.getIp_address(),
                    st,
                    logEntity.getTimestamp()
            );
            dm.query(sql);
            logEntity.setAction(act);
            logEntity.setStatus(st);
    }
    public List<LogEntity> getAllLogs() throws SQLException {
       List<LogEntity> log=new ArrayList<>();
        String sql="SELECT id,user_id,action,details,ip_address,status,created_at FROM LOGS";
        try{
           List<Map<String,Object>> logs=dm.query(sql);
           if(!logs.isEmpty()){
               return lm.mapToLogEntityList(logs);

           }
        } catch(SQLException e){
            throw  new RuntimeException("Error finding Logs"+e.getMessage(),e);
        }
        return log;
    }

}
