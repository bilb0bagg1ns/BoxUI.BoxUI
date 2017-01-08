package com.box.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */
@Document(collection = "users")
public class User {

    @Id
    public String id;

	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	
	
	public User() {
	}
	
	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	
	public User(String userName, String password, String firstName, String lastName) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return lastName + "," + firstName;
	}

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}


	
}
