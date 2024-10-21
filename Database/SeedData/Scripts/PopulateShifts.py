from datetime import datetime, timedelta

start_date = datetime(2023, 9, 1)
end_date = datetime(2024, 9, 1)

shift_times = [
    ("10:00:00", "14:00:00"),
    ("14:00:00", "18:00:00"),
    ("18:00:00", "21:00:00")
]
weekdays = [0, 1, 2, 3, 4]


def date_range(start, end):
    current = start
    while current <= end:
        yield current
        current += timedelta(days=1)

shift_statements = []
shift_id = 1


for current_date in date_range(start_date, end_date):
    if current_date.weekday() in weekdays:
        for start_time, end_time in shift_times:
            shift_statements.append(
                f"INSERT INTO Shifts (shiftstarttime, shiftendtime, shiftdate) "
                f"VALUES ('{start_time}', '{end_time}', '{current_date.date()}');"
            )
            shift_id += 1

with open('PopulateShifts.sql', 'w') as file:
    for statement in shift_statements:
        file.write(statement + '\n')