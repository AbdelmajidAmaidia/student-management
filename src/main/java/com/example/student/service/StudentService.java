package com.example.student.service;


import org.springframework.stereotype.Service;
import com.example.student.model.Student;


import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {


    public List<Student> getStudents(){

        List<Student> students=new ArrayList<>();

        students.add(
            new Student(1L,"Ahmed")
        );

        students.add(
            new Student(2L,"Ali")
        );


        return students;

    }

}
