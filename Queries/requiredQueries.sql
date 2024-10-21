--Weekly Sales History
SELECT 
    COUNT(*) AS DayOrderCount
FROM 
    Orders
WHERE 
    orderDate >= DATE '2023-09-01' -- CHANGE DATE HERE
    AND orderDate < DATE '2023-09-01' + INTERVAL '7 days';

--Realistic Sales History
SELECT 
    COUNT(*) AS HourOrderCount,
    SUM(orderPrice) AS HourlyRevenue
FROM 
    Orders
WHERE 
    orderTime >= TIME '12:00:00' -- CHANGE START TIME HERE
    AND orderTime < TIME '12:00:00' + INTERVAL '1 hour';

--Peak Sales Day
WITH Top10Sales AS (
    SELECT 
        orderprice AS TotalOrderPrice
    FROM 
        Orders
    WHERE 
        orderDate = '2023-09-01' -- CHANGE DATE HERE
    ORDER BY 
        TotalOrderPrice DESC
    LIMIT 10
)
SELECT 
    SUM(TotalOrderPrice) AS TotalSumOfTop10
FROM 
    Top10Sales;

--Menu Item Inventory
SELECT 
    menuItem.menuname,
    COUNT(menuItemInventory.inventoryid) AS inventoryItemCount
FROM 
    menuItem
JOIN 
    menuItemInventory ON menuItem.menuid = menuItemInventory.menuid
JOIN 
    inventory ON menuItemInventory.inventoryid = inventory.inventoryid
WHERE 
    menuItem.menuname = 'Orange Chicken'  -- CHANGE MENU ITEM HERE
GROUP BY 
    menuItem.menuname;
