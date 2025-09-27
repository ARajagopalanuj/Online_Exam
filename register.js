

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const msg = document.getElementById("responseMsg");

    if(form){
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // stop auto-redirect

        const user = document.getElementById("reg-username").value;

        try {
            const response = await fetch("  https://9aa6e279a946.ngrok-free.app/exam/user/register", {
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
            const response= await fetch(" https://9aa6e279a946.ngrok-free.app/exam/user/login",{
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
            const apiUrl=` https://9aa6e279a946.ngrok-free.app/exam/user/${element.topic}`;
            try{
                const response=await fetch(apiUrl,{
                    method:"GET",
                    headers:{"Content-Type":"application/json"}
                });
                const data=await response.json();
                let questionSet=data.list;

                    localStorage.setItem("questions",JSON.stringify(questionSet));
                    console.log(questionSet);
            }catch(error){
                console.error("Error:",error.message);

            }
        })

        div.appendChild(document.createElement("br"));

        div.appendChild(button);

        container.appendChild(div);
        
        
       });
}
});
