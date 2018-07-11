package com.lp.springmvc.demo.mapper;

import java.util.List;

import com.lp.springmvc.demo.entity.MyUser;

public interface MyUserMapper {
	List<MyUser> findAll();
}
