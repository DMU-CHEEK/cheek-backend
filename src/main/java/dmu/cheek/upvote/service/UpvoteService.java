package dmu.cheek.upvote.service;

import dmu.cheek.upvote.repository.UpvoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpvoteService {

    private final UpvoteRepository upvoteRepository;
}
