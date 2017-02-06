package com.wavelabs.service;


import java.util.HashSet;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.wavelabs.model.Department;
import com.wavelabs.model.Employee;
import com.wavelabs.utility.Helper;

/**
 * Perform save operation on {@link Employee}, {@link Department} domains.
 * 
 * @author gopikrishnag
 *
 */
public class PersistanceService {
	/**
	 * <h3>Persist Employee object</h3>
	 * <ul>
	 * <li>Create a transient object</li>
	 * <li>set values to Employee</li>
	 * <li>persist Employee record</li>
	 * </ul>
	 * 
	 * @param id
	 *            of Employee
	 * @param name
	 *            of Employee
	 * @param sal
	 *            of Employee
	 * @param department
	 *            of Employee
	 */
	public static void createEmployee(int id, String name, double sal, Department department) {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Employee e1 = new Employee();
		e1.setId(id);
		e1.setName(name);
		e1.setSal(sal);
		e1.setDepartment(department);
		session.save(e1);
		tx.commit();
		session.close();
	}

	/**
	 * <h3>persist department and associated employees</h3>
	 * <ul>
	 * <li>Creates transient Department</li>
	 * <li>sets properties of Department</li>
	 * <li>Persist department</li>
	 * </ul>
	 * 
	 * @param id
	 *            of department
	 * @param name
	 *            of department
	 * @param departmentEmployees
	 *            employees belongs to department
	 */
	public static void createDepartment(int id, String name, Employee[] departmentEmployees) {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Set<Employee> setOfEmployees = new HashSet<Employee>();
		for (Employee e : departmentEmployees) {
			setOfEmployees.add(e);
		}
		Department d1 = new Department();
		d1.setId(id);
		d1.setName(name);
		d1.setSetOfEmployee(setOfEmployees);
		session.save(d1);
		tx.commit();
		session.close();
	}
	/**
	 * <h3>prints all departments and employees
	 * <h3>
	 * <ul>
	 * <li>creates Query object to retrieve Department records</li>
	 * <li>prints department and associated employees</li>
	 * </ul>
	 * 
	 * @return Query
	 */
	public static Query getAllRecords() {
		Session session = Helper.getSession();
		Query query = session.createQuery("from " + Department.class.getName());
		return query;
	}
}
