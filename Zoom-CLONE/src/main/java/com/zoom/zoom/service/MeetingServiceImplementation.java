package com.zoom.zoom.service;


import com.zoom.zoom.entity.Meeting;
import com.zoom.zoom.repository.MeetingRepository;
import com.zoom.zoom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingServiceImplementation implements MeetingService{
    private MeetingRepository meetingRepository;
    private UserRepository userRepository;

    @Autowired
    public MeetingServiceImplementation(UserRepository userRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.meetingRepository=meetingRepository;
    }

    @Override
    public List<Meeting> getAllScheduledMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @Override
    public void scheduleMeeting(Meeting meeting) {
        meetingRepository.save(meeting);
    }

    @Override
    public Meeting findMeetingById(Integer meetingId) {
        return meetingRepository.findMeetingById(meetingId);
    }

    @Override
    public void delete(Meeting meeting) {
        meetingRepository.delete(meeting);
    }

    @Override
    public List<Meeting> getAllScheduledMeetingsByHostId(Integer userId) {
        return meetingRepository.findMeetingsByHostId(userId);
    }
}
