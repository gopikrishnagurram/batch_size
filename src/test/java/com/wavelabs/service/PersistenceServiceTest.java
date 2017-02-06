package com.wavelabs.service;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.internal.SQLQueryImpl;
import org.hibernate.internal.util.xml.XmlDocument;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wavelabs.metadata.CollectionAttributes;
import com.wavelabs.metadata.HbmFileMetaData;
import com.wavelabs.metadata.ManyToOneAttributes;
import com.wavelabs.metadata.XmlDocumentBuilder;
import com.wavelabs.model.Department;
import com.wavelabs.model.Employee;
import com.wavelabs.service.PersistanceService;
import com.wavelabs.tableoperations.CRUDTest;
import com.wavelabs.utility.Helper;

/**
 * <p>
 * Tests the all the methods in {@link PersistanceService} class.
 * </p>
 * 
 * @author gopikrishnag
 *
 */
public class PersistenceServiceTest {

	private HbmFileMetaData empHbm = null;
	private HbmFileMetaData deptHbm = null;
	private CRUDTest crud = null;

	/**
	 * <p>
	 * Initializes {@link HbmFileMetaData}, {@link CRUDTest} Class objects. This
	 * objects useful through out all unit test cases.
	 * </p>
	 * 
	 */
	@BeforeTest
	public void intillization() {
		Helper.getSessionFactory();
		XmlDocumentBuilder xmb = new XmlDocumentBuilder();
		XmlDocument document = xmb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Department.hbm.xml");
		deptHbm = new HbmFileMetaData(document, Helper.getSessionFactory());
		XmlDocument document2 = xmb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Employee.hbm.xml");
		empHbm = new HbmFileMetaData(document2, Helper.getSessionFactory());
		crud = new CRUDTest(Helper.getSessionFactory(), Helper.getConfiguration(), Helper.getSession());
	}

	/**
	 * Checks batch size attribute value in set mapping in Department mapping
	 * file.
	 * <ul>
	 * <li>{@link batch-size="2"} test case will pass
	 * <li>
	 * <li>{@link batch-size="any other value then 2"} test case will fail
	 * <li>
	 * </ul>
	 */
	@Test(priority = 1, description = "To test batch size mentioned in hbm file as per requirment")
	public void batchSizeTest() {
		Assert.assertEquals(deptHbm.getAttributeOfSet(0, CollectionAttributes.batchsize), "2");
	}

	/**
	 * Provides data to {@link #testCreateEmployee()}.
	 * 
	 * @return Object[][]
	 */
	@DataProvider(name = "records")
	public Object[][] dbForRecords() {
		Object[][] obj = { { 1, "gopi", 20000, new Department(1, "DEVELOPER") },
				{ 2, "raja", 19000, new Department(2, "HR") }, { 3, "Tharun", 17800, new Department(3, "REPORTING") } };
		return obj;
	}

	/**
	 * <h4>tests
	 * {@link PersistanceService#createEmployee(int, String, double, Department)}
	 * method persist record in table or not.</h4>
	 * <ul>
	 * <li>If createEmployee(int, String, double, Department) successfully
	 * insert record in table, Then test case will pass</li>
	 * <li>if createEmployee(int, String, double, Department) failed to insert
	 * record in table, Then test case will fail</li>
	 * </ul>
	 */
	@Test(priority = 2, description = "checks Employee records inserted or not in table", dataProvider = "records")
	public void testCreateEmployee(int id, String name, double sal, Department department) {
		PersistanceService.createEmployee(id, name, sal, department);
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordInserted(Employee.class, id), true,
				" Employee record is not inserted in table");
	}

	/**
	 * <h4>tests
	 * {@link PersistanceService#createDepartment(int, String, Employee[])}
	 * method persist record in table or not</h4>
	 * <ul>
	 * <li>if createDepartment(int id, String name, Employee[] employee)
	 * successfully inserts record in table, test case will pass</li>
	 * <li>if createDepartment(int id, String name, Employee[] employee) failed
	 * then test case will fail</li>
	 * </ul>
	 */
	@Test(priority = 3, description = "checks the Deparment records inserted or not in table")
	public void testCreateDepartment() {
		PersistanceService.createDepartment(4, "TESTING", new Employee[] { new Employee(4, "murali", 18000),
				new Employee(5, "kumar", 16000), new Employee(6, "Abhi", 16000), new Employee(7, "abc", 16000) });
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordInserted(Employee.class, 4), true,
				"Department record is not inserted in table");
		Assert.assertEquals(crud.isRecordInserted(Employee.class, 5), true,
				"Department record is not inserted in table");
	}

	/**
	 * <p>
	 * checks the name of class mapped in many to one tag.
	 * </p>
	 * <p>
	 * if {@link Department} class is mapped then test case will pass
	 * </p>
	 * <p>
	 * if {@link Department} class is not mapped then test case will fail
	 * </p>
	 */
	@Test(priority = 5, description = "checks the className defined in many-to-one attribute")
	public void testManyToOneClassAttribute() {
		Assert.assertEquals(empHbm.getManyToOneAttribute("department", ManyToOneAttributes.Class),
				Department.class.getName(), "Department class is not mapped in one to many tag");
	}

	/**
	 * verifies the Query object given by
	 * {@link PersistenceServiceTest#testGetAllRecords()} method. If Query
	 * unable to produce expected result set, then test case will fail.
	 */
	@Test(priority = 7, description = "checks the result set given by query object.")
	public void testGetAllRecords() {
		Query query = PersistanceService.getAllRecords();
		@SuppressWarnings("unchecked")
		List<Department> departments = (List<Department>) query.list();
		Assert.assertEquals(departments.size(), 4, "Check HQL written in getAllRecords method and return Query object");
		for (Department d : departments) {
			Set<Employee> set = d.getSetOfEmployee();
			for (Employee e : set) {
				System.out.println(e.getName());
			}
		}
	}

	/**
	 * Tests query object given by {@link PersistanceService#getAllRecords()} is
	 * Sql or Hql. If object is HQL type then test case will fail If object is
	 * SQL type then test case will pass.
	 */
	@Test(priority = 6, description = "Verifies DataProvider used the HQL or SQL to get records")
	public void testSqlOrHql() {
		Query query = PersistanceService.getAllRecords();
		Assert.assertEquals(query instanceof SQLQueryImpl, false, " You have used SQL, please Use HQL");
	}

	/**
	 * Closes SessionFactory.
	 */
	@AfterTest
	public void closeResources() {
		try {
			Helper.getSessionFactory().close();
		} catch (Exception e) {

		}
	}
}
