package dmu.cheek.collection.model;

import lombok.Getter;

@Getter
public class CollectionDto {

    private long collectionId;

    private String thumbnailPicture;

    @Getter
    public static class Request {
        private long memberId;
        private long categoryId;
        private long storyId;
    }
}
