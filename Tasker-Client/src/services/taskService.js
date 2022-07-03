import axios from "axios";
const env_URL = process.env.REACT_APP_APIURL
 //const REQUEST_URL = "http://localhost:8080/api/taskmanager";
const REQUEST_URL = (env_URL==null) ? "http://152.67.99.60:8085/api/taskmanager": env_URL + "/api/taskmanager";
class taskService{
    getTaskList(){
      return axios.get(REQUEST_URL+"/list");
    }
    addTask(taskDescription, taskDate){
      return axios.post(REQUEST_URL+"/save", JSON.stringify({taskDescription:taskDescription,taskDate:taskDate}),{
        headers: {
          // Overwrite Axios's automatically set Content-Type
          'Content-Type': 'application/json'
        }})
    }
    changeStatus(task){
      return axios.post(REQUEST_URL+"/save",task,{
        headers: {
          // Overwrite Axios's automatically set Content-Type
          'Content-Type': 'application/json'
        }})
    }
    deleteCompleted(){
      return axios.delete(REQUEST_URL+"/clear", {
        headers: {
          // Overwrite Axios's automatically set Content-Type
          'Content-Type': 'application/json'
        }});
    }
}
export default new taskService;