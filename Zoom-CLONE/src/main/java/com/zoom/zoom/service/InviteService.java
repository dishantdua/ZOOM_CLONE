package com.zoom.zoom.service;

import com.zoom.zoom.entity.Invite;

public interface InviteService {
    void saveInvite(Invite invite);

    Invite findByMeetingIdAndUserId(Integer meetingId, Integer userId);
}
