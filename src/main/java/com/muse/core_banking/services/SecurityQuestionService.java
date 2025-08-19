package com.muse.core_banking.services;


import com.muse.core_banking.dto.securityQuestion.AddSecurityQuestionDto;
import com.muse.core_banking.dto.securityQuestion.UpdateSecurityQuestionDto;
import com.muse.core_banking.entities.SecurityQuestion;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.repositories.SecurityQuestionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class SecurityQuestionService {

    private final SecurityQuestionRepository securityQuestionRepository;
    private final CustomerRepository customerRepository;


    public void addSecurityQuestion(List<AddSecurityQuestionDto> request) throws BadRequestException {
        if (request == null || request.isEmpty()) {
            throw new BadRequestException("Request list cannot be empty");
        }

        var customerId = request.getFirst().customerId();
        findCustomer(Long.parseLong(customerId));

        var securityQuestions = request.stream().map(( s) ->
                SecurityQuestion.builder()
                        .Question(s.question())
                        .Answer(s.answer())
                        .customerId(Long.parseLong(s.customerId()))
                        .build()
        ).toList();
        securityQuestionRepository.saveAll(securityQuestions);
    }

    public void updateSecurityQuestion(List<UpdateSecurityQuestionDto> request) throws BadRequestException {
        if (request == null || request.isEmpty()) {
            throw new BadRequestException("Request list cannot be empty");
        }
        var customerId = Long.parseLong(request.getFirst().customerId());
        findCustomer(customerId);

        Map<Long, String> updates = request.stream()
                .collect(Collectors.toMap(UpdateSecurityQuestionDto::id, UpdateSecurityQuestionDto::answer));

        List<SecurityQuestion> questions = securityQuestionRepository.findAllById(updates.keySet());

        for (SecurityQuestion question : questions) {
            if (!Objects.equals(question.getCustomerId(), customerId)) {
                throw new BadRequestException(
                        "Security question with ID " + question.getId() + " does not belong to customer " + customerId
                );
            }
        }

        questions.forEach(q -> {
            String newAnswer = updates.get(q.getId());
            if (newAnswer != null) {
                q.setAnswer(newAnswer);
            }
        });

        securityQuestionRepository.saveAll(questions);
    }

    private void findCustomer(Long id) throws BadRequestException {
        customerRepository.findById(id).
                orElseThrow(() -> new BadRequestException("Customer not found"));
    }
}


















