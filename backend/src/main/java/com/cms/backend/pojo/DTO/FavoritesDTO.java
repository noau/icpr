package com.cms.backend.pojo.DTO;

import com.cms.backend.pojo.Favorites;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoritesDTO {

    private List<Favorites> favorites;

    public FavoritesDTO(List<Favorites> favorites) {
        this.favorites = favorites;
    }

}
