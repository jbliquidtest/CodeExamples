"use strict";
//// ############ consts  for changing html ###############
const about_text = `<div class="aboutus-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h2 class="aboutus-title">About me</h2>
                        <p class="aboutus-text">HI! I'M Ofek</p>
                        <p class="aboutus-text">Computer science student at the IDC Herzliya. On my free time I love playing my guitar and piano.</p>
                        <p class="aboutus-text">I also enjoy reading and hiking, not in the same time ;).</p>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus-banner">
                        <img class="static" src="my_gif_banner.gif"><img class="active" src="my_gif.gif">
                    </div>
                </div>
            </div>
        </div>
    </div>`;
const education_text = `<div class="aboutus-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">Omer high school</h3>
                        <p class="aboutus-text">5 points electives: Mathematics, Physics, English, </p>
                        <p class="aboutus-text">Computer science and a Computer science project.</p>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">IDC Herzliya </h3>
                        <p class="aboutus-text">Computer science B.Sc.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>`;

const employment_text = `<div class="aboutus-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">Technical Team Leader - Intelligence Technology Unit - 81</h3>
                        <p class="aboutus-text">Worked jointly with R&D engineers, senior managers, product managers and cyber teams</p>
                        <p class="aboutus-text">Leveraged state of the art technology to enhance the intelligence scene.</p>
                        <p class="aboutus-text">Led a team of 12 technicians and project frontrunners, emphasizing on individual training and professional development.</p>
                        <p class="aboutus-text">Researching and implementing engineering solutions for advanced projects in many fields such as RF, hardware, optics and physics.</p>
                        <p class="aboutus-text">Leveraged state of the art technology to enhance the intelligence scene.</p>
                    </div>
                </div>
                 <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">Technical-Operational Team member  - Intelligence Technology Unit - 81 </h3>
                        <p class="aboutus-text">Completed extensive technology training in computer software and hardware.</p>
                        <p class="aboutus-text">Comprehensive hands-on training for advanced technological operations.</p>
                        <p class="aboutus-text">Operated in intensive and dynamic work environment under high pressure conditions.</p>
                        <p class="aboutus-text">Worked with a broad variety of technologies in several fields: Electronics, Optics, Cyber and more.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>`;
const hobbies_text = `<div class="aboutus-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">Sport </h3>
                        <p class="aboutus-text">Basketball</p>
                        <img class="pic" src="kid_basketball.jfif">
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">Music </h3>
                        <p class="aboutus-text">playing guiter and piano</p>
                        <img class="pic" src="jimi.jfif">
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="aboutus">
                        <h3 class="aboutus-title">books </h3>
                        <p class="aboutus-text">fantasy</p>
                        <img class="pic" src="book.jpg">
                    </div>
                </div>
            </div>
        </div>
    </div>`;



    const contact_text = `<div class="container contact-form">
            <div class="contact-image">
                <img src="https://image.ibb.co/kUagtU/rocket_contact.png" alt="rocket_contact"/>
            </div>
            <form name="myForm" onchange="mail()" action="mailto:someone@example.com" method="post" onsubmit="return validateForm()">
                <h3>Drop Us a Message</h3>
               <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <input type="text" name="txtName" class="form-control" placeholder="Your Name *" value="" />
                        </div>
                        <div class="form-group">
                            <input type="text" name="txtEmail" class="form-control" placeholder="Your Email *" value="" />
                        </div>
                        <div class="form-group">
                            <input type="text" name="txtPhone" class="form-control" placeholder="Your Phone Number *" value="" />
                        </div>
                        <div class="form-group">
                            <input type="submit" name="btnSubmit" class="btnContact" value="Send Message" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <textarea name="txtMsg" class="form-control" placeholder="Your Message *" style="width: 100%; height: 150px;"></textarea>
                        </div>
                    </div>
                </div>
            </form>
	</div>`;

//// ############ function for validate the form when submit  ###############
function validateForm() {
  let name = document.forms["myForm"]["txtName"].value;
  let email = document.forms["myForm"]["txtEmail"].value;
  let phone = document.forms["myForm"]["txtPhone"].value;
  let message = document.forms["myForm"]["txtMsg"].value;
  let atpos = email.indexOf("@");
  let dotpos = email.lastIndexOf(".");     

  // check id there is an empty fields
  if (name == "" || email  == "" || phone == "" || message == "") {
    alert("all detalis must be filled out");
    return false;
  }

  // check if this is a structure of a valid email
  if (atpos < 1 || ( dotpos - atpos < 2 )) {
    alert("Please enter correct email ID");
    document.forms["myForm"]["txtEmail"].focus() ;
    return false;
 }

  // check if the phone numer is only numbers
 if (!(/^\d+$/.test(phone))){
 	alert("Please enter correct phone numer");
    document.forms["myForm"]["txtPhone"].focus() ;
    return false;
	}
}

//// ############ function for edit mailto in form    ###############
function mail() {
let name = document.forms["myForm"]["txtName"].value;
let email = document.forms["myForm"]["txtEmail"].value;
let phone = document.forms["myForm"]["txtPhone"].value;
let message = encodeURIComponent(document.forms["myForm"]["txtMsg"].value);
var str = "mailto:someone@example.com?subject=Message&body=";
str += "hi, my name is " + name +" ,";
str += "my phone number is " + phone +" .";
str += "%0D%0A"+message;
str += "%0D%0ARegards,%0D%0A" + name;
document.forms["myForm"].action = str;
}



//// ############  Listeners   ###############

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("about").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = about_text;
     //validation code to see State field is mandatory.  
    }  ); 

     document.getElementById("home").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = about_text ;
     //validation code to see State field is mandatory.  
    }  ); 
    
	document.getElementById("education").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = education_text ;
     //validation code to see State field is mandatory.  
    }  );

    document.getElementById("employment").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = employment_text ;
     //validation code to see State field is mandatory.  
    }  );

    document.getElementById("hobbies").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = hobbies_text ;
     //validation code to see State field is mandatory.  
    }  );

    document.getElementById("contact").addEventListener('click', ()=>{
     document.getElementById("main").innerHTML = contact_text ;
     //validation code to see State field is mandatory.  
    }  );

    

});