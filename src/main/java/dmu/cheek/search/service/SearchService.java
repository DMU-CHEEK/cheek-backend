package dmu.cheek.search.service;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.resolver.memberInfo.MemberInfoDto;
import dmu.cheek.member.model.Member;
import dmu.cheek.member.model.MemberDto;
import dmu.cheek.member.repository.MemberRepository;
import dmu.cheek.question.model.Question;
import dmu.cheek.question.repository.QuestionRepository;
import dmu.cheek.s3.service.S3Service;
import dmu.cheek.search.model.SearchDto;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final QuestionRepository questionRepository;
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    public SearchDto searchAll(String keyword, MemberInfoDto memberInfoDto, long categoryId) {
        Member loginMember = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<SearchDto.Member> memberList = searchMember(keyword, loginMember);
        List<SearchDto.Story> storyList = searchStory(keyword, categoryId, loginMember);
        List<SearchDto.Question> questionList = searchQuestion(keyword, categoryId, loginMember);

        log.info("search all, keyword: {}", keyword);

        return SearchDto.builder()
                .memberDto(memberList)
                .storyDto(storyList)
                .questionDto(questionList)
                .memberResCnt(memberList.size())
                .storyResCnt(storyList.size())
                .questionResCnt(questionList.size())
                .build();
    }

    public List<SearchDto.Member> searchMember(String keyword, Member loginMember) {
        List<Member> memberList = memberRepository.findByNicknameContaining(keyword);

        log.info("search in members, keyword: {}", keyword);

        return memberList.stream()
                .map(memberData -> {
                    int followerCnt = memberData.getToMemberConnectionList().size();
                    boolean isFollowing = memberData.getToMemberConnectionList()
                            .stream()
                            .anyMatch(memberConnection -> memberConnection.getFromMember().getMemberId() == loginMember.getMemberId());

                    return SearchDto.Member.builder()
                            .memberId(memberData.getMemberId())
                            .nickname(memberData.getNickname())
                            .profilePicture(s3Service.getResourceUrl(memberData.getProfilePicture()))
                            .description(memberData.getDescription())
                            .information(memberData.getInformation())
                            .isFollowing(isFollowing)
                            .followerCnt(followerCnt)
                            .build();
                })
                .toList();

    }

    public List<SearchDto.Story> searchStory(String keyword, long categoryId, Member loginMember) {
        List<Story> storyList = storyRepository.findByCategoryIdAndText(categoryId, keyword);

        log.info("search in stories, keyword: {}", keyword);

        return storyList.stream()
                .filter(story ->
                    loginMember.getFromBlockList().stream()
                            .noneMatch(block -> block.getToMember().getMemberId() == story.getMember().getMemberId())
                )
                .map(storyData -> SearchDto.Story.builder()
                        .storyId(storyData.getStoryId())
                        .storyPicture(s3Service.getResourceUrl(storyData.getStoryPicture()))
                        .text(storyData.getText())
                        .modifiedAt(storyData.getModifiedAt())
                        .build())
                .toList();
    }

    public List<SearchDto.Question> searchQuestion(String keyword, long categoryId, Member loginMember) {
        List<Question> questionList = questionRepository.findListByCategoryAndText(categoryId, keyword);

        log.info("search in questions, keyword: {}", keyword);
        return questionList.stream()
                .filter(question ->
                        loginMember.getFromBlockList().stream()
                                .noneMatch(block -> block.getToMember().getMemberId() == question.getMember().getMemberId())
                )
                .map(questionData -> SearchDto.Question.builder()
                        .questionId(questionData.getQuestionId())
                        .content(questionData.getContent())
                        .modifiedAt(questionData.getModifiedAt())
                        .categoryId(questionData.getCategory().getCategoryId())
                        .storyCnt(questionData.getStoryList().size())
                        .memberDto(MemberDto.Concise.builder()
                                .memberId(questionData.getMember().getMemberId())
                                .nickname(questionData.getMember().getNickname())
                                .profilePicture(s3Service.getResourceUrl(questionData.getMember().getProfilePicture()))
                                .build()
                        )
                        .build())
                .toList();
    }

    public void addRecentSearch(MemberInfoDto memberInfoDto, String keyword) {
        String key = "member:" + memberInfoDto.getMemberId() + ":recent-searches";

        redisTemplate.opsForList().remove(key, 0, keyword); //중복값 삭제
        redisTemplate.opsForList().leftPush(key, keyword);
        redisTemplate.opsForList().trim(key, 0, 9); //최대 10개 검색어

        log.info("register recent-search, memberId: {}, keyword: {}", memberInfoDto.getMemberId(), keyword);
    }

    public SearchDto.Keyword getRecentSearches(MemberInfoDto memberInfoDto) {
        String key = "member:" + memberInfoDto.getMemberId() + ":recent-searches";
        List<String> recentSearches = redisTemplate.opsForList().range(key, 0, -1); //최신순 조회

        log.info("get recent-searches, memberId: {}", memberInfoDto.getMemberId());

        return SearchDto.Keyword
                .builder()
                .keyword(recentSearches)
                .build();
    }

    public void clearAllRecentSearches() {
        Set<String> keys = redisTemplate.keys("member:*:recent-searches");

        if (keys != null && !keys.isEmpty())
            redisTemplate.delete(keys);
    }

    public void saveTrendingKeyword(String keyword) {
        redisTemplate.opsForZSet().incrementScore("trending-keyword", keyword, 1);
    }

    public SearchDto.Keyword getTrendingKeywordsInWeek() {
        Set<ZSetOperations.TypedTuple<String>> keywordList = redisTemplate.opsForZSet().rangeWithScores("trending-keyword", 0, -1);

        log.info("get top trending-keywords in week");

        if (keywordList != null) {
            return SearchDto.Keyword.builder()
                    .keyword(
                            keywordList.stream()
                                    .sorted((keyword1, keyword2) -> Double.compare(keyword2.getScore(), keyword1.getScore())) // 점수 기준 내림차순 정렬
                                    .limit(5) //상위 5개
                                    .map(ZSetOperations.TypedTuple::getValue) //키워드 추출
                                    .toList()
                    ).build();
        } else {
            return SearchDto.Keyword.builder()
                    .keyword(Collections.emptyList())
                    .build();
        }
    }

    public void clearTrendingKeywords() {
        redisTemplate.delete("trending-keyword");
    }
}
