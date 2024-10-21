import csv
from datetime import datetime

orders_csv = 'orders.csv'
ordermenuitem_csv = 'ordermenuitem.csv'
sql_file_path = 'populate_orders_and_menuitems.sql'

with open('orders.sql', 'w') as sql_file:

    with open('/Users/danielwarren/Repos/Classes/CSCE331/project-2-team-XC/Database/SeedData/Scripts/orders.csv', 'r') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) 

        sql_file.write(f"INSERT INTO orders (ordertime, orderdate, employeeid, orderprice, paymentmethod) \n VALUES \n")
        rows = []
        for row in reader:
            ordertime, orderdate, employeeid, orderprice, paymentmethod = row
            rows.append(f"('{ordertime}', '{orderdate}', {employeeid}, {orderprice}, '{paymentmethod}')")

        sql_file.write(",\n".join(rows))
        sql_file.write(";\n")
        
        

with open('ordermenuitem.sql', 'w') as sql_file:
    with open('/Users/danielwarren/Repos/Classes/CSCE331/project-2-team-XC/Database/SeedData/Scripts/ordermenuitem.csv', 'r') as csvfile:
        sql_file.write(f"INSERT INTO ordermenuitem (ordermenuitemid, orderid, menuitemid) \n VALUES \n")
        reader = csv.reader(csvfile)
        next(reader) 

        rows = []
        for row in reader:
            orderid, menuitemid = row
            rows.append(f"({orderid}, {menuitemid})")

        sql_file.write(",\n".join(rows))
        sql_file.write(";\n") 

print(f"SQL insert statements written to {sql_file_path}")
