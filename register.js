

document.addEventListener("DOMContentLoaded", async () => {
    const url="https://04084dc874a2.ngrok-free.app";
    const form = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const msg = document.getElementById("responseMsg");

    if(form){
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // stop auto-redirect

        const user = document.getElementById("reg-username").value;

        try {
            const response = await fetch(url+"/exam/user/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ user })
            });
            console.log(url);
            const data = await response.json();

            if (data.success) {
                msg.style.color = "green";
                msg.textContent = "✅ " + data.message; // show backend message
            } else {
                msg.style.color = "red";
                msg.textContent = "❌ " + data.message;
            }
        } catch (error) {
            console.error("Error:", error);
            msg.style.color = "red";
            msg.textContent = "Backend not reachable! "+error.message;
        }
    });
}


if(loginForm){
    loginForm.addEventListener("submit",async (event)=>{
        event.preventDefault();
        const user=document.getElementById("login-username").value.trim();
        const password=document.getElementById("login-password").value.trim();

        try{
            const userDetails={
                user:user,
                password:password
            }
            sessionStorage.setItem("username",user);
            const response= await fetch(url+"/exam/user/login",{
                method:"POST",
                headers:{"Content-Type":"application/json"},
                body:JSON.stringify(userDetails)
            })
            const data=await response.json();
            if(data.success){
                console.log('success');
                msg.style.color = "green";
                msg.textContent =data.message; 
                let topics=data.topics;
                localStorage.setItem("topic",JSON.stringify(topics));
                
                 if(data.admin){
                     setTimeout(()=>{
                window.location.href="admin.html";
                
                },1000)

                 }else{

                setTimeout(()=>{
                window.location.href="dashboard.html";
                
                },1000)
                }
            }else{
                console.log('unsuccess');
                msg.style.color = "red";
                msg.textContent = data.message;
            }
            
        }catch(error){
            console.error("Error:", error);
            msg.style.color = "red";
            msg.textContent = "Backend not reachable! "+error.message;
        }
    })
 }
 const container=document.getElementById("container");
 if(container){
 const topics=localStorage.getItem("topic")?JSON.parse(localStorage.getItem("topic")):[];
       
       
       topics.forEach(element => {
      
        let div=document.createElement("div");
        div.textContent=element.topic;
        let button=document.createElement("button");
        button.textContent="AArambikalaama";

        button.addEventListener("click", async () => {
    const apiUrl = `${url}/exam/user/${element.topic}`;

    try {
        console.log("Fetching questions from:", apiUrl);
        const response = await fetch(apiUrl, { 
            method: "GET" ,
        
          headers: {
                        "ngrok-skip-browser-warning": "true"
                    }
                });

        if (!response.ok) {
            throw new Error("HTTP error " + response.status);
        }

        const contentType = response.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error("Expected JSON response, got: " + contentType);
        }

        const data = await response.json();      

        if (data.success) {
            const questionSet = data.list;
            localStorage.setItem("questions", JSON.stringify(questionSet));
            console.log("Questions fetched:", questionSet);
            window.location.href = "exam.html";
        } else {
            console.error("Backend error:", data.message || data.error);
            alert("Failed to fetch questions: " + (data.message || data.error));
        }

    } catch (error) {
        console.error("Error fetching questions:", error.message);
        alert("Cannot reach backend! " + error.message);
    }
});


div.appendChild(document.createElement("br"));
div.appendChild(button);
container.appendChild(div);

       });
}
const exam=document.getElementById("exam")
if(exam){
    const questions=localStorage.getItem("questions")?JSON.parse(localStorage.getItem("questions")):[];
  
    questions.forEach((element,index)=>{
       let div= document.createElement("div");
       let question=document.createElement("span");
       question.textContent=element.question;
       function createOption(optionText, optionValue){
        let label=document.createElement("label");
            let radio=document.createElement("input");
            radio.type="radio"
            radio.name="question_"+index;
            radio.value=optionValue;
            label.append(radio);
            label.appendChild(document.createTextNode(" "+optionText));
            return label;
        }

       
       


        div.append(question);
         div.appendChild(document.createElement("br"));
        div.appendChild(createOption(element.op1,element.op1));
         div.appendChild(document.createElement("br"));
        div.appendChild(createOption(element.op2,element.op2));
         div.appendChild(document.createElement("br"));
        div.appendChild(createOption(element.op3,element.op3));
         div.appendChild(document.createElement("br"));
         div.appendChild(createOption(element.op4,element.op4));
         div.appendChild(document.createElement("br"));
         div.appendChild(createOption(element.op5,element.op5));
            


       
       div.appendChild(document.createElement("br"));
       div.appendChild(document.createElement("hr"));
       exam.appendChild(div);
       

    })
    let button=document.createElement("button");
    button.id="submitBtn";
  
    button.appendChild(document.createTextNode("submit"));
    exam.appendChild(button);
    const submitBtn=document.getElementById("submitBtn");
submitBtn.addEventListener("click",async()=>{
    const answers=[];
    questions.forEach((element,index)=>{
        const selected=document.querySelector(`input[name="question_${index}"]:checked`);
        answers.push({
            qid:element.id,
            answer:selected?selected.value:"none"
        })

    })
    const answerSet={
        questions:answers
    }
    

    try{
        const responseAnswer=await fetch(url+"/exam/user/writeExam",{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(answerSet)
        });

        const data=await responseAnswer.json();
        console.log(data.success);
        console.log(data.message);
        if(data.success){
            
            alert(`successfully\n${data.message}/${answers.length}`);
            window.location.href="result.html";
        }else{
            alert(`Failed ${data.message||data.error||"unknown error"}`);
        }
    }catch(error){
        console.error("Error:",error.message);
        alert(error.message);

    }
    
})

}
let history=document.getElementById("historyTable");


