## Social Network

A new social network site has the following data tables.

Table: `users`
| id | name | sex |
|----|------|-----|
| 1  | Ann| null   |
| 2  | Steve| m      |
| 3  | Mary| f      |
| 4  | Brenda| f      |

Table: `friends`
| user1 | user2 |
|-------|-------|
| 1     | 2     |
| 1     | 3     |
| 2     | 3     |

Select data that will be returned by the following SQL query:

```sql
SELECT user.name, COUNT(*) FROM users
LEFT JOIN friends
ON users.id = friends.user1 OR users.id = friends.user2
WHERE users.sex = 'f'
GROUP BY users.name;
```

Output:

| name | count |
|------|-------|
| Mary | 2     |
| Brenda| 1     |


## Pets

Information about pets is kept in two separate tables.

Table: `dogs`
- `id`: integer unique dog id
- `name`: VARCHAR(50) dog's name

Table: `cats`
- `id`: integer unique cat id
- `name`: VARCHAR(50) cat's name

Write a query that select all distinct pet names.

### Example:
```sql
CREATE TABLE dogs (
  id INTEGER NOT NULL PRIMARY KEY, 
  name VARCHAR(50) NOT NULL
);

CREATE TABLE cats (
  id INTEGER NOT NULL PRIMARY KEY, 
  name VARCHAR(50) NOT NULL
);

INSERT INTO dogs(id, name) values(1, 'Lola');
INSERT INTO dogs(id, name) values(2, 'Bella');
INSERT INTO cats(id, name) values(1, 'Lola');
INSERT INTO cats(id, name) values(2, 'Kitty');

-- Expected output (in any order):
-- name     
-- -----
-- Bella    
-- Kitty    
-- Lola  
```

```sql
SELECT DISTINCT name FROM (
  SELECT * FROM dogs
  UNION 
  SELECT * FROM cats
  );
```

## Regional Sales Comparison

An insurance company maintains records of sales made by its employees. Each employee is assigned to a state. States 
are grouped under regions. The following tables contain the data:

Table: `regions`

- `id`: integer unique region id
- `name`: VARCHAR(50) region's name

Table: `states`

- `id`: integer unique state id
- `name`: VARCHAR(50) state's name
- `regionId`: integer region id

Table: `employees`

- `id`: integer unique employee id
- `name`: VARCHAR(50) employee's name
- `stateId`: integer state id

Table: `sales`

- `id`: integer unique sale id
- `amount`: INTEGER sale amount
- `employeeId`: integer employee id

Management requires a comparative region sales analysis report.
Write a query that returns:
- the name of the region
- Average sales per employee for the region (Average sales = Total sales made for the region / Number of employees in the region)
- The difference between average sales of the region with the highest average sales and the average sales per 
  employee for the region

Employees can have multiple sales. A region with no sales should be also returned. Use `0` for average sales per 
employee for such a region when calculating the 2nd and 3rd column.

