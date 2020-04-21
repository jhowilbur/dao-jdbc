# dao-jdbc
# Welcome to my project tutorial.

## My project's goal:
1. Know the main features of JDBC in theory and practice
1. Develop the basic structure of a project with JDBC
1. Implement the DAO standard manually with JDBC

## Overview of my JDBC project
- Java Database Connectivity (JDBC): standard Java API for data access
- Official pages:
https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/
https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html
- Packages: java.sql and javax.sql (supplemental server API)
###
![Screenshot_1](https://user-images.githubusercontent.com/59379254/79895890-fdf62700-83dd-11ea-8db0-2ee6ec27af6d.png)
###
## Installation of tools:
- Install MySQL Server and MySQL Workbench

## Project preparation in Eclipse
Check list:
- [x] Using MySQL Workbench, create a database called "mydatabase"
- [x] Download the MySQL Java Connector
- [x] If it does not already exist, create a User Library containing the MySQL driver .jar file
1. Window -> Preferences -> Java -> Build path -> User Libraries
1. Give the name of the MySQLConnector User Library
1. Add external JARs -> (locate the jar file)
- [x] Create a new Java Project
1. Add the MySQLConnector User Library to the project
- [x] In the project's root folder, create a "db.properties" file containing the connection data:
~~~XML
user=developer
password=123456
dburl=jdbc:mysql://localhost:3306/mydatabase
useSSL=false
~~~
- [x] In the "db" package, create a custom DbException exception
- [x] In the "db" package, create a DB class with static auxiliary methods
1. Obtain and close a connection to the bank

## Demo: recover data
- SQL script:
~~~sql
CREATE TABLE department (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE seller (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) NOT NULL,
  Email varchar(100) NOT NULL,
  BirthDate datetime NOT NULL,
  BaseSalary double NOT NULL,
  DepartmentId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (DepartmentId) REFERENCES department (id)
);

INSERT INTO department (Name) VALUES 
  ('Computers'),
  ('Electronics'),
  ('Fashion'),
  ('Books');

INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES 
  ('Bob Brown','bob@gmail.com','1998-04-21 00:00:00',1000,1),
  ('Maria Green','maria@gmail.com','1979-12-31 00:00:00',3500,2),
  ('Alex Grey','alex@gmail.com','1988-01-15 00:00:00',2200,1),
  ('Martha Red','martha@gmail.com','1993-11-30 00:00:00',3000,4),
  ('Donald Blue','donald@gmail.com','2000-01-09 00:00:00',4000,3),
  ('Alex Pink','bob@gmail.com','1997-03-04 00:00:00',3000,2);
~~~

- API:
- [x] Statement
- [x] ResultSet
1. the first () [moves to position 1, if any]
1. beforeFirst () [moves to position 0]
1. next () [moves to the next, returns false if it is already in the last]
1. absolute (int) [moves to the given position, remembering that real data starts at 1]
- Check list:
- [x] Use the SQL script to create the "mydatabase" database
- [x] Make a small program to recover the departments
- [x] In class DB, create static helper methods to close ResultSet and Statement
Attention: the ResultSet object contains the
data stored as a table:
###
![Screenshot_2](https://user-images.githubusercontent.com/59379254/79896223-7957d880-83de-11ea-96c6-c035ff4adf84.png)
###
## Demo: insert data
- API:
- [x] PreparedStatement
- [x] executeUpdate
- [x] Statement.RETURN_GENERATED_KEYS
- [x] getGeneratedKeys
- Check list:
- [x] Simple insertion with preparedStatement
- [x] Insertion with Id recovery

## Demo: update data
~~~java
conn = DB.getConnection();

	st = conn.prepareStatement(
		"UPDATE seller "
	+ "SET BaseSalary = BaseSalary + ? "
	+ "WHERE "
	+ "(DepartmentId = ?)");

st.setDouble(1, 200.0);
st.setInt(2, 2);

int rowsAffected = st.executeUpdate();
System.out.println("Done! Rows affected: " + rowsAffected);
~~~

## Demo: delete data
- Check list:
- [x] Create DbIntegrityException
- [x] Handle the referential integrity exception

## Demo: Transactions
- API:
- [x] setAutoCommit(false)
- [x] commit()
- [x] rollback()

## Data Access Object (DAO) design pattern
- References:
https://www.devmedia.com.br/dao-pattern-persistencia-de-dados-using-o-padrao-dao/30999
https://www.oracle.com/technetwork/java/dataaccessobject-138824.html
- General idea of the DAO standard:
- [X] For each entity, there will be an object responsible for accessing data related to this
entity. 
- For example:
1. the Client: ClienteDao
1. Product: ProductDao
1. The Order: OrderDao
- [X] Each DAO will be defined by an interface.
- [X] Dependency injection can be done using the Factory design pattern
###
![Screenshot_3](https://user-images.githubusercontent.com/59379254/79896806-30ecea80-83df-11ea-9481-f4292aa2a87a.png)
###

## Department entity class, Seller entity class
###
![Screenshot_4](https://user-images.githubusercontent.com/59379254/79896887-511ca980-83df-11ea-9e14-9851dfcc6467.png)
###

## Entity class checklist:
- [x] Attributes
- [x] Constructors
- [x] Getters/Setters
- [x] hashCode and equals
- [x] toString
- [x] implements Serializable

## DepartmentDao and SellerDao interfaces
~~~java
public interface SellerDao {

	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();

}

public interface DepartmentDao {

	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();

}
~~~

## SellerDaoJDBC and DaoFactory
~~~java
public class DaoFactory {
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
	}
}
~~~
~~~java
public class SellerDaoJDBC implements SellerDao {

	@Override
	public void insert(Seller obj) {
	}

	@Override
	public void update(Seller obj) {
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Seller findById(Integer id) {
		return null;
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}
~~~

## findById implementation
- SQL Query:
~~~sql
SELECT seller.*,department.Name as DepName
FROM seller INNER JOIN department
ON seller.DepartmentId = department.Id
WHERE seller.Id = ?
~~~
###
![Screenshot_5](https://user-images.githubusercontent.com/59379254/79897485-3139b580-83e0-11ea-9c12-5866aa676cb2.png)
###

## Reusing instantiation
~~~java
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
~~~

## findByDepartment implementation
- SQL Query:
~~~sql
SELECT seller.*,department.Name as DepName
FROM seller INNER JOIN department
ON seller.DepartmentId = department.Id
WHERE DepartmentId = ?
ORDER BY Name
~~~
###
![Screenshot_6](https://user-images.githubusercontent.com/59379254/79897673-6940f880-83e0-11ea-8b38-0499ce73fe96.png)
###

## findAll implementation
- SQL Query:
~~~sql
SELECT seller.*,department.Name as DepName
FROM seller INNER JOIN department
ON seller.DepartmentId = department.Id
ORDER BY Name
~~~

## insert implementation
- SQL Query:
~~~sql
INSERT INTO seller
(Name, Email, BirthDate, BaseSalary, DepartmentId)
VALUES
(?, ?, ?, ?, ?)
~~~

## update implementation
- SQL Query:
~~~sql
UPDATE seller
SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?
WHERE Id = ?
~~~
