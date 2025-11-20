package jy.WorkOutwithAgent.AI.Controller;


import jy.WorkOutwithAgent.AI.AssistantModels.Assistant;
import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    @Qualifier("assistantWithTools")
    private final Assistant assistantWithTools;



    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String message = request.get("message");
        log.info("Admin chat message from {}: {}", customUserDetails.getUsername(), message);

        if (message == null || message.trim().isEmpty()) {
            throw new GlobalException("메시지가 필요합니다.", "MESSAGE_REQUIRED", HttpStatus.BAD_REQUEST);
        }

        return assistantWithTools.chat(customUserDetails.getUsername(), message);
    }
}
