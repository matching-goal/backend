package matchingGoal.matchingGoal.matching.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
//  private Long memberImg;
  private String title;
  private String content;
//  private Long img;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private Integer viewCount;
  private StatusType status;
//  private Integer requestCount;
  private String region;
  private String stadium;
  private LocalDate date;
  private LocalTime time;

  public static BoardResponseDto convertToDto(MatchingBoard matchingBoard) {
    return BoardResponseDto.builder()
        .id(matchingBoard.getId())
        .memberId(matchingBoard.getMember().getId())
        .nickname(matchingBoard.getMember().getNickname())
        .title(matchingBoard.getTitle())
        .content(matchingBoard.getContent())
        .createdDate(matchingBoard.getCreatedDate())
        .modifiedDate(matchingBoard.getModifiedDate())
        .viewCount(matchingBoard.getViewCount())
        .status(matchingBoard.getStatus())
//        .requestCount()
        .region(matchingBoard.getRegion())
        .stadium(matchingBoard.getGame().getStadiumName())
        .date(matchingBoard.getGame().getDate())
        .time(matchingBoard.getGame().getTime())
        .build();
  }
}
