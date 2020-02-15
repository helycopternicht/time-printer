# HOW TO INSTALL time-printer

### Requirements
To start project you need:
 - jdk >= 8
 - docker
 - docker-compose
 
### Step 1. 
Clone project and enter project directory
```
git clone https://github.com/helycopternicht/time-printer.git
cd time-printer
```

### Step 2. 
Setup database. Please be sure that you have no another app on 3306 port
```
docker-compose up -d
docker-compose ps
```

### Step 3.
Build application
```
./mvnw package
```

### Step 4.
Start application in write mode. Without parameters. And after few time press Ctrl+C to terminate app.
```
java -jar target/time-printer-0.0.1-SNAPSHOT.jar
```

### Step 4.
Start application in read mode ('-p' parameter) to see printed results.
```
java -jar target/time-printer-0.0.1-SNAPSHOT.jar -p
```

We've done it.
Instruction requires linux based OS. For other operation systems may need some changes
