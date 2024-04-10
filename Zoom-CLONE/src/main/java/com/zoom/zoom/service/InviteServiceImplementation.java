package com.zoom.zoom.service;

import com.zoom.zoom.entity.Invite;
import com.zoom.zoom.repository.InviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InviteServiceImplementation implements InviteService{

    InviteRepository inviteRepository;

    @Autowired
    public InviteServiceImplementation(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    @Override
    public void saveInvite(Invite invite) {
        inviteRepository.save(invite);
    }

    @Override
    public Invite findByMeetingIdAndUserId(Integer meetingId, Integer userId) {
        return inviteRepository.findByMeetingIdAndUserId(meetingId,userId);
    }
}
