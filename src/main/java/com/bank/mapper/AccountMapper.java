package com.bank.mapper;

import com.bank.dto.AccountDTO;
import com.bank.entity.AccountEntity;

public class AccountMapper {


    public AccountEntity dtoToEntity(AccountDTO accountDTO){
        if(accountDTO==null)return null;

        double interest=accountDTO.isLocked()?0.05:0.03;
                //fd ==0.05
                //savings ==0.03

        AccountEntity accountEntity=new AccountEntity(
                accountDTO.getAccountNumber(),
                accountDTO.getUserId(),
                accountDTO.getAccountType(),
                accountDTO.getBalance(),
                accountDTO.isLocked(),
                accountDTO.getCreatedAt(),
                accountDTO.getUpdatedAt(),
                accountDTO.getAccountType(),
                interest
        );
        return accountEntity;
    }


    public AccountDTO entityToDTO(AccountEntity accountEntity){
        if(accountEntity==null)return null;

        AccountDTO accountDTO=new AccountDTO(
                accountEntity.getAccount_number(),
                accountEntity.getUser_id(),
                accountEntity.getAccount_type(),
                accountEntity.getBalance(),
                accountEntity.isIs_locked(),
                accountEntity.getAccount_created(),
                accountEntity.getAccount_updated()
        );

        return accountDTO;
    }
}
