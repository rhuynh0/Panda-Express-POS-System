import psycopg2
from datetime import datetime, timedelta
import random

conn = psycopg2.connect(
    dbname="team_xc_db",
    user="team_xc",
    password="kindcake33",
    host="csce-315-db.engr.tamu.edu",  
    port="5432"        
)

cursor = conn.cursor()

employee_names = ['Joseph Dillard', 'Jane Smith', 'Chris Johnson', 'Ashley Brown', 'Michael White',
                  'Laura Black', 'Brian Green', 'Emily Gray', 'David Blue', 'Sarah Yellow']
job_titles = ['Cashier', 'Cook', 'Manager', 'Shift Supervisor', 'Delivery Driver', 'Server']

# Helper function to generate random employee data
def generate_employee_data(employee):
    employeename = random.choice(employee_names)  # Random employee name
    jobtitle = random.choice(job_titles)  # Random job title
    startdate = (datetime.now() - timedelta(days=random.randint(100, 3650))).strftime('%Y-%m-%d')  # Random start date within the last 10 years
    hourlywage = round(random.uniform(10.0, 30.0), 2)  # Random hourly wage between $10 and $30
    
    return (employee, employeename, jobtitle, startdate, hourlywage)

# Populate the employees table with 10 random entries
employeeID = 0
for _ in range(10):
    employeeID += 1
    employee_data = generate_employee_data(employeeID)
    cursor.execute('''
    INSERT INTO employees (employeeID, employeename, jobtitle, startdate, hourlywage)
    VALUES (%s, %s, %s, %s, %s)
    ''', employee_data)

# Commit changes and close the connection
conn.commit()
cursor.close()
conn.close()

print("Employees table populated with 10 entries.")
