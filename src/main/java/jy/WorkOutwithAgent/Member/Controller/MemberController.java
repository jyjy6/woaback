package jy.WorkOutwithAgent.Member.Controller;


import jy.WorkOutwithAgent.Auth.Util.AuthUtils;
import jy.WorkOutwithAgent.Member.DTO.MemberDto;
import jy.WorkOutwithAgent.Member.DTO.MemberFormDto;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Member.Service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody MemberFormDto memberFormDto) {
        memberService.registerUser(memberFormDto);
        return new ResponseEntity<>("회원가입이 완료되었습니다", HttpStatus.CREATED);
    }

    @GetMapping("/userinfo")
    public MemberDto getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("유저정보요청됨: {}",customUserDetails);
        AuthUtils.loginCheck(customUserDetails);

        return memberService.getUserInfo(customUserDetails);
    }



}
