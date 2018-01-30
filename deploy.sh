#!/bin/bash
set -e
mvn clean install
mvn assembly:assembly
scp target/otp-auth-1.0-SNAPSHOT-jar-with-dependencies.jar glassfish@snurran.sics.se:/var/www/hops/otp-auth-2.0.jar
