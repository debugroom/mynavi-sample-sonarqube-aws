#!/usr/bin/env bash

set -e

if [ "${1:0:1}" != '-' ]; then
  exec "$@"
fi

# Parse Docker env vars to customize SonarQube
#
# e.g. Setting the env var sonar.jdbc.username=foo
#
# will cause SonarQube to be invoked with -Dsonar.jdbc.username=foo

export SONARQUBE_JDBC_URL="jdbc:postgresql://$SONARQUBE_RDS_ENDPOINT/$SONARQUBE_RDS_USERNAME"

declare -a sq_opts

while IFS='=' read -r envvar_key envvar_value
do
    if [[ "$envvar_key" =~ sonar.* ]]; then
        sq_opts+=("-D${envvar_key}=${envvar_value}")
    fi
done < <(env)

exec java -jar lib/sonar-application-$SONAR_VERSION.jar \
  -Dsonar.log.console=true \
  -Dsonar.jdbc.username="$SONARQUBE_RDS_USERNAME" \
  -Dsonar.jdbc.password="$SONARQUBE_RDS_PASSWORD" \
  -Dsonar.jdbc.url="$SONARQUBE_JDBC_URL" \
  -Dsonar.web.javaAdditionalOpts="$SONARQUBE_WEB_JVM_OPTS -Djava.security.egd=file:/dev/./urandom" \
  "${sq_opts[@]}" \
  "$@"