import axios from "axios";

const REQUEST_URL = "http://localhost:8080/api/taskmanager";
class fiboService{
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

    getFiboList(element){
        return axios.post(REQUEST_URL + "/fibonacci", JSON.stringify({elements:element}), {
            headers: {
              // Overwrite Axios's automatically set Content-Type
              'Content-Type': 'application/json'
            }});
    }
}
export default new fiboService;