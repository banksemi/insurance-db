package com.sideproject.caregiver_management.server;

import lombok.Builder;
import lombok.Data;

@Builder // Builder 패턴을 이용해서 파이썬 kwargs처럼 가독성을 높여보자
@Data
public class ServerStatusResponse {
    private String status;
}
