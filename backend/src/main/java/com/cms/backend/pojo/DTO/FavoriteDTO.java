package com.cms.backend.pojo.DTO;

import com.cms.backend.controller.UserController;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteDTO {

    private List<UserController.FavoriteGet> favorites;

    public FavoriteDTO(List<UserController.FavoriteGet> favorites) {
        this.favorites = favorites;
    }

}
