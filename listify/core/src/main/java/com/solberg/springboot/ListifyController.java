package com.solberg.springboot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solberg.dto.CheckListItemDto;
import com.solberg.dto.ListDto;
import com.solberg.models.ListItem;
import com.solberg.models.ListifyList;
import com.solberg.models.User;
import com.solberg.persistence.ListDao;
import com.solberg.persistence.UserDao;

@RestController
@RequestMapping("/api")
public class ListifyController {

  private static final Logger logger = LoggerFactory.getLogger(ListifyController.class);

  @Autowired
  private UserDao userDao;

  @Autowired
  private ListDao listDao;

  @GetMapping("/test")
  public ListItem testList() {
    return new ListItem("test");
  }

  @GetMapping("/li/{id}")
  public ResponseEntity<ListDto> getList(@PathVariable long id) {
    ListifyList list = listDao.findListById(id);

    if (list == null) {
      return ResponseEntity.notFound().build();
    }

    ListDto listDto = convertToListDto(list);

    return ResponseEntity.ok(listDto);
  }

  private ListDto convertToListDto(ListifyList list) {
    ListDto listDto = new ListDto();
    listDto.setId(list.getId());
    listDto.setName(list.getName());
    listDto.setListItems(list.getListItems().stream()
        .map(item -> new CheckListItemDto(item.getName(), item.getState()))
        .collect(Collectors.toList()));
    return listDto;
  }

  @PostMapping("/list")
  public ResponseEntity<Map<String, Object>> postList(@RequestBody ListDto list,
      @AuthenticationPrincipal Jwt jwt) {
    logger.debug("Received list with name: " + list.getName() + " and items: " + list.getListItems());
    User user = userDao.findUserByEmail(jwt.getClaimAsString("email"));

    if (user == null) {
      return ResponseEntity.status(404).body(Map.of("message", "User not found"));
    }

    logger.debug("Posted by " + user.toString());

    ListifyList userList;

    if (list.getId() != null) {
      userList = listDao.findListById(list.getId());
      logger.debug(userList.toString());
      if (userList == null || !userList.getUser().equals(user)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "List not found or access denied"));
      }
      userList.setName(list.getName());
      userList.getListItems().clear();
    } else {
      userList = new ListifyList(user, list.getName());
    }

    for (CheckListItemDto item : list.getListItems()) {
      userList.addListItems(new ListItem(item.getText(), item.isChecked()));
    }

    listDao.saveList(userList);
    logger.debug("Successfully saved " + userList.getName());

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Received " + list.getListItems().size() + " items");
    response.put("items", list.getListItems());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/dashboard/lists")
  public List<Map<String, Object>> getUserLists(@AuthenticationPrincipal Jwt jwt) {
    // TODO: update to use responseentity

    String username = jwt.getClaimAsString("name");
    String email = jwt.getClaimAsString("email");

    logger.debug("Accessed by: " + username.toString());

    Map<String, Object> list = new HashMap<>();
    list.put("author", "John Doe"); // Author's name
    list.put("listitems", Collections.singletonList("Milk")); // List of items

    // Return a list containing the map
    return Collections.singletonList(list);
  }
}
