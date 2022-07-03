# TaskerApplication
 Application to manage task

## Overview
This is a simple task application with the following functions:
1. User can see a list of tasks that they have (sorted by date - earliest at top)
2. User can add a new task to the list
3. User can tick off the task that they have completed
4. User can remove all the task they have completed

## Folders:
1. Tasker (API endpoints with java script - dropwizard.io)
2. Tasker-Client (Reactjs UI)
3. TaskerAPITest-mocha-awesome (API endpoint test for Tasker)
4. TaskerUI-testng (UI testing for Tasker-Client)

## API Endpoint
### Platform
- Developed using dropwizard.io framework with maven
- Initial maven setup : 
    mvn archetype:generate -DarchetypeGroupId=io.dropwizard.archetypes -DarchetypeArtifactId=java-simple -DarchetypeVersion=2.0.0
- Database:
    H2 
    Uses hibernate for data persistance
    Create table upon startup
### Dependencies
- dropwizard-dependencies
- dropwizard-core
- dropwizard-db
- dropwizard-hibernate
- h2
- mysql-connector-java (alternative database not in use)
### Entities and DAO
- Task
    - id: UUID (PK)
    - taskDescription: String
    - taskDate: Date
    - taskCompleted: boolean (identify if the task has been completed)
- TaskDAO (extends AbstractDAO - io.dropwizard.hibernate.AbstractDAO)
    - findById() : Task                returns a Task given an id
    - findAll()  : List<Task>          returns a List<Task> given an id
    - save(Task) : UUID                save the Task and returns the id
    - deleteCompletedTask() : void     deletes the Task that have been completed
### Resource
- TaskManagerResource Path("/api/taskmanager")
    - Has a constructor with a TaskDAO argument
    - Endpoints:
        - GET  - Path("/list")  : getTaskList() 
            - gets all of the tasks as a list.
            - sort the task by date       
            - returns all of the task as a json file (in an array)
        - POST - Path("/save")  : saveTask(Task task)
            - receive a Task as an argument
            - checks if the task has id.
            - If no, save it as a new task
            - If yes, modify the completed status
        - GET  - Path("/get/{id}") : getTaskById(String id)
            - receive a path variable as id
            - finds if the id matches a Task 
            - If not found, throw error
            - If found, return Task as a json
        - DELETE - Path("/clear") : clearCompletedTask()
            - Obtain a list of completed task
            - Delete the completed task
            - Return status

## UI WITH REACT
### Components
- Home
    - Page for the user to manage tasks
    - Uses Home.css for styling
    - Obtain a list of task upon landing on page : findData
    - Displays the form for new task by clicking on button triggering : displayForm
    - Saves the new task by clicking on save button triggering : addTask
        - Description field is validated to be not null
        - Date field is validated to be in the form yyyy-mm-dd
    - Task can be checked (meaning completed) or unchecked (meaning not completed) : selectTask
    - Completed task can be deleted triggering : deleteCompletedTask
### Services
- taskService
    - Uses axios as a means of interacting with API
    - Methods:
        - GET: getTaskList()                        : Call the API with path "/api/taskmanager/list"
        - POST: addTask(taskDescription, taskDate)  : Call the API with path "/api/taskmanager/save" with json file (taskDescription, taskDate)
        - POST: changeStatus(task)                  : Call the API with path "/api/taskmanager/save" with json file (for task)
        - DELETE: deleteCompleted()                 : Call the API with path "/api/taskmanager/clear"
### Packages
- "axios": "^0.21.1"
- "@fortawesome/free-regular-svg-icons": "^6.1.1"
- "@fortawesome/react-fontawesome": "^0.2.0"
- "react": "^18.2.0",

## Containerising CRUD service & UI React (with docker)
- Navigate to the root folder of the respective component
    - Tasker (CRUD service)
    - Tasker-Client (React UI)
