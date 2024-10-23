package com.cms.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Attachment {

    public Attachment(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String url;

    private String name;

    private Integer submissionId;

    private Integer assignmentId;

    private String examId;

    private String pptId;

    private String exerciseId;

    private Integer avatarId;

    private Integer answerId;

    private String syllabusId;

    private String calendarId;

    private Integer allowDownload;

    private Integer attachmentFolderId;

}
