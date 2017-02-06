package com.wavelabs.model;

/**
 * Entity class represents Employee table in relational Database.
 * 
 * @author gopikrishnag
 *
 */
public class Employee {

	private int id;
	private String name;
	private double sal;
	private Department department;

	public Employee() {

	}

	public Employee(int id, String name, double sal) {
		this.id = id;
		this.name = name;
		this.sal = sal;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public double getSal() {
		return sal;
	}

	public void setSal(double sal) {
		this.sal = sal;
	}

}
