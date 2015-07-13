Android Volley  
----------
This is an unofficial mirror for [android volley library](https://android.googlesource.com/platform/frameworks/volley), the source code will synchronize periodically with the official volley repository.


## Status

Volley is already published to Maven Central.  

* [![Maven Central](http://img.shields.io/badge/2015.07.13-com.mcxiaoke.volley:library:1.0.17-brightgreen.svg)](http://search.maven.org/#artifactdetails%7Ccom.mcxiaoke.volley%7Clibrary%7C1.0.17%7Cjar) 


## Usage

### for Maven

```
<dependency>
    <groupId>com.mcxiaoke.volley</groupId>
    <artifactId>library</artifactId>
    <version>{latest-version}</version>
</dependency>
```


### for Gradle

```
compile 'com.mcxiaoke.volley:library:1.0.17'
```


## History

* **1.0.17 2015.07.13**
    * merge upstream, stop logging slow requests outside of debug logs
    * merge pr #64, fix leaking the last request object
    
* **1.0.16 2015.05.18**
    * merge pr, fix NegativeArraySizeException
    * merge upstream, Use a BufferedOutputStream to read and write cache
    
* **1.0.15 2015.03.18**
    * add two missing constructors for JsonArrayRequest and JsonObjectRequest
    * add unique identifier for Request

* **1.0.14 2015.03.17**
    * add more constructors for JsonArrayRequest and JsonObjectRequest
    * update and fix gradle build, using new build.gradle
    
* **1.0.13 2015.03.16**
    * merge pr, added constructor to JSONArrayRequest 
    
* **1.0.12 2015.03.12**
    * merge upstream, fix ImageLoader.getCacheKey() 
    * merge upstream, Fix broken DiskBasedCache 
    * merge upstream, Modify header parser to handle must-revalidate.
    
* **1.0.11 2015.03.03**
    * merge upstream, Add a RequestFinishedListener to RequestQueue. 
    * merge upstream, Change the default character encoding for JSON responses to UTF-8    

* **1.0.10 2014.12.30**
    * merge upstream, Use the view size and scale type to restrict NIV requests. 
    * merge pr, Add a getImageURL method to NetworkImageView 
    * merge pr, Add the options of setting DiskBasedCache sizes in Volley.java 
    
* **1.0.9  2014.11.26**
    * merge upstream, Fix deprecation warnings (now errors in 0.14.4) w/ gradle.
    
* **1.0.8  2014.11.07**
    * merge upstream, Metric for network time and getBackoffMultiplier()    

    
* **1.0.7  2014.10.13**
    * merge upstream, Add locale to HttpHeaderParserTest.rfc1123Date(long millis)
    * merge upstream, Copy cache header for 304 response
    
* **1.0.6  2014.09.10**
    * merge pr, fixed bug where Disk cache misses for redirects 
    * merge upstream, Fix crash/OOM in DiskBasedCache
    * merge upstream, allow use of custom ImageRequests in ImageLoader
    
* **1.0.5  2014.06.18**
    * merge upstream, Fixes bug in PATCH method. Sets the method before setting the body.
    * update gradle and build tools version, fix build
    
* **1.0.4  2014.05.04**
    * merge upstream, Guard against NullPointerException currently occurring in Volley when a Request is given a url whose host is null.
    
* **1.0.3  2014.04.01** 
    * merge upstream, ImageLoader: setError after null check 
    
* **1.0.2  2014.02.28** 
    * merge upstream, Use the view size to restrict NIV requests. 
    * merge upstream, Fix generic type parameter for RequestQueue.add(Request) 
    * merge pr, added support for handling 301 and 302 http redirects
    * using standalone deploy gradle build file and script

* **1.0.1  2014.02.13** 
    * add gradle build support, add readme, release using gradle

* **1.0.0  2014.01.03** 
    * create volley mirror at github, release first version


## Attention  

This project is just a mirror of volley, if you have found any bugs or need some features, please create an issue at [AOSP Issue Tracker](https://code.google.com/p/android/issues/list).


## License


    Copyright (C) 2014 Xiaoke Zhang
    Copyright (C) 2011 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

