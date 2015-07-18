#!/bin/bash
# This is how you execute it
#

java -Djavax.net.ssl.trustStore=./src/main/resources/cert.postcrossing.com.jks  -jar ./target/postcrossing-jsoup-1.0-SNAPSHOT-jar-with-dependencies.jar 

