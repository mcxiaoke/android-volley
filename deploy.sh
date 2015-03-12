#!/usr/bin/env bash
./gradlew -b deploy.gradle clean assembleRelease uploadArchives
