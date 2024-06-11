package dmu.cheek.question.service;

import dmu.cheek.question.model.QuestionDto;
import dmu.cheek.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void register(QuestionDto.Request questionRequestDto) {
//        questionRequestDto
    }

}
