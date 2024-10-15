package dmu.cheek.search.service;

import dmu.cheek.global.config.security.service.MemberDetails;
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

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, String> redisTemplate;

    public SearchDto searchAll(String keyword, long loginMemberId, long categoryId) {
        List<SearchDto.Member> memberList = searchMember(keyword, loginMemberId);
        List<SearchDto.Story> storyList = searchStory(keyword, categoryId);
        List<SearchDto.Question> questionList = searchQuestion(keyword, categoryId);

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

    public List<SearchDto.Member> searchMember(String keyword, long loginMemberId) {
        List<Member> memberList = memberRepository.findByNicknameContaining(keyword);

        log.info("search in members, keyword: {}", keyword);

        return memberList.stream()
                .map(memberData -> {
                    int followerCnt = memberData.getToMemberConnectionList().size();
                    boolean isFollowing = memberData.getToMemberConnectionList()
                            .stream()
                            .anyMatch(memberConnection -> memberConnection.getFromMember().getMemberId() == loginMemberId);

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

    public List<SearchDto.Story> searchStory(String keyword, long categoryId) {
        List<Story> storyList = storyRepository.findByCategoryIdAndText(categoryId, keyword);

        log.info("search in stories, keyword: {}", keyword);

        return storyList.stream()
                .map(storyData -> SearchDto.Story.builder()
                        .storyId(storyData.getStoryId())
                        .storyPicture(s3Service.getResourceUrl(storyData.getStoryPicture()))
                        .text(storyData.getText())
                        .modifiedAt(storyData.getModifiedAt())
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
                        .modifiedAt(questionData.getModifiedAt())
                        .categoryId(questionData.getCategory().getCategoryId())
                        .memberDto(MemberDto.Concise.builder()
                                .memberId(questionData.getMember().getMemberId())
                                .nickname(questionData.getMember().getNickname())
                                .profilePicture(questionData.getMember().getProfilePicture())
                                .build()
                        )
                        .build())
                .toList();

    }

    public void addRecentSearch(long memberId, String keyword) {
        String key = "member:" + memberId + ":recent-searches";

        redisTemplate.opsForList().remove(key, 0, keyword); //중복값 삭제
        redisTemplate.opsForList().leftPush(key, keyword);
        redisTemplate.opsForList().trim(key, 0, 9); //최대 10개 검색어

        log.info("register recent-search, memberId: {}, keyword: {}", memberId, keyword);
    }

    public SearchDto.Keyword getRecentSearches(long memberId) {
        String key = "member:" + memberId + ":recent-searches";
        List<String> recentSearches = redisTemplate.opsForList().range(key, 0, -1); //최신순 조회

        log.info("get recent-searches, memberId: {}", memberId);

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
