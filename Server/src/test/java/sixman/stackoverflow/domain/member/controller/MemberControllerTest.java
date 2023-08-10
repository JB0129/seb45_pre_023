package sixman.stackoverflow.domain.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberDeleteApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberPasswordUpdateAPiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberUpdateApiRequest;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.global.response.PageInfo;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원가입 API")
    void signup() throws Exception {
        //given
        MemberCreateApiRequest request = MemberCreateApiRequest.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234abcd!")
                .build();

        Long memberId = 1L;

        given(memberService.signup(any(MemberCreateServiceRequest.class))).willReturn(memberId);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON));

        //then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + memberId));

        //restdocs
        actions.andDo(documentHandler.document(
                requestFields(
                        fieldWithPath("email").description("회원 email"),
                        fieldWithPath("nickname").description("회원 nickname"),
                        fieldWithPath("password").description("회원 password")
                ),
                responseHeaders(
                        headerWithName("Location").description("생성된 회원의 URI")
                )
        ));
    }

    @Test
    @DisplayName("회원 조회 API")
    void getMembers() throws Exception {
        //given
        Long memberId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/members/{memberId}", memberId)
                        .accept(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        //restdocs
        actions.andDo(documentHandler.document(
                pathParameters(
                        parameterWithName("memberId").description("조회할 회원의 ID")
                ),
                responseFields(
                        fieldWithPath("data.memberId").description("회원 ID"),
                        fieldWithPath("data.email").description("회원 email"),
                        fieldWithPath("data.nickname").description("회원 nickname"),
                        fieldWithPath("data.image").description("회원 이미지 url"),
                        fieldWithPath("data.myIntro").description("회원 소개글"),
                        fieldWithPath("data.authority").description("회원 권한"),
                        fieldWithPath("data.question").description("회원 질문"),
                        fieldWithPath("data.question.questions[]").description("회원이 작성한 질문 리스트"),
                        fieldWithPath("data.question.questions[].questionId").description("작성한 질문 ID"),
                        fieldWithPath("data.question.questions[].title").description("작성한 질문 제목"),
                        fieldWithPath("data.question.questions[].views").description("작성한 질문 조회수"),
                        fieldWithPath("data.question.questions[].recommend").description("작성한 질문 추천수"),
                        fieldWithPath("data.question.questions[].createdDate").description("작성한 질문 생성일"),
                        fieldWithPath("data.question.questions[].updatedDate").description("작성한 질문 수정일"),
                        fieldWithPath("data.question.pageInfo").description("작성한 질문 페이징 정보"),
                        fieldWithPath("data.question.pageInfo.page").description("질문 현재 페이지"),
                        fieldWithPath("data.question.pageInfo.size").description("질문 페이지 사이즈"),
                        fieldWithPath("data.question.pageInfo.totalPage").description("질문 전체 페이지 수"),
                        fieldWithPath("data.question.pageInfo.totalSize").description("질문 전체 개수"),
                        fieldWithPath("data.question.pageInfo.first").description("질문 첫 페이지 여부"),
                        fieldWithPath("data.question.pageInfo.last").description("질문 마지막 페이지 여부"),
                        fieldWithPath("data.question.pageInfo.hasNext").description("다음 페이지가 있는지"),
                        fieldWithPath("data.question.pageInfo.hasPrevious").description("이전 페이지가 있는지"),
                        fieldWithPath("data.answer").description("회원 답변"),
                        fieldWithPath("data.answer.answers[]").description("회원이 작성한 답변 리스트"),
                        fieldWithPath("data.answer.answers[].answerId").description("작성한 답변 ID"),
                        fieldWithPath("data.answer.answers[].questionId").description("작성한 답변 질문 ID"),
                        fieldWithPath("data.answer.answers[].questionTitle").description("작성한 답변 질문 제목"),
                        fieldWithPath("data.answer.answers[].content").description("작성한 답변 내용"),
                        fieldWithPath("data.answer.answers[].recommend").description("작성한 답변 추천 수"),
                        fieldWithPath("data.answer.answers[].createdDate").description("작성한 답변 생성일"),
                        fieldWithPath("data.answer.answers[].updatedDate").description("작성한 답변 수정일"),
                        fieldWithPath("data.answer.pageInfo").description("작성한 답변 페이징 정보"),
                        fieldWithPath("data.answer.pageInfo.page").description("답변 현재 페이지"),
                        fieldWithPath("data.answer.pageInfo.size").description("답변 페이지 사이즈"),
                        fieldWithPath("data.answer.pageInfo.totalPage").description("답변 전체 페이지 수"),
                        fieldWithPath("data.answer.pageInfo.totalSize").description("답변 전체 개수"),
                        fieldWithPath("data.answer.pageInfo.first").description("답변 첫 페이지 여부"),
                        fieldWithPath("data.answer.pageInfo.last").description("답변 마지막 페이지 여부"),
                        fieldWithPath("data.answer.pageInfo.hasNext").description("다음 페이지가 있는지"),
                        fieldWithPath("data.answer.pageInfo.hasPrevious").description("이전 페이지가 있는지"),
                        fieldWithPath("data.tags[]").description("회원이 작성한 질문의 태그 목록"),
                        fieldWithPath("data.tags[].tagId").description("태그 ID"),
                        fieldWithPath("data.tags[].tagName").description("태그 이름"),
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("status").description("응답 상태"),
                        fieldWithPath("message").description("응답 메시지")
                )
        ));
    }

    @Test
    @DisplayName("회원 수정 API")
    void updateMember() throws Exception {
        //given
        Long memberId = 1L;
        MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                .nickname("update nickname")
                .myIntro("update myIntro")
                .build();

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members/{memberId}", memberId)
                        .header("Authorization", "Bearer abc.12a.333")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restdocs
        actions.andDo(documentHandler.document(
                pathParameters(
                        parameterWithName("memberId").description("수정할 회원의 ID")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("JWT Token")
                ),
                requestFields(
                        fieldWithPath("nickname").description("수정할 회원의 nickname"),
                        fieldWithPath("myIntro").description("수정할 회원의 소개글")
                )
        ));
    }

    @Test
    @DisplayName("회원 수정 API - 이미지 수정")
    void updateImage() throws Exception {
        //given
        Long memberId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", MediaType.TEXT_PLAIN_VALUE, "file content".getBytes());

        //when
        ResultActions actions = mockMvc.perform(
                multipart("http://localhost:8080/members/{member-id}/image", memberId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer abc.12a.333"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "bucket url"));

        //restdocs
        actions.andDo(documentHandler.document(
                pathParameters(
                        parameterWithName("member-id").description("이미지를 수정할 회원의 ID")
                ),
                requestHeaders(
                        headerWithName("Content-Type").description(" multipart/form-data"),
                        headerWithName("Authorization").description("JWT Token")
                ),
                requestParts(
                        partWithName("file").description("수정할 이미지 파일")
                )
        ));
    }

    @Test
    @DisplayName("회원 수정 API - 비밀번호 수정")
    void updatePassword() throws Exception {
        //given
        Long memberId = 1L;
        MemberPasswordUpdateAPiRequest request = MemberPasswordUpdateAPiRequest.builder()
                .password("prev password")
                .newPassword("new password")
                .build();

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members/{member-id}/password", memberId)
                        .header("Authorization", "Bearer abc.12a.333")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restdocs
        actions.andDo(documentHandler.document(
                pathParameters(
                        parameterWithName("member-id").description("비밀번호를 수정할 회원의 ID")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("JWT Token")
                ),
                requestFields(
                        fieldWithPath("password").description("이전 비밀번호"),
                        fieldWithPath("newPassword").description("새로운 비밀번호")
                )
        ));
    }

    @Test
    @DisplayName("회원 탈퇴 API")
    void deleteMember() throws Exception {
        //given
        Long memberId = 1L;
        MemberDeleteApiRequest request = MemberDeleteApiRequest.builder()
                .password("1234abcd!!")
                .build();

        //when
        ResultActions actions = mockMvc.perform(
                delete("/members/{member-id}", memberId)
                        .header("Authorization", "Bearer abc.12a.333")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restdocs
        actions.andDo(documentHandler.document(
                pathParameters(
                        parameterWithName("member-id").description("탈퇴할 회원의 ID")
                ),
                requestHeaders(
                        headerWithName("Authorization").description("JWT Token")
                ),
                requestFields(
                        fieldWithPath("password").description("회원의 비밀번호")
                )
        ));
    }

    private MemberResponse getMemberResponse(Long memberId) {
        List<MemberResponse.MemberQuestion> questions = new ArrayList<>();
        List<MemberResponse.MemberAnswer> answers = new ArrayList<>();

        for(long i = 1; i <= 5; i++){

            MemberResponse.MemberQuestion question = MemberResponse.MemberQuestion.builder()
                    .questionId(i)
                    .title("title " + i)
                    .views(100)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            questions.add(question);

            MemberResponse.MemberAnswer answer = MemberResponse.MemberAnswer.builder()
                    .answerId(i)
                    .questionTitle("title " + i)
                    .content("content " + i)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            answers.add(answer);
        }

        MemberResponse.MemberQuestionPageResponse question = MemberResponse.MemberQuestionPageResponse.builder()
                .questions(questions)
                .pageInfo(PageInfo.of(new PageImpl(questions, PageRequest.of(1, 10), 100)))
                .build();

        MemberResponse.MemberAnswerPageResponse answer = MemberResponse.MemberAnswerPageResponse.builder()
                .answers(answers)
                .pageInfo(PageInfo.of(new PageImpl(answers, PageRequest.of(1, 10), 100)))
                .build();

        List<MemberResponse.MemberTag> tags = new ArrayList<>();

        for(long i = 1; i <= 5; i++){
            MemberResponse.MemberTag tag = MemberResponse.MemberTag.builder()
                    .tagId(i)
                    .tagName("tag " + i)
                    .build();

            tags.add(tag);
        }

        MemberResponse memberResponse = MemberResponse.builder()
                .memberId(memberId)
                .email("test@test.com")
                .nickname("nickname")
                .image("test_url.com")
                .myIntro("hi! im test")
                .authority(Authority.ROLE_USER)
                .question(question)
                .answer(answer)
                .tags(tags)
                .build();
        return memberResponse;
    }

}