package dmu.cheek.search.service;

import com.querydsl.core.BooleanBuilder;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.QMember;
import dmu.cheek.member.repository.MemberRepository;
import dmu.cheek.question.model.QQuestion;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.repository.QuestionRepository;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.search.model.SearchDto;
import dmu.cheek.story.model.QStory;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import dmu.cheek.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final StoryRepository storyRepository;
    private final S3Service s3Service;

    public SearchDto searchAll(String keyword, long loginMemberId, long categoryId) {
        List<SearchDto.Member> memberList = searchMember(keyword, loginMemberId);
        List<SearchDto.Story> storyList = searchStory(keyword, categoryId);
        List<SearchDto.Question> questionList = searchQuestion(keyword, categoryId);

        log.info("search all, keyword: {}", keyword);

        return SearchDto.builder()
                .memberDto(memberList)
                .storyDto(storyList)
                .questionDto(questionList)
                .build();
    }

    public List<SearchDto.Member> searchMember(String keyword, long loginMemberId) {
        List<Member> memberList = memberRepository.findByNicknameContaining(keyword);

        log.info("search in members, keyword: {}", keyword);

        return memberList.stream()
                .map(memberData -> {
                    long followerCnt = memberData.getToMemberConnectionList().size();
                    boolean isFollowing = memberData.getToMemberConnectionList()
                            .stream()
                            .anyMatch(memberConnection -> memberConnection.getFromMember().getMemberId() == loginMemberId);

                    return SearchDto.Member.builder()
                            .memberId(memberData.getMemberId())
                            .profilePicture(s3Service.getResourceUrl(memberData.getProfilePicture()))
                            .description(memberData.getDescription())
                            .information(memberData.getInformation())
                            .isFollowing(isFollowing)
                            .followerCnt(followerCnt)
                            .resultCnt(memberList.size())
                            .build();
                })
                .toList();

    }

    public List<SearchDto.Story> searchStory(String keyword, long categoryId) {
        List<Story> storyList = storyRepository.findListByCategoryAndText(categoryId, keyword);

        log.info("search in stories, keyword: {}", keyword);

        return storyList.stream()
                .map(storyData -> SearchDto.Story.builder()
                        .storyId(storyData.getStoryId())
                        .storyPicture(s3Service.getResourceUrl(storyData.getStoryPicture()))
                        .text(storyData.getText())
                        .resultCnt(storyList.size())
                        .build())
                .toList();
    }

    public List<SearchDto.Question> searchQuestion(String keyword, long categoryId) {
        List<Question> questionList = questionRepository.findListByCategoryAndText(categoryId, keyword);

        log.info("search in questions, keyword: {}", keyword);

        return questionList
                .stream()
                .map(questionData -> SearchDto.Question.builder()
                        .questionId(questionData.getQuestionId())
                        .content(questionData.getContent())
                        .resultCnt(questionList.size())
                        .build())
                .toList();

    }

}
