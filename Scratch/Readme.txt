video: https://www.youtube.com/watch?v=dOUhhYe4wpo
for API collection login to postman

H2 - http://localhost:8080/h2-console/

@RequestPart -  @RequestPart the content of the part is passed through an HttpMessageConverter to resolve the method argument with the 'Content-Type' of the request part in mind. This is analogous to what @RequestBody does to resolve an argument based on the content of a regular request.
               so, we can parse @Requestbody as @RequestPart as "abaghel" and reportData need to be a json file.
HttpServletResponse - response of server


To resolve /h2-console not working - localhost refused to connect:
-------------------------------------------------------------------
By default Spring Security disables rendering within an iframe because allowing a webpage to be added to a frame can be a security issue, for example Clickjacking.
Since H2 console runs within a frame so while Spring security is enabled, frame options has to be disabled explicitly, in order to get the H2 console working.
http.csrf().disable()
    .headers().frameOptions().disable();

For Spring Boot 3+ and Spring Security 6, add following lines into your SecurityFilterChain Bean. The newer versions of Spring security heavily use lambda expressions for configurations. The following lines use method references in Java 8+.
http.csrf(AbstractHttpConfigurer::disable)
    .headers(httpSecurityHeadersConfigurer ->
        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
    );


//DB tables
SELECT * FROM MOVIE;
SELECT * FROM MOVIE_CAST;
SELECT * FROM USERS;
SELECT * FROM FORGOT_PASSWORD;


// for mail services to work follow below steps
1. Enable 2FA in gmail
2. Add App passwords and enter that password in properties file without spaces
3. To send normal mail use JavaMailSender class
4. For sending attachments in mail use MimeMessage class


Note: Mail service is not working in my Office lap