package com.example.student;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.student.service.StudentService;



public class StudentServiceTest {


@Test
void testStudents(){


StudentService service =
        new StudentService();


assertEquals(
        2,
        service.getStudents().size()
);


}


}
