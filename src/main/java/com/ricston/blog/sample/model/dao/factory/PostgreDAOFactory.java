package com.ricston.blog.sample.model.dao.factory;

import javax.sql.DataSource;

import com.ricston.blog.sample.model.dao.TodoUserDAO;
import com.ricston.blog.sample.model.dao.postgre.PostgreTodoUserDAO;

public class PostgreDAOFactory implements DAOFactory {
	
	private DataSource dataSource;
	
	public PostgreDAOFactory() {
	}

	public PostgreDAOFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TodoUserDAO getTodoUserDAO() {
		TodoUserDAO dao = new PostgreTodoUserDAO(dataSource);
		return dao;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
