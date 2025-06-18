package com.example.absoluteweb.forum.services;

import com.example.absoluteweb.forum.DTO.TopicDTO;
import com.example.absoluteweb.forum.entity.Topic;
import com.example.absoluteweb.forum.entity.User;
import com.example.absoluteweb.forum.exceptions.TopicException;
import com.example.absoluteweb.forum.repository.TopicRep;
import com.example.absoluteweb.forum.repository.UserRep;
import com.example.absoluteweb.forum.principals.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TopicService {
    private static final Set<String> ALLOWED_SUBSECTIONS = Set.of(
            "dev-news",
            "server-info",
            "bugs-fixes",
            "suggestions",
            "news",
            "discussions",
            "speaking"
    );

    private final TopicRep topicRepository;
    private final UserRep userRepository;

    @Autowired
    public TopicService(TopicRep topicRepository, UserRep userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createTopic(TopicDTO topicDTO, UserPrincipal principal) throws TopicException {
        User user = userRepository.findById(principal.getId()).orElse(null);
        if (user == null){
            throw new TopicException("Автор теми не знайдений");
        }
        if(topicDTO.getTitle().isBlank()){
            throw new TopicException("Назва теми має бути заповненою");
        }
        if(topicDTO.getMessage().isBlank()){
            throw new TopicException("Текст теми має бути заповненим");
        }
        if(topicDTO.getTitle().length()>250){
            throw new TopicException("Назва теми не може перевищувати 250 символів");
        }
        if(topicDTO.getMessage().length()>5000){
            throw new TopicException("Текст теми не може перевищувати 5000 символів");
        }
        if(topicDTO.getSubSection().isBlank()){
            throw new TopicException("Підрозділ форуму не може бути порожнім");
        }
        if((topicDTO.getSubSection().equals("dev-news")||topicDTO.getSubSection().equals("server-info"))
        && !user.getRole().equals("ADMIN")){
            throw new TopicException("Тему в цьому підрозділі може створювати тільки адміністратор");
        }
        if (!ALLOWED_SUBSECTIONS.contains(topicDTO.getSubSection())) {
            throw new TopicException("Обрана підсекція не існує");
        }
        Topic topic = new Topic();
        topic.setCreatedBy(user);
        topic.setCreationDate(LocalDateTime.now());
        topic.setSubSection(topicDTO.getSubSection());
        topic.setTitle(topicDTO.getTitle());
        topic.setMessage(topicDTO.getMessage());
        topic.setStatus("ACTIVE");
        try {
            topicRepository.save(topic);
            TopicDTO responseDTO = new TopicDTO();
            responseDTO.setId(topic.getId());
            responseDTO.setSubSection(topic.getSubSection());
            responseDTO.setTitle(topic.getTitle());
            responseDTO.setMessage(topic.getMessage());
            responseDTO.setStatus(topic.getStatus());
            responseDTO.setCreatedBy(user.getId());
            responseDTO.setCreationDate(topic.getCreationDate());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            throw new TopicException("Помилка збереження теми : " + e.getMessage());
        }
    }

    public ResponseEntity<List<TopicDTO>> getTopicsBySection(String subSection) {
        List<Topic> topics = topicRepository.findBySubSection(subSection);
        List<TopicDTO> dtos = new ArrayList<>();
        for (Topic topic : topics) {
            TopicDTO dto = new TopicDTO();
            dto.setId(topic.getId());
            dto.setStatus(topic.getStatus());
            dto.setCreatedBy(topic.getCreatedBy().getId());
            dto.setCreationDate(topic.getCreationDate());
            dto.setSubSection(topic.getSubSection());
            dto.setTitle(topic.getTitle());
            dto.setMessage(topic.getMessage());
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<TopicDTO> getTopicById(Long id) {
       Topic topic = topicRepository.findById(id).orElse(null);
       if(topic == null) throw new TopicException("Topic not found");
       TopicDTO dto = new TopicDTO();
       dto.setId(topic.getId());
       dto.setStatus(topic.getStatus());
       dto.setCreatedBy(topic.getCreatedBy().getId());
       dto.setCreationDate(topic.getCreationDate());
       dto.setSubSection(topic.getSubSection());
       dto.setTitle(topic.getTitle());
       dto.setMessage(topic.getMessage());
       return ResponseEntity.ok(dto);
    }
    public ResponseEntity<?> updateTopic(Long id, TopicDTO dto, UserPrincipal principal) throws TopicException {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic == null){
            throw new TopicException("Тему не знайдено");
        }
        if (!topic.getCreatedBy().getId().equals(principal.getId())) {
            throw new TopicException("Ви не можете редагувати чужу тему");
        }
        if(dto.getTitle().isBlank()){
            throw new TopicException("Назва теми має бути заповненою");
        }
        if(dto.getMessage().isBlank()){
            throw new TopicException("Текст теми має бути заповненим");
        }
        if(dto.getTitle().length()>250){
            throw new TopicException("Назва теми не може перевищувати 250 символів");
        }
        if(dto.getMessage().length()>5000){
            throw new TopicException("Текст теми не може перевищувати 5000 символів");
        }
        topic.setTitle(dto.getTitle());
        topic.setMessage(dto.getMessage());
        topic.setSubSection(dto.getSubSection());
        topicRepository.save(topic);

        TopicDTO updatedDto = new TopicDTO();
        updatedDto.setId(topic.getId());
        updatedDto.setStatus(topic.getStatus());
        updatedDto.setCreatedBy(topic.getCreatedBy().getId());
        updatedDto.setCreationDate(topic.getCreationDate());
        updatedDto.setSubSection(topic.getSubSection());
        updatedDto.setTitle(topic.getTitle());
        updatedDto.setMessage(topic.getMessage());

        return ResponseEntity.ok(updatedDto);
    }

    public void deleteTopic(Long id, UserPrincipal principal) throws TopicException {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic == null){
            throw new TopicException("Тему не знайдено");
        }
        if (!topic.getCreatedBy().getId().equals(principal.getId())) {
            throw new TopicException("Ви не можете видалити чужу тему");
        }
        topicRepository.delete(topic);
    }

}
