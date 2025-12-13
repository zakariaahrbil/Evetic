package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.dtos.auth.response.LoginResDto;
import org.zalmoxis.evetic.dtos.auth.request.RegisterReqDto;
import org.zalmoxis.evetic.dtos.auth.response.RegisterResDto;

import java.util.UUID;

public interface UserService
{
    UUID getUserIdByUsername(String username);
    RegisterResDto register(RegisterReqDto registerReqDto);
    LoginResDto login(String username);

}
