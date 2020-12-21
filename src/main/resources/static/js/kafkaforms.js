function getFormElements(formId) {
     var values = {};
    $.each($(formId).serializeArray(), function(i, field) {
        values[field.name] = field.value;
    });
    return values;
}

$(document).ready(function(){

    $("#sendform").submit(function(e) {
       e.preventDefault();
       values = getFormElements("#sendform");
       console.log(values['to']);
       $.ajax({
           url: "send",
           type: "post",
           data: {
               message: values['message'],
               to: values['to'] //$('#username').val()
           },
           success: function(data,status) {
               $("#sendform").after("<p> Sent! </p>");
           }
       });
    });



    $("#receiveform").submit(function(e){
        e.preventDefault();
        values = getFormElements("#receiveform");

        console.log(values['username']);
        $.ajax({
            url: "receive",
            type: "get",
            data: {
                username: values['username'] //$('#username').val()
            },
            success: function(data,status){
                $("#receiveform").after("<p>" + data + "</p>");
                console.log("Data: " + data + "\nStatus: " + status);
            }
        });
    });
});