- Setup the files
    - In order to run the files locally, modify the api path of the taskService
    - Alternatively, specify the url of the api using the environmental variable REACT_API_URL in the Dockerfile
    - e.g. web:
            environment:
                - REACT_API_URL="http://localhost:8080"
- Build the docker images
    - Use: docker build -t <repo/file>:<tag>
        - e.g. Use: docker build -t leebaojin/tasker:v1.0           (in Tasker)
        - e.g. Use: docker build -t leebaojin/taskerclient:v1.0     (in Tasker-Client)
- Run docker images
    - Use: docker run -d -p=<mapped_port>:<port_used> --name <name_given> <repository>
        - e.g. docker run -d -p=3000:3000 --name taskerclient leebaojin/taskerclient:v1.0
- Push to docker hub
    - Use: docker push <repository>
        - e.g. docker push leebaojin/taskerclient:v1.0

## Running Tests
### Testing the API
- Test case written using javascript
- Use mocha-awesome and chai for the test
- To install mocha-awesome and chai
    - Navigate to the folder
    - Install mocha-awesome
        - npm install
        - npm install --save-dev mochawesome
    - Install chai
        - npm install chai-http
- Running the test
    - Run the test
        - check that the package.json file (should have test:awesome under scripts)
        - place the test script in the "test" folder
        - npm run test:awesome
    - Obtain result
        - open the "mochawesome-report" folder
        - open the mochawesome.html
- Test Sequence:
    1. Check server is present
    2. Attempt to save a new task ("/api/taskmanager/save")
    3. Attempt to get the saved task ("/api/taskmanager/get/{id}")
    4. Repeat task 2 and 3
    5. Get a list of task ("/api/taskmanager/list")
    6. Modify task 1 to completed and save ("/api/taskmanager/save")
    7. Delete completed task ("/api/taskmanager/clear")
    8. Verify task has been deleted ("/api/taskmanager/get/{id}")
    9. Change task 2 status and delete
- Test details can be found: https://docs.google.com/spreadsheets/d/1VRKMAjyNwBoEFTmCUQ-iuRqF9w4R5S0r/edit?usp=sharing&ouid=111269330940438351616&rtpof=true&sd=true

### Testing the UI
- Test written in Java using selenium and testNG
- Dependencies
    - selenium-java
    - testng
    - monte-screen-recorder
- Others
    - uses MyScreenRecorder (class file)from: https://github.com/naveenanimation20/ScreenRecorder
- Running the test
    - Open command prompt and navigate to the folder TaskerUI-testng
    - To run the test to the webserver hosting the page
        - Use : mvn test
    - Alternatively, if an alternate url to be used
        - Use : mvn test "-DUI_URL=(your url)"
        - e.g. : mvn test "-DUI_URL=http://localhost:8080/"
    - Obtain the result
        - open target/surefire-reports/Surefire suite
        - There is a report: Surefire test.html
    - Obtain video
        - open recordings
        - There is a .avi video file present with the video of the test
    - Link to a test: https://drive.google.com/file/d/1jtJW33NxN0P1qdXtBykaqabwsnO8uFo9/view?usp=sharing
- Test Sequence:
    1. Test that the correct site has been navigated to
    2. Click on New button to display the new task form
        - Verify that the form is displayed
    3. Enter invalid date on new task form (with description blank). Click save
        - Verify that an error is displayed
    4. Enter the valid date and a description. Click save
    5. Verify that the new task appear at the top of task list
    6. Click on the "-" button to close the new task form
        - Verify that the form is hidden
    7. Click on checkbox of the newly created task
        - Verify that it is checked
    8. Click on the "clear completed" button below
        - Verify that the checked task has been removed
- Test details can be found: https://docs.google.com/spreadsheets/d/1VRKMAjyNwBoEFTmCUQ-iuRqF9w4R5S0r/edit?usp=sharing&ouid=111269330940438351616&rtpof=true&sd=true