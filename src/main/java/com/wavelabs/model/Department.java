package com.wavelabs.model;


import java.util.Set;
/**
 * Entity class represents Department in relational database.
 * @author gopikrishnag
 */
public class Department {

	private int id;
	private String name;
	private Set<Employee> setOfEmployee;
	public Department() {

		
	
	}
	public Department(int id,String name)
	{
		this.id=id;
		this.name=name;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getSetOfEmployee() {
		return setOfEmployee;
	}


	public void setSetOfEmployee(Set<Employee> setOfEmployee) {
		this.setOfEmployee = setOfEmployee;
	}

}
