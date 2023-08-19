# danspro
Development Test PT Dans Multi Pro

How To Install :
- Create database on postgresql 9.5 or higher with name danspro
- Change username / password auth for postgres in application.properties
- Run Project
- Open DB and Create user for auth JWT in table s_account (example username : ratwareid , password: 12345)

Example API Request :
1. Login
<pre>
curl --location --request POST 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "ratwareid",
    "password" : "12345"
}'
</pre>

2. Job List
- change the token as received at login 
<pre>
  curl --location --request GET 'localhost:8080/job/list' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXR3YXJlaWQiLCJleHAiOjE2OTI0Mzc4OTcsImlhdCI6MTY5MjQyMzQ5N30.befqs55mtbPnOopW6zCsjEBmtzO8ChDNGN3-sBzs08ZdZIE40I_s6YNR8BRNK90NuCqne8aL7KzS_l_3BiBSMg'
</pre>

3. Job Detail
<pre>
  curl --location --request GET 'localhost:8080/job/detail/32bf67e5-4971-47ce-985c-44b6b3860cdb' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYXR3YXJlaWQiLCJleHAiOjE2OTI0Mzc4OTcsImlhdCI6MTY5MjQyMzQ5N30.befqs55mtbPnOopW6zCsjEBmtzO8ChDNGN3-sBzs08ZdZIE40I_s6YNR8BRNK90NuCqne8aL7KzS_l_3BiBSMg'
</pre>

Any Question of the following code / setup , please email me at : jerryerlangga82@gmail.com
Best Regards,

Jerry Erlangga
