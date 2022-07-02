import React, { useEffect, useState } from "react";
import taskService from "../services/taskService";
import './Home.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCalendar } from '@fortawesome/free-regular-svg-icons'

function Home() {

  const [displayformstatus, setDisplayformstatus] = useState(false);
  const [taskDescription, setTaskDescription] = useState("");
  const [taskDate, setTaskDate] = useState("");
  const [taskList, setTaskList] = useState([]);
  const [dateInputErr, setDateInputErr] = useState(false);
  const [descInputErr, setDescInputErr] = useState(false);

  const calendarEle = <FontAwesomeIcon icon={faCalendar} />

  useEffect(() => {
    findData();
  }, [])

  const findData = () => {
    taskService.getTaskList().then(
      response => {
        setTaskList(response.data);
        console.log(response.data);
        
      }
    ).catch(e => {
      console.log(e);
    })
  }

  const displayForm = (e) => {
    e.preventDefault();
    setDisplayformstatus(!displayformstatus);
  }

  const selectTask = (e) => {
    //To set if the task is completed or not

    let i = e.target.value;
    taskList[i].taskCompleted = !taskList[i].taskCompleted;
    taskService.changeStatus(taskList[i]).then(
      response=>{
        setTaskList(taskList);
        console.log(response.data);
      }
    ).catch(e=>{
      taskList[i].taskCompleted = !taskList[i].taskCompleted;
      console.log(e);
    })

    
  }

  const getDate = (unixDate) => {
    //To convert unix timestamp to viewable date
    var d = new Date(unixDate);
    var yyyy = d.getFullYear().toString();
    var mm = (d.getMonth() + 1).toString();
    mm = mm.length > 1 ? mm : ("0" + mm);
    var dd = d.getDate().toString();
    dd = dd.length > 1 ? dd : ("0" + dd);
    return yyyy + "-" + mm + "-" + dd;
  }

  const addTask = (e) =>{
    e.preventDefault();
    var d = null;
    setDateInputErr(false);
    setDescInputErr(false);
    
    try{
      obtainValidDate();
      d = Date.parse(taskDate);
    }catch(e){
      setDateInputErr(true);
      if(taskDescription == ""){
        setDescInputErr(true);
      }
      return;
    }

    if(taskDescription == ""){
      setDescInputErr(true);
    }
    if(d != null){
      taskService.addTask(taskDescription, d).then(
        response => {
          //Add to front of list
          taskList.unshift(response.data);
          setTaskList(taskList);
          clearNewTask();
          console.log(response.data);

        }
      ).catch(e =>{
        console.log(e);
      })
    }
  }

  const obtainValidDate = () =>{
    //Validate the date input
    var dateArray = taskDate.split("-");
    if(dateArray.length != 3){
      throw 'Invalid Date Input. Input should be yyyy-mm-dd'
    }
    if(dateArray[0].length != 4 || isNaN(parseInt(dateArray[0])) || parseInt(dateArray[0])<0){
      throw 'Invalid Date Input. Input should be yyyy-mm-dd'
    }
    if(dateArray[1].length != 2 || isNaN(parseInt(dateArray[1])) || parseInt(dateArray[1])<0){
      throw 'Invalid Date Input. Input should be yyyy-mm-dd'
    }
    if(dateArray[2].length != 2 || isNaN(parseInt(dateArray[2])) || parseInt(dateArray[2])<0){
      throw 'Invalid Date Input. Input should be yyyy-mm-dd'
    }

  }

  const clearNewTask = () =>{
    setTaskDescription("");
    setTaskDate("");

  }

  const deleteCompletedTask = (e) => {
    e.preventDefault();
    taskService.deleteCompleted().then(
      response =>{
        console.log(response.data);
        setTaskList([]);
        findData();
        
      }
    ).catch(e =>{
console.log(e);
    })
  }


  return (
    <div className="wrapper">
      <div className="Heading"><h3>Tasker</h3></div>

      <div className="createButtonContainer">
        {displayformstatus ?
          <button className="createButtonNegative" onClick={(e) => displayForm(e)}>-</button>
          :
          <button className="createButton" onClick={(e) => displayForm(e)}>NEW</button>
        }
      </div>
      {displayformstatus ?

        <div className="createContainer">
          <div className="createContainerHead">Add Task</div>
          <form className="formContainer">
            <label htmlFor="description" className="formlabel">
              Description
            </label>
            <input
              type="text"
              className="formtextinput"
              id="description"
              value={taskDescription}
              onChange={(e) => setTaskDescription(e.target.value)}
              name="description"
            />
            {descInputErr ? 
            <p className="errMsg">*Cannot be empty</p>:""}

            <label htmlFor="date" className="formlabel">
              Date
            </label>
            <input
              type="text"
              className="formtextinput"
              id="date"
              value={taskDate}
              onChange={(e) => setTaskDate(e.target.value)}

              name="date"
            />
            {dateInputErr ? 
            <p className="errMsg">*Invalid Date Input. Input should be yyyy-mm-dd</p>:""}
            <div className="buttonContainer">
              <button className="saveButton" onClick={(e)=>addTask(e)}>Save</button>
            </div>

          </form>

        </div> :
        <div></div>


      }


      <br />

      <div className="tasklistcontainer">
        {taskList && taskList.map((task, index) => (
          <div className="tasklist">

            <div className="displaytask">
              <p><span className="displayDesc">{task.taskDescription}</span></p>
              <p> {calendarEle} {getDate(task.taskDate)}</p>
            </div>
            <div className="displaycheckbox">
              <input className="checktask" type="checkbox" name="checkbox" value={index} onChange={(e) => selectTask(e)} defaultChecked={task.taskCompleted} />


            </div>
          </div>


        ))}

        

      </div>
      <br />
        <div className="createButtonContainer">
        <button className="clearButton" onClick={(e) => deleteCompletedTask(e)}>Clear Completed</button>
        
        
      </div>
    </div>
  );
}

export default Home;