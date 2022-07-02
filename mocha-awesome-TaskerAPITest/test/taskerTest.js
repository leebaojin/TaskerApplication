const chai = require("chai");
const chaiHttp = require("chai-http");
const expect = chai.expect;
//const baseUrl = "http://localhost:8080/api/taskmanager";
const baseUrl = "http://152.67.99.60:8085/api/taskmanager";

chai.use(chaiHttp);
describe("Tasker Test", function(){
    var createId;
    const des ='To Complete Testing Assignment';
    const tdate = 1659571200;

    var createId2;
    const des2 ='To Complete the Challenge';
    const tdate2 = 1659916800;

    it('Check Server is live', function(done){
        //Simple check of the server
        chai.request(baseUrl)
        .get('/list')
        .end(function (err, res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            done();
        })
    })
    it('Create a new a new task: POST - /save', function(done){
        //Check if can save new task
        chai.request(baseUrl)
        .post('/save')
        .send({
            taskDescription: des,
	        taskDate: tdate
        })
        .end(function (err, res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("id");
            createId = res.body.id;
            done();
        })
    })
    it('Find the created task: GET - /get/{id}', function(done){
        // Check if can get task by id
        chai.request(baseUrl)
        .get('/get/'+createId)
        .end(function(err,res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("id");
            expect(res.body.taskDescription).to.equal(des);
            expect(res.body.taskDate).to.equal(tdate);
            expect(res.body.taskCompleted).to.equal(false);
            done();
        })
    })
    it('Create a 2nd task: POST - /save', function(done){
        //Check if can save new task (2nd task)
        chai.request(baseUrl)
        .post('/save')
        .send({
            taskDescription: des2,
	        taskDate: tdate2
        })
        .end(function (err, res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("id");
            createId2 = res.body.id;
            done();
        })
    })
    it('Get list of task: GET - /list', function(done){
        chai.request(baseUrl)
        .get('/list')
        .end(function(err,res){
            expect(err).to.be.null;
            expect(res.body).to.be.an("array");
            const task1 = res.body.find(t => t.id == createId);
            expect(task1).to.be.an("object");
            expect(task1.taskDescription).to.equal(des);
            const task2 = res.body.find(t=> t.id==createId2);
            expect(task2).to.be.an("object");
            expect(task2.taskDescription).to.equal(des2);
            done();
        })
    })
    it('Modify the taskCompleted status: POST - /save', function(done){
        chai.request(baseUrl)
        .post('/save')
        .send({
            id: createId2,
            taskDescription: des2,
	        taskDate: tdate2,
            taskCompleted: true
        })
        .end(function(err, res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("taskCompleted");
            expect(res.body.taskCompleted).to.equal(true);
            done();
        })
    })
    it('Delete completed task: DELETE - /clear', function(done){
        chai.request(baseUrl)
        .delete('/clear')
        .end(function(err,res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            done();
        })
    })
    it('Verify that the task has been deleted', function(done){
        chai.request(baseUrl)
        .get('/get/'+createId2)
        .end(function(err,res){
            expect(res).to.have.status(400);
            done();
        })
    })
    it('Clean up the data - set to completed', function(done){
        chai.request(baseUrl)
        .post('/save')
        .send({
            id: createId,
            taskDescription: des,
	        taskDate: tdate,
            taskCompleted: true
        })
        .end(function(err, res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("taskCompleted");
            expect(res.body.taskCompleted).to.equal(true);
            done();
        })

    })
    it('Clean up the data - delete completed', function(done){
        chai.request(baseUrl)
        .delete('/clear')
        .end(function(err,res){
            expect(err).to.be.null;
            expect(res).to.have.status(200);
            done();
        })
        
    })
    it('Clean up the data - verify deletion', function(done){
        chai.request(baseUrl)
        .get('/get/'+createId)
        .end(function(err,res){
            expect(res).to.have.status(400);
            done();
        })
    })

})