### Example:
```sql
CREATE TABLE regions(
  id INTEGER PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE states(
  id INTEGER PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  regionId INTEGER NOT NULL,
  FOREIGN KEY (regionId) REFERENCES regions(id)
); 

CREATE TABLE employees (
  id INTEGER PRIMARY KEY,
  name VARCHAR(50) NOT NULL, 
  stateId INTEGER NOT NULL,
  FOREIGN KEY (stateId) REFERENCES states(id)
);

CREATE TABLE sales (
  id INTEGER PRIMARY KEY,
  amount INTEGER NOT NULL,
  employeeId INTEGER NOT NULL,
  FOREIGN KEY (employeeId) REFERENCES employees(id)
);

INSERT INTO regions(id, name) VALUES(1, 'North');
INSERT INTO regions(id, name) VALUES(2, 'South');
INSERT INTO regions(id, name) VALUES(3, 'East');
INSERT INTO regions(id, name) VALUES(4, 'West');
INSERT INTO regions(id, name) VALUES(5, 'Midwest');

INSERT INTO states(id, name, regionId) VALUES(1, 'Minnesota', 1);
INSERT INTO states(id, name, regionId) VALUES(2, 'Texas', 2);
INSERT INTO states(id, name, regionId) VALUES(3, 'California', 3);
INSERT INTO states(id, name, regionId) VALUES(4, 'Columbia', 4);
INSERT INTO states(id, name, regionId) VALUES(5, 'Indiana', 5);

INSERT INTO employees(id, name, stateId) VALUES(1, 'Jaden', 1);
INSERT INTO employees(id, name, stateId) VALUES(2, 'Abby', 1);
INSERT INTO employees(id, name, stateId) VALUES(3, 'Amaya', 2);
INSERT INTO employees(id, name, stateId) VALUES(4, 'Robert', 3);
INSERT INTO employees(id, name, stateId) VALUES(5, 'Tom', 4);
INSERT INTO employees(id, name, stateId) VALUES(6, 'William', 5);

INSERT INTO sales(id, amount, employeeId) VALUES(1, 2000, 1);
INSERT INTO sales(id, amount, employeeId) VALUES(2, 3000, 2);
INSERT INTO sales(id, amount, employeeId) VALUES(3, 4000, 3);
INSERT INTO sales(id, amount, employeeId) VALUES(4, 1200, 4);
INSERT INTO sales(id, amount, employeeId) VALUES(5, 2400, 5);

-- e.g. 'Minnesota' is the only state under the 'North' region. 
-- Total sales made by employees 'Jaden' and 'Abby' for the state of 'Minnesota' is 5000 (2000 + 3000)
-- Total employees in the state of 'Minnesota' is 2
-- Average sales per employee for the 'North' region = Total sales made for the region (5000) / Total number of employees (2) = 2500
-- Difference between the average sales of the region with the highest average sales ('South'), 
-- and the average sales per employee for the region ('North') = 4000 - 2500 = 1500.
-- Similarly, no sale has been made for the only state 'Indiana' under the region 'Midwest'.
-- So the average sales per employee for the region is 0.
-- And, the difference between the average sales of the region with the highest average sales ('South'), 
-- and the average sales per employee for the region ('Midwest') = 4000 - 0 = 4000.

-- Expected output (rows in any order):
-- name     average   difference
-- -----------------------------
-- North	2500	  1500             
-- South 	4000	  0
-- East		1200   	  2800
-- West		2400	  1600
-- Midwest  	0         4000
```

```sql
SELECT regions.name, 
       COALESCE(SUM(sales.amount) / COUNT(DISTINCT employees.id), 0) AS average,
       COALESCE(MAX(SUM(sales.amount) / COUNT(DISTINCT employees.id)) OVER(), 0) - COALESCE(SUM(sales.amount) / COUNT(DISTINCT employees.id), 0) AS difference
FROM regions
LEFT JOIN states
ON regions.id = states.regionId
LEFT JOIN employees
ON states.id = employees.stateId
LEFT JOIN sales
ON employees.id = sales.employeeId
GROUP BY regions.name;
```

## Enrollments
A table containing students enrolled in a yearly course has incorrect data in records with ids between 20 and 100
(inclusive).

Table: `enrollments`

- `id`: integer unique enrollment id
- `year`: integer representing the year of the course
- `studentId`: integer representing the student id

Write a query that updates the field `year` of every faulty record to 2020.

```sql
UPDATE enrollments
SET year = 2020
WHERE id BETWEEN 20 AND 100;
```

## Workers

The following data definition defines an organization's employee hierarchy.

An employee is a manager if any other employee has their `managerId` set to the employee's `id`. That means `John` 
is a manager if at least one other employee has their `managerId` set to John's `id`.

Table: `employees`

- `id`: integer unique employee id
- `name`: VARCHAR(30) employee's name
- `managerId`: integer manager id

Write a query that selects only the names of employees who are not managers.

### Example:
```sql
CREATE TABLE employees (
  id INTEGER NOT NULL PRIMARY KEY,
  managerId INTEGER, 
  name VARCHAR(30) NOT NULL,
  FOREIGN KEY (managerId) REFERENCES employees(id)
);

INSERT INTO employees(id, managerId, name) VALUES(1, NULL, 'John');
INSERT INTO employees(id, managerId, name) VALUES(2, 1, 'Mike');

-- Expected output (in any order):
-- name
-- ----
-- Mike

-- Explanation:
-- In this example.
-- John is Mike's manager. Mike does not manage anyone.
-- Mike is the only employee who does not manage anyone.
```

