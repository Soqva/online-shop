package com.s0qva.application.dto;

import com.s0qva.application.dto.dictionary.DictionaryRoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean blocked;
    private List<DictionaryRoleDto> roles;
}
