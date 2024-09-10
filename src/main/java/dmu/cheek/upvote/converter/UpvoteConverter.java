package dmu.cheek.upvote.converter;

import dmu.cheek.upvote.model.Upvote;
import dmu.cheek.upvote.model.UpvoteDto;
import org.springframework.stereotype.Component;

@Component
public class UpvoteConverter {

    public UpvoteDto convertToDto(Upvote upvote) {
        if (upvote == null)
            return null;

        return UpvoteDto.builder()
                .upvoteId(upvote.getUpvoteId())
                .isUpvoted(upvote.isUpvoted())
                .build();
    }

    public Upvote convertToEntity(UpvoteDto upvoteDto) {
        if (upvoteDto == null)
            return null;

        return Upvote.naturalFields()
                .upvoteId(upvoteDto.getUpvoteId())
                .isUpvoted(upvoteDto.isUpvoted())
                .build();
    }
}
