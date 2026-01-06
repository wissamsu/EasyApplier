package com.Wissam.EasyApplier.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Model.JobAnswers;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.JobAnswersRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserJobAnswersService {

  private final JobAnswersRepository repository;
  private final ObjectMapper objectMapper;

  public Map<String, Object> getAnswers(User user) {
    return repository.findByUser(user)
        .map(row -> {
          try {
            // <-- fixed type safety warning here
            return objectMapper.readValue(
                row.getAnswersJson(),
                new TypeReference<Map<String, Object>>() {
                });
          } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
          }
        })
        .orElse(new HashMap<>());
  }

  public void saveAnswers(User user, Map<String, Object> answers) {
    try {
      String json = objectMapper.writeValueAsString(answers);

      JobAnswers row = repository.findByUser(user)
          .orElse(JobAnswers.builder()
              .user(user)
              .build());

      row.setAnswersJson(json);
      repository.save(row);

    } catch (Exception e) {
      throw new RuntimeException("Failed to save answers JSON", e);
    }
  }

  public void setField(User user, String fieldName, Object value) {
    Map<String, Object> answers = getAnswers(user);
    answers.put(fieldName, value); // add or update
    saveAnswers(user, answers);
  }

  public Object getField(User user, String fieldName) {
    Map<String, Object> answers = getAnswers(user);
    return answers.get(fieldName);
  }
}
