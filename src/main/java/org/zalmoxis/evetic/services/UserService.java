package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.dtos.RegisterReqDto;
import org.zalmoxis.evetic.dtos.RegisterResDto;

import java.util.UUID;

public interface UserService
{
    UUID getUserIdByUsername(String username);

    RegisterResDto register(RegisterReqDto registerReqDto);
}
