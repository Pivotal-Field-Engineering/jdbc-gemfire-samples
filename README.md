# jdbc-gemfire-samples
 * **gemfire**  - Gemfire project space (client,server,model)
  * **dataflow** - Custom SCS components for SCDF (jdbc-source,groovy-processor,gemfire-sink)
   * **test**     - Local test suite (gemfire-sink-test)

# Problem Statement

SCDF makes it simple to select a jdbc src and ingest the contents into
Gemfire.  However, in this particular reference had multiple goals to
be accomplished that surrounded a simple task:

1. Produce a Java Object Model in the spirit of java beans instead of using the Entity and
Column names from the RDBMS schema.
2. Create existing tables equivalent to current sql tables (authors,
books, titles, etc.)
3. Refresh all tables (except sales related - txn) that have source
data in underlying SQL tables daily ( bulk load or incremental )
4. Load sales data from current sales tables.
5. Insert/update/read Sales from java application (source is RabbitMQ and other clients/consumers)
[6. Persistence store in case of VM/Server reboot -- out of scope for samples]
[7. Use LDAP for Authentication -- out of scope for samples]
[8. Remove older Sales thru a cron job ]
[9. CSV file loader to load appointments from CSV file.]


** Starting Spring Cloud Dataflow and registering required apps
Follow the guidelines found at
[Getting Start : http://docs.spring.io/spring-cloud-dataflow/docs/1.0.1.RELEASE/reference/html/getting-started-deploying-spring-cloud-dataflow.html]. This
reference application is using postgres as a jdbc src and a custom
gemfire app for the sink.  The reasons for that will be explained in
the gemfire README.md

* Register the app
app register --name jdbc-gemfire-task --type task --uri
file:///[local-git-repo]/jdbc-gemfire-samples/dataflow/task/jdbc-gemfire-task/target/jdbc-gemfire-task-1.3.7.RELEASE.jar
--force
app list
task list

you can launch the task from either the shell or use
http://localhost:9393/dashboard/index.html#/tasks/definitions. The
parameters we're collected from the UI so that it could be scripted
as seen above.

The tasks are created in no particular order:
task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=Author --jdbcgemfire.commit-interval=1000
--jdbcgemfire.sql="select * from authors"' --name pubs-author

task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=Author --jdbcgemfire.commit-interval=1000
--jdbcgemfire.sql="select * from authors"' --name pubs-editors

task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
<<<<<<< HEAD
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=Editor --jdbcgemfire.commit-interval=1000
=======
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver 
--jdbcgemfire.region-name=Editor --jdbcgemfire.commit-interval=1000 
>>>>>>> master
--jdbcgemfire.sql="select * from editors"' --name pubs-editors

task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=Publisher --jdbcgemfire.commit-interval=1000
--jdbcgemfire.sql="select * from publishers"' --name pubs-publishers

task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=Sale --jdbcgemfire.commit-interval=1000
--jdbcgemfire.sql="select * from sales"' --name pubs-sales

task create --definition 'jdbc-gemfire-task
--jdbcgemfire.datasource.username=postgres
--jdbcgemfire.datasource.url=jdbc:postgresql://localhost:5432/postgres
--jdbcgemfire.datasource.driver-class-name=org.postgresql.Driver
--jdbcgemfire.region-name=SalesDetail
--jdbcgemfire.commit-interval=1000
--jdbcgemfire.sql="select * from salesdetails"'
--name pubs-sales-details



** Starting Gemfire

Background on requirements 