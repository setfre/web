package psn.service;

import org.springframework.beans.factory.annotation.Autowired;

import psn.mapper.UserMapper;

public abstract class BaseService {
	@Autowired
	protected UserMapper userMapper;
}
