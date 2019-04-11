package com.example.mainactivity;

import com.example.mainactivity.notActivities.ReadWriteTotalTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class ReadWriteTotalTimeTest { //instrumentation test

    @Test
    void setCorrectStringFormatTest()throws Exception{
        String expected;
        String actual;

        //todo: MOCK in order to test this class ???
        // TODO: 05.04.2019 how do i test side effects ????

        //minutes < 10     seconds < 10
        expected = "01:01";
        actual = ReadWriteTotalTime.setCorrectStringFormat();
        assertEquals(expected, actual);

        //minutes < 10     seconds > 10
        expected = "01:11";
        actual = ReadWriteTotalTime.setCorrectStringFormat(/*minutesLessThanTen, secondsMoreThanTen*/);
        assertEquals(expected, actual);

        //minutes > 10      seconds > 10
        expected = "11:11";
        actual = ReadWriteTotalTime.setCorrectStringFormat(/*minutesMoreThanTen, secondsMoreThanTen*/);
        assertEquals(expected, actual);

        //minutes > 10      seconds < 10
        expected = "11:01";
        actual = ReadWriteTotalTime.setCorrectStringFormat(/*minutesMoreThanTen, secondsLessThanTen*/);
        assertEquals(expected, actual);
    }


}
