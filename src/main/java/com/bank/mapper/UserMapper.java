package com.bank.mapper;

import com.bank.dto.UserDTO;
import com.bank.entity.UserEntity;
import com.bank.util.PasswordUtil;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class UserMapper {

    public static UserEntity mapToUserEntity(Map<String, Object>row){
        if(row==null || row.isEmpty()){
            return null;
        }
        UserEntity user=new UserEntity();
        user.setId((Long) row.get("id"));
        user.setUsername((String) row.get("username"));
        user.setPasswordHash((String)row.get("password_hash"));
        user.setFullName((String) row.get("full_name"));
        user.setEmail((String) row.get("email"));
        user.setPhone((String)row.get("phone"));
        user.setCreatedAt((Timestamp) row.get("created_at"));
        user.setUpdatedAt((Timestamp) row.get("updated_at"));

        return user;
    }

    public static UserDTO UserEnityToDto(UserEntity user){

        if(user==null){
            return null;
        }

        UserDTO userDTO=new UserDTO();
        userDTO.setId( user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());



        return userDTO;
    }

    public static UserEntity UserDtoToUserEntity(UserDTO userDto){
        if(userDto==null){
            return null;
        }
        UserEntity entity=new UserEntity();
        entity.setId(userDto.getId());
        entity.setUsername(userDto.getUsername());
        entity.setFullName(userDto.getFullName());
        entity.setEmail(userDto.getEmail());
        entity.setPhone(userDto.getPhone());


        return entity;
    }

    public static List<UserEntity> mapToUserEntityList(List<Map<String, Object>> rows) {
        if (rows == null) {
            return new ArrayList<>();
        }

        return rows.stream()
                .map(UserMapper::mapToUserEntity)
                .collect(Collectors.toList());
    }

}
