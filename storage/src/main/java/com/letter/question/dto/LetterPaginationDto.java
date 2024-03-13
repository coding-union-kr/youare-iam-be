package com.letter.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "대화방 페이징 관련 데이터 DTO")
@Getter
@Setter
public class LetterPaginationDto {

    @Schema(description = "대화방 데이터 리스트")
    private List<LetterDetailResponse> letters;

    @Schema(description = "다음 아이템의 ID")
    private long nextCursor;

    @Schema(description = "사용자의 ID")
    private String myId;

    public LetterPaginationDto() {
        this.nextCursor = -1;
    }
}
