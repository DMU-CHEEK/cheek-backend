package dmu.cheek.question.controller;

import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@Slf4j
@Tag(name = "Question API", description = "질문 기능")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping()
    @Operation(summary = "질문 등록", description = "질문 등록 API")
    public ResponseEntity<String> register(@RequestBody QuestionDto.RegisterReq registerReq) {
        questionService.register(registerReq);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "특정 유저의 질문 리스트 조회", description = "특정 유저의 질문 리스트 조회 API")
    public ResponseEntity<List> searchByMember(@PathVariable(name = "memberId") long memberId) {
        List<QuestionDto.ResponseList> questionList = questionService.searchByMember(memberId);

        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "질문 조회", description = "질문 단건 조회 API")
    public ResponseEntity<QuestionDto.ResponseOne> search(@PathVariable(name = "questionId") long questionId) {
        QuestionDto.ResponseOne question = questionService.search(questionId);

        return ResponseEntity.ok(question);
    }

    @PatchMapping()
    @Operation(summary = "질문 수정", description = "질문 수정 API")
    public ResponseEntity<String> update(@RequestBody QuestionDto.UpdateReq updateReq) {
        questionService.update(updateReq);

        return ResponseEntity.ok("ok");
    }
}
