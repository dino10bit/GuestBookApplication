function getallentries(){
$.ajax({
    url: "/guestbook/admin/entries",
    type: "get",
    headers: { 'Authorization':  'Bearer '+$('#token').val()},
    contentType: "application/json",
    beforeSend: function() {
        $('#loading-area').show();
    },
    success: function(data) {
        $('#loading-area').hide();
        if(data == 'There is no data for this request') {
            swal("Data Not Found", "There is no data with us for this request", "error");
        }
        else {
            var tabledata = [];
            var obj = data;

            $("#guestbook-datatable > tbody").html("");
            for(var k = 0; k < obj.length; k++) {
                var row = {};
                row["SLNo"] = k+1;;
                row["Name"] = obj[k].user;
                if(obj[k].textEntry!=null){
                    row["entry"] = obj[k].textEntry;
                }
                else{
                    row["entry"] ='<a href="#" onClick=viewImage("'+obj[k].id+'")>'+obj[k].fileName+'</a>';
                }
                row["status"] = obj[k].status;
                row["id"] = obj[k].id;
                tabledata.push(row);
                
                var code;
                code = '<tbody><tr><td>' + row.SLNo + '</td><td>' + row.Name + '</td><td>' + row.entry + '</td><td>' + row.status + '</td>'
                    + '<td class="text-center">'
                    + '<button type="button" class="btn btn-sm btn btn-info editBtn" id="edit_entry" data-entryid="'+row.id+'"><i class="fa fa-edit"></i></button>';
                    if(row.status!=='Approved'){
                    code = code + '<button type="button" class="btn btn-sm btn-success ml-2 approveBtn" id="approve_entry" data-entryid="'+row.id+'"><i class="fa fa-check"></i></button>';
                    }
                    code = code + '<button type="button" class="btn btn-sm btn-danger ml-2 deleteBtn" id="delete_entry" data-entryid="'+row.id+'"><i class="fa fa-times"></i></button>'
                    +'</td></tr></tbody>';
                $('#guestbook-datatable').append(code);
            }
        }
    },
    error: function(data) {
        console.error(data);
        $('#loading-area').hide();
    }
});
}

$(document).on("click",".approveBtn", function(){
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/guestbook/admin/approveentry/'+$(this).data('entryid'),
		  type: "POST",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
		    alert(err.responseJSON.message)
		  },
		  success: function(data) {
			  alert(data);
			  getallentries();
		  }
		});
});

$(document).on("click",".deleteBtn", function(){
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: "http://localhost:8080/guestbook/admin/delete/entry/"+$(this).data('entryid'),
		  type: "DELETE",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
		    alert(err.responseJSON.message)
		  },
		  success: function(data) {
			  alert(data);
			  getallentries();
		  }
		});
});

function viewImage(id){
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/guestbook/admin/viewimageentry/'+id,
		  type: "GET",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
		    alert(err.responseJSON.message)
		  },
		  success: function(data) {
			 
		  }
		});
}

$(document).on("click",".editBtn", function(){
	 $('#customFile').val('');
	 $('#textEntry').val('');
	 $('#customFilename').val('');
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/guestbook/admin/entry/'+$(this).data('entryid'),
		  type: "GET",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
		    alert(err.responseJSON.message)
		  },
		  success: function(entrydata) {
			  $('#textEntry').val(entrydata.textEntry);
			  $('#customFilename').html(entrydata.fileName);
			  $('#entryid').val(entrydata.id);
			  $("#myModal").modal();
		  }
		});
});

function updateentry(event){
	event.preventDefault();
	if(($('#customFile').val() === '') && $('#textEntry').val() === ''){
		alert("Make an entry in text or upload an image to update the entry");
		return;
	}else if(($('#customFile').val() !== '') && $('#textEntry').val() !== ''){
		alert("Either Text Entry or Image Upload is applicable.");
		return;
	}else if(($('#customFileName').val() !== '') && $('#textEntry').val() !== ''){
		alert("An image already exists. Please update image");
		return;
	}
	var form = $('.form-signin')[0];
	var data = new FormData(form);
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/guestbook/admin/update/'+$('#entryid').val(),
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
			 $('#customFilename').val('');
		    alert(err.responseJSON.message);
		  },
		  success: function(data) {
			  $('#customFile').val('');
			  $('#textEntry').val('');
			  $('#customFilename').val('');
			  alert(data);
			  getallentries();
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

$(document).ready(function() {
	getallentries();
	});

function logout(){
	window.location = '/';
}