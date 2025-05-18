package com.example.sst.infrastructure.repository;

import com.example.sst.infrastructure.dto.UserAuthenticationData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("""
            SELECT
            	u.name user_name,
            	u.email email,
            	u.password password,
            	r.name role_name
            FROM
            	users u
            INNER JOIN user_roles ur
                ON u.email = ur.email
            INNER JOIN roles r
                ON ur.role_id = r.role_id
            WHERE
            	u.email = #{email}
            """)
    List<UserAuthenticationData> selectByEmail(String email);

    @Insert("""
            INSERT INTO users (name, email, password)
            VALUES (#{name}, #{email}, #{password})
            """)
    void insertUser(String name, String email, String password, int roleId);

    @Insert("""
            INSERT INTO user_roles (email, role_id)
            VALUES (#{email}, #{roleId})
            """)
    void insertUserRole(String email, int roleId);
}
