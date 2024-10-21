from datetime import datetime, timedelta
import random

start_date = datetime(2023, 9, 1)
end_date = datetime(2024, 9, 1)

shift_times = [
    ("10:00:00", "14:00:00"),
    ("14:00:00", "18:00:00"),
    ("18:00:00", "21:00:00")
]
weekdays = [0, 1, 2, 3, 4]

# Function to create dates
def date_range(start, end):
    current = start
    while current <= end:
        yield current
        current += timedelta(days=1)

shift_statements = []
employee_shift_statements = []
employee_shift_statements.append(f"INSERT INTO employeeshifts (employeeid, shiftID) \n VALUES \n")
shift_id = 1
employee_shiftsid = 1

managers = [
    1,
    8
]

shiftlead = [
    3,
    9
]

cook = [
    4,
    7
]

driver = [
    5,
    10
]

cashier = [
    2,
    6
]



# Loop through dates, weekdays, and then of the three shifts in day to create shifts
for current_date in date_range(start_date, end_date):
    if current_date.weekday() in weekdays:
        for start_time, end_time in shift_times:
            shift_statements.append(
                f"INSERT INTO shifts (shiftstarttime, shiftendtime, shiftdate) "
                f"VALUES ('{start_time}', '{end_time}', '{current_date.date()}');"
            )
            
            
            # manager
            employee_shift_statements.append(
                f"({managers[random.randint(0, 1)]}, {shift_id}),"
            )
            # shiftlead
            employee_shiftsid += 1
            employee_shift_statements.append(
                f"({shiftlead[random.randint(0, 1)]}, {shift_id}),"
            )
            employee_shiftsid += 1
            # cook
            employee_shift_statements.append(
                f"({cook[random.randint(0, 1)]}, {shift_id}),"
            )
            employee_shiftsid += 1
            # driver
            employee_shift_statements.append(
                f"({driver[random.randint(0, 1)]}, {shift_id}),"
            )
            employee_shiftsid += 1
            # cashier
            employee_shift_statements.append(
                f"({cashier[random.randint(0, 1)]}, {shift_id}),"
            )
            employee_shiftsid += 1
            shift_id += 1
            
employee_shift_statements.append(
                f"({cashier[random.randint(0, 1)]}, {shift_id - 1});"
            )

with open('PopulateShifts.sql', 'w') as file:
    for statement in shift_statements:
        file.write(statement + '\n')
        
with open('PopulateEmployeeShifts.sql', 'w') as file:
    for statement in employee_shift_statements:
        file.write(statement + '\n')
