rateeverything
==============

# Rate Everything application

Should be up at: https://www.rateeverythingapp.com/

This app lets you rate anything (as Yes, No, or Maybe)!
Group items into categories for organization purposes.
You can also add notes to explain the rating.

# Build:

```
mvn compile the entire project
docker build -t romanstolper/rateeverything:1.0 .
docker push romanstolper/rateeverything:1.0 (requires docker login)
```

## Run:

`docker run -d -p 8080:8080 romanstolper/rateeverything:1.0`

# EC2 notes:

When you download a pem ec2 ssh key, need to chmod 600 on it.

To ssh into the ec2 instance, must provide PEM and username ec2_user:
`ssh -i ~/dev/rateeverything_kp.pem ec2-user@IPADDRESS`

# Steps to set up EC2 AWS Linux host for running the app:

## Install java 8:

```
sudo yum install -y java-1.8.0-openjdk.x86_64
sudo /usr/sbin/alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java
sudo /usr/sbin/alternatives --set javac /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/javac
```

## Install docker:

```
sudo yum update -y
sudo yum install -y docker
sudo service docker start
```

## Install apache:

```
sudo yum update -y
sudo yum install -y httpd24
sudo service httpd start
sudo chkconfig httpd on
```

## Install certbot:

```
sudo yum-config-manager --enable epel
wget https://dl.eff.org/certbot-auto
chmod a+x certbot-auto
sudo ./certbot-auto --debug
```

Full instructions here: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/SSL-on-an-instance.html

## Configure apache to proxy requests on 443 to 8080:

Open `/etc/httpd/conf.d/ssl.conf`
Add these lines:
```
ProxyPreserveHost On
ProxyPass / http://0.0.0.0:8080/
ProxyPassReverse / http://0.0.0.0:8080/
```

Restart apache: `sudo service httpd restart`