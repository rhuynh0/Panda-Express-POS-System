# project-2-team-XC

STEPS TO RUN:
Double check that you're connected to a VPN

CONNECT TO DATABASE:
psql -h csce-315-db.engr.tamu.edu -U team_xc -d team_xc_db

Navigate to /app/ and run the following command
javac -d bin -cp "bin\postgresql-42.2.8.jar" src\frontend\*.java src\backend\*.java src\models\*.java

Then, run the following command
java -cp "bin;bin\postgresql-42.2.8.jar" frontend.POSSystem

