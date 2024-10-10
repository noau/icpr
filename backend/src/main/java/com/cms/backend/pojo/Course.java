package com.cms.backend.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Course {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 255)
    private String id;

    @Column(name = "course_number", nullable = false, length = 255)
    private String courseNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "semester_year", nullable = false, length = 255)
    private String semesterYear;

    @Column(name = "class_number", nullable = false, unique = true, length = 255)
    private Integer classNumber;

    @Column(name = "start", nullable = false, length = 255)
    private String start;

    @Column(name = "end", nullable = false, length = 255)
    private String end;

    @Column(name = "academy", nullable = false, length = 255)
    private String academy;

    @Column(name = "teacher", nullable = false, length = 255)
    private String teacher;

}
