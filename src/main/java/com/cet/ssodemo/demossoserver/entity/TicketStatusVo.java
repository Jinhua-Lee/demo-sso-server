package com.cet.ssodemo.demossoserver.entity;

import lombok.Data;

/**
 * @author Jinhua
 */
@Data
public class TicketStatusVo {

    private String ticket;
    private boolean validated = false;

    public TicketStatusVo(String ticket) {
        this.ticket = ticket;
    }

    public void validatePass() {
        this.validated = true;
    }
}
