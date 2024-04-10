package com.zoom.zoom.controller;

import com.zoom.zoom.entity.Invite;
import com.zoom.zoom.entity.Meeting;
import com.zoom.zoom.entity.User;
import com.zoom.zoom.service.InviteService;
import com.zoom.zoom.service.MeetingService;
import com.zoom.zoom.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/meeting-controller")
public class MeetingController {

    MeetingService meetingService;
    UserService userService;
    InviteService inviteService;

    @Autowired
    public MeetingController(MeetingService meetingService, UserService userService , InviteService inviteService) {
        this.meetingService = meetingService;
        this.userService = userService;
        this.inviteService = inviteService;
    }

    @GetMapping("/scheduleMeeting")
    public String scheduleMeeting(Model model){
        Meeting meeting = new Meeting();
        model.addAttribute("meeting",meeting);
        return "schedule-meeting-form";
    }

    @PostMapping("/scheduleMeeting")
    public String scheduleMeeting(@ModelAttribute Meeting meeting,
                                  Authentication authentication,
                                  @RequestParam("invitees") String invitees){
        User currentLoggedInUser = userService.getCurrentUser();
        if(currentLoggedInUser==null){
            System.out.println("Please login first");
            return "redirect:/authentication-controller/login";
        }

        List<String> inviteeEmails = Arrays.asList(invitees.split("\\s*,\\s*"));
        List<User> validInvitees = new ArrayList<>();
        for (String email : inviteeEmails) {
            User invitedUser = userService.getUserByEmail(email.trim());
            if (invitedUser != null) {
                validInvitees.add(invitedUser);
            }
        }

        meeting.setInvitedUsers(validInvitees);
        meeting.setHost(currentLoggedInUser);
        meetingService.createMeeting(meeting);

        return "redirect:/userDashboard";
    }

    @GetMapping("/startScheduledMeeting")
    public String startScheduledMeeting(@RequestParam("meetingId") Integer meetingId,
                                        Model model){
        System.out.println("Meeting id : " + meetingId);
        Meeting meeting = meetingService.findMeetingById(meetingId);
        System.out.println("MEETING IS : " + meeting);
        return "redirect:/newMeeting";
    }

    @GetMapping("/deleteScheduledMeeting")
    public String deleteScheduledMeeting(@RequestParam("meetingId") Integer meetingId){
        System.out.println("Meeting id : " + meetingId);
        Meeting meeting = meetingService.findMeetingById(meetingId);
        System.out.println("MEETIG IS : " + meeting);
        meetingService.delete(meeting);
        return "redirect:/userDashboard";
    }

    @PostMapping("/acceptMeetingInvite")
    public String acceptMeetingInvite(@RequestParam("inviteId") Integer meetingId){
        User currentLoggedInUser = userService.getCurrentUser();
        Integer userId = currentLoggedInUser.getId();

        Invite invite = inviteService.findByMeetingIdAndUserId(meetingId, userId);
        System.out.println("INVITE IS : " + invite.getMeeting_id() + " " + invite.getUser_id() + " status: " + invite.getStatus());
        invite.setStatus(1);
        inviteService.saveInvite(invite);
        System.out.println("INVITE IS : " + invite.getMeeting_id() + " " + invite.getUser_id() + " status: " + invite.getStatus());
        return "redirect:/userDashboard";
    }

    @PostMapping("/declineMeetingInvite")
    public String declineMeetingInvite(@RequestParam("inviteId") Integer meetingId){
        User currentLoggedInUser = userService.getCurrentUser();
        Meeting meeting = meetingService.findMeetingById(meetingId);
        Integer userId = currentLoggedInUser.getId();
        currentLoggedInUser.getInvitedMeetings().remove(meeting);
        userService.updateUser(currentLoggedInUser);
        return "redirect:/userDashboard";
    }

}
