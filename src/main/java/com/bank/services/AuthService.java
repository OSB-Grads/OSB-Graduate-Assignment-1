package com.bank.services;
import com.bank.entity.UserEntity;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.util.PasswordUtil;
public class AuthService {

private final userDao dao;
private final PasswordUtil util;
public AuthService(userDao dao,PasswordUtil util){
    this.dao=dao;
    this.util=util;
}
    public UserDTO LoginUser(String user,String password) throws Exception{
       UserEntity userEntity=new UserEntity();
       try{
           userEntity=dao.getUserByUsername(user);
           if(userEntity!=null){
               String pass=userEntity.getPasswordHash();


           }
       }
       catch{

       }
   }

}
