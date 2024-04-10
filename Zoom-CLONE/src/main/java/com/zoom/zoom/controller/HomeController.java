package com.zoom.zoom.controller;

import com.zoom.zoom.entity.Invite;
import com.zoom.zoom.entity.Meeting;
import com.zoom.zoom.entity.User;
import com.zoom.zoom.service.InviteService;
import com.zoom.zoom.service.MeetingService;
import com.zoom.zoom.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    private MeetingService meetingService;
    private UserService userService;
    private InviteService inviteService;

    @Autowired
    public HomeController(MeetingService meetingService, UserService userService,InviteService inviteService) {
        this.meetingService = meetingService;
        this.userService = userService;
        this.inviteService = inviteService;
    }

    @GetMapping
    public String homePage(){
        return "home";
    }

    @GetMapping("/newMeeting")
    public String newMeeting(Model model){
        return "redirect:/newMeeting.html";
    }

    @GetMapping("/joinMeeting")
    public String joinMeeting(Model model){
        return "join-meeting";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/userDashboard")
    public String userDashboard(Model model){
        User currentLoggedInUser = userService.getCurrentUser();
        if(currentLoggedInUser==null){
            System.out.println("Please Login first");
            return "sign-in";
        }

        Integer userId = currentLoggedInUser.getId();
        List<Meeting> scheduledMeetings = meetingService.getAllScheduledMeetingsByHostId(userId);
        Map<Meeting,Integer> invitedMeetings = new HashMap<>();

        for(Meeting currMeeting: currentLoggedInUser.getInvitedMeetings()){
            Integer meetingId = currMeeting.getId();
            Invite invite = inviteService.findByMeetingIdAndUserId(meetingId,userId);
            int status = invite.getStatus();
            invitedMeetings.put(currMeeting,status);
        }

        model.addAttribute("scheduledMeetings", scheduledMeetings);
        model.addAttribute("invitedMeetings", invitedMeetings);
        System.out.println("INSIDE DASHBOARD FN. INVITED MEETINGS : " + invitedMeetings);
        model.addAttribute("meeting", new Meeting());
        return "user-dashboard";
    }

}
