package com.cms.backend.controller;

import com.cms.backend.pojo.Attachment;
import com.cms.backend.service.AttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    public AttachmentService attachmentService;

    private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Void> deleteAttachment(@RequestBody Attachment attachment) {
        attachmentService.removeById(attachment);
        return ResponseEntity.noContent().build();
    }
}
