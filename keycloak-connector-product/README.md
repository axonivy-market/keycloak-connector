# Keycloak Connector
The Keycloak Connector extends the functionality of the existing Keycloak integration in Axon Ivy.

It enables use cases such as the approval of users who are initially created in Axon Ivy and then stored in Keycloak. The initial user registration takes place in Axon Ivy, and corresponding user accounts are automatically created in Keycloak.

The product also supports interaction with Keycloak for use cases such as role assignments and session management.

Additionally, the Keycloak login theme can be customized and modified to ensure a consistent and branded user experience.

## Demo

### User register and approval
1. User Registration: Applicants complete the required fields and submit their application for reviewing.
![registration-form-view](images/registration-form.png)

2. Task Creation for Administrative Review: After applicant submission, a review task is automatically generated and assigned to users with the Admin Keycloak role for further evaluation.
![review-task](images/review-task.png)

3. Administrative Approval Process: An administrator reviews application details and decides whether to approve or reject the request. If approved, the admin can assign `user role` to define the user's access permission in Keycloak.
![role-assignment](images/role-assignment.png)

4. User Account Provisioning: For approved applications, a new user account is created in Keycloak. A confirmation email is sent to the user, including a temporary password and instructions for their first login.
![confirmation-mail](images/confirmation-mail.png)

### User management
Administrators can:
- View the list of registered users, access detailed user information
![manage-user](images/manage-user.png)
- Perform key actions such as reset, lock or delete user
![manage-action](images/manage-actions.png)

For advanced or full user management, administrators can use the **Keycloak Admin Console**.
![manage-action](images/user-list.png)

> [!IMPORTANT]
> In a server environment, users can choose option "Log in with Keycloak". After choosing to register, they will be redirected from the login page to a secure external registration form.

## Setup
If you don't have access to an existing Keycloak instance, you can quickly spin up a new one using Docker. Below is a sample Docker Compose configuration you can use:

```
@docker-compose.yaml@
```

This setup is intended for demonstration and testing purposes. To get started, make sure to provide the admin password config. Then, launch the container with the command `docker-compose up -d`. You can find the file at the following path: `keycloak-connector-demo/docker/docker-compose.yaml`

```
@variables.yaml@
```

### Create role groups for admin approval process
To grant access to approved users, you need to define roles in your Keycloak realm.
![create-group-name](images/create-role-group.png)


### Override the default "Register" link on the login form
![config-registration-URL](images/config-registration-url.png)

You can personalize the Keycloak registration page, particularly the registration link, by modifying an existing theme `keycloak.v2`. This custom form collects necessary information to better support administrative approval and custom workflows. Follow the steps below:

1. Start `Configuration Management` process with role of Keycloak admin.

2. Upload the Theme JAR: Upload a Keycloak theme JAR file to load the available themes and their configurations. The theme JAR file is typically located in the Keycloak container at *opt/keycloak/lib/lib/main* with name starts with *org.keycloak.keycloak-themes*. For testing, you can use the provided sample theme JAR:
![custom-theme](doc/org.keycloak.keycloak-themes-26.1.5.jar)

3. Select the Target Theme: Choose the theme you want to customize from the loaded list. This will serve as the base or reference for your new theme. The available themes shown are extracted from the uploaded JAR file.

4. Update the Registration URL: Enter the desired registration URL to which users should be redirected when they click the "Register" link on the login page. This override allows you to integrate with a custom registration system or external page.
![config-page-view](images/config-management.png)

5. Generate the Custom Theme: Click the `Generate Login Theme` button to create a new theme based on your selected reference theme, with the updated logic to redirect users to the custom registration URL.

6. Apply customized theme for login page: Extract & copy `custom-theme` folder that we downloaded to the Keycloak container's theme directory *opt/keycloak/themes*. Then, configure your realm to use this new theme for the login page.
![theme-selection](images/theme-selection.png)
