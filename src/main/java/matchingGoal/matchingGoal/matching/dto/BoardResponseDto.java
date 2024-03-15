package matchingGoal.matchingGoal.matching.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.matching.domain.StatusType;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {

  private Long id;
  private Long memberId;
  private String nickname;
  private Long memberImg;
  private String title;
  private String content;
  private List<Long> imgList;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private Integer viewCount;
  private StatusType status;
  private Integer requestCount;
  private String region;
  private String stadium;
  private LocalDate date;
  private LocalTime time;

  public static BoardResponseDto of(MatchingBoard matchingBoard) {
    return BoardResponseDto.builder()
        .id(matchingBoard.getId())
        .memberId(matchingBoard.getMember().getId())
        .memberImg(matchingBoard.getMember().getImageId())
        .nickname(matchingBoard.getMember().getNickname())
        .title(matchingBoard.getTitle())
        .content(matchingBoard.getContent())
        .imgList(matchingBoard.getImgList().stream()
            .map(Image::getId)
            .collect(Collectors.toList()))
        .createdDate(matchingBoard.getCreatedDate())
        .modifiedDate(matchingBoard.getModifiedDate())
        .viewCount(matchingBoard.getViewCount())
        .status(matchingBoard.getStatus())
        .region(matchingBoard.getRegion())
        .stadium(matchingBoard.getGame().getStadiumName())
        .date(matchingBoard.getGame().getDate())
        .time(matchingBoard.getGame().getTime())
        .build();
  }

  public void setRequestCount(Integer requestCount) {
    this.requestCount = requestCount;
  }
}
