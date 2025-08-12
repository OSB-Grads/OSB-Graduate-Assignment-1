package com.bank.mapper;
import com.bank.dto.LogDTO;
import com.bank.entity.LogEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogMapper {
    public static LogEntity mapTOLogEntity(Map<String,Object> row){

        LogEntity log=new LogEntity();
        log.setId((int)row.get("id"));
        log.setUser_id((int)row.get("user_id"));
        log.setAction((String)row.get("action"));
        log.setDetails((String)row.get("details"));
        log.setIp_address((String)row.get("ip_address"));
        log.setStatus((String)row.get("status"));
        log.setTimestamp((Timestamp)row.get("created_at"));

        return log;
    }

    public static LogDTO LogEntityTODTO(LogEntity log){
        if(log==null){
            return null;
        }
        LogDTO logDto=new LogDTO();
        logDto.setId((int)log.getId() );
        logDto.setUser_id((int)log.getUser_id());
        logDto.setAction((String)log.getAction());
        logDto.setDetails((String)log.getDetails());
        logDto.setIpAddress((String) log.getIp_address());
        logDto.setStatus((String) log.getStatus());
        logDto.setCreatedAt((Timestamp) log.getTimestamp());

        return logDto;
    }

    public static LogEntity LogDtotoLogEntity(LogDTO logDTO){
        if(logDTO ==null){
            return null;
        }
        LogEntity log=new LogEntity();
        log.setId(logDTO.getId());
        log.setUser_id(logDTO.getUser_id());
        log.setAction(logDTO.getAction());
        log.setDetails(logDTO.getDetails());
        log.setIp_address(logDTO.getIpAddress());
        log.setStatus(logDTO.getStatus());
        log.setTimestamp(logDTO.getCreatedAt());

        return log;
    }

    public static List<LogEntity> mapToLogEntityList(List<Map<String, Object>> rows){
        if(rows==null){
            return new ArrayList<>();
        }
         return rows.stream()
                 .map(LogMapper::mapTOLogEntity)
                 .collect(Collectors.toList());
    }
}
