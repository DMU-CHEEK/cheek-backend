package dmu.cheek.story.converter;

import dmu.cheek.s3.service.S3Service;
import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoryConverter {

    private final S3Service s3Service;

    public Story convertToEntity(StoryDto storyDto) {
        if (storyDto == null)
            return null;

        return Story.naturalFields()
                .storyId(storyDto.getStoryId())
                .storyPicture(s3Service.getResourceUrl(storyDto.getStoryPicture()))
                .build();
    }

    public StoryDto convertToDto(Story story) {
        if (story == null)
            return null;

        return StoryDto.builder()
                .storyId(story.getStoryId())
                .storyPicture(story.getStoryPicture())
                .build();
    }
}
