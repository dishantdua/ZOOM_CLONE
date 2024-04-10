package com.zoom.zoom.repository;

import com.zoom.zoom.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Integer> {

    @Query("select i from Invite i where i.meeting_id =:meetingId and i.user_id =:userId")
    Invite findByMeetingIdAndUserId(Integer meetingId, Integer userId);
}
