package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.Folder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FolderDTO {

    List<Folder> folders;

    public FolderDTO(List<Folder> folders) {
        this.folders = folders;
    }

}
