CREATE TABLE Employees (
    employeeID SERIAL PRIMARY KEY,
    employeeName varchar(100),
    jobTitle varchar(50),
    startDate date,
    hourlyWage float
);

CREATE TABLE Shifts (
    shiftID SERIAL PRIMARY KEY,
    shiftStartTime time,
    shiftEndTime time,
    shiftDate date
);

CREATE TABLE EmployeeShifts (
    employeeID INT,
    shiftID INT,
    FOREIGN KEY (employeeID) REFERENCES employees(employeeID),
    FOREIGN KEY (shiftID) REFERENCES shifts(shiftID)
);

CREATE TABLE Orders (
    orderID SERIAL PRIMARY KEY,
    orderTime time,
    orderDate date,
    employeeID int,
    orderPrice float,
    paymentMethod varchar(100),
    FOREIGN KEY (employeeID) REFERENCES employees(employeeID)
);

CREATE TABLE Inventory (
    inventoryID SERIAL PRIMARY KEY,
    itemName varchar(50),
    quantityInStock float,
    quantityUnits varchar(50),
    reorderThreshold float,
    unitPrice float
);

CREATE TABLE MenuItem (
    menuID SERIAL PRIMARY KEY,
    price float,
    menuName varchar(50),
    itemType varchar(10)
);

CREATE TABLE MenuItemInventory (
    menuID int,
    inventoryID int,
    quantityUsed float,
    PRIMARY KEY (menuID, inventoryID),
    FOREIGN KEY (menuID) REFERENCES MenuItem(menuID),
    FOREIGN KEY (inventoryID) REFERENCES Inventory(inventoryID)
);

CREATE TABLE OrderMenuItem (
    orderID int,
    menuItemID int,
    FOREIGN KEY (orderID) REFERENCES orders(orderID),
    FOREIGN KEY (menuItemID) REFERENCES MenuItem(menuID)
);