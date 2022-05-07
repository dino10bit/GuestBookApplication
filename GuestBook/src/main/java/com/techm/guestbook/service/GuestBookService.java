package com.techm.guestbook.service;

import java.util.List;

import com.techm.guestbook.entity.GuestBookEntry;


public interface GuestBookService {

    public List <GuestBookEntry> findAllEntries() throws Exception;

    public GuestBookEntry findGuestBookEntryById(Integer id)  throws Exception;
    
    public void deleteGuestBookEntryById(Integer id) throws Exception;

    public void saveGuestBookEntry(GuestBookEntry newEntry) throws Exception;
    
    public void updateGuestBookEntry(GuestBookEntry entry) throws Exception;

	public void approveGuestBookEntry(Integer id) throws Exception;
	
	public GuestBookEntry viewImageEntry(Integer id) throws Exception;
	
}
