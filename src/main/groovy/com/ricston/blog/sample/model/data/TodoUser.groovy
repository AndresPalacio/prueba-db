package com.ricston.blog.sample.model.data

import org.apache.commons.lang.builder.ToStringBuilder

class TodoUser {
	long id;
	String email;
	String password;
	String confirmationCode;
	boolean registered;
	
	def propertyMissing(String name, value) {
		if(isConfirmationCode(name)) {
			this.confirmationCode = value
		} else {
			unknownProperty(name)
		}
	}

	def propertyMissing(String name) {
		if(isConfirmationCode(name)) {
			return confirmationCode
		} else {
			unknownProperty(name)
		}
	}

	private boolean isConfirmationCode(String name) {
		'confirmation_code'.equals(name)
	}

	def unknownProperty(String name) {
		throw new MissingPropertyException(name, this.class)
	}

	@Override
	public String toString() {
		ToStringBuilder.reflectionToString(this);
	}
}
