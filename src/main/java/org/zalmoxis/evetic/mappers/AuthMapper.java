package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.LoginResDto;
import org.zalmoxis.evetic.dtos.RegisterResDto;
import org.zalmoxis.evetic.entities.User;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper
{
    RegisterResDto toDto(User user);
}
