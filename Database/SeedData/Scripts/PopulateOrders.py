import csv
from datetime import datetime, timedelta
import random

start_date = datetime(2023, 9, 1)
weeks_to_generate = 52  
orders_per_week = 2500
chunk_size = 500  

employee_ids = list(range(1, 11))  
payment_methods = ['Credit Card', 'Debit Card', 'Cash', 'Mobile Payment', 'Gift Card']

menu_items = {
    1: ('White Rice', 'Side', 4.40),
    2: ('Fried Rice', 'Side', 4.40),
    3: ('Chow Mein', 'Side', 4.40),
    4: ('Super Greens', 'Side', 4.40),
    5: ('Orange Chicken', 'Entree', 8.50),
    6: ('Teriyaki Chicken', 'Entree', 8.50),
    7: ('Broccoli Beef', 'Entree', 8.50),
    8: ('Kung Pao Chicken', 'Entree', 8.50),
    9: ('Beijing Beef', 'Entree', 8.50),
    10: ('Sirloin Steak', 'Entree', 8.50),
    11: ('Honey Walnut Shrimp', 'Entree', 11.50),
    12: ('Honey Sesame Chicken Breast', 'Entree', 8.50),
    13: ('Mushroom Chicken', 'Entree', 8.50),
    14: ('Sweetfire Chicken Breast', 'Entree', 8.50),
    15: ('String Bean Chicken Breast', 'Entree', 8.50),
    16: ('Black Pepper Chicken', 'Entree', 8.50),
    17: ('Blazing Bourbon Chicken', 'Entree', 8.50),
    18: ('Chicken Egg Roll', 'Appetizer', 2.00),
    19: ('Veggie Spring Roll', 'Appetizer', 2.00),
    20: ('Chicken Teriyaki Bowl', 'Combo', 8.30),
    21: ('Orange Chicken Plate', 'Combo', 9.80)
}

def generate_random_time(date):
    random_time = (date + timedelta(hours=random.randint(8, 20), minutes=random.randint(0, 59))).strftime('%H:%M:%S')
    return random_time

def generate_order_data(orderid, week_num, current_day):
    base_date = start_date + timedelta(weeks=week_num, days=current_day)
    
    ordertime = generate_random_time(base_date)
    orderdate = base_date.strftime('%Y-%m-%d')
    employeeid = random.choice(employee_ids)
    paymentmethod = random.choice(payment_methods)
    
    return [ordertime, orderdate, employeeid, 0.0, paymentmethod]  

def generate_ordermenuitem_data(orderid, ordermenuitemid):
    total_price = 0.0
    num_items = random.randint(1, 5) 
    
    ordermenuitem_entries = []
    
    for _ in range(num_items):
        menuitemid = random.choice(list(menu_items.keys()))
        _, _, item_price = menu_items[menuitemid]
        total_price += item_price
        
        ordermenuitem_entries.append([orderid, menuitemid])
        ordermenuitemid += 1
    
    return ordermenuitem_entries, total_price

# List to store all order data and ordermenuitem data
order_data = []
ordermenuitem_data = []

orderid = 1
ordermenuitemid = 1
for week in range(weeks_to_generate):
    current_day = 0  
    for _ in range(orders_per_week):
        order = generate_order_data(orderid, week, current_day)
        
        menu_items_for_order, total_price = generate_ordermenuitem_data(orderid, ordermenuitemid)
        ordermenuitem_data.extend(menu_items_for_order)
        ordermenuitemid += len(menu_items_for_order)

        order[3] = round(total_price, 2)  

        order_data.append(order)
        
        orderid += 1

        if orderid % chunk_size == 0:
            current_day = (current_day + 1) % 7  

with open('orders.csv', 'w', newline='') as csvfile:
    fieldnames = ['ordertime', 'orderdate', 'employeeid', 'orderprice', 'paymentmethod']
    writer = csv.writer(csvfile)

    writer.writerow(fieldnames)

    writer.writerows(order_data)

# Write the ordermenuitem data to 'ordermenuitem.csv'
with open('ordermenuitem.csv', 'w', newline='') as csvfile:
    fieldnames = ['orderid', 'menuitemid']
    writer = csv.writer(csvfile)

    writer.writerow(fieldnames)

    writer.writerows(ordermenuitem_data)

print(f"CSV files 'orders.csv' and 'ordermenuitem.csv' created.")