```sql
SELECT name FROM employees
WHERE id NOT IN (SELECT managerId FROM employees WHERE managerId IS NOT NULL);
```

## Sessions

App usage data are kept in the following table:

Table: `sessions`

- `id`: unique session id
- `userId`: user id for the session
- `duration`: duration of the session in minutes

```sql
CREATE TABLE sessions (
  id INTEGER NOT NULL PRIMARY KEY,
  userId INTEGER NOT NULL,
  duration DECIMAL NOT NULL
);

INSERT INTO sessions(id, userId, duration) VALUES(1, 1, 10);
INSERT INTO sessions(id, userId, duration) VALUES(2, 2, 18);
INSERT INTO sessions(id, userId, duration) VALUES(3, 1, 14);

-- Expected output:
-- UserId  AverageDuration
-- -----------------------
-- 1       12
```

Write a query that selects `userId` and average session `duration` for each user who has more than one session. The result should be ordered by `userId`.

```sql
SELECT userId, AVG(duration) AS AverageDuration
FROM sessions
GROUP BY userId
HAVING COUNT(*) > 1
ORDER BY userId;
```

## Students

Given the following data definition, write a query that returns the number of students whose first name is `John`.

Table: `students`
- `id`: integer unique student id
- `firstName`: VARCHAR(30) student's first name
- `lastName`: VARCHAR(30) student's last name

```sql
SELECT COUNT(1) FROM students 
WHERE firstName = 'John';
```

## Web Shop

Each item in a web shop belongs to a seller. To ensure service quality, each seller has rating. The data are kept in 
the following tables.

Table: `sellers`
- `id`: integer unique seller id
- `name`: VARCHAR(50) seller's name
- `rating`: INTEGER seller's rating

Table: `items`
- `id`: integer unique item id
- `name`: VARCHAR(50) item's name
- `sellerId`: integer seller id

Write a query that selects the item name and the name of its seller for each item that belongs to a seller with a 
rating greater than 4. The query should return the name of the item as the first column and name of the seller as 
the second column.

### Example:
```sql
CREATE TABLE sellers (
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  rating INTEGER NOT NULL
);

CREATE TABLE items (
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  sellerId INTEGER,
  FOREIGN KEY (sellerId) REFERENCES sellers(id)
);

INSERT INTO sellers(id, name, rating) VALUES(1, 'Roger', 3);
INSERT INTO sellers(id, name, rating) VALUES(2, 'Penny', 5);

INSERT INTO items(id, name, sellerId) VALUES(1, 'Notebook', 2);
INSERT INTO items(id, name, sellerId) VALUES(2, 'Stapler', 1);
INSERT INTO items(id, name, sellerId) VALUES(3, 'Pencil', 2);

-- Expected output (in any order):
-- Item      Seller
-- ----------------
-- Notebook  Penny
-- Pencil    Penny
```

```sql
SELECT items.name AS Item, sellers.name AS Seller
FROM items
JOIN sellers
ON items.sellerId = sellers.id
WHERE sellers.rating > 4;
```

## Users and Roles

The following two tables are used to define users and their respective roles:

Table: `users`
- `id`: integer unique user id
- `userName`: VARCHAR(50) user's name

Table: `roles`
- `id`: integer unique role id
- `role`: VARCHAR(50) role's name

The `usersRole` table should contain the mapping between each user and their roles. Each user can have many roles 
and each role can have many users.

Modify the provided SQL create table statement so that:
- only users from the users table can exist within `usersRoles` table
- only roles from the roles table can exist within `usersRoles` table
- A user can only hae a specific role once.

```sql
CREATE TABLE usersRoles (
    userId INTEGER NOT NULL,
    roleId INTEGER NOT NULL,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (roleId) REFERENCES roles(id)
);
```
