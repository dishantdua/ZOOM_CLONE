package com.zoom.zoom.restcontroller;

import com.zoom.zoom.entity.Meeting;
import com.zoom.zoom.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        System.out.println("INSIDE MEETINF REST CONTROLLER");
        List<Meeting> meetings = meetingService.getAllScheduledMeetings();
        return ResponseEntity.ok(meetings);
    }

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.ok(createdMeeting);
    }
}