if(history){
    const userHistory={
        user:sessionStorage.getItem("username")
    }
   console.log(userHistory);
    
    try{
    const response=await fetch(url+"/exam/user/showHistory",{
        method:"post",
         headers:{"Content-Type":"application/json"},
            body:JSON.stringify(userHistory)
    })
    const data=await response.json();
    const historySet=data.historyList;
    
    if(data.success){
        historySet.forEach((element,index)=>{
            console.log(historySet);
        
        let tr=document.createElement("tr");
        let td1=document.createElement("td");
        td1.textContent=index;
        let td2=document.createElement("td");
        td2.textContent=element.questionId;
        let td3=document.createElement("td");
        td3.textContent=element.status;
        let td4=document.createElement("td");
        td4.textContent=element.answer;
        let td5=document.createElement("td");
        td5.textContent=element.correctAnswer;

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
        history.appendChild(tr);

        });
    }
}catch(error){
    console.error(data.message);

}
}

const answersubmit=document.getElementById("answersubmit");
if(answersubmit){
answersubmit.addEventListener("click",async ()=>{
    const topic =document.getElementById("fixedValue").value.trim();
    const question=document.getElementById("question").value.trim();
    const option1=document.getElementById("option1").value.trim();
    const option2=document.getElementById("option2").value.trim();
    const option3=document.getElementById("option3").value.trim();
    const option4= document.getElementById("option4").value.trim();
    const answer=document.getElementsById("answer").value.trim();
    if(answer===""){
        answer="none of the above";
    }

      const adminMsg=document.getElementById("responseadmin");
    
    const addQuestion={
        question:question,
        option1:option1,
        option2:option2,
        option3:option3,
        option4:option4,
        answer:answer,
        topic:topic
    }
    
    
if (
    !question || !option1 || !option2 || !option3 || !option4 || !answer
) {
    alert("Must fill all fields");
} 
else if ([option1, option2, option3, option4,"none of the above"].includes(answer)) {
    const response=await fetch(url+"/exam/admin/insert",{
        method:"POST",
         headers:{"Content-Type":"application/json"},
            body:JSON.stringify(addQuestion)

    })
    console.log(question ,option1,option2, option3, option4 , answer)
    const data=await response.json();
    if(data.success){
        adminMsg.style.color="green";
        adminMsg.textContent=data.message;
             question=document.getElementById("question")="";
            option1=document.getElementById("option1")="";
            option2=document.getElementById("option2")="";
            option3=document.getElementById("option3")="";
            answer=document.getElementById("answer")="";
        
    }else{
        adminMsg.style.color="red";
        adminMsg.textContent=data.message;
    }
} 
else {
    alert("Enter a valid answer");
}

})
function generateValue(){
    let code=Math.floor(100000+ Math.random()*900000).toString();

    let today=new Date();
    document.getElementById("fixedValue").value=code+" | "+(today.toString().substring(0,16));
}
 window.onload=generateValue;



}

 const textCode=document.getElementById("testCode").value.trim();

 if(textCode){
    const code={topic:textCode}

    const response=await fetch(url+"/exam/admin/viaCode",{
        method:"POST",
        headers:{"Content-Type":"application/json"},
            body:JSON.stringify(addQuestion)
    })
    const data= await response.json();

    if(data.success){
         const questionSet = data.list;
            localStorage.setItem("questions", JSON.stringify(questionSet));
            console.log("Questions fetched:", questionSet);
            window.location.href = "exam.html";
        
    }else{
        alert(data.message);
    }

 }

});
