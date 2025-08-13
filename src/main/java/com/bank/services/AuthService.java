package com.bank.services;
import com.bank.db.LogDAO;
import com.bank.entity.UserEntity;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.exception.InvalidCredentialsException;
import com.bank.exception.UserAlreadyExist;
import com.bank.exception.UserNotfoundException;
import com.bank.util.PasswordUtil;
import com.bank.mapper.UserMapper;
import com.bank.services.LogService;
public class AuthService {

private static  userDao dao;
private static  PasswordUtil passwordUtil;
private final UserMapper userMapper;
private final LogService logService;
public AuthService(userDao dao,PasswordUtil passwordUtil ,UserMapper userMapper,LogService logService){
    this.dao=dao;
    this.passwordUtil=passwordUtil;
    this.userMapper=userMapper;
    this.logService=logService;
}
    public UserDTO validateUserCredentials(String user,String password) throws Exception,InvalidCredentialsException, UserNotfoundException {
       UserEntity userEntity=dao.getUserByUsername(user);
        String pass=userEntity.getPasswordHash();
           if(userEntity==null){
               logService.logintoDB(-1L, LogDAO.Action.AUTHENTICATION,"Failed authentication as userDetails is not present in the DB","user_ip",LogDAO.Status.FAILURE);
               throw new UserNotfoundException();
           }
          if(!passwordUtil.VerifyPassword(password,pass)){
              logService.logintoDB(-1L, LogDAO.Action.AUTHENTICATION,"Invalid Password","user_ip",LogDAO.Status.FAILURE);
            throw new InvalidCredentialsException();
        }
        logService.logintoDB(userEntity.getId(), LogDAO.Action.AUTHENTICATION,"Successfully LoggedIn","user_ip",LogDAO.Status.SUCCESS);
        return userMapper.UserEnityToDto(userEntity);
   }


    public static String[] SignInUser(String user, String password) throws Exception, UserAlreadyExist {

        UserEntity userEntity=dao.getUserByUsername(user);
        if(userEntity != null){
            throw new UserAlreadyExist();
        }
        String hash= passwordUtil.hashPassword(password);
        return new String[]{user,hash};


    }
}
