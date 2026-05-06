
![RealWorld example apps cover](./doc/image/realworld-cover.png)
[![Build](https://github.com/raeperd/realworld-springboot-java/actions/workflows/build.yml/badge.svg)](https://github.com/raeperd/realworld-springboot-java/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[ReadWorld.io](https://github.com/gothinkster/realworld) backend project using spring boot java using `spring-security`, `spring-data-jpa`

# Insprired by

- [우아한형제들 기술 불로그 | Todo list 만들기는 이제 그만](https://woowabros.github.io/experience/2020/04/14/stop-making-todo-list.html)
- [우아한형제들 기술 블로그 | Gradle 프로젝트에 JaCoCo 설정하기](https://woowabros.github.io/experience/2020/02/02/jacoco-config-on-gradle-project.html)
- [우아한형제들 기술 블로그 | 우린 Git-flow를 사용하고 있어요](https://woowabros.github.io/experience/2017/10/30/baemin-mobile-git-branch-strategy.html)
- [Github | Realworld.io](https://github.com/gothinkster/realworld)


 # Getting started

 ## Build from scratch
 ``` shell
 $ ./gradlew build bootRun
 ```

 ## Using docker
 ``` shell
 $ docker run --rm -p 8080:8080 ghcr.io/raeperd/realworld-spring-boot-java:main
 ```

- Dockerhub registry is [here](https://hub.docker.com/repository/docker/raeperd/realworld-spring-boot-java)  
- Container tags are simply branch name of this repository following git-flow strategy



## How to test 

After run application, you can try one of followings

### using shell script

``` shell
$ ./doc/run-api-tests.sh
```

### using postman 

Import [`./doc/Conduit.postman_collection.json`](./doc/Conduit.postman_collection.json) in your postman application 



And also, pure `gradle test` covers almost every line of code.

More details can be found in [`./doc/README.md`](./doc/README.md) and  [original source](https://github.com/gothinkster/realworld/tree/master/spec)

# Overview

## Design Principal

- Always `final` whenever possible
- Always package private class whenever possible
- **Always test every package, class, method, instruction in codes**
  - Except for some boilerplate `equals` and `hashcode` method
  - This is validated by [jacoco-gradle-plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html).
  - Coverage verification in [`./test.gradle`](./test.gradle)
- Try to avoid including additional dependencies as much as possible
  - Implements JWT generation / validation logic without 3rd party library [#3](https://github.com/raeperd/realworld-springboot-java/issues/3)
- Try to maintain codes in domain package remain POJO
  - Except for special spring annotations like `@Service`, `@Repository`, `@Transactional`
  - Prohibit use of lombok in domain package
- Try to follow all modern best practices for spring-boot project
  
## Diagrams 

- You can open full diagram file in [`realworld.drawio`](./realworld.drawio) using [draw.io](https://app.diagrams.net/)

### User

![realworld-User](./doc/image/realworld-User.png)

- Separate password encoding logic out of User.
- User must be created with password encoder.

### Article

![realworld-Article](./doc/image/realworld-Article.png)

- Article contains other elements with `@Embedded` classes
- Try to reduce number of repositories.
- Prefer `@JoinTable` to `@JoinColumn`

### JWT 

![realworld-Jwt](./doc/image/realworld-Jwt.png)

- Try not to use 3rd party library
- Serialization and Deserialization are seperated with interfaces
- Domain package contains interface, infrastructure code provide implementation  
- Application package do stuff with spring-security logic

## Performance

Result of [`./doc/run-api-tests.sh`](./doc/run-api-tests.sh), run 100 times for each version.

| Version | Runs | Requests | Failures | Avg response time | P50 | P90 | P95 | P99 | Max | Avg full run duration |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| Before Spring Boot 4 upgrade (`d9c19f1`) | 100 | 3100 | 0 | 21.64ms | 6.0ms | 113ms | 123ms | 137ms | 188ms | 16.848s |
| After Spring Boot 4 upgrade (`18432fa`) | 100 | 3100 | 0 | 21.10ms | 6.0ms | 115ms | 119ms | 131ms | 233ms | 16.866s |

Current version is not meaningfully slower overall. Average and tail latency are slightly better, with one higher max outlier.

# What can be done more

- User class doing so many things now. It can be improved someway.
- Service classes can be divided into smaller services
- Test cases order can be improved

# Contact

You can contact me with [email](raeperd117@gmail.com) or issue in this project

# License
[MIT License](./LICENSE)

# Referenced

- [JSON Web Token Introduction - jwt.io](https://jwt.io/introduction)  
- [Symmetric vs Asymmetric JWTs. What is JWT? | by Swayam Raina | Noteworthy - The Journal Blog](https://blog.usejournal.com/symmetric-vs-asymmetric-jwts-bd5d1a9567f6)
- [presentations/auth.md at master · alex996/presentations · GitHub](https://github.com/alex996/presentations/blob/master/auth.md)

