package com.ricston.blog.sample.model.dao;

import com.ricston.blog.sample.model.data.TodoUser;

public interface TodoUserDAO {

	/**
	 * 
	 * @param email
	 * @return the TodoUser with the given email
	 */
	public TodoUser findTodoUserByEmail(String email);

	/**
	 * 
	 * @param id
	 * @return the TodoUser with the given id
	 */
	public TodoUser findTodoUserById(long id);

	/**
	 * 
	 * @param confirmationCode
	 * @return the TodoUser with the given confirmationCode
	 */
	public TodoUser findTodoUserByConfirmationCode(String confirmationCode);

	/**
	 * 
	 * @param user
	 * @return TodoUser passed in with any db generated values filled in (e.g.
	 *         the db generated id)
	 */
	public TodoUser insertTodoUser(TodoUser user);

	/**
	 * 
	 * @param user
	 * @return the number of rows updated
	 */
	public int updateTodoUser(TodoUser user);

	/**
	 * 
	 * @param email
	 * @return the number of rows deleted
	 */
	public int deleteTodoUserByEmail(String email);

	/**
	 * 
	 * @param id
	 * @return the number of rows deleted
	 */
	public int deleteTodoUserById(long id);

	public void executeprocedure();

	public void resultData();

	public void prepare();
}
