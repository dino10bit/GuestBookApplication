package com.techm.guestbook;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techm.guestbook.entity.GuestBookEntry;
import com.techm.guestbook.security.CustomUserDetailsService;
import com.techm.guestbook.security.JwtAuthenticationEntryPoint;
import com.techm.guestbook.security.JwtUtil;
import com.techm.guestbook.service.GuestBookService;

@SpringBootTest(
        classes = {SpringSecurityWebAuxTestConfig.class})
@AutoConfigureMockMvc
public class GuestBookRestControllerTest {

	@Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    
    @MockBean
    GuestBookService guestBookService;
    
    @MockBean
    JwtUtil jwtTokenUtil;
    
    @MockBean
    CustomUserDetailsService userDetailsService;
    
    @MockBean
    JwtAuthenticationEntryPoint unauthorizedHandler;
    
    GuestBookEntry guestBookEntry1 = new GuestBookEntry(1, "username1", "Test Entry", null, new Timestamp(System.currentTimeMillis()),null,null);
    GuestBookEntry guestBookEntry2 = new GuestBookEntry(2, "username2", "Test Entry2", null, new Timestamp(System.currentTimeMillis()),null,null);
    
    @Test
    @WithUserDetails(value="admin")
    public void getAllEntries_success() throws Exception {
        List<GuestBookEntry> records = new ArrayList<>(Arrays.asList(guestBookEntry1, guestBookEntry2));
        
        Mockito.when(guestBookService.findAllEntries()).thenReturn(records);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/guestbook/admin/entries")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].user", is("username2")));
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void getAllEntries_failure() throws Exception {
        
        Mockito.when(guestBookService.findAllEntries()).thenThrow(new RuntimeException());
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/guestbook/admin/entries")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void getEntryById_success() throws Exception {
        
        Mockito.when(guestBookService.findGuestBookEntryById(guestBookEntry1.getId())).thenReturn(guestBookEntry1);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/guestbook/admin/entry/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.user", is("username1")));
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void getEntryById_NoRecordFound() throws Exception {
        
    	Mockito.when(guestBookService.findGuestBookEntryById(5))
    													.thenThrow(new NoResultException(""));
        
    	mockMvc.perform(MockMvcRequestBuilders
                .get("/guestbook/admin/entry/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void getEntryById_failure() throws Exception {
        
    	Mockito.when(guestBookService.findGuestBookEntryById(guestBookEntry1.getId())).thenThrow(new RuntimeException());
        
    	mockMvc.perform(MockMvcRequestBuilders
                .get("/guestbook/admin/entry/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void deleteEntryById_success() throws Exception {
    	Mockito.when(guestBookService.findGuestBookEntryById(guestBookEntry1.getId())).thenReturn((guestBookEntry1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/guestbook/admin/delete/entry/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void deleteEntryById_NoRecordFound() throws Exception {
        
    	Mockito.doThrow(new NoResultException("")).when(guestBookService).deleteGuestBookEntryById(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .delete("/guestbook/admin/delete/entry/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void deleteEntryById_failure() throws Exception {
        
    	Mockito.doThrow(new RuntimeException()).when(guestBookService).deleteGuestBookEntryById(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .delete("/guestbook/admin/delete/entry/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
    
    @Test
    @WithUserDetails(value="guest")
    public void createEntry_success() throws Exception {
        
    	String content = "Test Entry";
    	MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE, content.getBytes());
        
        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/guestbook/guest/add")
                .file(file)
                .param("textEntry", "Test Entry"))
        		.andExpect(status().isOk());
        
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void updateEntry_success() throws Exception {
    	GuestBookEntry guestBookEntryUpdate = new GuestBookEntry(2, "username2", "Update Test Entry2", null, new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),"admin1");
    	
        mockMvc.perform(MockMvcRequestBuilders
                .post("/guestbook/admin/update")
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(this.mapper.writeValueAsString(guestBookEntryUpdate)))
        		.andExpect(status().isOk());
        
    }
    
    @Test
    @WithUserDetails(value="admin")
    public void approveEntry_success() throws Exception {
    	GuestBookEntry guestBookEntryUpdate = new GuestBookEntry(2, "username2", "Update Test Entry2", null, new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),"admin1");
    	guestBookEntryUpdate.setStatus("Approved");
    	
        mockMvc.perform(MockMvcRequestBuilders
                .post("/guestbook/admin/approveentry")
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(this.mapper.writeValueAsString(guestBookEntryUpdate)))
        		.andExpect(status().isOk());
        
    }

}
