package com.cms.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.backend.pojo.Favorite;
import com.cms.backend.pojo.Folder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

}
