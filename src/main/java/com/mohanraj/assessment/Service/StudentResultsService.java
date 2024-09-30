package com.mohanraj.assessment.Service;

import com.mohanraj.assessment.Model.StudentResults;
import com.mohanraj.assessment.Repository.StudentResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Marks this class as a Spring service
public class StudentResultsService {

    private final StudentResultsRepository studentResultsRepository;

    @Autowired
    public StudentResultsService(StudentResultsRepository studentResultsRepository) {
        this.studentResultsRepository = studentResultsRepository;
    }

    public StudentResults saveStudentResults(StudentResults results) {
        return studentResultsRepository.save(results);
    }

    public List<StudentResults> getResultsByUsername(String username) {
        return studentResultsRepository.findByUsername(username);
    }

    public List<StudentResults> getResultsByEmail(String email) {
        return studentResultsRepository.findByEmail(email);
    }

    // Additional service methods can be added as needed
}
