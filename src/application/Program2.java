package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
	
	DepartmentDao departmentDao = new DaoFactory().createDepartmentDao();
		
	System.out.println("=== TEST 1: department findById ====");
	Department department = departmentDao.findById(1);		
	System.out.println(department);
	
	System.out.println("\n=== TEST 2: department findAll ====");
	List<Department> list = departmentDao.findAll();
	for (Department obj : list) {
		System.out.println(obj);
	}
	
	System.out.println("\n=== TEST 3: department insert ====");
	Department newDepartment = new Department(null, "Music");
	departmentDao.insert(newDepartment);
	System.out.println("Inserted! New Id: " + newDepartment.getId());
	
	System.out.println("\n=== TEST 4: department update ====");
	Department department2 = departmentDao.findById(1);
	department2.setName("Food");
	departmentDao.update(department2);
	System.out.println("Update completed");
	
	System.out.println("\n=== TEST 5: department delete ====");
	System.out.println("Enter id for delete test: ");
	int id = scan.nextInt();
	departmentDao.deleteById(id);
	System.out.println("Delete completed");
	
	scan.close();
}
	
}
