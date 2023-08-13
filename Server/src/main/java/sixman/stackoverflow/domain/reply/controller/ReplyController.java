package sixman.stackoverflow.domain.reply.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyUpdateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.ReplyService;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;


import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }


    @PostMapping("/answers/{answer-id}/replies")
    public ResponseEntity<Void> postReply(@PathVariable("answer-id")Long answerId,
                                                       @RequestBody ReplyCreateApiRequest request) {

        // Long createdReply = replyService.createreply(request); // 나중에 바로 반환할 때 보여주자

        replyService.createreply(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/answers/{answer-id}/replies/{reply-id}")
    public ResponseEntity<ApiSingleResponse<ReplyResponse>> getReply(@PathVariable("reply-id") Long replyId) {

        ReplyResponse replyResponse = getReplyResponse(replyId);

        return ResponseEntity.ok(ApiSingleResponse.ok(replyResponse));

    }

    @PatchMapping("/answers/{answer-id}/replies/{reply-id}")
    public ResponseEntity<Void> patchReply(@PathVariable("reply-id") Long replyId,
                                           @RequestBody ReplyUpdateApiRequest request) {
        replyService.updateReply(replyId, request.getContent());
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/answers/{answer-id}/replies/{reply-id}")
    public ResponseEntity<Void> deleteReply(@PathVariable("reply-id") Long replyId) {

        replyService.deleteReply(replyId);

        return ResponseEntity.noContent().build();
    }



    private ReplyResponse getReplyResponse(Long replyId) {
        Reply reply = replyService.findreply(replyId);

        ReplyResponse replyResponse = ReplyResponse.builder() // 초기화 과정
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .build();

        return replyResponse;

    }


}
