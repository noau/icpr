package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "attachment", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Attachment {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "submission_id", nullable = false)
    private Integer submissionId;

    @Column(name = "assignment_id", nullable = false)
    private Integer assignmentId;

    @Column(name = "exam_id", nullable = false, length = 255)
    private String examId;

    @Column(name = "ppt_id", nullable = false)
    private String pptId;

    @Column(name = "exercise_id", nullable = false)
    private String exerciseId;

    @Column(name = "avatar_id", nullable = false)
    private String avatarId;

    @Column(name = "answer_id", nullable = false, length = 255)
    private String answerId;
}
