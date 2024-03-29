# :rocket::rocket: PWr-API :rocket::rocket:
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/bd8c106b8f4943e28702cbc54ce776b5)](https://www.codacy.com/gh/komp15/PWr-API/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=komp15/PWr-API&amp;utm_campaign=Badge_Grade) 
[![Development deployment - pwr-api-dev](https://github.com/komp15/PWr-API/actions/workflows/main_pwr-api-dev.yml/badge.svg)](https://github.com/komp15/PWr-API/actions/workflows/main_pwr-api-dev.yml)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)
[![Status page](https://img.shields.io/badge/StatusPage-UP-<COLOR>.svg)](https://pwr-api-status.wmsdev.pl/)


Unofficial PWr's api that integrates polwro, JSOS, edukacja.cl, iparking, eportal and prowadzacy, providing access to all internal university systems at your fingertips.

If you want to become an API-developer, please join our team WMS_DEV on jira (ask @komp15, or @Woojciech for support) or read the code, documentation (https://pwr-api-dev.azurewebsites.net/swagger-ui/index.html), and you can test and use current API development build at https://pwr-api-dev.azurewebsites.net/api, or use latest stable production verion at http://pwr-api-prod.wmsdev.pl. 

API is hosted DigitalOcean's Droplet and can be not production ready in terms of load. Please consider it when developing applications using it.

If you want to achieve faster responses you can use our code directly in your project, or start up the latest build on your server.

Feel free to open any issues.

Waiting to see your contribution,
WMS_DEV

# Useful urls
* [prod-documentation](http://pwr-api-prod.wmsdev.pl/swagger-ui/index.html)
* [prod-baseurl](http://pwr-api-prod.wmsdev.pl)
* [dev-documentation](https://pwr-api-dev.azurewebsites.net/swagger-ui/index.html)
* [dev-baseurl](https://pwr-api-dev.azurewebsites.net)
* [sandbox](http://sandbox.pwrapi.wmsdev.pl/)
* [sanbox-repo](https://github.com/komp15/pwr-api-sandbox)
* [status page](https://pwr-api-status.wmsdev.pl/)

# PWr-API sandbox
We have developed a simple, react based web application that allows you to understand purpose of our project a little bit better, it is available [here](http://sandbox.pwrapi.wmsdev.pl/) (polish language), and its source code is available [here](https://github.com/komp15/pwr-api-sandbox)

# Production usage
In order to use the API in real life production applications, we strongly reccomend to setup your own enviroment with the latest release of our API. Please remember, that the API is public, and everybody can use it as they want. We provide full support during setup in your enviroment.

# General purpose
As students of Wroclaw's University of Science and technology, we had a lot of ideas for software, that could make university life easier and more modern. The biggest problem with developing it, was going through all the authentication and web-scraping of university systems. We wanted to enable young developers to access internal systems with ease and make developing software for our university much faster. 

For now, our API consists of around 40 endpoints, which allow easy access to teacher's lesson plans, student's own marks, lessons and so on. We plan to constantly move on with it, and keep it hosted as long as possible.

If you are a university professor and want to know more about usage, implementation details, or have any questions, please reach out to our internal email: 266553@student.pwr.edu.pl

![289902855_806560397416770_8047910213326961316_n](https://user-images.githubusercontent.com/77535280/179359955-2ad91dce-2e03-4b69-82e9-8e5bd9b07835.png)
![290303916_1171606993405086_9166082031093296942_n](https://user-images.githubusercontent.com/77535280/179359995-bdfb2a39-fec7-4d6a-b23c-4389554ed03f.png)


# API Documentation

Each endpoint is documented using swagger. From there, you can also try it out by yourself and send a couple of requests. Below you can find a bit of implementation details connected with each service we are providing.


# Edukacja API
edukacja.cl system is considered to be a legacy one. It's only purpose is to serve as an enrollment system. We provide two endpoints for it.

The first one (/api/edukacja) is kind of heath-endpoint where you can test if login and password are valid for edukacja.cl usage.

The second one, enables you to download courses that student can enroll into. Please be aware, that this feature is only working, when there are any open enrollments in the system.

Its implementation is based on Selenium framework. Due to dynamic loading of content, we weren't able to use pure http request to get access to the system. This is the reason, why this module can be much slower than the rest.

# Eportal API
EPortal API enables you to get basic access to PWr's ePortal system. Generally speaking, we are aware of official Moodle API, but to be honest we didn't write to any university official to get access to it. We still wanted to create an interface that is accessible to everyone. 

Implementation is based on reverse engineering of API and HTTP requests (OkHttp3 client was used). Generally speaking, the section is pretty basic, but allows developers to get user's calendar, courses or marks. Use case for this API could be, for example, an app, that notifies you if new mark is added in course.

# Forum API
Forum API allows you to access pol-wro teacher reviews without the need of having an account. You can also fetch the best teachers in categories, or the worst ones. Implementation is based on a Scrapper, that runs each week/month on a remote server and updates our database.

All the reviews are stored in our database, and can be accessed using an API. In this module we used Spring's JDBCTemplates.

# JSOS API
This API can get you access to central university system. You can get all information about a given student, get his marks, courses, and even lesson plan for given week. Implementation is based on HTTP requests, where authentication is handled with our custom logic for simulating OAuth.

# Parking API
This simple and very useful module allows you to get a current state of PWr's public parkings. Its implementation is based on reverse engineering a website. The first endpoint (/api/parking) returns response from API mapped for our custom and simple objects. The raw endpoint on the other hand, returns parkings with array of historic status of available places. This changed after PWr's migration to skd service. Please be aware, that trend field will always be zero, because of that migration. In the second version of the API this field will be removed.

# Prowadzacy API
Prowadzacy API is an API for prowadzacy.pwr.edu.pl, this mostly unknown site, allows you to get plan of teacher, get plan for given room, or even get all lessons in a course. It has a couple of limitations when speaking of querying because of website design (we tried to describe it best as we can in swagger docs), but still can be used in very interesting ways.

# News API
News API is a feature introduced in PWr-API 1.1.0. The module is able to scrape news from main Politechnika's website and all of the faculties websites. Its implementation is based on scraping RSS feeds, and usual webscraping, if no RSS is supported. 

# Contributors
Thanks to our amazing backend team at [@WMS-DEV](https://github.com/WMS-DEV) developing PWr-API, especially:
[@Woojciech](https://github.com/Woojciech),
[@MDybek](https://github.com/MDybek),
[@lukcyn](https://github.com/lukcyn),
[@Gawron97](https://github.com/Gawron97),
[@MIK0o0](https://github.com/MIK0o0)

# Conclusion and license
Thank you very much for showing your interest in our project. If you want, you can leave a star on it, or just simply use it and share your projects with us! We are waiting for your input. Feel free to open issues, pull requests, and use it as you want.

Speaking of license, just do what you want with it, but don't harm anyone ;) 


