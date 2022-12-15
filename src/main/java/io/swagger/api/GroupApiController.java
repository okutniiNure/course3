package io.swagger.api;

import io.swagger.exception.ApiException;
import io.swagger.model.AccessGroup;
import io.swagger.repository.AccessGroupRepository;
import io.swagger.service.auth.HeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GroupApiController implements GroupApi{
    private final AccessGroupRepository accessGroupRepository;

    private final HeaderService headerService;

    @Override
    public AccessGroup createGroup(String groupName, Set<String> usernames, HttpServletResponse response) {
        String owner = headerService.getUsername();
        if(accessGroupRepository.existsById(groupName)){
            throw new ApiException(HttpStatus.CONFLICT.value(), "Already exists");
        }
        AccessGroup accessGroup = new AccessGroup() ;
        accessGroup.setUsernames(usernames);
        accessGroup.setOwner(owner);
        accessGroup.setName(groupName);
        return accessGroupRepository.save(accessGroup);
    }

    @Override
    public AccessGroup getGroup(String groupName, HttpServletResponse response) {
        String username = headerService.getUsername();
        AccessGroup accessGroup = accessGroupRepository.findById(groupName).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "No group with such name: " + groupName));

        if(!accessGroup.getUsernames().contains(username) && !accessGroup.getOwner().equals(username)){
            throw new ApiException(HttpStatus.FORBIDDEN.value(), "Not yrs...");
        }
        return accessGroup;
    }

    @Override
    public void deleteGroup(String groupName, HttpServletResponse response) {
        String username = headerService.getUsername();
        AccessGroup accessGroup = accessGroupRepository.findById(groupName).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "No group with such name: " + groupName));

        if(!accessGroup.getOwner().equals(username)){
            throw new ApiException(HttpStatus.FORBIDDEN.value(), "Not yrs...");
        }
        accessGroupRepository.delete(accessGroup);
    }

    @Override
    public AccessGroup editGroup(String groupName, Set<String> usernames, HttpServletResponse response) {
        String username = headerService.getUsername();
        AccessGroup accessGroup = accessGroupRepository.findById(groupName).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value(), "No group with such name: " + groupName));

        if(!accessGroup.getOwner().equals(username)){
            throw new ApiException(HttpStatus.FORBIDDEN.value(), "Not yrs...");
        }
        accessGroup.setUsernames(usernames);
        accessGroupRepository.save(accessGroup);
        return null;
    }
}
