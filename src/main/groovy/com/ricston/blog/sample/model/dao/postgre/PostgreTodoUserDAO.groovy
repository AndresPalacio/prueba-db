package com.ricston.blog.sample.model.dao.postgre

import groovy.sql.Sql

import javax.sql.DataSource

import com.ricston.blog.sample.model.dao.TodoUserDAO
import com.ricston.blog.sample.model.data.TodoUser

class PostgreTodoUserDAO implements TodoUserDAO {
	private Sql sql

	public PostgreTodoUserDAO(DataSource dataSource) {
		sql = new Sql(dataSource)
	}

	/**
	 *
	 * @param email
	 * @return the TodoUser with the given email
	 */
	public TodoUser findTodoUserByEmail(String email) {
		sql.firstRow """SELECT * FROM todouser WHERE email = $email"""

	}

	/**
	 *
	 * @param id
	 * @return the TodoUser with the given id
	 */
	public TodoUser findTodoUserById(long id) {
		sql.firstRow """SELECT * FROM todouser WHERE id = $id"""
	}

	/**
	 *
	 * @param confirmationCode
	 * @return the TodoUser with the given confirmationCode
	 */
	public TodoUser findTodoUserByConfirmationCode(String confirmationCode) {
		sql.firstRow """SELECT * FROM todouser WHERE confirmation_code = $confirmationCode"""
	}

	/**
	 *
	 * @param user
	 * @return TodoUser passed in with any db generated values filled in (e.g. the db generated id)
	 */
	public TodoUser insertTodoUser(TodoUser user) {
		def keys = sql.executeInsert """
			INSERT INTO todouser (email, password, registered, confirmation_code)
				VALUES (${user.email}, ${user.password}, ${user.registered}, ${user.confirmationCode});"""
		user.id = keys[0][0]
		return user;
	}

	/**
	 *
	 * @param user
	 * @return the number of rows updated
	 */
	public int updateTodoUser(TodoUser user) {
		sql.executeUpdate """
			UPDATE todouser
				SET email = ${user.email}, password = ${user.password}, registered = ${user.registered}, confirmation_code = ${user.confirmationCode}
				WHERE id = ${user.id};"""
	}

	/**
	 *
	 * @param email
	 * @return the number of rows deleted
	 */
	public int deleteTodoUserByEmail(String email) {
		sql.executeUpdate """DELETE FROM todouser WHERE email = $email;"""
	}

	/**
	 *
	 * @param id
	 * @return the number of rows deleted
	 */
	public int deleteTodoUserById(long id) {
		sql.executeUpdate """DELETE FROM todouser WHERE id = $id;"""
	}

	public void executeprocedure() {
		sql.call("call public.transfer(\n" +
				"\t?,?,?)", [1, 2, 1000], { message ->
			println message;
		})
	}

	public void  prepare(){
		sql.execute("drop table if exists accounts;\n" +
				"\n" +
				"create table accounts (\n" +
				"    id int generated by default as identity,\n" +
				"    name varchar(100) not null,\n" +
				"    balance dec(15,2) not null,\n" +
				"    primary key(id)\n" +
				");\n" +
				"\n" +
				"insert into accounts(name,balance)\n" +
				"values('Bob',10000);\n" +
				"\n" +
				"insert into accounts(name,balance)\n" +
				"values('Alice',10000);")
	}


	public void  resultData(){
		sql.eachRow("select balance from accounts where id = ?",[1],{
			result -> assert result.balance == 9000.00
		})
	}


	public resultsNew(){
		sql.call ("{call public.transfer(?, ?, ?)}", [1, 2, 1000])

	}


	public otherMethod(){
		def result = sql.call(" ? = call public.transfer(\n" +
				"\t1,2,1000)",[Sql.BOOLEAN()])
		print(result);
	}

}
