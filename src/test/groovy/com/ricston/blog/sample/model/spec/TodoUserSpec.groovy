package com.ricston.blog.sample.model.spec

import spock.lang.Specification

import com.ricston.blog.sample.model.data.TodoUser

class TodoUserSpec extends Specification {
	
	TodoUser user
	String testCode = 'street_triple'

	def setup() {
		user = new TodoUser()
	}
	
	def "setting confirmationCode normally works"() {
		when: user.confirmationCode = testCode
		then: user.confirmationCode == testCode
	}

	def "set/get confirmationCode using confirmation_code table name works"() {
		when: user.confirmation_code = testCode
		
		then:
		user.confirmationCode == testCode
		user.confirmation_code == testCode
	}

	def "can construct new TodoUser from map"() {
		given: 'a map (or GroovyRowResult) retrieved with a SELECT statement'
		def queryResultMap = [id: 1, email: 'blah@gmail.com', password: 'supersecret', confirmation_code: 'abc', registered: false]

		when: 'using the map to construct a new TodoUser'
		user = new TodoUser(queryResultMap)
		
		then: "keys in map are mapped to TodoUser property names including the key 'confirmation_code' which is mapped to the property confirmationCode"
		user.id == queryResultMap.id
		user.email == queryResultMap.email
		user.password == queryResultMap.password
		user.confirmationCode == queryResultMap.confirmation_code
		user.registered == queryResultMap.registered
	}
	
	def "setting an undefined/unmapped property throws MissingPropertyException"() {
		given: 'a property which is not defined or mapped to a defined property in TodoUser'
		def undefinedProperty = 'confirmationURI'
		
		when: 'setting the undefined property on a TodoUser'
		user."$undefinedProperty" = 123
		
		then: 'a MissingPropertyException ex will be thrown'
		MissingPropertyException ex = thrown()

		and: 'ex.type is the type on which the property was attempted to be called'
		ex.type == TodoUser.class

		and: 'ex.property is the name of the property that could not be found i.e. the undefined property'
		ex.property == undefinedProperty
	}

	def "getting an undefined/unmapped property throws MissingPropertyException"() {
		given: 'a property which is not defined or mapped to a defined property in TodoUser'
		def undefinedProperty = 'confirmationURI'
		
		when: 'getting the undefined property on a TodoUser'
		def p = user."$undefinedProperty"
		
		then: 'a MissingPropertyException ex will be thrown'
		MissingPropertyException ex = thrown()

		and: 'ex.type is the type on which the property was attempted to be called'
		ex.type == TodoUser.class

		and: 'ex.property is the name of the property that could not be found i.e. the undefined property'
		ex.property == undefinedProperty
	}
}
