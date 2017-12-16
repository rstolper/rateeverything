rateeverything
==============

Rate Everything application

Sometimes up at: http://web.rateeverythingapp.com/
That host shuts the application down after 24hr of inactivity, so if you get a timeout error try reloading again, the timeout may be due to OpenShift bringing the server back up.
There is currently no authentication, pick a username and stick with it. 

Dev notes:

When you download a pem ec2 ssh key, need to chmod 600 on it.

Steps to create AMI off the default AWS Linux:

Install java 8:

sudo yum install -y java-1.8.0-openjdk.x86_64
sudo /usr/sbin/alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java
sudo /usr/sbin/alternatives --set javac /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/javac

Install docker:

sudo yum update -y
sudo yum install -y docker
sudo service docker start

To ssh into the instance, must provide PEM and username ec2_user:
ssh -i ~/dev/rateeverything_kp.pem ec2-user@IPADDRESS

Build steps:

mvn compile the entire project
docker build -t romanstolper/rateeverything:1.0 .
docker push romanstolper/rateeverything:1.0 (requires docker login)