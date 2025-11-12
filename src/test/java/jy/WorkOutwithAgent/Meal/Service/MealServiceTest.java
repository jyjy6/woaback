package jy.WorkOutwithAgent.Meal.Service;

import jy.WorkOutwithAgent.GlobalErrorHandler.GlobalException;
import jy.WorkOutwithAgent.Meal.DTO.MealRequestDto;
import jy.WorkOutwithAgent.Meal.Entity.Meal;
import jy.WorkOutwithAgent.Meal.Entity.MealType;
import jy.WorkOutwithAgent.Meal.Repository.MealRepository;
import jy.WorkOutwithAgent.Member.Entity.Member;
import jy.WorkOutwithAgent.Member.Repository.MemberRepository;
import jy.WorkOutwithAgent.Member.Service.CustomUserDetails;
import jy.WorkOutwithAgent.Member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MealService mealService;

    private Member member;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .email("test@test.com")
                .roles(Set.of("ROLE_USER"))
                .build();

        userDetails = new CustomUserDetails(member);
    }

    @Test
    @DisplayName("식사 기록 생성 성공")
    void createMeal_Success() {
        // given
        MealRequestDto requestDto = MealRequestDto.builder()
                .memberId(1L)
                .foodName("닭가슴살")
                .mealType(MealType.DINNER)
                .calories(250.0)
                .protein(40.0)
                .mealDate(LocalDateTime.now())
                .build();

        Meal savedMeal = Meal.builder()
                .id(10L)
                .member(member)
                .foodName("닭가슴살")
                .mealType(MealType.DINNER)
                .calories(250.0)
                .protein(40.0)
                .mealDate(requestDto.getMealDate())
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

        // when
        Meal result = mealService.createMeal(requestDto, userDetails);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getFoodName()).isEqualTo("닭가슴살");
        assertThat(result.getMember().getId()).isEqualTo(1L);

        verify(memberRepository).findById(1L);
        verify(mealRepository).save(any(Meal.class));
    }

    @Test
    @DisplayName("식사 기록 생성 실패 - 요청한 회원 ID가 DB에 없는 경우")
    void createMeal_Fails_WhenMemberIdDoesNotExistInDb() {
        // given
        // 로그인한 사용자(ID: 1)가 자신의 식사 기록을 생성하려 함
        MealRequestDto requestDto = MealRequestDto.builder()
                .memberId(1L)
                .foodName("닭가슴살")
                .build();
        // 따라서 AuthUtils.validateMemberId(1L, userDetails)는 성공적으로 통과함

        // 하지만, DB에서 해당 회원을 찾을 수 없는 상황을 시뮬레이션
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        // 권한 검사는 통과했지만 회원을 찾을 수 없으므로 MemberNotFoundException이 발생해야 함
        assertThrows(MemberNotFoundException.class, () -> {
            mealService.createMeal(requestDto, userDetails);
        });

        // findById는 호출되었지만, save는 호출되지 않았는지 확인
        verify(memberRepository).findById(1L);
        verify(mealRepository, never()).save(any(Meal.class));
    }

    @Test
    @DisplayName("식사 기록 생성 실패 - 권한 없는 사용자")
    void createMeal_Fails_WhenUserIsUnauthorized() {
        // given
        MealRequestDto requestDto = MealRequestDto.builder()
                .memberId(2L) // Trying to create a meal for another user
                .foodName("닭가슴살")
                .build();
        // userDetails is for memberId 1L

        // when & then
        assertThrows(GlobalException.class, () -> {
            mealService.createMeal(requestDto, userDetails);
        });

        verify(memberRepository, never()).findById(anyLong());
        verify(mealRepository, never()).save(any(Meal.class));
    }
}
