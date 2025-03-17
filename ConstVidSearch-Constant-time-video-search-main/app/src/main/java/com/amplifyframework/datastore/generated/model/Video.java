package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Video type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Videos", type = Model.Type.USER, version = 1)
public final class Video implements Model {
  public static final QueryField ID = field("Video", "id");
  public static final QueryField UNIQUE_ID = field("Video", "unique_id");
  public static final QueryField INPUT_TEXT = field("Video", "inputText");
  public static final QueryField DESCRIPTION = field("Video", "description");
  public static final QueryField THUMBNAIL_URL = field("Video", "thumbnailUrl");
  public static final QueryField VIDEO_URL = field("Video", "videoUrl");
  public static final QueryField UPLOADING_TIME = field("Video", "uploading_time");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String unique_id;
  private final @ModelField(targetType="String") String inputText;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="String") String thumbnailUrl;
  private final @ModelField(targetType="String") String videoUrl;
  private final @ModelField(targetType="String") String uploading_time;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getUniqueId() {
      return unique_id;
  }
  
  public String getInputText() {
      return inputText;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getThumbnailUrl() {
      return thumbnailUrl;
  }
  
  public String getVideoUrl() {
      return videoUrl;
  }
  
  public String getUploadingTime() {
      return uploading_time;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Video(String id, String unique_id, String inputText, String description, String thumbnailUrl, String videoUrl, String uploading_time) {
    this.id = id;
    this.unique_id = unique_id;
    this.inputText = inputText;
    this.description = description;
    this.thumbnailUrl = thumbnailUrl;
    this.videoUrl = videoUrl;
    this.uploading_time = uploading_time;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Video video = (Video) obj;
      return ObjectsCompat.equals(getId(), video.getId()) &&
              ObjectsCompat.equals(getUniqueId(), video.getUniqueId()) &&
              ObjectsCompat.equals(getInputText(), video.getInputText()) &&
              ObjectsCompat.equals(getDescription(), video.getDescription()) &&
              ObjectsCompat.equals(getThumbnailUrl(), video.getThumbnailUrl()) &&
              ObjectsCompat.equals(getVideoUrl(), video.getVideoUrl()) &&
              ObjectsCompat.equals(getUploadingTime(), video.getUploadingTime()) &&
              ObjectsCompat.equals(getCreatedAt(), video.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), video.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUniqueId())
      .append(getInputText())
      .append(getDescription())
      .append(getThumbnailUrl())
      .append(getVideoUrl())
      .append(getUploadingTime())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Video {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("unique_id=" + String.valueOf(getUniqueId()) + ", ")
      .append("inputText=" + String.valueOf(getInputText()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("thumbnailUrl=" + String.valueOf(getThumbnailUrl()) + ", ")
      .append("videoUrl=" + String.valueOf(getVideoUrl()) + ", ")
      .append("uploading_time=" + String.valueOf(getUploadingTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UniqueIdStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Video justId(String id) {
    return new Video(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      unique_id,
      inputText,
      description,
      thumbnailUrl,
      videoUrl,
      uploading_time);
  }
  public interface UniqueIdStep {
    BuildStep uniqueId(String uniqueId);
  }
  

  public interface BuildStep {
    Video build();
    BuildStep id(String id);
    BuildStep inputText(String inputText);
    BuildStep description(String description);
    BuildStep thumbnailUrl(String thumbnailUrl);
    BuildStep videoUrl(String videoUrl);
    BuildStep uploadingTime(String uploadingTime);
  }
  

  public static class Builder implements UniqueIdStep, BuildStep {
    private String id;
    private String unique_id;
    private String inputText;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String uploading_time;
    public Builder() {
      
    }
    
    private Builder(String id, String unique_id, String inputText, String description, String thumbnailUrl, String videoUrl, String uploading_time) {
      this.id = id;
      this.unique_id = unique_id;
      this.inputText = inputText;
      this.description = description;
      this.thumbnailUrl = thumbnailUrl;
      this.videoUrl = videoUrl;
      this.uploading_time = uploading_time;
    }
    
    @Override
     public Video build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Video(
          id,
          unique_id,
          inputText,
          description,
          thumbnailUrl,
          videoUrl,
          uploading_time);
    }
    
    @Override
     public BuildStep uniqueId(String uniqueId) {
        Objects.requireNonNull(uniqueId);
        this.unique_id = uniqueId;
        return this;
    }
    
    @Override
     public BuildStep inputText(String inputText) {
        this.inputText = inputText;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }
    
    @Override
     public BuildStep videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
    
    @Override
     public BuildStep uploadingTime(String uploadingTime) {
        this.uploading_time = uploadingTime;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String uniqueId, String inputText, String description, String thumbnailUrl, String videoUrl, String uploadingTime) {
      super(id, unique_id, inputText, description, thumbnailUrl, videoUrl, uploading_time);
      Objects.requireNonNull(unique_id);
    }
    
    @Override
     public CopyOfBuilder uniqueId(String uniqueId) {
      return (CopyOfBuilder) super.uniqueId(uniqueId);
    }
    
    @Override
     public CopyOfBuilder inputText(String inputText) {
      return (CopyOfBuilder) super.inputText(inputText);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder thumbnailUrl(String thumbnailUrl) {
      return (CopyOfBuilder) super.thumbnailUrl(thumbnailUrl);
    }
    
    @Override
     public CopyOfBuilder videoUrl(String videoUrl) {
      return (CopyOfBuilder) super.videoUrl(videoUrl);
    }
    
    @Override
     public CopyOfBuilder uploadingTime(String uploadingTime) {
      return (CopyOfBuilder) super.uploadingTime(uploadingTime);
    }
  }
  

  public static class VideoIdentifier extends ModelIdentifier<Video> {
    private static final long serialVersionUID = 1L;
    public VideoIdentifier(String id) {
      super(id);
    }
  }
  
}
