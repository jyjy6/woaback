package jy.WorkOutwithAgent.Member;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Member.DTO.MemberFormDto;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Member.Service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공 - 정상적인 회원 데이터로 가입")
    void registerUser_Success() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        String encodedPassword = "encoded_password_123";

        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(false);
        when(memberRepository.existsByEmail(memberFormDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(memberFormDto.getPassword())).thenReturn(encodedPassword);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            return Member.builder()
                    .id(1L)
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .email(member.getEmail())
                    .displayName(member.getDisplayName())
                    .phone(member.getPhone())
                    .sex(member.getSex())
                    .age(member.getAge())
                    .height(member.getHeight())
                    .weight(member.getWeight())
                    .privacyAccepted(member.isPrivacyAccepted())
                    .termsAccepted(member.isTermsAccepted())
                    .marketingAccepted(member.isMarketingAccepted())
                    .build();
        });

        // when
        Member savedMember = memberService.registerUser(memberFormDto);

        // then
        assertNotNull(savedMember);
        assertEquals(memberFormDto.getUsername(), savedMember.getUsername());
        assertEquals(encodedPassword, savedMember.getPassword());
        assertEquals(memberFormDto.getEmail(), savedMember.getEmail());
        assertEquals(memberFormDto.getDisplayName(), savedMember.getDisplayName());

        verify(memberRepository).existsByUsername(memberFormDto.getUsername());
        verify(memberRepository).existsByDisplayName(memberFormDto.getDisplayName());
        verify(memberRepository).existsByEmail(memberFormDto.getEmail());
        verify(passwordEncoder).encode(memberFormDto.getPassword());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 아이디")
    void registerUser_Fail_DuplicateUsername() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class,
                () -> memberService.registerUser(memberFormDto));

        assertEquals("이미 사용 중인 아이디입니다", exception.getMessage());
        assertEquals("USERNAME_ALREADY_EXISTS", exception.getErrorCode());

        verify(memberRepository).existsByUsername(memberFormDto.getUsername());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    void registerUser_Fail_DuplicateDisplayName() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class,
                () -> memberService.registerUser(memberFormDto));

        assertEquals("이미 사용 중인 닉네임입니다", exception.getMessage());
        assertEquals("DISPLAYNAME_ALREADY_EXISTS", exception.getErrorCode());

        verify(memberRepository).existsByUsername(memberFormDto.getUsername());
        verify(memberRepository).existsByDisplayName(memberFormDto.getDisplayName());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void registerUser_Fail_DuplicateEmail() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(false);
        when(memberRepository.existsByEmail(memberFormDto.getEmail())).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class,
                () -> memberService.registerUser(memberFormDto));

        assertEquals("이미 사용 중인 이메일입니다", exception.getMessage());
        assertEquals("EMAIL_ALREADY_EXISTS", exception.getErrorCode());

        verify(memberRepository).existsByUsername(memberFormDto.getUsername());
        verify(memberRepository).existsByDisplayName(memberFormDto.getDisplayName());
        verify(memberRepository).existsByEmail(memberFormDto.getEmail());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 null")
    void registerUser_Fail_PasswordNull() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        memberFormDto.setPassword(null);

        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(false);
        when(memberRepository.existsByEmail(memberFormDto.getEmail())).thenReturn(false);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class,
                () -> memberService.registerUser(memberFormDto));

        assertEquals("비밀번호는 필수 입력 항목입니다", exception.getMessage());
        assertEquals("PASSWORD_REQUIRED", exception.getErrorCode());

        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 빈 문자열")
    void registerUser_Fail_PasswordEmpty() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        memberFormDto.setPassword("");

        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(false);
        when(memberRepository.existsByEmail(memberFormDto.getEmail())).thenReturn(false);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class,
                () -> memberService.registerUser(memberFormDto));

        assertEquals("비밀번호는 필수 입력 항목입니다", exception.getMessage());
        assertEquals("PASSWORD_REQUIRED", exception.getErrorCode());

        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 성공 - 비밀번호 암호화 확인")
    void registerUser_Success_PasswordEncoded() {
        // given
        MemberFormDto memberFormDto = createValidMemberFormDto();
        String rawPassword = "password123";
        String encodedPassword = "encoded_password_123";
        memberFormDto.setPassword(rawPassword);

        when(memberRepository.existsByUsername(memberFormDto.getUsername())).thenReturn(false);
        when(memberRepository.existsByDisplayName(memberFormDto.getDisplayName())).thenReturn(false);
        when(memberRepository.existsByEmail(memberFormDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Member savedMember = memberService.registerUser(memberFormDto);

        // then
        assertEquals(encodedPassword, savedMember.getPassword());
        assertNotEquals(rawPassword, savedMember.getPassword());
        verify(passwordEncoder).encode(rawPassword);
    }

    private MemberFormDto createValidMemberFormDto() {
        MemberFormDto dto = new MemberFormDto();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setDisplayName("테스터");
        dto.setPhone("010-1234-5678");
        dto.setSex("M");
        dto.setAge(25);
        dto.setHeight(175.5);
        dto.setWeight(70.0);
        dto.setPrivacyAccepted(true);
        dto.setTermsAccepted(true);
        dto.setMarketingAccepted(false);
        return dto;
    }
}