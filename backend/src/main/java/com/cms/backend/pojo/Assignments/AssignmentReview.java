package com.cms.backend.pojo.Assignments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "assignment_review", uniqueConstraints = {@UniqueConstraint(columnNames = "submission_id")})
public class AssignmentReview {
    @Id
    @Column(name = "submission_id", nullable = false, unique = true)
    private Integer submissionId;

    @Column(name = "grade", nullable = false)
    private Float grade;

    @Column(name = "feedback", nullable = false, length = 255)
    private String feedback;

    @Column(name = "graded_at", nullable = false, length = 255)
    private String gradedAt;
}
