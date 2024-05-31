package com.aditya.visitorproject.API;

import com.aditya.visitorproject.dtos.SearchUserInfoDTO;
import com.aditya.visitorproject.dtos.VisitTrackerDTO;
import com.aditya.visitorproject.service.NewVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*") // if using gateway then keep commented otherwise uncomment.
public class VisitTrackerAPI {

    @Autowired
    UserProfileAPI userProfileAPI;

    @Autowired
    private NewVisitService newVisitService;


    @GetMapping("/search-home")
    public SearchUserInfoDTO searchHome(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String firstName, @RequestParam String phoneNum, @RequestParam String societyName, @RequestParam(required = false) String addressName) throws RuntimeException{
        String token = userProfileAPI.extractJwtToken(authorizationHeader);
        String currentUsername = userProfileAPI.getUsername(token);
        return newVisitService.searchSociety(firstName, phoneNum, societyName, addressName, currentUsername);
    }

    @PostMapping("/set-visit")
    public void setVisit(@RequestParam String visitorUsername, @RequestParam String ownerUsername, @RequestBody VisitTrackerDTO visitTrackerDTO) {
        newVisitService.setVisit(visitorUsername, ownerUsername, visitTrackerDTO);
    }

    @GetMapping("/received-approval-requests")
    public List<VisitTrackerDTO> receivedApprovalRequest(@RequestHeader("Authorization") String authorizationHeader) {
        String token = userProfileAPI.extractJwtToken(authorizationHeader);
        String username = userProfileAPI.getUsername(token);
        return newVisitService.getPendingRequestsForApproval(username);
    }

    @GetMapping("/approved-requests-pending-visit")
    public List<VisitTrackerDTO> approvedRequestsWhichArePending(@RequestHeader("Authorization") String authorizationHeader) {
        String token = userProfileAPI.extractJwtToken(authorizationHeader);
        String username = userProfileAPI.getUsername(token);
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
        String token = userProfileAPI.extractJwtToken(authorizationHeader);
        String username = userProfileAPI.getUsername(token);
        return newVisitService.getMyRequestedVisits(username);
    }
}