package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.dtos.auth.response.LoginResDto;
import org.zalmoxis.evetic.dtos.auth.request.RegisterReqDto;
import org.zalmoxis.evetic.dtos.auth.response.RegisterResDto;
import org.zalmoxis.evetic.dtos.event.response.EventResDto;
import org.zalmoxis.evetic.entities.Event;

import java.util.Optional;
import java.util.UUID;

public interface UserService
{
    UUID getUserIdByUsername(String username);
    RegisterResDto register(RegisterReqDto registerReqDto);
    LoginResDto login(String username);

}
