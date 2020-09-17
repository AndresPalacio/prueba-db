package com.ricston.blog.sample.model.spec;

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

import com.ricston.blog.sample.model.data.TodoUser
import com.ricston.blog.sample.model.dao.postgre.PostgreTodoUserDAO


// because it supplies a new application context after each test, the initialize-database in initdb.xml is
// executed for each test/specification
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration('classpath:testContext.xml')
class PostgreTodoUserDAOSpec extends Specification {

	@Autowired
	DataSource dataSource

	PostgreTodoUserDAO postgreTodoUserDAO

	def setup() {
		postgreTodoUserDAO = new PostgreTodoUserDAO(dataSource)
	}

	def "updateTodoUser spec - happy path"() {
		given: "a TodoUser in the db"
		TodoUser user = postgreTodoUserDAO.findTodoUserByEmail 'anon@gmail.com'

		and: "we save the value of confirmationCode before changing it"
		def oldCode = user.confirmationCode
		def newCode = user.confirmationCode = "lalala"

		when: "the user is updated"
		def rowsUpdated = postgreTodoUserDAO.updateTodoUser user

		and: "we search for the user again"
		TodoUser updatedUser = postgreTodoUserDAO.findTodoUserByEmail 'anon@gmail.com'

		then: "the rowsUpdated by the update should be 1"
		rowsUpdated == 1

		and: "the user has an updated state"
		updatedUser.id == user.id
		updatedUser.email == user.email
		updatedUser.registered == user.registered
		updatedUser.confirmationCode == newCode
		updatedUser.confirmationCode != oldCode

	}

	def "findTodoUserById when user exists in db"() {
		given: "a db populated with a TodoUser with email anon@gmail.com"
		String email = 'anon@gmail.com'

		when: "searching for a TodoUser with that email"
		TodoUser user = postgreTodoUserDAO.findTodoUserByEmail email

		and: "searching again using the id of the found user"
		TodoUser userByIdSearch = postgreTodoUserDAO.findTodoUserById user.id

		then: "the two users retrieved should have the same properties"
		user.id == userByIdSearch.id
		user.email == userByIdSearch.email
		user.password == userByIdSearch.password
		user.registered == userByIdSearch.registered
		user.confirmationCode == userByIdSearch.confirmationCode
	}

	def "findTodoUserByConfirmationCode when user exists in db"() {
		given: "a confirmationCode which exists in the db"
		String confirmationCode = 'abcdefg'

		when: "searching using that confirmationCode"
		TodoUser user = postgreTodoUserDAO.findTodoUserByConfirmationCode confirmationCode

		then: "the expected user is found"
		user.email == 'abc.j123@gmail.com'
		user.password == 'abc123'
		user.registered == false
		user.confirmationCode == confirmationCode
	}

	def "findTodoUserById when user does not exist in db"() {
		given: "an id which does not exist in the db"
		long id = 123456

		when: "searching using that id"
		TodoUser user = postgreTodoUserDAO.findTodoUserById id

		then: "the result of the search is null"
		null == user
	}

	def "findTodoUserByEmail when user does not exist in db"() {
		given: "an email which does not exist in the db"
		String email = 'blahblah'

		when: "searching using that email"
		TodoUser user = postgreTodoUserDAO.findTodoUserByEmail email

		then: "the result of the search is null"
		null == user
	}

	def "findTodoUserByEmail when user exists in db"() {
		given: "a db populated with a TodoUser with email anon@gmail.com and the password given below"
		String email = 'anon@gmail.com'
		String password = 'anon'

		when: "searching for a TodoUser with that email"
		TodoUser user = postgreTodoUserDAO.findTodoUserByEmail email

		then: "the row is found such that the user returned by findTodoUserByEmail has the correct password"
		user.password == password
	}

	def "insertTodoUser spec - happy path"() {
		given: "a TodoUser user to insert"
		String userEmail = 'newUser@gmail.com'
		TodoUser user = new TodoUser([email: userEmail, password: 'pass', registered: false, confirmationCode: 'yiddish'])

		when: "user is inserted"
		user = postgreTodoUserDAO.insertTodoUser user

		and: "user is searched for"
		TodoUser userFromDb = postgreTodoUserDAO.findTodoUserByEmail userEmail

		then: "user returned by insertTodoUser has non null id"
		user.id != null

		and: "properties of user found from search are equal to the properties of user inserted"
		userFromDb.id == user.id
		userFromDb.email == user.email
		userFromDb.password == user.password
		userFromDb.registered == user.registered
		userFromDb.confirmationCode == user.confirmationCode

	}

	def "deleteTodoUserByEmail spec - happy path"() {
		given: "a database populated with a TodoUser with the email anon@gmail.com"
		String email = 'anon@gmail.com'

		when: "deleting this user by email"
		int rowsDeleted = postgreTodoUserDAO.deleteTodoUserByEmail email

		then: "searching for the user by email will return null"
		null == postgreTodoUserDAO.findTodoUserByEmail(email)

		and: "1 row was deleted"
		1 == rowsDeleted
	}

	def "deleteTodoUserById spec - happy path"() {
		given: "a database populated with a TodoUser with the email anon@gmail.com"
		String email = 'anon@gmail.com'

		and: "after getting the id of this user"
		long id = postgreTodoUserDAO.findTodoUserByEmail(email).id

		when: "deleting this user by id"
		int rowsDeleted = postgreTodoUserDAO.deleteTodoUserById id

		then: "searching for the user by email will return null"
		null == postgreTodoUserDAO.findTodoUserByEmail(email)

		and: "1 row was deleted"
		1 == rowsDeleted
	}

	def "execute procedure"(){
		given: "prepare"
		postgreTodoUserDAO.prepare();

		when: "execute procedure"
		postgreTodoUserDAO.executeprocedure();

		then: "consulta el elemento"
		postgreTodoUserDAO.resultData();


	}

}