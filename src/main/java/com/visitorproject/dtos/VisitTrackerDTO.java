package com.visitorproject.dtos;

import com.visitorproject.entity.VisitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class VisitTrackerDTO {

    private Long id;

    private Integer version;

    private String visitorUsername;

    private String ownerUsername;

    private String addressName; // to save the AddressId in the tracker Object

    private LocalDateTime visitDateTime;

    private LocalDateTime exitDateTime;

    private LocalDateTime dateTimeOfVisitSchedule;

    private VisitType visitType;

    private LocalDateTime actualVisitDateTime;

    private LocalDateTime actualExitDateTime;

    private String AuthCode;

    private Integer NumberOfVisitors;

    private Boolean isVehiclePresent;

    private String visitorVehicleName;

    private Boolean ownerApproval;

    private LocalDateTime approvalOrRejectionTime;

    private String rejectionReason;

    private String extraManualComments;
}
