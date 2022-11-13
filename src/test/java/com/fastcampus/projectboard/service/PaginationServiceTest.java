package com.fastcampus.projectboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class) // webEnvironment 옵션의 디폴트는 MOCK (Mocking 할 웹환경을 넣어주는 것임.), NONE 을 쓰면 SpringBootTest 의 무게를 줄 일 수 있음. classes 옵션의 기본값은 스프링부트의 모든 빈스캔 대상들을 configuration 으로 불러들여 테스트함.
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다")
    @MethodSource // ParameterizedTest 할 때 메서드 주입 방식을 사용하겠다는 의미.
    @ParameterizedTest(name = "[{index} {0}, {1} => {2}]") // JUNIT 꺼임. ParameterizedTest 에다가 값을 연속적으로 주입해서 동일한 대상 메서드를 여러번 테스트하면 입출력을 볼 수 있음.
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber, int totalPages, List<Integer> expected){ // 입력 파라미터를 매개변수로 줄 수 있음. 마지막으로는 검증하고 싶은 값을 넣어주는 것임.
        // Given

        // When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    // 입력값을 넣어주는 MethodSource 는 static 으로 만드는 것임. 이름은 테스트 메서드의 이름과 동일하게 가져가도 되고, @MethodSource 어노테이셔션에 옵션을 주어서 할 수 있음.
    // MethodSource 는 Stream 타입을 받음
    // ParameterizedTest 를 하지 않았더라면 여러개의 테스트를 작성해서 돌려봤어야 하는데, ParameterizedTest 기법 덕분에 테스트해보고 싶은 값을 여러개를 동시에 넣고 나란히 볼 수 있음.
    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() {
        return Stream.of(
                arguments(0, 13, List.of(0,1,2,3,4)),
                arguments(1, 13, List.of(0,1,2,3,4)),
                arguments(2, 13, List.of(0,1,2,3,4)),
                arguments(3, 13, List.of(1,2,3,4,5)),
                arguments(4, 13, List.of(2,3,4,5,6)),
                arguments(5, 13, List.of(3,4,5,6,7)),
                arguments(6, 13, List.of(4,5,6,7,8)),
                arguments(10, 13, List.of(8,9,10,11,12)),
                arguments(11, 13, List.of(9,10,11,12)),
                arguments(12, 13, List.of(10,11,12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength(){
        // Given

        // When
        int barLength = sut.currentBarLength();

        // Then
        assertThat(barLength).isEqualTo(5);

    }
}