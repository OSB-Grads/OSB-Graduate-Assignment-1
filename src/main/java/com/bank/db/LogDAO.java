package com.bank.db;
import com.bank.db.DatabaseManager;
import com.bank.entity.LogEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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


    public LogDAO(DatabaseManager dm) {
        this.dm = dm;
    }

    public void insertlog(LogEntity logEntity,Action action,Status status) throws SQLException {
        String act= action.name();
        String st=status.name();
            String sql=String.format(
                    "INSERT INTO LOGS (id,user_id,action,details,ip_address,status,createdAt) VALUES ('%d','%d','%s','%s','%s','%s','%s')",
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
        List<LogEntity> logs=new ArrayList<>();
        String sql="SELECT id,user_id,action,details,ip_address,status,createdAt FROM LOGS";
        try(ResultSet rs = (ResultSet) dm.query(sql)){
            while(rs.next()){
                LogEntity log=new LogEntity();
                log.setId(rs.getInt("id"));
                log.setUser_id(rs.getInt("user_id"));
                log.setAction(rs.getString("action"));
                log.setDetails(rs.getString("details"));
                log.setIp_address(rs.getString("ip_address"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                logs.add(log);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return logs;
    }

}
