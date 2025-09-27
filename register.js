

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const msg = document.getElementById("responseMsg");

    if(form){
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // stop auto-redirect

        const user = document.getElementById("reg-username").value;

        try {
            const response = await fetch(" https://1850a6cff694.ngrok-free.app/exam/user/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ user })
            });

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
            const response= await fetch("https://1850a6cff694.ngrok-free.app/exam/user/login",{
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
                
                setTimeout(()=>{
                window.location.href="dashboard.html";
                
                },1000)
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

        button.addEventListener("click",async ()=>{
            const apiUrl=`https://1850a6cff694.ngrok-free.app/exam/user/${element.topic}`;
            try{
                console.log(apiUrl);
            const response = await fetch(apiUrl, { method: "GET" });

                if(!response.ok){
                    throw new Error("HTTP error");
                }
                const contentType=response.headers.get("content-type");
                if(contentType&& contentType.includes("application/json")){
                const data=await response.json();
                let questionSet=data.list;
                if(data.success){

                    localStorage.setItem("questions",JSON.stringify(questionSet));
                    console.log(questionSet);
                    window.location.href="exam.html";
                }else if(data.message){
                    console.error(data.message);

                }else if(data.error){
                    console.error("backendException ",data.error);
                }
            }else{
                const text=await response.text();
                console.error(text);
            
                }
        }
            catch (error){
                console.error("Error:",error.message);

            }
        })

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
    console.log(answerSet)

    try{
        const responseAnswer=await fetch("https://1850a6cff694.ngrok-free.app/exam/user/writeExam",{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(answerSet)
        });

        const data=await responseAnswer.json();
        console.log(data.success);
        console.log(data.message);
        if(data.success){
            
            alert(`successfully\n${data.message}/${answers.length}`);
        }else{
            alert(`Failed ${data.message||data.error||"unknown error"}`);
        }
    }catch(error){
        console.error("Error:",error.message);
        alert("aaaa"+error.message);

    }
    
})
}

});
