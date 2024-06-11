package dmu.cheek.question.controller;

import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@Slf4j
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody QuestionDto.Request questionRequestDto) {
        questionService.register(questionRequestDto);
        return ResponseEntity.ok("ok");
    }
}
