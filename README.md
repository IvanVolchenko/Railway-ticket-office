# Railway-ticket-office


___This project was created for educational purposes.___

- To see the list of dependencies go [here](./pom.xml).<br>
- Look at `application.properties` [here](./src/main/resources/application.properties).<br>

In this project MySql is used, that's why all properties are done for this
DB. To run this project on your computer you have to change the properties
file and create a schema for your tables.

___SQL___ settings (after the first launch of the application):

1. In order to create default users with administrator click [users](./sql/users.sql)
   (default password for created person - `password`).<br>
2. To create default stations click [stations](./sql/stations.sql).<br>
3. For creation default statements  click [statements](./sql/statements.sql).
4. And for contacts click [contacts](./sql/contacts.sql).

There are three roles in the project: guest, user, and administrator.
Brief description of the roles:
1. Guest:<br>
   The guest can leave a feedback to the administrator,
   enter his personal account, register himself 
   and find trains between stations (with the main
   information about them).


2. User:<br>
   He can do whatever the guest does.
   He can also buy a ticket. 
   He has a personal account with his tickets
   and personal details that he can change.


3. Administrator:<br>
   He can do everything a user does except buy tickets.
   He has a main admin menu where he can:
   - create and delete contacts in his notebook;
   - check the list of users and delete them;
   - create and delete stations;
   - create new routes, search routes and delete them;
   - add, change and delete stops along the route;
   - view the list of sold tickets for each route
and delete them;
   - check statements from people.

The event log was implemented by logback. Look [here](./src/main/resources/logback.xml).<br>
Events log are in `logs` folder.<br>
The applications supports two languages: english and spanish.
There are two files for this purpose [english](./src/main/resources/lang/messages.properties)
and [spanish](./src/main/resources/lang/messages_es.properties).