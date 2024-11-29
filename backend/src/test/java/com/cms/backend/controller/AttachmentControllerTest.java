package com.cms.backend.controller;

import com.cms.backend.pojo.Attachment;
import com.cms.backend.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AttachmentControllerTest {
    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();
    }

    @Test
    void testDeleteAttachment() throws Exception {
        mockMvc.perform(delete("/attachment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 45}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAttachment() throws Exception {
        Attachment mockAttachment = new Attachment();
        mockAttachment.setName("历史试卷.xlsx");
        mockAttachment.setUrl("http://localhost:65/历史试卷.xlsx");
        when(attachmentService.getById(45)).thenReturn(mockAttachment);
        mockMvc.perform(get("/attachment/get?id=45"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("历史试卷.xlsx"))
                .andExpect(jsonPath("$.url").value("http://localhost:65/历史试卷.xlsx"));
    }

    @Test
    void testUploadAttachment() throws Exception {
        Attachment mockAttachment = new Attachment("http://localhost:65/testFile.png", "testFile.png");
        when(attachmentService.save(any(Attachment.class))).thenAnswer(invocation -> {
            Attachment arg = invocation.getArgument(0);
            arg.setId(1);
            return true;
        });
        MockMultipartFile file = new MockMultipartFile("file", "testFile.png", "image/png", "testFile.png".getBytes());
        mockMvc.perform(multipart("/attachment/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value("http://localhost:65/testFile.png"));
    }

}