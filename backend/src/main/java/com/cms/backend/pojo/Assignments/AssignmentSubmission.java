package com.cms.backend.pojo.Assignments;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "assignment_submission", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class AssignmentSubmission {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "assignment_id", nullable = false)
    private Integer assignmentId;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "submitted_at", nullable = false, length = 255)
    private String submittedAt;

    @Column(name = "content", nullable = false, length = 255)
    private String content;
}
