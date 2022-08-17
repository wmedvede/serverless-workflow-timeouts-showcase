### Preparing your environment

1. Install [minikube](https://minikube.sigs.k8s.io/docs/start/)
2. Install Knative using the [quickstarts](https://knative.dev/docs/getting-started/) since a DNS will be configured for you.

> **NOTE:** Every time you restart your minikube installation, be sure that you do it with the knative profile activated, and that you have enabled the minikube tunnel for this profile after it has started.

### Building the project

Open a terminal window, go to the serverless-workflow-timeouts-showcase directory, and execute these commands to be sure the generated images are stored in the Minikube internal registry. 

```shell
eval $(minikube -p knative docker-env)

mvn clean install -Pknative
```

### Postgresql database setup:

Deploy the postgresql database service with the following command:

```shell
kubectl apply -f kubernetes/timeouts-showcase-database.yml

# After executing the command above you will see an output like this:

secret/timeouts-showcase-database created
deployment.apps/timeouts-showcase-database created
service/timeouts-showcase-database created
```

### Jobs Service with Postgresql persistence installation:

Deploy the Jobs Service with the following command:

```shell
kubectl apply -f kubernetes/jobs-service-postgresql-knative.yml

# After executing the command above you will see an output like this:

service.serving.knative.dev/jobs-service-postgresql created
trigger.eventing.knative.dev/jobs-service-postgresql-create-job-trigger created
trigger.eventing.knative.dev/jobs-service-postgresql-cancel-job-trigger created
sinkbinding.sources.knative.dev/jobs-service-postgresql-sb configured
```

### Jobs Service logs (optional step):

To see the jobs service logs you can execute this procedure:

```shell
kubectl get pod | grep jobs-service-postgresql

# After executing the command above you will see an output like this:

jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552   2/2     Running   0          19m

# Note that it might take some time for the service to start, and the pod name can be different in your installation.

# To see the jobs service logs you can execute this command:

kubectl logs jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552

__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2022-08-16 07:49:20,357 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.kie.kogito.jobs.service.json.JacksonConfiguration:-1] (main) Jackson customization initialized.
2022-08-16 07:49:20,826 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.license.VersionPrinter:-1] (main) Flyway Community Edition 8.5.13 by Redgate
2022-08-16 07:49:20,826 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.license.VersionPrinter:-1] (main) See what's new here: https://flywaydb.org/documentation/learnmore/releaseNotes#8.5.13
2022-08-16 07:49:20,826 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.license.VersionPrinter:-1] (main) 
2022-08-16 07:49:20,928 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.database.base.BaseDatabaseType:-1] (main) Database: jdbc:postgresql://timeouts-showcase-database:5432/postgres (PostgreSQL 13.4)
2022-08-16 07:49:20,952 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.schemahistory.JdbcTableSchemaHistory:-1] (main) Creating Schema History table "public"."flyway_schema_history" ...
2022-08-16 07:49:21,044 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.command.DbMigrate:-1] (main) Current version of schema "public": << Empty Schema >>
2022-08-16 07:49:21,053 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.command.DbMigrate:-1] (main) Migrating schema "public" to version "2.0.0 - Create Table"
2022-08-16 07:49:21,095 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.command.DbMigrate:-1] (main) Migrating schema "public" to version "2.0.1 - job details increase job id size"
2022-08-16 07:49:21,111 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.flywaydb.core.internal.command.DbMigrate:-1] (main) Successfully applied 2 migrations to schema "public", now at version v2.0.1 (execution time 00:00.093s)
2022-08-16 07:49:21,438 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [io.quarkus:-1] (main) jobs-service-postgresql 2.0.0-SNAPSHOT on JVM (powered by Quarkus 2.11.2.Final) started in 1.863s. Listening on: http://0.0.0.0:8080
2022-08-16 07:49:21,438 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [io.quarkus:-1] (main) Profile prod activated. 
2022-08-16 07:49:21,438 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [io.quarkus:-1] (main) Installed features: [agroal, cdi, flyway, jdbc-postgresql, kafka-client, narayana-jta, oidc, reactive-pg-client, reactive-routes, resteasy, resteasy-jackson, security, smallrye-context-propagation, smallrye-fault-tolerance, smallrye-health, smallrye-openapi, smallrye-reactive-messaging, smallrye-reactive-messaging-http, smallrye-reactive-messaging-kafka, swagger-ui, vertx]
2022-08-16 07:49:21,588 jobs-service-postgresql-00001-deployment-76f7f6bb76-6z552 INFO  [org.kie.kogito.jobs.service.scheduler.JobSchedulerManager:-1] (executor-thread-0) Loading scheduled jobs completed !
```

### Timeouts showcase service installation

To install the example workflows execute these commands.

```shell
kubectl apply -f timeouts-showcase/target/kubernetes/knative.yml


# After executing the commands above you will see an output like this:

service.serving.knative.dev/timeouts-showcase created

trigger.eventing.knative.dev/visa-denied-event-type-trigger-timeouts-showcase created
trigger.eventing.knative.dev/callback-state-event-type-trigger-timeouts-showcase created
trigger.eventing.knative.dev/visa-approved-event-type-trigger-timeouts-showcase created
sinkbinding.sources.knative.dev/sb-timeouts-showcase created

```

To get the URL to access the service you can use this command:

```shell
kn service list | grep timeouts-showcase

# After executing the commands above you will see an output like this:

NAME                      URL                                                             LATEST                          AGE     CONDITIONS   READY   REASON
timeouts-showcase         http://timeouts-showcase.default.10.105.86.217.sslip.io         timeouts-showcase-00002         3m50s   3 OK / 3     True 
```

Note that the output above might be different in your installation.

### Verify the timeouts showcase is functioning using CURL

To execute the following commands you must use the http://timeouts-showcase.default.10.105.86.217.sslip.io corresponding to your installation.

1. Create a `switch_state_timeouts` SW instance and verify that the timeout was executed.

```shell
curl -X 'POST' \
  'http://timeouts-showcase.default.10.105.86.217.sslip.io/switch_state_timeouts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "workflowdata": {}
}'

# The command above will produce an output like this:

{"id":"2e8e1930-9bae-4d60-b364-6fbd61128f51","workflowdata":{}}
```

If you execute the following command in the next 30 seconds after the SW instance was created, you'll get the following results:
```shell
curl -X 'GET' \
  'http://timeouts-showcase.default.10.105.86.217.sslip.io/switch_state_timeouts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "workflowdata": {}
}'

# The command above will produce an output like this:

{"id":"2e8e1930-9bae-4d60-b364-6fbd61128f51","workflowdata":{}}
```

Execute this command 30+ seconds after the SW instance was created, and you'll get the following results.
```shell
curl -X 'GET' \
  'http://timeouts-showcase.default.10.105.86.217.sslip.io/switch_state_timeouts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "workflowdata": {}
}'

# The command above will produce an output like this [], which means that the SW has timed-out.

[]
```

