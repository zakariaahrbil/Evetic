package org.zalmoxis.evetic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.ticket.response.TicketDetailsResDto;
import org.zalmoxis.evetic.dtos.ticket.response.TicketResDto;
import org.zalmoxis.evetic.mappers.TicketMapper;
import org.zalmoxis.evetic.services.QrCodeService;
import org.zalmoxis.evetic.services.TicketService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController
{
    private final UUIDFromAuthentication uuidFromAuthentication;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    @GetMapping
    public ResponseEntity<Page<TicketResDto>> listAllTicketsForUser(Pageable pageable,
            Authentication authentication)
    {
        Page<TicketResDto> tickets = ticketService.listAllTicketsForUser(uuidFromAuthentication.getUUIDFromAuthentication(authentication),
                pageable).map(ticketMapper::toTicketResDto);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsResDto> getTicketForOwner(@PathVariable("ticketId") UUID ticketId,
            Authentication authentication)
    {
        TicketDetailsResDto ticketDetailsResDto = ticketMapper.toTicketDetailsResDto(
                ticketService.getTicketForOwner(ticketId,
                        uuidFromAuthentication.getUUIDFromAuthentication(authentication)));

        return new ResponseEntity<>(ticketDetailsResDto, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/qr-code" )
    public ResponseEntity<byte[]> getTicketQrCode(@PathVariable("ticketId") UUID ticketId,
            Authentication authentication)
    {
        byte[] qrCodeImage = qrCodeService.getQrCodeImageForOwner(ticketId,
                uuidFromAuthentication.getUUIDFromAuthentication(authentication));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);

        return ResponseEntity.ok().headers(headers).body(qrCodeImage);
    }

}
