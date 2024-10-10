package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.Folders;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderDTO {

    List<Folders> folders;

    public FolderDTO(List<Folders> folders) {
        this.folders = folders;
    }

}
