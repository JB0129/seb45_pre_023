package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixman.stackoverflow.domain.member.service.dto.request.MemberDeleteServiceRequest;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDeleteApiRequest {

    @NotNull(message = "{validation.member.password}")
    private String password;

    public MemberDeleteServiceRequest toServiceRequest(Long deleteMemberId) {

        return MemberDeleteServiceRequest.builder()
                .deleteMemberId(deleteMemberId)
                .password(password)
                .build();
    }
}
