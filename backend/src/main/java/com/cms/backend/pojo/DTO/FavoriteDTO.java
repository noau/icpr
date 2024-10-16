package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.Favorite;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteDTO {

    private List<Favorite> favorites;

    public FavoriteDTO(List<Favorite> favorites) {
        this.favorites = favorites;
    }

}
