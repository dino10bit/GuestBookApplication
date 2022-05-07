package com.techm.guestbook.web;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techm.guestbook.entity.GuestBookEntry;
import com.techm.guestbook.payload.ErrorMessage;
import com.techm.guestbook.service.GuestBookService;

@RequestMapping("/guestbook")
@RestController
public class GuestBookRestController {

	private static String PENDING="Pending";
	
    @Autowired
    private GuestBookService guestBookService;

    @GetMapping("/admin/entries")
    public ResponseEntity<Object> getAllEntries() {
        try {
        	Object list = guestBookService.findAllEntries();
        	return new ResponseEntity<Object>(list, HttpStatus.OK);
        }catch(Exception e) {
        	ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }

    @GetMapping("/admin/entry/{id}")
    public ResponseEntity<Object>  findGuestBookEntryById(@PathVariable("id") Integer id) {
        try {
        	GuestBookEntry entry = this.guestBookService.findGuestBookEntryById(id);
        	return new ResponseEntity<Object>(entry, HttpStatus.OK);
        }catch(NoResultException ex) {
        	ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(ex.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.NO_CONTENT);
		}
		catch(Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @DeleteMapping("/admin/delete/entry/{id}")
    public ResponseEntity<Object> deleteGuestBookEntryById(@PathVariable("id") Integer id) {
        
        try {
        	this.guestBookService.deleteGuestBookEntryById(id);
        	return new ResponseEntity<Object>("Entry Deleted Sucessfully", HttpStatus.OK);
        }catch(NoResultException ex) {
        	ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(ex.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.NO_CONTENT);
		}
		catch(Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PostMapping(value="/guest/add", consumes= {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> addEntry(@RequestPart(name="textEntry", required=false) String textEntry, @RequestPart(name="imageEntry", required=false) MultipartFile file) {
        try {
        	if(StringUtils.hasText(textEntry) && file.getSize()>0) {
        		ErrorMessage errorMessage = new ErrorMessage();
    			errorMessage.setMessage("Only Text Entry or Image Upload is applicable");
    			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
        	}
        	GuestBookEntry guestBookEntry = new GuestBookEntry(); 
        	guestBookEntry.setUser(((org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        	guestBookEntry.setTextEntry(textEntry);
        	guestBookEntry.setFileName(file.getOriginalFilename());
        	byte[] bytes = file.getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        	guestBookEntry.setContent(blob);
        	guestBookEntry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        	guestBookEntry.setStatus(PENDING);
        	this.guestBookService.saveGuestBookEntry(guestBookEntry);
        	return new ResponseEntity<Object>("Entry Created", HttpStatus.OK);
        }
		catch(Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
		}
    }

    @PostMapping(value="/admin/update/{id}", consumes= {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateEntry(@RequestPart(name="textEntry", required=false) String textEntry, @RequestPart(name="imageEntry", required=false) MultipartFile file,@PathVariable Integer id) {
        try {
        	if(StringUtils.hasText(textEntry) && file.getSize()>0) {
        		ErrorMessage errorMessage = new ErrorMessage();
    			errorMessage.setMessage("Only Text Entry or Image Upload is applicable");
    			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
        	}else {
            	GuestBookEntry entry = this.guestBookService.findGuestBookEntryById(id);
            	entry.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            	if(StringUtils.hasText(textEntry)){
            		entry.setTextEntry(textEntry);
                	entry.setContent(null);
            	}else {
            		entry.setTextEntry(null);
            		entry.setFileName(file.getOriginalFilename());
                	byte[] bytes = file.getBytes();
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
                    entry.setContent(blob);
            	}

             	this.guestBookService.updateGuestBookEntry(entry);
             	return new ResponseEntity<Object>("Entry Updated", HttpStatus.OK);
        	}
         }catch(NoResultException ex) {
          	ErrorMessage errorMessage = new ErrorMessage();
  			errorMessage.setMessage(ex.getMessage());
  			return new ResponseEntity<Object>(errorMessage, HttpStatus.NO_CONTENT);
  		}
 		catch(Exception e) {
 			ErrorMessage errorMessage = new ErrorMessage();
 			errorMessage.setMessage(e.getMessage());
 			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
 		}
    }
    
    @PostMapping("/admin/approveentry/{id}")
    public ResponseEntity<Object> approveEntry(@PathVariable Integer id) {
    	System.out.println("from approveEntry"+id);
    	 try {
         	this.guestBookService.approveGuestBookEntry(id);
         	return new ResponseEntity<Object>("Entry Approved", HttpStatus.OK);
         }catch(NoResultException ex) {
         	ErrorMessage errorMessage = new ErrorMessage();
 			errorMessage.setMessage(ex.getMessage());
 			return new ResponseEntity<Object>(errorMessage, HttpStatus.NO_CONTENT);
 		}
 		catch(Exception e) {
 			ErrorMessage errorMessage = new ErrorMessage();
 			errorMessage.setMessage(e.getMessage());
 			return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
 		}
    }
    
    @GetMapping(value="/admin/viewimageentry/{id}", produces= {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Object>  viewImageEntry(@PathVariable("id") Integer id) {
        try {
        	GuestBookEntry entry = this.guestBookService.findGuestBookEntryById(id);
        	byte[] content = entry.getContent().getBytes(1L,(int)entry.getContent().length());
        	return ResponseEntity.ok()
                    .contentLength(content.length)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + entry.getFileName())
                    .body(content);
        }catch(NoResultException ex) {
        	ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(ex.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.NO_CONTENT);
		}
		catch(Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(e.getMessage());
			return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
