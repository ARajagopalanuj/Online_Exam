document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const msg = document.getElementById("responseMsg");

    if(form){
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // stop auto-redirect

        const user = document.getElementById("reg-username").value;

        try {
            const response = await fetch("  https://f6f97af516b8.ngrok-free.app/exam/user/register", {
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
            msg.textContent = "Backend not reachable! "+data.message;
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
            const response= await fetch(" https://f6f97af516b8.ngrok-free.app/exam/user/login",{
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
        let button=documet.createElement("button");
        button.textContent="AArambikalaama";
        
        container.appendChild(div);
        
        
       });
}
});
