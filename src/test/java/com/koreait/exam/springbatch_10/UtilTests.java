package com.koreait.exam.springbatch_10;
import com.koreait.exam.springbatch_10.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class UtilTests {
    @Test
    @DisplayName("특정 연 월의 마지막 날을 계산")
    void t1(){
        int endDayOf_2023_02 = Util.date.getEndDayOf(2023,02);
        assertThat(endDayOf_2023_02).isEqualTo(28);
        int endDayOf_2024_02 = Util.date.getEndDayOf(2024,02);
        assertThat(endDayOf_2024_02).isEqualTo(29);
        int endDayOf_2024_02x = Util.date.getEndDayOf(2024,02);
        assertThat(endDayOf_2024_02x).isEqualTo(28);
        int endDayOf_2024_04 = Util.date.getEndDayOf(2024,04);
        assertThat(endDayOf_2024_04).isEqualTo(30);
    }
}