--Who worked on a given day
SELECT DISTINCT
    Employees.employeeID,
    Employees.employeeName,
    Employees.jobtitle,
    Shifts.shiftDate
FROM 
    Employees
JOIN 
    EmployeeShifts ON Employees.employeeID = EmployeeShifts.employeeID
JOIN 
    Shifts ON EmployeeShifts.shiftID = Shifts.shiftID
WHERE 
    Shifts.shiftDate = '2023-10-02'; --Enter the date

--How many employees do we have under a specified job title
SELECT 
    jobtitle,
    COUNT(employeeID) AS numberOfEmployees
FROM 
    Employees
GROUP BY 
    jobtitle;

--How many orders were placed on a given date under a specific payment method
SELECT 
    paymentmethod,
    COUNT(orderid) AS numberOfOrders
FROM 
    orders
WHERE 
    orderdate = '2023-09-01' -- Enter the desired date
    AND paymentmethod = 'Mobile Payment' -- Enter the payment method
GROUP BY 
    paymentmethod;

--What items are in need of a reorder
SELECT 
    Inventory.inventoryID,
    Inventory.itemName,
    Inventory.quantityinstock,
    Inventory.quantityunits,
    Inventory.reorderthreshold
FROM 
    Inventory
WHERE 
    Inventory.quantityinstock <= Inventory.reorderthreshold;

--Who handled the most orders in a given month
SELECT 
    Employees.employeeID,
    Employees.employeeName,
    COUNT(Orders.orderID) AS numberOfOrders
FROM 
    Employees
JOIN 
    Orders ON Employees.employeeID = Orders.employeeID
WHERE 
    EXTRACT(MONTH FROM Orders.orderDate::DATE) = 10
GROUP BY 
    Employees.employeeID, Employees.employeeName
ORDER BY 
    numberOfOrders DESC 
LIMIT 1;

--Given a month, what is the busiest day of the week based on order volume
SELECT 
    TO_CHAR(Orders.orderDate::DATE, 'Day') AS dayOfWeek,
    COUNT(Orders.orderID) AS orderVolume
FROM 
    Orders
WHERE 
    EXTRACT(MONTH FROM Orders.orderDate::DATE) = 10
GROUP BY 
    dayOfWeek
ORDER BY 
    orderVolume DESC
LIMIT 1;

--List the top 5 employees based on total sales
SELECT 
    Employees.employeeID, Employees.employeeName, SUM(Orders.orderPrice) AS totalSales
FROM 
    Orders
JOIN 
    Employees ON Orders.employeeID = Employees.employeeID
GROUP BY 
    Employees.employeeID
ORDER BY 
    totalSales DESC
LIMIT 5;

--List the top 5 employees based on total hours worked
SELECT 
    Employees.employeeID,
    Employees.employeeName,
    SUM(EXTRACT(HOUR FROM (Shifts.shiftEndTime - Shifts.shiftStartTime)) 
        + EXTRACT(MINUTE FROM (Shifts.shiftEndTime - Shifts.shiftStartTime)) / 60) AS totalHoursWorked
FROM 
    Employees
JOIN
    EmployeeShifts ON Employees.employeeID = EmployeeShifts.employeeID
JOIN 
    Shifts ON EmployeeShifts.shiftID = Shifts.shiftID
WHERE 
    Shifts.shiftStartTime IS NOT NULL AND Shifts.shiftEndTime IS NOT NULL
GROUP BY 
    Employees.employeeID, Employees.employeeName
ORDER BY 
    totalHoursWorked DESC
LIMIT 5;



--List the top 5 menu items based on total sales
SELECT 
    MenuItem.menuID,
    MenuItem.menuName,
    COUNT(OrderMenuItem.OrderMenuItemID) AS timesOrdered,
    SUM(MenuItem.price) AS totalSales
FROM 
    MenuItem
JOIN 
    OrderMenuItem ON MenuItem.menuID = OrderMenuItem.menuItemID
JOIN 
    Orders ON OrderMenuItem.orderID = Orders.orderID
GROUP BY 
    MenuItem.menuID, MenuItem.menuName
ORDER BY 
    totalSales DESC
LIMIT 5;

--List the top 5 busiest shifts based on order volume
SELECT 
    Shifts.shiftID,
    Shifts.shiftDate,
    Shifts.shiftStartTime,
    Shifts.shiftEndTime,
    COUNT(DISTINCT Orders.orderID) AS orderVolume
FROM 
    Shifts
JOIN 
    EmployeeShifts ON Shifts.shiftID = EmployeeShifts.shiftID
JOIN 
    Orders ON Orders.employeeID = EmployeeShifts.employeeID
    AND Orders.orderDate = Shifts.shiftDate
    AND Orders.orderTime BETWEEN Shifts.shiftStartTime AND Shifts.shiftEndTime
GROUP BY 
    Shifts.shiftID, Shifts.shiftDate, Shifts.shiftStartTime, Shifts.shiftEndTime
ORDER BY 
    orderVolume DESC
LIMIT 5;