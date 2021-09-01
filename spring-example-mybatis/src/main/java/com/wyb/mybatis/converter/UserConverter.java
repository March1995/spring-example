package com.wyb.mybatis.converter;

import com.wyb.mybatis.dao.model.UserDo;
import com.wyb.mybatis.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(source = "id", target = "userId"),
    })
    UserDTO item2Dto(UserDo item);

    List<UserDTO> items2Dto(List<UserDo> items);

}
