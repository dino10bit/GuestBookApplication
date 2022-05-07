package com.techm.guestbook.serviceimpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techm.guestbook.entity.GuestBookEntry;
import com.techm.guestbook.repository.GuestBookEntryRepository;
import com.techm.guestbook.service.GuestBookService;

@Service("GuestBookService")
public class GuestBookServiceImpl implements GuestBookService{
	
	private static String APPROVED="Approved";
	
	@Autowired
    private GuestBookEntryRepository guestBookEntryRepository;

	@Override
    public List <GuestBookEntry> findAllEntries() throws Exception{
        return this.guestBookEntryRepository.findAll();
    }

	@Override
    public GuestBookEntry findGuestBookEntryById(Integer id)  throws Exception{
    	Optional<GuestBookEntry> entry =  this.guestBookEntryRepository.findGuestBookEntryById(id);
    	if(entry.isPresent()) {
	    		return entry.get();
	    	}
    	throw new NoResultException("No record found");
    }

	@Override
    public void deleteGuestBookEntryById(Integer id) throws Exception{
    	Optional<GuestBookEntry> entry =  this.guestBookEntryRepository.findGuestBookEntryById(id);
    	if(entry.isPresent()) {
    		this.guestBookEntryRepository.deleteGuestBookEntryById(id);
    		return;
	    }
    	throw new NoResultException("No record found");
        
    }

	@Override
    public void saveGuestBookEntry(GuestBookEntry newEntry) throws Exception{
        this.guestBookEntryRepository.save(newEntry);
    }
    
	@Override
    public void updateGuestBookEntry(GuestBookEntry entry) throws Exception{
    	Optional<GuestBookEntry> optional = this.guestBookEntryRepository.findGuestBookEntryById(entry.getId());
    	if(optional.isPresent()) {
    		GuestBookEntry updateEntry = optional.get();
    		updateEntry.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        	updateEntry.setUpdatedBy(entry.getUpdatedBy());
        	updateEntry.setTextEntry(entry.getTextEntry());
        	this.guestBookEntryRepository.save(updateEntry);
        	return;
    	}
    	throw new NoResultException("No record found");
    }

	@Override
	public void approveGuestBookEntry(Integer id) throws Exception{
		Optional<GuestBookEntry> optional = this.guestBookEntryRepository.findGuestBookEntryById(id);
    	if(optional.isPresent()) {
    		GuestBookEntry updateEntry = optional.get();
    		updateEntry.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        	updateEntry.setStatus(APPROVED);
        	this.guestBookEntryRepository.save(updateEntry);
        	return;
    	}
    	throw new NoResultException("No record found");
		
	}

	@Override
	public GuestBookEntry viewImageEntry(Integer id) throws Exception {
		Optional<GuestBookEntry> optional = this.guestBookEntryRepository.findGuestBookEntryById(id);
    	if(optional.isPresent()) {
    		GuestBookEntry entry = optional.get();
        	return entry;
    	}
    	throw new NoResultException("No record found");
		
	}
	

}
