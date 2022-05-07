package com.techm.guestbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.techm.guestbook.entity.GuestBookEntry;

@Repository
public interface GuestBookEntryRepository extends JpaRepository <GuestBookEntry, Integer> {

    Optional<GuestBookEntry> findGuestBookEntryById(Integer id);

    @Transactional
    void deleteGuestBookEntryById(Integer id);

}
