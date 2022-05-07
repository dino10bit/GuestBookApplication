function createentry(event) {
	event.preventDefault();
	if(($('#customFile').val() === '') && $('#textEntry').val() === ''){
		alert("Make an entry in text or upload an image to create the entry");
		return;
	}else if(($('#customFile').val() !== '') && $('#textEntry').val() !== ''){
		alert("Either Text Entry or Image Upload is applicable.");
		return;
	}
	var form = $('.form-signin')[0];
	var data = new FormData(form);
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: "http://localhost:8080/guestbook/guest/add",
		  type: "POST",
		  enctype: 'multipart/form-data',
		  headers: { 'Authorization':  token},
		  contentType: false,
		  processData: false,
		  cache: false,
		  data : data,
		  error: function(err) {
			 $('#customFile').val('');
			 $('#textEntry').val('');
		    alert(err.responseJSON.message);
		  },
		  success: function(data) {
			  $('#customFile').val('');
			  $('#textEntry').val('');
			  alert(data);
		  }
		});

  }

function validateFileUploaded(){
	var fileName = document.getElementById("customFile").value;
    var idxDot = fileName.lastIndexOf(".") + 1;
    var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
    if(!(extFile=="jpg" || extFile=="jpeg" || extFile=="png")){
    	$('#customFile').val('');
        alert("Only jpg/jpeg and png files are allowed!");
    }   
}

function logout(){
	window.location = '/';
}