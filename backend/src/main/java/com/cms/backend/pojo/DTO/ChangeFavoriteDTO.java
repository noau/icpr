package com.cms.backend.pojo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeFavoriteDTO {

    private Integer id;

    private Integer favoriteId;

    public ChangeFavoriteDTO(Integer id, Integer favoriteId) {
        this.id = id;
        this.favoriteId = favoriteId;
    }

}
