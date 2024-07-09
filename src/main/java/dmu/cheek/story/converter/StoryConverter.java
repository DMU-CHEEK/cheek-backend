package dmu.cheek.story.converter;

import dmu.cheek.story.model.Story;
import dmu.cheek.story.model.StoryDto;
import org.springframework.stereotype.Component;

@Component
public class StoryConverter {

    public Story convertToEntity(StoryDto storyDto) {
        if (storyDto == null)
            return null;

        return Story.naturalFields()
                .storyId(storyDto.getStoryId())
                .storyPicture(storyDto.getStoryPicture())
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
