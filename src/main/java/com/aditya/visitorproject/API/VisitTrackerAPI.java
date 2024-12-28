package com.aditya.visitorproject.API;

import com.aditya.visitorproject.dtos.SearchUserInfoDTO;
import com.aditya.visitorproject.dtos.VisitTrackerDTO;
import com.aditya.visitorproject.service.NewVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin//("*")
@RequiredArgsConstructor
public class VisitTrackerAPI {

    final private UserProfileAPI userProfileAPI;

    final private NewVisitService newVisitService;

    private String getUsernameWithAuthorizationHeader(String authorizationHeader){
        String token = userProfileAPI.extractJwtToken(authorizationHeader);
        return userProfileAPI.getUsername(token);
    }

    @GetMapping("/search-home")
    public SearchUserInfoDTO searchHome(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String firstName, @RequestParam String phoneNum, @RequestParam String societyName, @RequestParam(required = false) String addressName) throws RuntimeException{
        String currentUsername = getUsernameWithAuthorizationHeader(authorizationHeader);
        return newVisitService.searchSociety(firstName, phoneNum, societyName, addressName, currentUsername);
    }

    @PostMapping("/set-visit")
    public void setVisit(@RequestParam String visitorUsername, @RequestParam String ownerUsername, @RequestBody VisitTrackerDTO visitTrackerDTO) {
        newVisitService.setVisit(visitorUsername, ownerUsername, visitTrackerDTO);
    }

    @GetMapping("/received-approval-requests")
    public List<VisitTrackerDTO> receivedApprovalRequest(@RequestHeader("Authorization") String authorizationHeader) {
        String username = getUsernameWithAuthorizationHeader(authorizationHeader);
        return newVisitService.getPendingRequestsForApproval(username);
    }

    @GetMapping("/approved-requests-pending-visit")
    public List<VisitTrackerDTO> approvedRequestsWhichArePending(@RequestHeader("Authorization") String authorizationHeader) {
        String username = getUsernameWithAuthorizationHeader(authorizationHeader);
        return newVisitService.approvedRequestsWhichArePending(username);
    }

    @PostMapping("/handle-approval-request")
    public String handleApprovalRequest( @RequestBody VisitTrackerDTO visitTrackerDTO){
        System.out.println(visitTrackerDTO);
        return newVisitService.handleApprovalRequest(visitTrackerDTO);
    }

    @PostMapping("/handle-rejection-request")
    public String handleRejectionRequest( @RequestBody VisitTrackerDTO visitTrackerDTO){
        System.out.println(visitTrackerDTO);
        return newVisitService.handleRejectionRequest(visitTrackerDTO);
    }

    @GetMapping("/get-my-requsted-visits")
    public List<VisitTrackerDTO> getMyRequestedVisits(@RequestHeader("Authorization") String authorizationHeader){
        String username = getUsernameWithAuthorizationHeader(authorizationHeader);
        return newVisitService.getMyRequestedVisits(username);
    }
}