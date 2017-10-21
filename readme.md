# Pact Testing with Kubernetes #

This project is a collection of a few Gradle based projects and Docker containers that 
demonstrate consumer-driven contract testing using Pact and Kubernetes. 

Highly cohesive and loosely coupled business functions can have a significant impact 
on your agility to continuously deliver features. Microservices in containers is an 
effective mechanism for continuous delivery. However, before you bite into that big 
sandwich, consider how provisioning a variety of data flavors as containerized endpoints 
could greatly improve your testing coverage.

How many times have you heard a colleague say, “Well that feature does not have 
integration tests because it requires a database with some specialized data”? 
Balderdash - put your data flavors in containers!

This project explores a solution to create a pipeline of data flavors. We use Docker 
images, Kubernetes Pods and Minikube to provision these endpoints. See how a Gradle project 
drives integration tests against these Pod endpoints, all ready for your continuous 
integration pipeline. In the end, you can see the power of Consumer Driven Contracts 
against your dataset flavors.

[Pact](https://docs.pact.io) is a testing framework that guarantees the Consumer Driven 
Contracts are satisfied by the Provider. A consumer is a client who defines a set of 
tests that define the contracts, the pacts. Those pacts are documents what is stored 
in a pact repository. At any time, the writers and tests of the API, the providers, 
can pull those contracts and run them as tests against their changing API.

Empower your team to create their own dataset flavors in containers for developing 
[Consumer Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html). 
See a wall to integration testing come down.

In this project, you will build and deploy two containers with a Gradle based project, 
deploy several containers to a Kubernetes cluster and interact with the Pact tools using 
Gradle based project. 

| Kubernetes containers           | Gradle based projects                         |
| ------------------------------- | --------------------------------------------- |
| Private container registry      | H2 Database seeded with some world statistics |
| H2 Database                     | Microservice to model the world data          |
| REST/JSON Microservice          | Testing with mocks producing Pacts            |
| Pact Broker                     | Pact verification                             |

The steps below are also captured in a collection of screen shot videos in the file 
readme-video-examples.zip

## Where Do I Start? ##

The consumer tests are a good place to start, but first get the technology stack up 
and running. The consumer will need to publish the pact and the service provider will
need to run the pacts.

  
## How do I set up the live service provider? ##

The cluster runs on Kubernetes. Here we use the 
[Minikube solution](https://kubernetes.io/docs/getting-started-guides/minikube/) 
to set up a simple Kubernetes cluster. You may run this project on Windows, 
OS X or Linux based on your preferences.


### Installs ###

Ensure you have a command prompt that can run shell scripts, on Windows Git Bash, ConEmu or 
Cmder can work well.

#### VirtualBox ####

Install [VirtualBox](https://www.virtualbox.org/wiki/Downloads), as this is an ideal cross platform virtual 
machine manager tested with Minikube. For any virtualization solution you may need to enable 
the virtualization BIOS setting. Here is an [article](https://docs.fedoraproject.org/en-US/Fedora/13/html/Virtualization_Guide/sect-Virtualization-Troubleshooting-Enabling_Intel_VT_and_AMD_V_virtualization_hardware_extensions_in_BIOS.html) 
on enabling Intel VT and AMD-V virtualization hardware extensions in the BIOS.

The chart below offers one-liner shell command that will download the current version of two 
executables needed to initialize and interact with Minikube and Kubernetes from the command line.

#### Minikube and KubeCtl ####

For Linux or OS X

    curl -Lo minikube https://github.com/kubernetes/minikube/releases/latest && chmod +x minikube && sudo mv minikube /usr/local/bin/
        
For Windows with Git Bash (or equivalent Linux shell)
    
    curl -Lo minikube.exe https://storage.googleapis.com/minikube/releases/latest/minikube-windows-amd64.exe



### Start Kubernetes ###

Normally Minikube is installed into the virtual machine with just a simple command 
'minikube start', however for this project there are a few other things needed so 
all those details are encompassed in this script. Before starting view the script to 
see what it does. 

    Start Kubernetes
    
    ./start.sh

In a few minutes Minikube will be running with a complete Kubernetes cluster and the 
script will complete. Next, set the environment parameters to allow Minikube and 
KubeCtl to interact with the new running cluster:

    Initialize environment settings
    
    . ./env.sh && minikube status

For each command prompt that interacts with Minikube be sure to always first source this 
env.sh script (you can add this sourcing to your .bash_profile). Kubernetes will now be 
running several support services including a dashboard, a private Docker registry and 
Heapster as a resource monitor. You can run the Kubernetes dashboard  and see these services 
running in the kube-system namespace.

    Explore Kubernetes

    minikube dashboard
     
Select `pacts` from the namespace dropdown and notice the Workload lists are empty. 
Kubernetes is now ready to start accepting some declarations so it can standup the
ms-cities and h2 database we require for the Pact verification.

## Deploy the Containers ##

### Deploy h2-world-a Container ###

    Build and deploy the database container
    
    cd h2-world-a && ./gradlew pushImage && cd ..
    
The database container image is now listed in the private registry running on the cluster.

### Deploy ms-cities Container ###

    Build and deploy the ms-cities microservice container
    
    cd ms-cities && ./gradlew pushImage && cd ..

The ms-cities container image is now listed in the private registry running on the cluster.

### View Container Registry Contents ###

While you just deployed two containers to the registry, it's difficult to see them without a view
that lists the contents of the registry. Run this command to add a service that enables
a browser based viewer for the registry:

    Start a registry viewer service
    
    kubectl create -f cluster/registry-ui

Shortly, based on your machine and network speed the registry will be browsable.
 
    View the registry 
    
    minikube service --namespace kube-system registry-ui
    
This command will point your browser to the registry listing page and there 
the `h2-world-a` and `ms-cities` repositories are listed.

### Start Pact Broker ###

The Pact files will need to be deployed to a Pack Broker, so the broker needs to be started.

    Start the Pact Broker Service

    kubectl create -f cluster/pact-broker

Shortly, the broker will be browsable.
 
    View the Pact broker contents

    minikube service --namespace pact pact-broker

The broker listing indicates the broker is responding, but will be empty since the Pacts have 
not been pushed yet.

### Start h2-world-a Service ###

While the database container image has been pushed into the registry the database service is not running.

    Start the database

    kubectl create -f cluster/h2-world-a

Shortly the database will be available. The database comes with a browser based SQL viewer. 
   
    Explore the database
    
    minikube service --namespace pact h2-world-a

This will open two tabs in your browser, close the second tab as it's the JDBC URL and in 
the first tab you will see a JDBC web form. In the web form connect to the database with 
this JDBC URL: 

    jdbc:h2:/usr/h2-data/world
    
Leave the `User Name` and `Password` fields blank and click the `Connect` button. At the 
subsequent browser page the world database can be explored with standard SQL commands.

### Start ms-cities Service ###

While the ms-cities microservice image has been pushed into the registry the microservice 
is not running.

    Start the ms-cities service
    
    kubectl create -f cluster/ms-cities

Shortly the ms-cities service will be available. 

    Request information from the service

    minikube service --namespace pact ms-cities
    
This will open a browser tab with the error message `Whitelabel Error Page`. This is 
expected as the service requires a specific REST call. Add any of these to the end of 
the browser URL 

    Relative REST calls for ms-cities
    
    /city/random, /city/denver, /city/list or /city/largest

## How Do I Generate and Verify Pacts? ##

### Publish Pact Tests ###

The Pact broker listing is currently empty. The Pacts need to be pushed to this broker.

    Push Pacts to Broker

    cd pact-consumer-cities && ./gradlew pactPublish && cd ..

Verify the Pacts have been deployed to the broker by viewing the listing.

    View the Pact broker listings

    minikube service --namespace pact pact-broker


### Verify Pact Tests ###

Services are running and the Pacts are deployed so now it's time to Run the provider tests 
and verify them against the producer running in Kubernetes.

    Verify the Pacts
    
    cd pact-provider-world && ./gradlew pactVerify && cd ..

The tests will run and generate the results. **But wait, the test failed!**. You should
see the error message "Expected 'Denver' but received 'This is wrong'". The pack 
verification for the /city/denver failed. To fix the problem modify the ms-world's 
CityController class's getDenver() method to correct the error.

    Rebuild and push the corrected ms-cities microservice

    cd ms-cities && ./gradlew pushImage && cd ..

Force the update of the existing ms-cities pod with

    kubectl delete pod --namespace pact $(kubectl get pod --namespace pact -o=custom-columns=NAME:.metadata.name | grep ms-cities)
	
	
Wait a few seconds, then again run the pactVerify and notice both tests now pass.

    RE-verify the Pacts
    
    cd pact-provider-world && ./gradlew pactVerify && cd ..

You should see the output of the verifications ending with "BUILD SUCCESSFUL".
	
## That's It ! ##

You now have performed Pact based tests using Pact, Kubernetes, Microservices and Containers. 
Notice how you could create more databases with different seeds, more client tests that generate
more contacts and more services with a variety of Pacts.


### How can I customize it? ###

There is more data in the database in other tables. You can use the ms-cities as an example 
microservice and use it to access different data. First look at the database contents and 
envision a new service to get more data. First write a Pact test that describes, from a 
customer point of view, what should be delivered. Verify the Pact tests run with the mocked 
service and publish the Pacts. Clone the ms-cities microservice and write your own model that 
provides the service you have envisioned. Deploy the new service.

### Credits and References ###

If you have further interest about this project and related topics you can meet me on tour
with the []No Fluff Just Stuff](www.nofluffjuststuff.com) events. Through any channel, I would 
appreciate your feedback.

This project was tested using Minikube v0.20.0 with Kubernetes V1.7.0 and VirtualBox 5.1.22

* [VirtualBox](https://www.virtualbox.org/wiki/VirtualBox)
* [Minikube](https://github.com/kubernetes/minikube)
* [Gradle](https://gradle.org)
* [Kubernetes](https://kubernetes.io)
* [Docker](https://www.docker.com)
* [Docker private registry](https://docs.docker.com/registry/)
* [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html)
* [Pact](https://docs.pact.io)
* [Pact Broker](https://github.com/DiUS/pact_broker-docker)
* [Open JDK and Java 9](https://hub.docker.com/_/openjdk/)
* [Docker Registry UI](https://github.com/mkuchin/docker-registry-web)
* [H2 Database](http://www.h2database.com/html/main.html)
* [World data source](https://dev.mysql.com/doc/index-other.html)
* [Gradle Docker plugin](https://github.com/bmuschko/gradle-docker-plugin)
* [Dius Pact](https://docs.pact.io)
* [Spring Boot](https://projects.spring.io/spring-boot/)
* [MyBatis](http://www.mybatis.org/mybatis-3/)
* [Fabric8](https://fabric8.io)
* [Guava](https://github.com/google/guava)
* [JUnit](http://junit.org/junit4/)
* [Linux Alpine Containers](https://hub.docker.com/_/alpine/)
* [SemVer](http://semver.org)


### How do I shut down? ###

    Suspend Minikube and the entire cluster
    
    minikube stop

or

    Delete the Kubernetes pact namespaces
    
    kubectl delete namespace pact

The easiest way to start fresh is to remove the Minikube virtual machine from VirtualBox

    
    This will remove the whole Minikube installation

    minikube delete
    
The Minikube installation can always be recreate with the `start.sh` script.
