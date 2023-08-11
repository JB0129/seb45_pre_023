package sixman.stackoverflow.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.member.controller.dto.MemberDeleteApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberPasswordUpdateAPiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberUpdateApiRequest;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.service.MemberService;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;
import sixman.stackoverflow.module.aws.s3service.S3Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;

    public MemberController(MemberService memberService, S3Service s3Service) {
        this.memberService = memberService;
        this.s3Service = s3Service;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<ApiSingleResponse<MemberResponse>> getMember(@PathVariable("member-id") Long memberId) {

        MemberResponse response = memberService.getMember(memberId);

        return ResponseEntity.ok(ApiSingleResponse.ok(response));
    }

    @GetMapping("/{member-id}/questions")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberQuestion>> getMemberQuestion(
            @PathVariable("member-id") Long memberId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {

        MemberResponse.MemberQuestionPageResponse memberQuestion
                = memberService.getMemberQuestion(memberId, page - 1, 5);

        Page<MemberResponse.MemberQuestion> memberQuestionPage = new PageImpl<>(
                memberQuestion.getQuestions(),
                PageRequest.of(page - 1, 5),
                memberQuestion.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberQuestionPage));
    }

    @GetMapping("/{member-id}/answers")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberAnswer>> getMemberAnswer(
            @PathVariable("member-id") Long memberId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {

        MemberResponse.MemberAnswerPageResponse memberAnswerPageResponse
                = memberService.getMemberAnswer(memberId, page - 1, 5);

        Page<MemberResponse.MemberAnswer> memberAnswerPage
                = new PageImpl<>(
                memberAnswerPageResponse.getAnswers(),
                PageRequest.of(page - 1, 5),
                memberAnswerPageResponse.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberAnswerPage));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updateMember(@PathVariable("member-id") Long updateMemberId,
                                             @RequestBody @Valid MemberUpdateApiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updateMember(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("member-id") Long updateMemberId,
                                             @RequestBody @Valid MemberPasswordUpdateAPiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updatePassword(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable("member-id") Long memberId,
                                            @RequestParam MultipartFile file
                                             ) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "https://sixman-images-test.s3.ap-northeast-2.amazonaws.com/test.png");

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") Long deleteMemberId,
                                             @RequestBody @Valid MemberDeleteApiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.deleteMember(loginMemberId, request.toServiceRequest(deleteMemberId));

        return ResponseEntity.noContent().build();
    }
}
