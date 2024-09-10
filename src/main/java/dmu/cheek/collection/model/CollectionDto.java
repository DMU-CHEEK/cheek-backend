package dmu.cheek.collection.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionDto {

    private long collectionId;

    @Getter
    public static class Request {
        private long memberId;
        private long categoryId;
        private long storyId;
        private String folderName;
    }

    public static class ResponseList {
    }
}
