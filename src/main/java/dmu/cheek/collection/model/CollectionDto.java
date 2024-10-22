package dmu.cheek.collection.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    @Getter
    public static class Delete {
        private List<Long> collectionIdList;
    }
}
