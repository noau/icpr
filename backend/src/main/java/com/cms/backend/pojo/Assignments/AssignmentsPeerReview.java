package com.cms.backend.pojo.Assignments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "assignment_peer_review", uniqueConstraints = {@UniqueConstraint(columnNames = "submission_id")})
public class AssignmentsPeerReview {
    @Id
    @Column(name = "submission_id", nullable = false, unique = true)
    private Integer submissionId;

    @Column(name = "reviewer_id", nullable = false)
    private Integer reviewerId;

    @Column(name = "grade", nullable = false)
    private Float grade;

    @Column(name = "feedback", nullable = false, length = 255)
    private String feedback;

    @Column(name = "reviewed_at", nullable = false, length = 255)
    private String reviewedAt;
}
