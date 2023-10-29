package com.visitorproject.dtos;

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

    private String visitorUsername;

    private LocalDateTime visitDateTime;

    private LocalDateTime exitDateTime;

    private Boolean isShortVisit;

    private LocalDateTime actualVisitDateTime;

    private LocalDateTime actualExitDateTime;

    private Integer numberOfVisitors;

    private Boolean isVehiclePresent;

    private Long vehicleId;

    private Boolean ownerApproval;

    private String rejectionReason;

    private String extraManualComments;

}
