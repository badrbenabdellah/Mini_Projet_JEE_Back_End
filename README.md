# mini_projet_jee_back_end

## How to Use
first create a new employee using the api endpoint `/api/employee` with the following body using a POST request with Postman or any other API testing tool:
```json
{
  "name": "John Doe",
  "email": "john@gmail.com",
  "phone": "1234567890",
  "address": "1234 Main St",
  "role": "Software Engineer",
  "password": "password"
}
```
you can use parameters like "employe_sup_id" to add a supervisor to the employee, "groupe_id" to add the employee to a group.

And when you start the frontend you can log in with the email and password you used to create the employee.
