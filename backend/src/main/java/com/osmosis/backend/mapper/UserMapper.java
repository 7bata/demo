package com.osmosis.backend.mapper;


import com.osmosis.backend.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from db_manage where username = #{text} or email = #{text}")
    Account findByUsernameOrEmail(String text);
}
