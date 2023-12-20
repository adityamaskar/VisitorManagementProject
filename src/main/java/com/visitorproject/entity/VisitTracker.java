package com.visitorproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class VisitTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private String visitorUsername;

    @Column(nullable = false)
    private String ownerUsername;

    @Column(nullable = false)
    private Long ownerAddressId;

    @Column(nullable = false)
    private LocalDateTime visitDateTime;

    @Column(nullable = false)
    private LocalDateTime exitDateTime;

    private LocalDateTime dateTimeOfVisitSchedule;

    @Enumerated(EnumType.STRING)
    private VisitType visitType;

    private LocalDateTime actualVisitDateTime;

    private LocalDateTime actualExitDateTime;

    private String AuthCode;

    @Column(nullable = false)
    private Integer NumberOfVisitors;

    @Column(nullable = false)
    private Boolean isVehiclePresent;

    private String visitorVehicleName;

    private Boolean ownerApproval;

    private LocalDateTime approvalOrRejectionTime;

    private String rejectionReason;

    private String extraManualComments;
}
