What are you need to do to run this code:

DATABASE:

    Execute below SQL script
    ```
        CREATE SCHEMA keycloak;
        CREATE SCHEMA "user";
    ```

KEYCLOAK:

    1. Open browser and go to http://localhost:9080/admin/master/console/
    2. Log in with the admin credentials
    3. Create a new realm called `Saga-app`
    4. Create a new client called `microservice-saga-app` with the following settings:
        - Client ID: microservice-saga-app
        - Root URL: http://localhost:8080
        - Valid Redirect URIs: http://localhost:8080/*
        - Web Origins: http://localhost:8080
        - Client Authentication: enabled 
        - Authorization: false
        - Direct Access Grants: enabled 
    5. Fetch the client secret from the `Credentials` tab and set it in the VM options of the user-service application:
        - `-Dkeycloak.credentials.secret=your-client-secret`
    6. Set roles for the microservice-saga-app clients:
        - Go to the `Roles` tab and create a role `ROLE_USER`, `ROLE_FINANCIAL_MANAGER`, `ROLE_INVENTORY_MANAGER`, `ROLE_ORDER_MANAGER`, `ROLE_SENIOR_MANAGER`
    7. Create groups for the microservice-saga-app clients:
        - Go to the `Groups` tab and create a group `Managers`
        - Create a subgroup `Financial Managers` under `Managers`
        - Create a subgroup `Inventory Managers` under `Managers`
        - Create a subgroup `Order Managers` under `Managers`
    8. Assign roles to the subgroups:
        - Go to the `Groups` tab and select the `Financial Managers` group
        - Go to the `Role Mappings` tab and assign the `ROLE_FINANCIAL_MANAGER` role
        - Go to the `Groups` tab and select the `Inventory Managers` group
        - Go to the `Role Mappings` tab and assign the `ROLE_INVENTORY_MANAGER` role
        - Go to the `Groups` tab and select the `Order Managers` group
        - Go to the `Role Mappings` tab and assign the `ROLE_ORDER_MANAGER` role
    9. Create users and assign them to the appropriate groups
        