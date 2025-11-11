package jy.WorkOutwithAgent.Member.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jy.WorkOutwithAgent.Member.Entity.Member;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFormDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 20, message = "아이디는 4-20자 사이여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "아이디는 영문, 숫자, _만 사용 가능합니다")
    private String username;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 15, message = "닉네임은 2-15자 사이여야 합니다")
    private String displayName;

    private String password;
    private String phone;
    private String sex;
    private Integer age;
    private double height;
    private double weight;
    private Set<String> roles;
    private boolean isPremium;
    private boolean privacyAccepted;
    private boolean termsAccepted;
    private boolean marketingAccepted;

    public Member convertToMember() {
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .displayName(this.displayName)
                .phone(this.phone)
                .sex(this.sex)
                .age(this.age)
                .height(this.height)
                .weight(this.weight)
                .privacyAccepted(this.privacyAccepted)
                .termsAccepted(this.termsAccepted)
                .marketingAccepted(this.marketingAccepted)
                .build();
    }

    public void updateMember(Member member) {
        member.setDisplayName(this.displayName);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setSex(this.sex);
        member.setAge(this.age);
        member.setHeight(this.height);
        member.setWeight(this.weight);
        member.setMarketingAccepted(this.marketingAccepted);
        // password는 따로 처리하므로 여기선 제외
    }

    public void adminUpdateMember(Member member) {
        member.setDisplayName(this.displayName);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setSex(this.sex);
        member.setAge(this.age);
        member.setHeight(this.height);
        member.setWeight(this.weight);
        member.setRoles(this.roles);
        member.setPremium(this.isPremium);
        member.setMarketingAccepted(this.marketingAccepted);
        // password는 따로 처리하므로 여기선 제외
    }

}