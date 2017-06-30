# Pact Testing with Kubernetes #

This project is a collection of a few Gradle based projects and Docker containers that demonstrate Pact testing using Kubernetes. 

Highly cohesive and loosely coupled business functions can have a significant impact on your agility to continuously deliver features. Microservices in containers is an effective mechanism for continuous delivery. However, before you bite into that big sandwich, consider how provisioning a variety of data flavors as containerized endpoints could greatly improve your testing.

How many times have you heard a colleague say, “Well that feature does not have integration tests because it requires a database with some specialized data”? Balderdash - put your data flavors in containers!

This project explores a solution to create a pipeline of data flavors. We use Docker images, Kubernetes Pods, Minikube to provision these endpoints. See how a Gradle project drives integration tests against these Pod endpoints, all ready for your continuous integration pipeline. In the end, you can see the power of Consumer Driven Contracts against your dataset flavors.

[Pact](https://docs.pact.io) is a testing tool that guarantees the Consumer Driven Contracts are satisfied by the Provider. A consumer is a client who defines a set of tests that define the contracts, the pacts. Those pacts are documents what is stored in a pact repository. At any time, the writers and tests of the API, the providers, can pull those contracts and run them as tests against their changing API.

Empower your team to create their own dataset flavors in containers for developing [Consumer Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html). See a wall to integration testing come down.

In this project, you will build and deploy two containers with a Gradle based project, deploy several containers to a Kubernetes cluster and interact with the Pact tools using Gradle based project. 

| Kubernetes containers           | Gradle based projects |
| ------------------------------- | ----------------------|
| Private container registry      | H2 Database seeded with some world statistics |
| H2 Database                     | Microservice to model the world data |
| REST/Json Microservice          | Pact testing project that produces and consumes the Pacts |
| Pact Broker |

## Where Do I Start? ##

Start with the tests. The Consumer Driven Contracts is what drives this whole project, so get familiar with those first. Checkout this whole project and start working by running `gradlew test` from the folder `pact-consumer`.

While the tests will generate pact files in the build directory. The next step is to publish those packs to a pact broker then have a provider consume those tests. The provider and broker will run on a Kubernetes cluster so next step is to setup the cluster with its containers.

  
## How do I get set up? ##

The cluster runs on Kubernetes. Here we use the Minikube solution to setup simple Kubernetes cluster. You should be able to run this project on Windows, OS X or Linux based on your preferences.


### Installs ###

* Install VirtualBox, this is an ideal virtual machine manager for Minikube
* Install Minikube
* Install KubeCtl
* Ensure you have a command prompt that can run Bash scripts, on Windows Git Bash, ConEmu or Cmder can work well.

### Start Kubernetes ###

* In the project directory run the `start.sh` script. Before starting view the script to see what it does
* Minikube will be running in a few minutes, source the init.sh file with ". ./init.sh". For each command prompt that interacts with Minikube you must always first source the init.sh script (you can add this sourcing to your .bash_profile)
* Kubernetes will now be running several support services including a dashboard, a private docker registry and heapster as a resource monitor.
* Type `minikube status` to ensure its running.
* Type `minikube dashboard` to explore its state.


### Deploy A Registry UI ###

* Change to the `cluster` folder and type `kubectl create -f registry-ui`
* You will now have a registry browser available discoverable with `minikube service list`

### Deploy A Pact Broker ###

* Change to the `cluster` folder and type `kubectl create -f pact-broker`
* You will now have a registry browser available discoverable with `minikube service list`


### Deploy the h2-world-a Container ###

* Change to the `h2-world-a` folder
* Build and deploy the container using `gradlew pushImage`
* In a minute or so verify the deployed database ready and alive with this command:
(todo - provide shell script to launch database browser )
* In the UI dialog connect to the database with this JDBC URL: jdbc:h2:/usr/h2-data/world, leave the User Name and Password field blank.

### Deploy the ms-world Container ###

* Change to the `ms-world` folder
* Build and deploy the container using `gradlew pushImage`
* In a minute or so verify the deployed microservice is ready and alive with this command:
(todo - provide shell script to launch browser with rest commands)

## How Do I Generate and Verify Pacts? ##

### Publish Pact Tests ###

* Change to the `pact-consumer-cities` folder
* Run the consumer tests and publish the pacts with `gradlew pactPublish`

### Verify Pact Tests ###

* Change to the `pact-provider-world` folder
* Run the provider tests and verify them against the producer running in Kubernetes with `gradlew packVerify`


## That's It ! ##

You now have performed Pact based tests using Pact, Kubernetes, Microservices and Containers. Notice how you could easily create more databases with different seeds and more services with a variety of pacts.


### How can I customize it? ###

There is more data in the database in other tables. 
You can use the ms-world as an example microservice and use it 
to access different data. First look at the database contents 
and envision a new service to get more data. First write a Pact
test that describes, from a customer point of view what should 
be delivered. Verify the Pact tests run with the mocked data 
and publish the Pacts. Clone the ms-world microservice and write 
your own model that provides the service you have envisioned. Deploy 
the new service.

### Credits and References ###

* [VirtualBox](https://www.virtualbox.org/wiki/VirtualBox)
* [Minikube](https://github.com/kubernetes/minikube)
* [Gradle](https://gradle.org)
* [Kubernetes](https://kubernetes.io)
* [Docker](https://www.docker.com)
* [Docker private registry](https://docs.docker.com/registry/)
* [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html)
* [Pact](https://docs.pact.io)*
* [Pact Broker](https://github.com/DiUS/pact_broker-docker)
* [Open JDK and Java 9](https://hub.docker.com/_/openjdk/)
* [Docker Registry UI](https://github.com/mkuchin/docker-registry-web)
* [H2 Database](http://www.h2database.com/html/main.html)
* [World data source](https://dev.mysql.com/doc/index-other.html)
* [Gradle docker plugin](https://github.com/bmuschko/gradle-docker-plugin)
* [Dius Pact](https://docs.pact.io)
* [Spring Boot](https://projects.spring.io/spring-boot/)
* [MyBatis](http://www.mybatis.org/mybatis-3/)
* [Fabric8](https://fabric8.io)
* [Guava](https://github.com/google/guava)
* [JUnit](http://junit.org/junit4/)
* [Linux Alpine Containers](https://hub.docker.com/_/alpine/)
* [SemVer](http://semver.org)


### How do I shut down? ###

* You can suspend Minikube and the entire cluster with `minikube stop`
* You can also just delete the Kubernetes namespaces with 
`kubectl namespace pact` 
* The easiest way to start fresh is `minikube delete`. This will 
remove the whole Minikube installation from the virtual machine and can always be recreate with `Minikube start [...]`