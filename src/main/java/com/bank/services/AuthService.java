package com.bank.services;
import com.bank.entity.UserEntity;
import com.bank.db.userDao;
import com.bank.dto.UserDTO;
import com.bank.exception.InvalidCredentialsException;
import com.bank.exception.UserAlreadyExist;
import com.bank.exception.UserNotfoundException;
import com.bank.util.PasswordUtil;
import com.bank.mapper.UserMapper;
public class AuthService {

private final userDao dao;
private final PasswordUtil passwordUtil;
private final UserMapper userMapper;
public AuthService(userDao dao,PasswordUtil passwordUtil ,UserMapper userMapper){
    this.dao=dao;
    this.passwordUtil=passwordUtil;
    this.userMapper=userMapper;
}
    public UserDTO validateUserCredentials(String user,String password) throws Exception,InvalidCredentialsException, UserNotfoundException {
       UserEntity userEntity=dao.getUserByUsername(user);
        String pass=userEntity.getPasswordHash();
           if(userEntity==null){
               throw new UserNotfoundException();
           }
          if(!passwordUtil.VerifyPassword(password,pass)){
            throw new InvalidCredentialsException();
        }
        return userMapper.UserEnityToDto(userEntity);
   }
   public String[] SignInUser(String user,String password) throws Exception, UserAlreadyExist {
    boolean is_present=false;
    String hash;
    UserEntity userEntity=dao.getUserByUsername(user);
    if(userEntity!=null){
        throw new UserAlreadyExist();
    }
   hash= passwordUtil.hashPassword(password);
    return new String[]{user,hash};


   }


}
