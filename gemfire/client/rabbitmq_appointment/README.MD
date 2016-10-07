
In order to have a stand alone application there are two options.

1. Using Maven

I added the pom.xml (it was missing only one parameter).
Using maven the only thing you do is:

$ mvn clean compile package -DskipTests=true


The above command will create a FAT jar that contains all the dependencies and can run stand alone with:

$ java -jar target/kaiser-rabbitmq-1.0.jar





2. Using Gradle (This is my prefered method because creates scripts!!)

You can use Gradle by using the IDE, just run the task listed as: distZip
The distZip task will create a zip file in:

build/distributions/kaiser-rabbitmq-1.0.zip

If you unzip it you will have the following structure:

.
├── bin
│   ├── kaiser-rabbitmq
│   └── kaiser-rabbitmq.bat
└── lib

(inside the lib folder all the dependencies)

and you can execute from the terminal:

$ bin/kaiser-rabbitmq





3. (Using Gradle from the command line)

You can use also gradle from your terminal, but you need to install the following:

Execute this on your terminal:

$ curl -s get.gvmtool.net | bash

the above command will install the GVM tool that contains different jvm tools like gradle in this case.
Follow the instructions after executing the above command.

After the gvm is installed you can open a new terminal (close the other) and install gradle

$ gvm install gradle


and thats it, you can run gradle just like:


$ gradle distZip


and it will create the zip file.
also you can run:

$ gralde tasks


it will give you the possible tasks available.

PoC Notes:  *** Warning: If local RabbitMQ env not running unit (integration) test(s) will not work ***
Setup for unit tests and execution.
* Local Rabbitmq server was lauched.
* Modified PR/RabbitMQApt.properties to point to localhost (uncomment for integrating code)
* Created 2 users (admin, mobile_user)
* Created 5 queues and 2 exchanges as outlined in PR/RabbitMQApt.properties







